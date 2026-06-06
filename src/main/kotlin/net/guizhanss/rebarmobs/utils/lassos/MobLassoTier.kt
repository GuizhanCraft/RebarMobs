package net.guizhanss.rebarmobs.utils.lassos

import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity

/**
 * Different tiers of mob lasso.
 */
enum class MobLassoTier {
    LEATHER {
        override fun capture(entity: LivingEntity): CaptureResult = capture(entity, LEATHER_ENTITY_TYPES)
    },
    AQUATIC {
        override fun capture(entity: LivingEntity): CaptureResult = capture(entity, AQUATIC_ENTITY_TYPES)
        override fun release(entityType: EntityType, blockMaterial: Material): Boolean = super.release(entityType, blockMaterial) ||
            (entityType in AQUATIC_ENTITY_TYPES && blockMaterial == Material.WATER)
    },
    GOLDEN {
        override fun capture(entity: LivingEntity): CaptureResult = capture(entity, GOLDEN_ENTITY_TYPES)
    },
    DIAMOND {
        override fun capture(entity: LivingEntity): CaptureResult = capture(entity, DIAMOND_ENTITY_TYPES)
    },
    HOSTILE {
        override fun capture(entity: LivingEntity): CaptureResult {
            capture(entity, HOSTILE_ENTITY_TYPES).let { if (it != CaptureResult.OK) return it }

            val maxHealth = entity.getAttribute(Attribute.MAX_HEALTH)?.value
                ?: return CaptureResult.HOSTILE_TOO_STRONG
            return if (entity.health > maxHealth * 0.5) CaptureResult.HOSTILE_TOO_STRONG else CaptureResult.OK
        }
    },
    ;

    /**
     * Attempt to capture the target [LivingEntity].
     */
    abstract fun capture(entity: LivingEntity): CaptureResult

    /**
     * Attempt to release the target [EntityType] in block with [Material].
     */
    open fun release(entityType: EntityType, blockMaterial: Material): Boolean = blockMaterial.isAir

    companion object {
        private val BLOCKED_ENTITY_TYPES = setOf(
            EntityType.PLAYER,
            EntityType.UNKNOWN,
            EntityType.ARMOR_STAND,
            EntityType.ENDER_DRAGON,
            EntityType.WITHER,
            EntityType.WARDEN,
            EntityType.ELDER_GUARDIAN,
        )

        private val LEATHER_ENTITY_TYPES = setOf(
            EntityType.CHICKEN,
            EntityType.COW,
            EntityType.HORSE,
            EntityType.LLAMA,
            EntityType.MOOSHROOM,
            EntityType.OCELOT,
            EntityType.PARROT,
            EntityType.PIG,
            EntityType.POLAR_BEAR,
            EntityType.RABBIT,
            EntityType.SHEEP,
            EntityType.FOX,
            EntityType.PANDA,
            EntityType.GOAT,
            EntityType.CAMEL,
            EntityType.SNIFFER,
            EntityType.ARMADILLO,
        )

        private val AQUATIC_ENTITY_TYPES = setOf(
            EntityType.DOLPHIN,
            EntityType.SQUID,
            EntityType.GLOW_SQUID,
            EntityType.TURTLE,
            EntityType.AXOLOTL,
            EntityType.FROG,
            EntityType.TADPOLE,
            EntityType.NAUTILUS,
        )

        private val GOLDEN_ENTITY_TYPES = LEATHER_ENTITY_TYPES + AQUATIC_ENTITY_TYPES + setOf(
            EntityType.BAT,
            EntityType.VILLAGER,
            EntityType.WANDERING_TRADER,
            EntityType.STRIDER,
            EntityType.HAPPY_GHAST,
        )

        private val DIAMOND_ENTITY_TYPES = GOLDEN_ENTITY_TYPES + setOf(
            EntityType.IRON_GOLEM,
            EntityType.ALLAY,
            EntityType.COPPER_GOLEM,
        )

        private val HOSTILE_ENTITY_TYPES = setOf(
            EntityType.BLAZE,
            EntityType.BOGGED,
            EntityType.BREEZE,
            EntityType.CAVE_SPIDER,
            EntityType.CREEPER,
            EntityType.CREAKING,
            EntityType.DROWNED,
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.EVOKER,
            EntityType.GHAST,
            EntityType.GUARDIAN,
            EntityType.HUSK,
            EntityType.ILLUSIONER,
            EntityType.MAGMA_CUBE,
            EntityType.PHANTOM,
            EntityType.PIGLIN_BRUTE,
            EntityType.PILLAGER,
            EntityType.RAVAGER,
            EntityType.SHULKER,
            EntityType.SILVERFISH,
            EntityType.SKELETON,
            EntityType.SLIME,
            EntityType.SPIDER,
            EntityType.STRAY,
            EntityType.VEX,
            EntityType.VINDICATOR,
            EntityType.WITCH,
            EntityType.WITHER_SKELETON,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.ZOMBIE_NAUTILUS,
            EntityType.CAMEL_HUSK,
            EntityType.PARCHED,
        )

        private fun capture(
            entity: LivingEntity,
            allowedTypes: Set<EntityType>,
        ): CaptureResult = when (entity.type) {
            in BLOCKED_ENTITY_TYPES -> CaptureResult.WRONG_ENTITY_TYPE
            !in allowedTypes -> CaptureResult.WRONG_ENTITY_TYPE
            else -> CaptureResult.OK
        }
    }
}
