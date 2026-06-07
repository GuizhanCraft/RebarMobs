package net.guizhanss.rebarmobs.utils.tags

import net.guizhanss.rebarmobs.utils.rmKey
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.Tag
import org.bukkit.entity.EntityType
import org.bukkit.plugin.java.JavaPlugin

class RebarMobsTag<T : Keyed>(
    private val tagKey: NamespacedKey,
) : Tag<T> {
    private var values: Set<T> = emptySet()

    override fun getKey(): NamespacedKey = tagKey

    override fun isTagged(item: T): Boolean = item in values

    override fun getValues(): Set<T> = values

    @JvmSynthetic
    internal fun setValues(values: Set<T>) {
        this.values = values
    }

    companion object {
        val GOLDEN_LASSO_ALLOWED = RebarMobsTag<EntityType>(rmKey("golden_lasso_allowed"))
        val AQUA_LASSO_ALLOWED = RebarMobsTag<EntityType>(rmKey("aqua_lasso_allowed"))
        val DIAMOND_LASSO_ALLOWED = RebarMobsTag<EntityType>(rmKey("diamond_lasso_allowed"))
        val HOSTILE_LASSO_ALLOWED = RebarMobsTag<EntityType>(rmKey("hostile_lasso_allowed"))
        val CREATIVE_LASSO_BLOCKED = RebarMobsTag<EntityType>(rmKey("creative_lasso_blocked"))

        private val entityTypeTags = listOf(
            GOLDEN_LASSO_ALLOWED,
            AQUA_LASSO_ALLOWED,
            DIAMOND_LASSO_ALLOWED,
            HOSTILE_LASSO_ALLOWED,
            CREATIVE_LASSO_BLOCKED,
        )

        @JvmSynthetic
        internal fun loadAll(plugin: JavaPlugin) {
            TagLoader(
                plugin = plugin,
                registry = Registry.ENTITY_TYPE,
                clazz = EntityType::class.java,
                tagRegistryKey = Tag.REGISTRY_ENTITY_TYPES,
            ).load(entityTypeTags)
        }
    }
}
