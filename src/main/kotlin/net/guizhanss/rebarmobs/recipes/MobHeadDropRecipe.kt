package net.guizhanss.rebarmobs.recipes

import io.github.pylonmc.rebar.guide.button.ItemButton
import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.recipe.FluidOrItem
import io.github.pylonmc.rebar.recipe.RebarRecipe
import io.github.pylonmc.rebar.recipe.RecipeInput
import io.github.pylonmc.rebar.recipe.RecipeType
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat
import net.guizhanss.rebarmobs.RebarMobs
import net.guizhanss.rebarmobs.utils.RebarMobsKeys
import net.guizhanss.rebarmobs.utils.rmTranslatableKey
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui

/**
 * Display recipe for Mob Heads, showing they drop from killing mobs with Soul Stealer enchantment.
 */
@JvmRecord
data class MobHeadDropRecipe(
    @get:JvmName("key")
    val key: NamespacedKey,
    val mobType: EntityType,
    val result: ItemStack,
) : RebarRecipe {
    override fun getKey() = key

    override val inputs get() = emptyList<RecipeInput>()
    override val results get() = listOf(FluidOrItem.of(result))

    override fun display(): Gui {
        val config = RebarMobs.configs.mobHeadsConfig.value.getEntityConfig(mobType)
        return Gui
            .builder()
            .setStructure(
                "# # # # # # # # #",
                "# # # # # # # # #",
                "# m # # i # # r #",
                "# # # # # # # # #",
                "# # # # # # # # #",
            )
            .addIngredient('#', GuiItems.backgroundBlack())
            .addIngredient(
                'm',
                ItemButton.from(ItemStack(Bukkit.getItemFactory().getSpawnEgg(mobType) ?: Material.BARRIER)),
            )
            .addIngredient(
                'i',
                ItemButton.from(
                    ItemStackBuilder
                        .gui(Material.IRON_SWORD, RebarMobsKeys.MOB_HEAD_DROP)
                        .name(Component.translatable(rmTranslatableKey("item.mob_head_info.name")))
                        .lore(
                            Component.translatable(
                                rmTranslatableKey("item.mob_head_info.lore"),
                                RebarArgument.of(
                                    "base-chance",
                                    UnitFormat.PERCENT.format(config.baseChance * 100).decimalPlaces(2),
                                ),
                                RebarArgument.of(
                                    "decapitator-chance",
                                    UnitFormat.PERCENT.format(config.decapitatorChance * 100).decimalPlaces(2),
                                ),
                            ),
                        ).build(),
                ),
            )
            .addIngredient('r', ItemButton.from(result))
            .build()
    }

    companion object {
        val RECIPE_TYPE = RecipeType<MobHeadDropRecipe>(RebarMobsKeys.MOB_HEAD_DROP)
    }
}
