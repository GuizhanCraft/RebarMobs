package net.guizhanss.rebarmobs.config.types

import net.guizhanss.guizhanlib.kt.minecraft.extensions.loadSectionMap
import net.guizhanss.rebarmobs.RebarMobs
import net.guizhanss.rebarmobs.config.types.MobHeadConfig.Companion.loadMobHeadConfigSection
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.EntityType

class MobHeadsConfig private constructor(
    private val base: MobHeadConfig,
    private val overrides: Map<EntityType, MobHeadConfig>,
) {
    fun getEntityConfig(entityType: EntityType): MobHeadFinalConfig {
        val override = overrides[entityType]
        return base.mergeWith(override).toFinalConfig()
    }

    companion object {
        fun ConfigurationSection?.loadMobHeadsConfig(): MobHeadsConfig {
            val base = this.loadMobHeadConfigSection()
            if (this == null) {
                return MobHeadsConfig(base, emptyMap())
            }

            val overrides = this.getConfigurationSection("overrides").loadSectionMap().mapNotNull { (key, value) ->
                runCatching {
                    EntityType.valueOf(key.uppercase()) to value.loadMobHeadConfigSection(false)
                }.onFailure {
                    RebarMobs.instance().logger.warning("Invalid entity type in mob-heads.overrides: $key")
                }.getOrNull()
            }.toMap()

            return MobHeadsConfig(base, overrides)
        }
    }
}
