package net.guizhanss.rebarmobs.items.tools.lassos

import io.github.pylonmc.rebar.config.ConfigSection
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import net.guizhanss.rebarmobs.utils.RebarMobsKeys
import net.guizhanss.rebarmobs.utils.lassos.CaptureResult
import net.guizhanss.rebarmobs.utils.tags.RebarMobsTag
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

class DiamondLasso(item: ItemStack) : BaseMobLasso(item) {
    override val holdingDuration: Int
        get() = HOLDING_DURATION

    override fun capture(entity: LivingEntity): CaptureResult = captureByTag(entity, RebarMobsTag.DIAMOND_LASSO_ALLOWED)

    companion object {
        private val settings = ConfigSection.fromSettings(RebarMobsKeys.DIAMOND_LASSO)

        val HOLDING_DURATION = settings.getOrThrow("holding-duration", ConfigAdapter.INTEGER)
    }
}
