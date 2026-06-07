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

class CreativeLasso(item: ItemStack) : BaseMobLasso(item) {
    override val holdingDuration: Int
        get() = HOLDING_DURATION

    override fun capture(entity: LivingEntity): CaptureResult = if (RebarMobsTag.CREATIVE_LASSO_BLOCKED.isTagged(entity.type)) {
        CaptureResult.WRONG_ENTITY_TYPE
    } else {
        CaptureResult.OK
    }

    override fun canReleaseAt(entityType: EntityType, blockMaterial: Material) = true

    companion object {
        private val settings = Settings.get(RebarMobsKeys.CREATIVE_LASSO)

        val HOLDING_DURATION = settings.getOrThrow("holding-duration", ConfigAdapter.INTEGER)
    }
}
