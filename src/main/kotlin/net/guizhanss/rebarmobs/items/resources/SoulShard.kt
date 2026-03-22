package net.guizhanss.rebarmobs.items.resources

import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.RebarItem
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import net.guizhanss.guizhanlib.kt.rebar.utils.persistentItemData
import net.guizhanss.rebarmobs.RebarMobs
import net.guizhanss.rebarmobs.datatypes.RebarMobsDataTypes
import net.guizhanss.rebarmobs.utils.RebarMobsKeys
import net.guizhanss.rebarmobs.utils.rmKey
import net.guizhanss.rebarmobs.utils.translatableKey
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack

class SoulShard(
    item: ItemStack,
) : RebarItem(item) {
    override fun getPlaceholders() =
        super.getPlaceholders().toMutableList().apply {
            add(
                RebarArgument.of(
                    "mob-type",
                    Component.translatable(
                        mobType?.translationKey() ?: translatableKey("no_mob_type"),
                    ),
                ),
            )
            add(RebarArgument.of("tier", 0)) // TODO: tier display
            add(RebarArgument.of("souls", soulAmount))
        }

    var mobType: EntityType? by persistentItemData(MOB_TYPE_KEY, RebarMobsDataTypes.ENTITY_TYPE) { null }
    var soulAmount: Int by persistentItemData(SOUL_AMOUNT_KEY, RebarSerializers.INTEGER) { 0 }

    companion object : Listener {
        val MOB_TYPE_KEY = rmKey("mob_type")
        val SOUL_AMOUNT_KEY = rmKey("soul_amount")

        private val soulStealerEnchant: Enchantment by lazy {
            RegistryAccess
                .registryAccess()
                .getRegistry(RegistryKey.ENCHANTMENT)
                .get(RebarMobsKeys.SOUL_STEALER) ?: error("Soul Stealer enchantment is not initialized!")
        }

        private fun getEnchantLevel(
            player: Player,
            enchant: Enchantment?,
        ): Int {
            if (enchant == null) return 0
            val mainHandLevel = player.inventory.itemInMainHand.getEnchantmentLevel(enchant)
            val offHandLevel = player.inventory.itemInOffHand.getEnchantmentLevel(enchant)
            return maxOf(mainHandLevel, offHandLevel)
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onEntityDeath(e: EntityDeathEvent) {
            val killer = e.damageSource.causingEntity
            if (killer !is Player) return

            val deadEntity = e.entity
            val bonusSouls = getEnchantLevel(killer, soulStealerEnchant)

            val contents = killer.inventory.contents + arrayOf(killer.inventory.itemInOffHand)
            var emptyShard: SoulShard? = null

            for (item in contents) {
                if (item == null || item.type.isAir) continue

                val shard = RebarItem.fromStack(item) as? SoulShard ?: continue

                when (shard.mobType) {
                    // Matched type
                    deadEntity.type -> {
                        shard.soulAmount += 1 + bonusSouls
                        return
                    }

                    // Empty shard, remember as candidate
                    null if emptyShard == null -> {
                        emptyShard = shard
                    }

                    // invalid, continue
                    else -> {}
                }
            }

            // setup empty shard
            emptyShard?.let {
                it.mobType = deadEntity.type
                it.soulAmount = 1 + bonusSouls
            }
        }
    }
}
