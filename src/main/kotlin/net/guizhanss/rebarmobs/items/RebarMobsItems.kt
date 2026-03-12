package net.guizhanss.rebarmobs.items

import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.papermc.paper.datacomponent.DataComponentTypes
import net.guizhanss.rebarmobs.items.resources.SoulShard
import net.guizhanss.rebarmobs.utils.RebarMobsKeys
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object RebarMobsItems {
    //<editor-fold desc="Soul Shards" defaultstate="collapsed">
    val SOUL_SHARD: ItemStack = ItemStackBuilder.rebar(Material.FLINT, RebarMobsKeys.SOUL_SHARD)
        .set(DataComponentTypes.MAX_STACK_SIZE, 1)
        .build()

    init {
        RebarItem.register<SoulShard>(SOUL_SHARD)
    }
    //</editor-fold>
}