package net.guizhanss.rebarmobs.utils.lassos

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity

data class CapturedMobSnapshot(val entityType: EntityType, val snapshotString: String)

/**
 * Create a [CapturedMobSnapshot] from the given entity.
 */
@Suppress("UnstableApiUsage")
fun captureEntity(entity: LivingEntity): CapturedMobSnapshot? = runCatching {
    val snapshot = entity.createSnapshot() ?: return null
    val snapshotString = snapshot.getAsString()
    Bukkit.getEntityFactory().createEntitySnapshot(snapshotString) // validate the created string
    CapturedMobSnapshot(entity.type, snapshotString)
}.getOrNull()

/**
 * Create a [LivingEntity] based on [CapturedMobSnapshot] at designated [Location].
 */
@Suppress("UnstableApiUsage")
fun releaseEntity(
    snapshot: CapturedMobSnapshot,
    location: Location,
): LivingEntity? = runCatching {
    val entitySnapshot = Bukkit.getEntityFactory().createEntitySnapshot(snapshot.snapshotString)
    val entity = entitySnapshot.createEntity(location)
    if (entity is LivingEntity && entity.isValid && entity.type == snapshot.entityType) {
        entity
    } else {
        entity.remove()
        null
    }
}.getOrNull()

enum class CaptureResult {
    /**
     * The only success result.
     */
    OK,

    /**
     * Low tier or blocked type.
     */
    WRONG_ENTITY_TYPE,

    /**
     * Hostile has too much health.
     */
    HOSTILE_TOO_STRONG,
}
