package net.guizhanss.rebarmobs.utils

import io.github.pylonmc.rebar.i18n.RebarTranslator.Companion.translate
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder.Companion.loreKey
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Force update the lore of the bound [ItemStack].
 */
fun RebarItem.refreshLore(player: Player) {
    stack.lore(listOf(Component.translatable(loreKey(key))))
    stack.translate(player, getPlaceholders())
}
