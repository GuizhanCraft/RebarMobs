package net.guizhanss.rebarmobs.commands.handlers

import io.github.pylonmc.rebar.i18n.RebarArgument
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import net.guizhanss.guizhanlib.kt.minecraft.command.KommandContext
import net.guizhanss.guizhanlib.kt.minecraft.command.KommandExecutor
import net.guizhanss.guizhanlib.kt.minecraft.items.edit
import net.guizhanss.guizhanlib.minecraft.utils.InventoryUtil
import net.guizhanss.rebarmobs.utils.rmTranslatableKey
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

object EnchantmentBookHandler : KommandExecutor {
    override fun execute(context: KommandContext) {
        val p = context.sender as Player
        val enchantment =
            context
                .argOrNull(0)
                ?.let {
                    NamespacedKey.fromString(it)
                }?.let { RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(it) }
                ?: run {
                    p.sendMessage(Component.translatable(messageKey("invalid-enchantment")))
                    return
                }
        val level = context.argOrNull(1)?.toIntOrNull() ?: 1
        if (level !in 1..255) {
            p.sendMessage(Component.translatable(messageKey("invalid-level")))
            return
        }

        val book =
            ItemStack(Material.ENCHANTED_BOOK).edit {
                meta {
                    (this as EnchantmentStorageMeta).addStoredEnchant(enchantment, level, true)
                }
            }

        InventoryUtil.push(p, book)
        p.sendMessage(
            Component
                .translatable(
                    messageKey("success"),
                ).arguments(
                    RebarArgument.of("enchantment", enchantment.description()),
                    RebarArgument.of("level", level),
                ),
        )
    }

    private fun messageKey(key: String) = rmTranslatableKey("command.enchantment_book.$key")
}
