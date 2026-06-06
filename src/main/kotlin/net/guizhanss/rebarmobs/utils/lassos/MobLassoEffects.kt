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

    fun releaseSuccess(player: Player, entity: LivingEntity) {
        val loc = entity.location.toCenterLocation()
        val world = entity.world

        // CLOUD spreading on ground
        world.spawnParticle(Particle.CLOUD, loc.clone().add(0.0, 0.1, 0.0), 20, 0.5, 0.1, 0.5, 0.05)
        // HEART above entity
        world.spawnParticle(Particle.HEART, loc.clone().add(0.0, 1.0, 0.0), 5, 0.3, 0.3, 0.3, 0.0)

        player.playSound(loc, Sound.BLOCK_TRIPWIRE_DETACH, 1.0f, 1.0f)
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
