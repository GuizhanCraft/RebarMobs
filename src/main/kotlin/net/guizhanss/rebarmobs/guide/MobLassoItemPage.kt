package net.guizhanss.rebarmobs.guide

import io.github.pylonmc.rebar.content.guide.RebarGuide
import io.github.pylonmc.rebar.guide.button.ItemButton
import io.github.pylonmc.rebar.guide.pages.base.PagedGuidePage
import io.github.pylonmc.rebar.guide.pages.item.ItemRecipesPage
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.recipe.ingredient.FluidOrItem
import io.github.pylonmc.rebar.util.gui.GuiItems
import net.guizhanss.rebarmobs.items.tools.lassos.BaseMobLasso
import net.guizhanss.rebarmobs.utils.tags.RebarMobsTag
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.Markers
import xyz.xenondevs.invui.gui.PagedGui

class MobLassoItemPage(
    private val stack: ItemStack,
    private val key: NamespacedKey,
    private val tag: RebarMobsTag<EntityType>,
    private val inverted: Boolean = false,
) : PagedGuidePage {

    override val title: Component
        get() = Component.translatable("rebar.guide.page.item_recipes")

    override fun getKey() = key

    override fun getGui(player: Player): Gui {
        val allPages = mutableListOf<Gui>()
        allPages.addAll(ItemRecipesPage(stack).pages)

        val allowedTypes = if (inverted) {
            Registry.ENTITY_TYPE.filter {
                it !in BaseMobLasso.BLOCKED_ENTITY_TYPES && !tag.isTagged(it)
            }
        } else {
            tag.getValues().filter { it !in BaseMobLasso.BLOCKED_ENTITY_TYPES }
        }.sortedBy { it.name }

        val spawnEggButtons = allowedTypes.mapNotNull { type ->
            Bukkit.getItemFactory().getSpawnEgg(type)?.let { eggMaterial ->
                ItemButton.of(ItemStack.of(eggMaterial)) { stack, _ ->
                    ItemStackBuilder.of(stack)
                        .name(Component.translatable(type.translationKey()))
                        .build()
                }
            }
        }

        spawnEggButtons.chunked(45).forEach { chunk ->
            val gui = PagedGui.itemsBuilder()
                .setStructure(
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                    "x x x x x x x x x",
                )
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .setContent(chunk)
                .build()
            allPages.add(gui)
        }

        val builder = PagedGui.guisBuilder()
            .setStructure(
                "< b # g # i # s >",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
            )
            .addIngredient('#', GuiItems.background())
            .addIngredient('<', GuiItems.pagePrevious())
            .addIngredient('b', RebarGuide.backButton)
            .addIngredient('g', RebarGuide.ingredientsButton(FluidOrItem.of(stack)))
            .addIngredient('i', RebarGuide.infoButton(FluidOrItem.of(stack)))
            .addIngredient('s', RebarGuide.searchItemsAndFluidsButton)
            .addIngredient('>', GuiItems.pageNext())
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addPageChangeHandler { _, newPage -> saveCurrentPage(player, newPage) }

        builder.setContent(allPages)
        return builder.build().apply { loadCurrentPage(player, this) }
    }
}
