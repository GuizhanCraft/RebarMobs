package net.guizhanss.rebarmobs.items.tools.lassos

import io.github.pylonmc.rebar.config.RebarConfig
import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.pylonmc.rebar.event.api.annotation.MultiHandler
import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.interfaces.BlockInteractRebarItemHandler
import io.github.pylonmc.rebar.item.interfaces.EntityInteractRebarItemHandler
import io.github.pylonmc.rebar.item.interfaces.InventoryTickerRebarItem
import io.papermc.paper.datacomponent.DataComponentTypes
import net.guizhanss.guizhanlib.kt.rebar.utils.delegates.persistentItemData
import net.guizhanss.rebarmobs.RebarMobs
import net.guizhanss.rebarmobs.datatypes.persistent.RebarMobsPersistentDataTypes
import net.guizhanss.rebarmobs.utils.RebarMobsKeys
import net.guizhanss.rebarmobs.utils.lassos.CaptureResult
import net.guizhanss.rebarmobs.utils.lassos.CapturedMobSnapshot
import net.guizhanss.rebarmobs.utils.lassos.MobLassoEffects
import net.guizhanss.rebarmobs.utils.lassos.captureEntity
import net.guizhanss.rebarmobs.utils.lassos.releaseEntity
import net.guizhanss.rebarmobs.utils.refreshLore
import net.guizhanss.rebarmobs.utils.rmKey
import net.guizhanss.rebarmobs.utils.rmTranslatableKey
import net.guizhanss.rebarmobs.utils.tryCatch
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

