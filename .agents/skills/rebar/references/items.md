# Rebar Item Reference

## Core Classes

| Class              | File                                                                                                                                                              | Purpose                                                                                                                                                                                 |
|--------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `RebarItem`        | [`item/RebarItem.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/item/RebarItem.kt)                               | Base class for all custom items. Each instance wraps exactly one `ItemStack`; the instance's state and operations are scoped to that stack. Must have a single `ItemStack` constructor. |
| `RebarItemSchema`  | [`item/RebarItemSchema.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/item/RebarItemSchema.kt)                   | Static item definition (key, template stack, class, optional block key).                                                                                                                |
| `ItemStackBuilder` | [`item/builder/ItemStackBuilder.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/item/builder/ItemStackBuilder.kt) | Builder for creating item stacks with Rebar metadata. Use `ItemStackBuilder.rebar(material, key)` to create a base Rebar item.                                                          |

## RebarItem Key Methods

| Method              | Signature                                                  | Description                                  |
|---------------------|------------------------------------------------------------|----------------------------------------------|
| `getKey()`          | `override fun getKey(): NamespacedKey`                     | Returns the item's NamespacedKey             |
| `getSettings()`     | `fun getSettings(): Config`                                | Returns settings from the item's config file |
| `getPlaceholders()` | `open fun getPlaceholders(): List<RebarArgument>`          | Returns lore placeholders (format: `%name%`) |
| `prePlace()`        | `open fun prePlace(context: BlockCreateContext): Boolean`  | Checks if the associated block can be placed |
| `place()`           | `open fun place(context: BlockCreateContext): RebarBlock?` | Places the associated block                  |

## RebarItem Companion Methods

| Method          | Signature                                           | Description                                                                                                    |
|-----------------|-----------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| `register()`    | `fun register(itemClass, template, rebarBlockKey?)` | Registers a new item type                                                                                      |
| `fromStack()`   | `fun fromStack(stack): RebarItem?`                  | Grab the possible RebarItem instance from ItemStack. Can pass the actual class reference for `as?` conversion. |
| `isRebarItem()` | `fun isRebarItem(stack): Boolean`                   | Checks if stack is a Rebar item. Cannot check actual class type.                                               |

## Item Base Interfaces (in `item/base/`)

Implement these to add behavior to your item.

| Interface                   | File                           | Key Methods                                              |
|-----------------------------|--------------------------------|----------------------------------------------------------|
| `RebarInteractor`           | `RebarInteractor.kt`           | `onInteract(event, priority)` — Right-click interaction  |
| `RebarBlockInteractor`      | `RebarBlockInteractor.kt`      | `onUsedToClickBlock(event, priority)` — Clicking blocks  |
| `RebarConsumable`           | `RebarConsumable.kt`           | `onConsume(event, priority)` — Eating/drinking           |
| `RebarWeapon`               | `RebarWeapon.kt`               | `onAttack(event, priority)` — Attacking entities         |
| `RebarTool`                 | `RebarTool.kt`                 | `onBreakBlock(event, priority)` — Breaking blocks        |
| `RebarArmor`                | `RebarArmor.kt`                | Armor-specific behavior                                  |
| `RebarProjectileItem`       | `RebarProjectileItem.kt`       | `onLaunch(event, priority)` — Launching projectiles      |
| `RebarBow`                  | `RebarBow.kt`                  | `onShoot(event, priority)` — Bow shooting                |
| `RebarArrow`                | `RebarArrow.kt`                | `onHit(event, priority)` — Arrow hitting                 |
| `RebarCooldownable`         | `RebarCooldownable.kt`         | `respectCooldown` property                               |
| `RebarItemDamageable`       | `RebarItemDamageable.kt`       | `damageItem(event, priority)` — Custom durability logic  |
| `RebarRepairable`           | `RebarRepairable.kt`           | `onRepair(event, priority)` — Anvil repair               |
| `RebarDroppable`            | `RebarDroppable.kt`            | `onDrop(event, priority)` — Dropping items               |
| `RebarPickupable`           | `RebarPickupable.kt`           | `onPickup(event, priority)` — Picking up items           |
| `RebarDispensable`          | `RebarDispensable.kt`          | `onDispense(event, priority)` — Dispenser behavior       |
| `RebarBucket`               | `RebarBucket.kt`               | `onEmpty(event, priority)` / `onFill(event, priority)`   |
| `RebarBottle`               | `RebarBottle.kt`               | Bottle-specific behavior                                 |
| `RebarSplashPotion`         | `RebarSplashPotion.kt`         | Splash potion behavior                                   |
| `RebarLingeringPotion`      | `RebarLingeringPotion.kt`      | Lingering potion behavior                                |
| `RebarItemEntityInteractor` | `RebarItemEntityInteractor.kt` | `onClickEntity(event, priority)` — Clicking entities     |
| `RebarInventoryEffectItem`  | `RebarInventoryEffectItem.kt`  | `onTickInInventory(event, priority)` — Inventory ticking |
| `RebarInventoryTicker`      | `RebarInventoryTicker.kt`      | `onTick(event, priority)` — Inventory slot ticking       |
| `RebarJoinHandler`          | `RebarJoinHandler.kt`          | `onJoin(event, priority)` — Player join events           |
| `RebarUnmergeable`          | `RebarUnmergeable.kt`          | Prevents item stack merging                              |

## Vanilla Behavior Interfaces

| Interface                 | File                         | Purpose                      |
|---------------------------|------------------------------|------------------------------|
| `VanillaCookingFuel`      | `VanillaCookingFuel.kt`      | Marks item as furnace fuel   |
| `VanillaCookingItem`      | `VanillaCookingItem.kt`      | Marks item as furnace output |
| `VanillaCraftingItem`     | `VanillaCraftingItem.kt`     | Marks item as craftable      |
| `VanillaAnvilItem`        | `VanillaAnvilItem.kt`        | Custom anvil behavior        |
| `VanillaSmithingMaterial` | `VanillaSmithingMaterial.kt` | Smithing material            |
| `VanillaSmithingMineral`  | `VanillaSmithingMineral.kt`  | Smithing mineral             |
| `VanillaSmithingTemplate` | `VanillaSmithingTemplate.kt` | Smithing template            |
