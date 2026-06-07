package net.guizhanss.rebarmobs.guide

import io.github.pylonmc.rebar.content.guide.RebarGuide
import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import net.guizhanss.rebarmobs.utils.RebarMobsKeys
import org.bukkit.Material
import org.bukkit.entity.Player

object RebarMobsPages {
    val MAIN = SimpleStaticGuidePage(RebarMobsKeys.REBAR_MOBS)
    val MATERIALS = SimpleStaticGuidePage(RebarMobsKeys.MATERIALS)
    val BLOCKS = SimpleStaticGuidePage(RebarMobsKeys.BLOCKS)
    val TOOLS = SimpleStaticGuidePage(RebarMobsKeys.TOOLS)
    val WEAPONS = SimpleStaticGuidePage(RebarMobsKeys.WEAPONS)
    val MOB_HEADS = SimpleStaticGuidePage(RebarMobsKeys.MOB_HEADS)
    val ADMIN_ITEMS = object : SimpleStaticGuidePage(RebarMobsKeys.ADMIN_ITEMS) {
        override fun shouldDisplay(player: Player): Boolean = player.hasPermission("rebar.guide.view_admin_pages")
    }

    init {
        RebarGuide.rootPage.addPage(Material.CREEPER_HEAD, MAIN)

        MAIN.addPage(Material.ECHO_SHARD, MATERIALS)
        MAIN.addPage(Material.SPAWNER, BLOCKS)
        MAIN.addPage(Material.FLINT_AND_STEEL, TOOLS)
        MAIN.addPage(Material.IRON_SWORD, WEAPONS)
        MAIN.addPage(Material.CREEPER_HEAD, MOB_HEADS)
        MAIN.addPage(Material.COMMAND_BLOCK, ADMIN_ITEMS)
    }
}
