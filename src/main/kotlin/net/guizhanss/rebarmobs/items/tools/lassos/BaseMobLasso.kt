package net.guizhanss.rebarmobs.items.tools.lassos

import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.pylonmc.rebar.event.api.annotation.MultiHandler
import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.base.RebarBlockInteractor
import io.github.pylonmc.rebar.item.base.RebarInventoryTicker
import io.github.pylonmc.rebar.item.base.RebarItemEntityInteractor
import net.guizhanss.guizhanlib.kt.rebar.utils.delegates.persistentItemData
import net.guizhanss.rebarmobs.RebarMobs
import net.guizhanss.rebarmobs.datatypes.persistent.RebarMobsPersistentDataTypes
import net.guizhanss.rebarmobs.utils.RebarMobsKeys
import net.guizhanss.rebarmobs.utils.lassos.CaptureResult
import net.guizhanss.rebarmobs.utils.lassos.CapturedMobSnapshot
import net.guizhanss.rebarmobs.utils.lassos.MobLassoEffects
import net.guizhanss.rebarmobs.utils.lassos.MobLassoTier
import net.guizhanss.rebarmobs.utils.lassos.captureEntity
import net.guizhanss.rebarmobs.utils.lassos.releaseEntity
import net.guizhanss.rebarmobs.utils.refreshLore
import net.guizhanss.rebarmobs.utils.rmKey
import net.guizhanss.rebarmobs.utils.rmTranslatableKey
import net.kyori.adventure.text.Component
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

abstract class BaseMobLasso(
    item: ItemStack,
    private val tier: MobLassoTier,
) : RebarItem(item),
    RebarBlockInteractor,
    RebarItemEntityInteractor,
    RebarInventoryTicker {

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
        )
    }

    @MultiHandler([EventPriority.HIGHEST])
    override fun onUsedToClickBlock(
        event: PlayerInteractEvent,
        priority: EventPriority,
    ) {
        if (event.hand != EquipmentSlot.HAND) return
        if (event.action != Action.RIGHT_CLICK_BLOCK || event.useItemInHand() == Event.Result.DENY) return
        val player = event.player
        val item = player.inventory.itemInMainHand
        val lasso = RebarItem.from<BaseMobLasso>(item) ?: return

        val type = lasso.capturedType ?: return
        val snapshotString = lasso.capturedSnapshot ?: return
        val clickedBlock = event.clickedBlock ?: return
        val spawnBlock = clickedBlock.getRelative(event.blockFace)
        if (!lasso.tier.release(type, spawnBlock.type)) {
            player.sendMessage(Component.translatable(lassoTranslatableKey("release.invalid-block-type")))
            MobLassoEffects.releaseFailureInvalidBlockType(player, spawnBlock.location)
            return
        }

        val location = spawnBlock.location.toCenterLocation()
        val spawned = releaseEntity(CapturedMobSnapshot(type, snapshotString), location) ?: run {
            player.sendMessage(Component.translatable(lassoTranslatableKey("release.error")))
            MobLassoEffects.releaseError(player, location)
            return
        }

        MobLassoEffects.releaseSuccess(player, spawned)

        lasso.capturedType = null
        lasso.capturedSnapshot = null
        lasso.refreshLore(player.locale())
        event.isCancelled = true
    }

    @MultiHandler([EventPriority.HIGHEST])
    override fun onUsedToRightClickEntity(
        event: PlayerInteractEntityEvent,
        priority: EventPriority,
    ) {
        if (event.hand != EquipmentSlot.HAND) return
        val player = event.player
        val item = player.inventory.itemInMainHand
        val lasso = RebarItem.from<BaseMobLasso>(item) ?: return
        event.isCancelled = true

        val capturedType = lasso.capturedType
        if (capturedType != null) {
            player.sendMessage(
                Component.translatable(
                    lassoTranslatableKey("capture.not-empty"),
                    RebarArgument.of("entity-type", Component.translatable(capturedType.translationKey())),
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

        val result = lasso.tier.capture(target)
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

        lasso.capturedType = snapshot.entityType
        lasso.capturedSnapshot = snapshot.snapshotString
        lasso.refreshLore(player.locale())
        target.remove()
    }

    override val baseTickInterval: Long
        get() = RebarMobs.configs.mobLassoAmbientInterval.value.toLong()

    override fun onTick(player: Player) {
        if (!RebarMobs.configs.mobLassoAmbientEnabled.value) return
        val type = capturedType ?: return
        val mainHand = player.inventory.itemInMainHand
        if (!mainHand.isSimilar(stack)) return
        MobLassoEffects.playAmbientSound(player, type)
    }

    companion object {
        val CAPTURED_TYPE_KEY = rmKey("lasso_captured_type")
        val CAPTURED_SNAPSHOT_KEY = rmKey("lasso_captured_snapshot")

        private fun lassoTranslatableKey(path: String) = rmTranslatableKey("item.mob_lasso.$path")
    }
}
