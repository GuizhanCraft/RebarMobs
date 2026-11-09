package net.guizhanss.rebarmobs

import net.guizhanss.guizhanlib.rebar.addon.AbstractAddon
import net.guizhanss.rebarmobs.commands.RebarMobsCommands
import net.guizhanss.rebarmobs.config.RebarMobsConfig
import net.guizhanss.rebarmobs.guide.RebarMobsPages
import net.guizhanss.rebarmobs.items.RebarMobsItems
import net.guizhanss.rebarmobs.listeners.MobHeadListener
import net.guizhanss.rebarmobs.recipes.RebarMobsRecipes
import net.guizhanss.rebarmobs.utils.tags.RebarMobsTag
import org.bstats.bukkit.Metrics
import org.bukkit.Material
import java.util.Locale

class RebarMobs : AbstractAddon() {
    override val defaultLanguage = Locale.ENGLISH

    override val material = Material.CREEPER_HEAD

    override fun enable() {
        setupMetrics()

        RebarMobsTag.loadAll(this)

        configs = RebarMobsConfig(this)

        RebarMobsPages
        RebarMobsItems
        RebarMobsRecipes

        setupListeners()

        RebarMobsCommands.register(this)
    }

    override fun disable() {
    }

    private fun setupListeners() {
        server.pluginManager.registerEvents(MobHeadListener(), this)
    }

    private fun setupMetrics() {
        val metrics = Metrics(this, 30082)
    }

    companion object {

        lateinit var configs: RebarMobsConfig
            private set

        fun instance() = getInstance<RebarMobs>()
    }
}
