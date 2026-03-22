package net.guizhanss.rebarmobs

import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.registry.data.EnchantmentRegistryEntry
import io.papermc.paper.registry.event.RegistryEvents
import io.papermc.paper.registry.keys.EnchantmentKeys
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.inventory.EquipmentSlotGroup

@Suppress("UnstableApiUsage", "unused")
class RebarMobsBootstrap : PluginBootstrap {
    override fun bootstrap(context: BootstrapContext) {
        context.lifecycleManager.registerEventHandler(
            RegistryEvents.ENCHANTMENT.compose().newHandler { event ->
                event.registry().register(
                    EnchantmentKeys.create(NamespacedKey(PLUGIN_NAMESPACE, "soul_stealer")),
                ) { builder ->
                    builder
                        .description(Component.translatable("$PLUGIN_NAMESPACE.enchantment.soul_stealer"))
                        .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_MELEE_WEAPON))
                        .anvilCost(3)
                        .maxLevel(5)
                        .weight(10)
                        .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                        .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                        .activeSlots(EquipmentSlotGroup.MAINHAND)
                }
            },
        )
    }

    companion object {
        private const val PLUGIN_NAMESPACE = "rebarmobs"
    }
}
