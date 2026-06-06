package net.guizhanss.rebarmobs.config

import net.guizhanss.guizhanlib.kt.minecraft.config.ConfigField
import net.guizhanss.guizhanlib.kt.minecraft.config.yamlConfig
import net.guizhanss.rebarmobs.RebarMobs
import net.guizhanss.rebarmobs.config.types.MobHeadsConfig
import net.guizhanss.rebarmobs.config.types.MobHeadsConfig.Companion.loadMobHeadsConfig

class RebarMobsConfig(
    plugin: RebarMobs,
) {
    lateinit var autoUpdateEnabled: ConfigField<Boolean>
    lateinit var autoUpdateIntervalDays: ConfigField<Int>
    lateinit var autoUpdateDownload: ConfigField<Boolean>
    lateinit var mobHeadsConfig: ConfigField<MobHeadsConfig>
    lateinit var mobLassoAmbientEnabled: ConfigField<Boolean>
    lateinit var mobLassoAmbientInterval: ConfigField<Int>

    private val config =
        yamlConfig(plugin, "config.yml") {
            autoUpdateEnabled = boolean("auto-update.enabled", true)
            autoUpdateIntervalDays = int("auto-update.interval-days", 1)
            autoUpdateDownload = boolean("auto-update.download", true)
            mobHeadsConfig = custom {
                it.getConfigurationSection("mob-heads").loadMobHeadsConfig()
            }
            mobLassoAmbientEnabled = boolean("mob-lasso.ambient.enabled", true)
            mobLassoAmbientInterval = int("mob-lasso.ambient.interval", 60)
        }

    init {
        reload()
    }

    fun reload() {
        config.reload()
    }
}
