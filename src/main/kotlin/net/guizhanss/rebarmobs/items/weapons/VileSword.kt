package net.guizhanss.rebarmobs.items.weapons

import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.pylonmc.rebar.event.api.annotation.MultiHandler
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.interfaces.EntityAttackRebarItemHandler
import net.guizhanss.rebarmobs.utils.RebarMobsKeys.VILE_SWORD_KILLED
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack

class VileSword(item: ItemStack) :
    RebarItem(item),
    EntityAttackRebarItemHandler {

    @MultiHandler([EventPriority.LOWEST])
    override fun onKillEntity(
        event: EntityDeathEvent,
        priority: EventPriority,
    ) {
        event.entity.persistentDataContainer.set(VILE_SWORD_KILLED, RebarSerializers.BOOLEAN, true)
    }
}