abstract class BaseMobLasso(
    item: ItemStack,
) : RebarItem(item),
    BlockInteractRebarItemHandler,
    EntityInteractRebarItemHandler,
    InventoryTickerRebarItem {

    var capturedType: EntityType? by persistentItemData(
        CAPTURED_TYPE_KEY,
        RebarMobsPersistentDataTypes.ENTITY_TYPE,
        null,
    )
    var capturedSnapshot: String? by persistentItemData(
        CAPTURED_SNAPSHOT_KEY,
        RebarSerializers.STRING,
        null,
    )
    var capturedAt: Long? by persistentItemData(
        CAPTURED_AT_KEY,
        RebarSerializers.LONG,
        null,
    )
    var lastAmbientAt: Long? by persistentItemData(
        LAST_AMBIENT_AT_KEY,
        RebarSerializers.LONG,
        null,
    )

    /**
     * The Rebar inventory ticks of how long the captured mobs will be automatically released.
     */
    abstract val holdingDuration: Int

    override fun getPlaceholders(): List<RebarArgument> {
        val type = capturedType
        return listOf(
            RebarArgument.of(
                "status",
                if (type != null) {
                    Component.translatable(
                        lassoTranslatableKey("status.filled"),
                        RebarArgument.of("entity-type", Component.translatable(type.translationKey())),
                    )
                } else {
                    Component.translatable(lassoTranslatableKey("status.empty"))
                },
            ),
            RebarArgument.of(
                "escape-after",
                if (holdingDuration <= 0) {
                    Component.translatable(lassoTranslatableKey("escape-after.never"))
                } else {
                    Component.translatable(
                        lassoTranslatableKey("escape-after.ticks"),
                        RebarArgument.of("ticks", Component.text(holdingDuration)),
                        RebarArgument.of(
                            "seconds",
                            Component.text(holdingDuration * RebarConfig.INVENTORY_TICKER_BASE_RATE / 20),
                        ),
                    )
                },
            ),
        )
    }

    // release start
    protected open fun canReleaseAt(
        entityType: EntityType,
        blockMaterial: Material,
    ): Boolean = blockMaterial.isAir

    /**
     * Release handler.
     */
    @MultiHandler([EventPriority.HIGHEST])
    override fun onInteractWithBlock(
        event: PlayerInteractEvent,
        priority: EventPriority,
    ) {
        if (event.action != Action.RIGHT_CLICK_BLOCK || event.useItemInHand() == Event.Result.DENY) return

        val type = capturedType ?: return
        val clickedBlock = event.clickedBlock ?: return
        val player = event.player
        val spawnBlock = clickedBlock.getRelative(event.blockFace)
        if (!canReleaseAt(type, spawnBlock.type)) {
            player.sendMessage(Component.translatable(lassoTranslatableKey("release.invalid-block-type")))
            MobLassoEffects.releaseFailureInvalidBlockType(player, spawnBlock.location)
            return
        }

        val location = spawnBlock.location.toCenterLocation()
        val spawned = releaseEntity(CapturedMobSnapshot(type, capturedSnapshot), location) ?: run {
            player.sendMessage(Component.translatable(lassoTranslatableKey("release.error")))
            MobLassoEffects.releaseError(player, location)
            return
        }

        event.isCancelled = true
        if (RebarMobs.configs.mobLassoRenameEnabled.value) {
            applyCustomName(spawned)
        }
        onRelease()
        refreshLore(player.locale())
        MobLassoEffects.releaseSuccess(player, spawned)
    }

    protected open fun onRelease() {
        capturedType = null
        capturedSnapshot = null
        capturedAt = null
        lastAmbientAt = null
    }

    private fun applyCustomName(entity: LivingEntity) {
        stack.getData(DataComponentTypes.CUSTOM_NAME)?.let { entity.customName(it) }
    }
    // release end

    // capture start
    abstract fun capture(entity: LivingEntity): CaptureResult

    /**
     * Capture handler.
     */
    @MultiHandler([EventPriority.HIGHEST])
    override fun onInteractWithEntity(
        event: PlayerInteractAtEntityEvent,
        priority: EventPriority,
    ) {
        event.isCancelled = true
        val player = event.player

        if (capturedType != null) {
            player.sendMessage(
                Component.translatable(
                    lassoTranslatableKey("capture.not-empty"),
                    RebarArgument.of("entity-type", Component.translatable(capturedType!!.translationKey())),
                ),
            )
            MobLassoEffects.captureFailureFull(player)
            return
        }

        val target = event.rightClicked as? LivingEntity ?: return

        if (target.persistentDataContainer.has(RebarMobsKeys.SOUL_CAGE_SPAWNED)) {
            player.sendMessage(Component.translatable(lassoTranslatableKey("capture.soul-cage-spawned")))
            MobLassoEffects.captureFailureSoulCage(player, target)
            return
        }

        val result = capture(target)
        if (result != CaptureResult.OK) {
            when (result) {
                CaptureResult.WRONG_ENTITY_TYPE -> {
                    player.sendMessage(
                        Component.translatable(
                            lassoTranslatableKey("capture.wrong-type"),
                            RebarArgument.of("entity-type", Component.translatable(target.type.translationKey())),
                        ),
                    )
                    MobLassoEffects.captureFailureWrongType(player, target)
                }

                CaptureResult.HOSTILE_TOO_STRONG -> {
                    player.sendMessage(Component.translatable(lassoTranslatableKey("capture.too-strong")))
                    MobLassoEffects.captureFailureTooStrong(player, target)
                }
            }
            return
        }

        val snapshot = captureEntity(target) ?: run {
            player.sendMessage(Component.translatable(lassoTranslatableKey("capture.error")))
            MobLassoEffects.captureError(player, target)
            return
        }

        MobLassoEffects.captureSuccess(player, target)

        capturedType = snapshot.entityType
        capturedSnapshot = snapshot.snapshot
        capturedAt = System.currentTimeMillis()
        refreshLore(player.locale())
        target.remove()
    }
    // capture end

    override val baseTickInterval = 1L

    override fun onTick(player: Player) {
        tryCatch("An error occurred while ticking lasso carrying") { handleTickCarrying(player) }
        tryCatch("An error occurred while ticking lasso ambient") { handleTickAmbient(player) }
    }

    private fun handleTickCarrying(player: Player) {
        val type = capturedType ?: return

        if (holdingDuration <= 0) return
        if (!isIntervalElapsed(capturedAt, holdingDuration)) return

        val snapshot = CapturedMobSnapshot(type, capturedSnapshot)
        val releasedEntity = releaseEntity(snapshot, player.location.toCenterLocation()) ?: return

        if (RebarMobs.configs.mobLassoRenameEnabled.value) {
            applyCustomName(releasedEntity)
        }
        onRelease()
        refreshLore(player.locale())
        MobLassoEffects.releaseSuccess(player, releasedEntity)
    }

    private fun handleTickAmbient(player: Player) {
        if (!RebarMobs.configs.mobLassoAmbientEnabled.value) return
        val type = capturedType ?: return

        val mainHand = player.inventory.itemInMainHand
        val mainHandLasso = RebarItem.from<BaseMobLasso>(mainHand) ?: return
        if (!mainHandLasso.stack.isSimilar(this.stack)) return

        if (!isIntervalElapsed(lastAmbientAt, RebarMobs.configs.mobLassoAmbientInterval.value)) return
        lastAmbientAt = System.currentTimeMillis()

        MobLassoEffects.playAmbientSound(player, type)
    }

    companion object {
        val CAPTURED_TYPE_KEY = rmKey("lasso_captured_type")
        val CAPTURED_SNAPSHOT_KEY = rmKey("lasso_captured_snapshot")
        val CAPTURED_AT_KEY = rmKey("lasso_captured_at")
        val LAST_AMBIENT_AT_KEY = rmKey("lasso_last_ambient_at")

        val BLOCKED_ENTITY_TYPES = setOf(
            EntityType.UNKNOWN,
            EntityType.PLAYER,
            EntityType.ARMOR_STAND,
        )

        @JvmSynthetic
        internal fun captureByTag(entity: LivingEntity, tag: Tag<EntityType>): CaptureResult {
            if (!isEntityTypeAllowed(entity.type, tag)) {
                return CaptureResult.WRONG_ENTITY_TYPE
            }
            return CaptureResult.OK
        }

        fun isEntityTypeAllowed(type: EntityType, tag: Tag<EntityType>) = type !in BLOCKED_ENTITY_TYPES && tag.isTagged(type)

        @JvmSynthetic
        internal fun isIntervalElapsed(lastTimestamp: Long?, intervalTicks: Int): Boolean {
            val last = lastTimestamp ?: 0L
            val intervalMs = intervalTicks.toLong() * RebarConfig.INVENTORY_TICKER_BASE_RATE * 50L
            return System.currentTimeMillis() - last >= intervalMs
        }

        private fun lassoTranslatableKey(path: String) = rmTranslatableKey("item.mob_lasso.$path")
    }
}
