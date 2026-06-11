package net.guizhanss.rebarmobs.guide

import io.github.pylonmc.rebar.guide.button.ItemButton
import net.guizhanss.rebarmobs.utils.tags.RebarMobsTag
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.ItemProvider

class MobLassoItemButton(
    private val stack: ItemStack,
    private val key: NamespacedKey,
    private val tag: RebarMobsTag<EntityType>,
    private val inverted: Boolean = false,
) : AbstractItem() {

    private val itemButton = ItemButton.of(stack)

    override fun getItemProvider(viewer: Player): ItemProvider = itemButton.getItemProvider(viewer)

    override fun getUpdatePeriod(what: Int): Int = itemButton.getUpdatePeriod(what)

    override fun handleClick(clickType: ClickType, player: Player, click: Click) {
        when (clickType) {
            ClickType.LEFT -> {
                MobLassoItemPage(stack, key, tag, inverted).open(player)
            }

            else -> itemButton.handleClick(clickType, player, click)
        }
    }
}
