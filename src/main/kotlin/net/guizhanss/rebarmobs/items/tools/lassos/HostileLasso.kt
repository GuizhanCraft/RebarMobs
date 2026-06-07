package net.guizhanss.rebarmobs.items.tools.lassos

import io.github.pylonmc.rebar.config.RebarConfig
import io.github.pylonmc.rebar.config.Settings
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.datatypes.RebarSerializers
import net.guizhanss.guizhanlib.kt.rebar.utils.delegates.persistentItemData
import net.guizhanss.rebarmobs.utils.RebarMobsKeys
import net.guizhanss.rebarmobs.utils.lassos.CaptureResult
import net.guizhanss.rebarmobs.utils.rmKey
import net.guizhanss.rebarmobs.utils.tags.RebarMobsTag
import org.bukkit.GameMode
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class HostileLasso(item: ItemStack) : BaseMobLasso(item) {
    private var lastHostileDamageAt: Long? by persistentItemData(
        LAST_HOSTILE_DAMAGE_AT_KEY,
        RebarSerializers.LONG,
        null,
    )

    override val holdingDuration: Int
        get() = HOLDING_DURATION

    override fun onRelease() {
        super.onRelease()
        lastHostileDamageAt = null
    }

    override fun capture(entity: LivingEntity): CaptureResult {
        captureByTag(entity, RebarMobsTag.HOSTILE_LASSO_ALLOWED)
            .let { if (it != CaptureResult.OK) return it }

        val maxHealth = entity.getAttribute(Attribute.MAX_HEALTH)?.value
            ?: return CaptureResult.HOSTILE_TOO_STRONG
        return if (entity.health > maxHealth * HEALTH_THRESHOLD) {
            CaptureResult.HOSTILE_TOO_STRONG
        } else {
            CaptureResult.OK
        }
    }

    override fun onTick(player: Player) {
        super.onTick(player)

        if (!CARRYING_DAMAGE_ENABLED) return
        if (capturedType == null) return
        if (CARRYING_DAMAGE_INTERVAL <= 0 || CARRYING_DAMAGE_AMOUNT <= 0) return

        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) return
        if (!isIntervalElapsed(lastHostileDamageAt, CARRYING_DAMAGE_INTERVAL)) return
        lastHostileDamageAt = System.currentTimeMillis()

        player.damage(CARRYING_DAMAGE_AMOUNT)
    }

    companion object {
        val LAST_HOSTILE_DAMAGE_AT_KEY = rmKey("lasso_last_hostile_damage_at")

        private val settings = Settings.get(RebarMobsKeys.HOSTILE_LASSO)

        val HOLDING_DURATION = settings.getOrThrow("holding-duration", ConfigAdapter.INTEGER)
        val HEALTH_THRESHOLD = settings.getOrThrow("health-threshold", ConfigAdapter.DOUBLE)
        val CARRYING_DAMAGE_ENABLED = settings.getOrThrow("carrying-damage.enabled", ConfigAdapter.BOOLEAN)
        val CARRYING_DAMAGE_INTERVAL = settings.getOrThrow("carrying-damage.interval", ConfigAdapter.INTEGER)
        val CARRYING_DAMAGE_AMOUNT = settings.getOrThrow("carrying-damage.amount", ConfigAdapter.DOUBLE)
    }
}
