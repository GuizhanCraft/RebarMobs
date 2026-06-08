package net.guizhanss.rebarmobs.utils.lassos

import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Registry
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import kotlin.math.cos
import kotlin.math.sin

/**
 * Holding particle and sound effects of mob lassos.
 */
object MobLassoEffects {

    fun captureSuccess(player: Player, entity: LivingEntity) {
        val world = entity.world
        val loc = entity.location.toCenterLocation()

        // SOUL_FIRE_FLAME spiral rising
        for (i in 0..20) {
            val angle = i * 0.5
            val y = i * 0.15
            val x = cos(angle) * 0.6
            val z = sin(angle) * 0.6
            world.spawnParticle(Particle.SOUL_FIRE_FLAME, loc.clone().add(x, y, z), 1, 0.0, 0.0, 0.0, 0.0)
        }

        // PORTAL scattered
        world.spawnParticle(Particle.PORTAL, loc, 30, 0.5, 0.5, 0.5, 0.5)

        player.playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
        player.playSound(loc, Sound.BLOCK_TRIPWIRE_ATTACH, 0.8f, 1.0f)
    }

    fun captureFailureFull(player: Player) {
        val loc = player.location.toCenterLocation()
        player.world.spawnParticle(Particle.WITCH, loc, 20, 0.3, 0.5, 0.3, 0.05)
        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.8f)
    }

    fun captureFailureWrongType(player: Player, target: LivingEntity) {
        val loc = target.location.toCenterLocation()
        target.world.spawnParticle(Particle.CRIT, loc, 15, 0.5, 0.5, 0.5, 0.1)
        player.playSound(loc, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
    }

    fun captureFailureTooStrong(player: Player, target: LivingEntity) {
        val loc = target.location.toCenterLocation()
        target.world.spawnParticle(Particle.WITCH, loc, 15, 0.5, 0.5, 0.5, 0.05)
        target.world.spawnParticle(Particle.SMOKE, loc, 15, 0.5, 0.5, 0.5, 0.05)
        player.playSound(loc, Sound.ENTITY_BLAZE_HURT, 0.8f, 0.8f)
    }

    fun captureFailureSoulCage(player: Player, target: LivingEntity) {
        val loc = target.location.toCenterLocation()
        target.world.spawnParticle(Particle.SMOKE, loc, 20, 0.3, 0.5, 0.3, 0.05)
        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.8f)
    }

    fun captureError(player: Player, target: LivingEntity) {
        val loc = target.location.toCenterLocation()
        target.world.spawnParticle(Particle.SMOKE, loc, 20, 0.5, 0.5, 0.5, 0.05)
        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.5f)
    }

    fun captureAttempt(player: Player, target: LivingEntity) {
        val loc = target.location.toCenterLocation()
        target.world.spawnParticle(Particle.END_ROD, loc, 5, 0.3, 0.5, 0.3, 0.02)
        player.playSound(loc, Sound.BLOCK_CHAIN_PLACE, 0.6f, 1.2f)
    }

    fun releaseSuccess(player: Player, entity: LivingEntity) {
        val loc = entity.location.toCenterLocation()
        val world = entity.world

        // CLOUD spreading on ground
        world.spawnParticle(Particle.CLOUD, loc.clone().add(0.0, 0.1, 0.0), 20, 0.5, 0.1, 0.5, 0.05)
        // HEART above entity
        world.spawnParticle(Particle.HEART, loc.clone().add(0.0, 1.0, 0.0), 5, 0.3, 0.3, 0.3, 0.0)

        player.playSound(loc, Sound.BLOCK_TRIPWIRE_DETACH, 1.0f, 1.0f)
    }

    fun releaseEscape(player: Player, entity: LivingEntity) {
        val loc = entity.location.toCenterLocation()
        val world = entity.world

        // ANGRY_VILLAGER + SMOKE — looks like the mob is breaking free
        world.spawnParticle(Particle.ANGRY_VILLAGER, loc.clone().add(0.0, 0.5, 0.0), 15, 0.4, 0.4, 0.4, 0.05)
        world.spawnParticle(Particle.SMOKE, loc, 25, 0.5, 0.5, 0.5, 0.08)
        // Chain break sound
        player.playSound(loc, Sound.BLOCK_CHAIN_BREAK, 1.0f, 0.8f)
        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 0.6f, 0.7f)
    }

    fun releaseSuccessInWater(player: Player, entity: LivingEntity) {
        val loc = entity.location.toCenterLocation()
        val world = entity.world

        // BUBBLE + SPLASH
        world.spawnParticle(Particle.BUBBLE_POP, loc, 20, 0.4, 0.3, 0.4, 0.05)
        world.spawnParticle(Particle.SPLASH, loc.clone().add(0.0, 0.2, 0.0), 15, 0.3, 0.1, 0.3, 0.0)
        player.playSound(loc, Sound.ENTITY_GENERIC_SPLASH, 0.8f, 1.0f)
        player.playSound(loc, Sound.BLOCK_TRIPWIRE_DETACH, 0.6f, 1.0f)
    }

    fun releaseFailureInvalidBlockType(player: Player, loc: Location) {
        val world = loc.world ?: return
        world.spawnParticle(Particle.SMOKE, loc.toCenterLocation(), 15, 0.3, 0.3, 0.3, 0.05)
        player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_BASS, 0.8f, 0.5f)
    }

    fun releaseError(player: Player, loc: Location) {
        val world = loc.world ?: return
        world.spawnParticle(Particle.SMOKE, loc.toCenterLocation(), 20, 0.3, 0.3, 0.3, 0.05)
        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.5f)
    }

    fun carryingDamage(player: Player, amount: Double) {
        val loc = player.location.toCenterLocation()
        val world = player.world

        // Small sparks / damage indicator
        world.spawnParticle(Particle.CRIT, loc.clone().add(0.0, 0.5, 0.0), 10, 0.3, 0.5, 0.3, 0.1)
        world.spawnParticle(Particle.DAMAGE_INDICATOR, loc.clone().add(0.0, 0.5, 0.0), 5, 0.2, 0.3, 0.2, 0.05)
        player.playSound(loc, Sound.ENTITY_PLAYER_HURT, 0.8f, 1.0f)
        player.playSound(loc, Sound.ENTITY_ZOMBIE_AMBIENT, 0.4f, 0.6f)
    }

    fun tickEffects(player: Player, entityType: EntityType) {
        val loc = player.location.toCenterLocation()
        val world = player.world

        // Tiny orbiting particle around the player to show "something is inside"
        val angle = System.currentTimeMillis() % 3600 / 10.0
        val x = kotlin.math.cos(Math.toRadians(angle)) * 0.8
        val z = kotlin.math.sin(Math.toRadians(angle)) * 0.8
        world.spawnParticle(Particle.SOUL, loc.clone().add(x, 0.5, z), 1, 0.0, 0.0, 0.0, 0.0)
    }

    fun playAmbientSound(player: Player, entityType: EntityType) {
        val base = "entity.${entityType.key.key}"
        val candidates = listOf(
            "$base.ambient",
            "$base.idle",
            "$base.hurt",
            "$base.step",
            "$base.squirt",
            "$base.burn",
            "$base.flop",
            "$base.charge",
        )
        val soundId = candidates.firstOrNull { soundExists(it) } ?: return
        player.playSound(player.location, soundId, SoundCategory.AMBIENT, 0.3f, 1.0f)
    }

    private fun soundExists(id: String): Boolean = Registry.SOUNDS.get(NamespacedKey.minecraft(id)) != null
}
