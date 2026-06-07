package net.guizhanss.rebarmobs.items.tools.lassos

import io.github.pylonmc.rebar.config.Settings
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import net.guizhanss.rebarmobs.utils.RebarMobsKeys
import net.guizhanss.rebarmobs.utils.lassos.CaptureResult
import net.guizhanss.rebarmobs.utils.tags.RebarMobsTag
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

class AquaLasso(item: ItemStack) : BaseMobLasso(item) {
    override val holdingDuration: Int
        get() = HOLDING_DURATION

    override fun capture(entity: LivingEntity): CaptureResult = captureByTag(entity, RebarMobsTag.AQUA_LASSO_ALLOWED)

    override fun canReleaseAt(
        entityType: EntityType,
        blockMaterial: Material,
    ): Boolean = super.canReleaseAt(entityType, blockMaterial) ||
        (RebarMobsTag.AQUA_LASSO_ALLOWED.isTagged(entityType) && blockMaterial == Material.WATER)

    companion object {
        private val settings = Settings.get(RebarMobsKeys.AQUA_LASSO)

        val HOLDING_DURATION = settings.getOrThrow("holding-duration", ConfigAdapter.INTEGER)
    }
}
