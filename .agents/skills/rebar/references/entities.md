# Rebar Entity Reference

## Core Classes

| Class | File | Purpose |
|-------|------|---------|
| `RebarEntity` | [`entity/RebarEntity.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/entity/RebarEntity.kt) | Base class for all custom entities. Each instance wraps exactly one `Entity`; the instance's state and operations are scoped to that entity. Must have a single `Entity` constructor (load constructor). You are responsible for spawning entities. |
| `RebarEntitySchema` | [`entity/RebarEntitySchema.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/entity/RebarEntitySchema.kt) | Static entity definition (key, entity class, RebarEntity class, persistence). |
| `EntityStorage` | [`entity/EntityStorage.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/entity/EntityStorage.kt) | Manages persistent storage of RebarEntity instances. |

## RebarEntity Key Methods

| Method | Signature | Description |
|--------|-----------|-------------|
| `getKey()` | `val key: NamespacedKey` | The entity's NamespacedKey (read from PDC) |
| `getWaila()` | `open fun getWaila(player): WailaDisplay?` | Returns WAILA text for the entity |
| `getPickItem()` | `open fun getPickItem(): ItemStack?` | Returns item for middle-click (default: null) |
| `write()` | `open fun write(pdc: PersistentDataContainer)` | Saves entity data. Do NOT assume this is only called on unload. |
| `writeDebugInfo()` | `open fun writeDebugInfo(pdc)` | Saves debug data (defaults to `write()`) |
| `onUnload()` | `open fun onUnload()` | Called when entity is unloaded (not deleted) |
| `getSettings()` | `fun getSettings(): Config` | Returns settings from the entity's config file |

## RebarEntity Companion Methods

| Method | Signature | Description |
|--------|-----------|-------------|
| `register()` | `fun register(key, entityClass, rebarEntityClass, isPersistent)` | Registers a new entity type |
| `initialiseRebarEntity()` | `fun initialiseRebarEntity(key, entity): E` | Sets the Rebar key on an entity's PDC |
| `Entity.isRebarEntity` | `val Entity.isRebarEntity: Boolean` | Extension to check if entity is a Rebar entity |
| `Entity.rebarEntity` | `val Entity.rebarEntity: RebarEntity<*>?` | Extension to get the RebarEntity wrapper |

## Entity Base Interfaces (in `entity/base/`)

Implement these to add behavior to your entity. Rebar auto-registers companion object listeners.

| Interface | File | Key Methods / Behavior |
|-----------|------|---------------------|
| `RebarTickingEntity` | `RebarTickingEntity.kt` | `tick()` — Periodic tick. `setTickInterval(ticks)`, `setAsync(boolean)`. |
| `RebarInteractEntity` | `RebarInteractEntity.kt` | `onInteract(event, priority)` — Entity interaction (right-click). |
| `RebarMountableEntity` | `RebarMountableEntity.kt` | `onMount(event, priority)` — Entity is mounted. |
| `RebarMountingEntity` | `RebarMountingEntity.kt` | `onMount(event, priority)` — Entity mounts something. |
| `RebarDeathEntity` | `RebarDeathEntity.kt` | `onDeath(event, priority)` — Entity death. |
| `RebarDamageableEntity` | `RebarDamageableEntity.kt` | `onDamage(event, priority)` — Entity takes damage. |
| `RebarCombustibleEntity` | `RebarCombustibleEntity.kt` | `onCombust(event, priority)` — Entity combusts. |
| `RebarExplosiveEntity` | `RebarExplosiveEntity.kt` | `onExplode(event, priority)` — Entity explodes. |
| `RebarProjectile` | `RebarProjectile.kt` | `onHit(event, priority)` — Projectile hits. |
| `RebarFirework` | `RebarFirework.kt` | Firework-specific behavior. |
| `RebarItemEntity` | `RebarItemEntity.kt` | Item entity behavior. |
| `RebarExperienceOrb` | `RebarExperienceOrb.kt` | Experience orb behavior. |
| `RebarBreedable` | `RebarBreedable.kt` | `onBreed(event, priority)` — Breeding behavior. |
| `RebarTameable` | `RebarTameable.kt` | `onTame(event, priority)` — Taming behavior. |
| `RebarLeashable` | `RebarLeashable.kt` | `onLeash(event, priority)` — Leash behavior. |
| `RebarDyeable` | `RebarDyeable.kt` | `onDye(event, priority)` — Dyeing behavior. |
| `RebarResurrectable` | `RebarResurrectable.kt` | Resurrection behavior. |
| `RebarPathingEntity` | `RebarPathingEntity.kt` | `onPath(event, priority)` — Pathfinding behavior. |
| `RebarMovingEntity` | `RebarMovingEntity.kt` | `onMove(event, priority)` — Movement behavior. |
| `RebarEnderman` | `RebarEnderman.kt` | Enderman-specific behavior (teleport, pick up block). |
| `RebarCreeper` | `RebarCreeper.kt` | Creeper-specific behavior (ignite, explode). |
| `RebarSlime` | `RebarSlime.kt` | Slime-specific behavior (split). |
| `RebarDragonFireball` | `RebarDragonFireball.kt` | Dragon fireball behavior. |
| `RebarEnderDragon` | `RebarEnderDragon.kt` | Ender dragon behavior. |
| `RebarTurtle` | `RebarTurtle.kt` | Turtle-specific behavior. |
| `RebarVillager` | `RebarVillager.kt` | Villager-specific behavior. |
| `RebarWitch` | `RebarWitch.kt` | Witch-specific behavior. |
| `RebarPiglin` | `RebarPiglin.kt` | Piglin-specific behavior. |
| `RebarCop` | `RebarCop.kt` | Wandering trader / merchant behavior. |
| `RebarBat` | `RebarBat.kt` | Bat-specific behavior. |
| `RebarSpellcaster` | `RebarSpellcaster.kt` | Spellcaster behavior (evoker, illusioner). |
| `RebarZombiePigman` | `RebarZombiePigman.kt` | Zombie pigman behavior. |
| `RebarUnloadEntity` | `RebarUnloadEntity.kt` | `onUnload(event, priority)` — Entity unload handling. |

## Entity Events

| Event | File | Fired When |
|-------|------|------------|
| `RebarEntityAddEvent` | `event/RebarEntityAddEvent.kt` | Entity is added to EntityStorage |
| `RebarEntityDeathEvent` | `event/RebarEntityDeathEvent.kt` | Entity dies |
| `RebarEntityLoadEvent` | `event/RebarEntityLoadEvent.kt` | Entity is loaded |
| `RebarEntityUnloadEvent` | `event/RebarEntityUnloadEvent.kt` | Entity is unloaded |
| `RebarEntitySerializeEvent` | `event/RebarEntitySerializeEvent.kt` | Entity is being saved |
| `RebarEntityDeserializeEvent` | `event/RebarEntityDeserializeEvent.kt` | Entity is being loaded |
| `RebarEntityWailaEvent` | `event/RebarEntityWailaEvent.kt` | WAILA is being displayed |
