package net.guizhanss.rebarmobs.config.types

import org.bukkit.configuration.ConfigurationSection

/**
 * Represents a configuration section containing only 1 layer of config options.
 */
@ConsistentCopyVisibility
internal data class MobHeadConfig private constructor(
    val baseChance: Double? = null,
    val decapitatorChance: Double? = null,
) {
    fun mergeWith(other: MobHeadConfig?): MobHeadConfig {
        if (other == null) return this
        return MobHeadConfig(
            other.baseChance ?: this.baseChance,
            other.decapitatorChance ?: this.decapitatorChance,
        )
    }

    fun toFinalConfig(): MobHeadFinalConfig = MobHeadFinalConfig(
        baseChance = baseChance!!,
        decapitatorChance = decapitatorChance!!,
    )

    companion object {
        private const val DEFAULT_BASE_CHANCE = 0.01
        private const val DEFAULT_DECAPITATOR_CHANCE = 0.02

        fun ConfigurationSection?.loadMobHeadConfigSection(defaults: Boolean = true) = MobHeadConfig(
            baseChance = this?.getDouble("base-chance")
                ?: if (defaults) DEFAULT_BASE_CHANCE else null,
            decapitatorChance = this?.getDouble("decapitator-chance")
                ?: if (defaults) DEFAULT_DECAPITATOR_CHANCE else null,
        )
    }
}

/**
 * The not null version of [MobHeadConfig].
 */
data class MobHeadFinalConfig(
    val baseChance: Double,
    val decapitatorChance: Double,
)
