# Rebar Block Reference

## Core Classes

| Class | File | Purpose |
|-------|------|---------|
| `RebarBlock` | [`block/RebarBlock.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/block/RebarBlock.kt) | Base class for all custom blocks. Each instance wraps exactly one `Block`; the instance's state and operations are scoped to that block. Must have two constructors: `(Block, BlockCreateContext)` and `(Block, PersistentDataContainer)`. |
| `RebarBlockSchema` | [`block/RebarBlockSchema.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/block/RebarBlockSchema.kt) | Static block definition (key, material, class). |
| `BlockStorage` | [`block/BlockStorage.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/block/BlockStorage.kt) | Manages persistent storage of RebarBlock instances in the world. |

## RebarBlock Key Methods

| Method | Signature | Description |
|--------|-----------|-------------|
| `getKey()` | `override fun getKey(): NamespacedKey` | Returns the block's NamespacedKey |
| `postLoad()` | `protected open fun postLoad()` | Called after load constructor. External data (inventories, entities, fluids) is already loaded. |
| `postInitialise()` | `open fun postInitialise()` | Called after both create and load constructors. Use for always-needed init (e.g., logistics groups). |
| `getWaila()` | `open fun getWaila(player): WailaDisplay?` | Returns WAILA text for the block |
| `getDropItem()` | `open fun getDropItem(context): ItemStack?` | Returns the item dropped when broken |
| `getPickItem()` | `open fun getPickItem(): ItemStack?` | Returns the item for middle-click |
| `write()` | `open fun write(pdc: PersistentDataContainer)` | Saves block data. Do NOT assume this is only called on unload. |
| `writeDebugInfo()` | `open fun writeDebugInfo(pdc)` | Saves debug data (defaults to `write()`) |
| `getSettings()` | `fun getSettings(): Config` | Returns settings from the block's config file |
| `getBlockTextureItem()` | `open fun getBlockTextureItem(): ItemStack?` | Returns item for custom block texture display |
| `getBlockTextureProperties()` | `open fun getBlockTextureProperties(): MutableMap<String, Pair<String, Int>>` | Returns custom block state properties for resource packs |
| `setupBlockTexture()` | `protected open fun setupBlockTexture(entity): BlockTextureEntity` | Initializes the block texture entity |
| `refreshBlockTextureItem()` | `fun refreshBlockTextureItem()` | Refreshes the block texture entity's item |
| `scheduleBlockTextureItemRefresh()` | `fun scheduleBlockTextureItemRefresh()` | Schedules texture refresh on next tick |

## RebarBlock Companion Methods

| Method | Signature | Description |
|--------|-----------|-------------|
| `register()` | `fun register(key, material, blockClass)` | Registers a new block type |
| `Block.rebarBlock` | `val Block.rebarBlock: RebarBlock?` | Extension to get the RebarBlock at a Block position |
| `Block.isVanillaBlock` | `val Block.isVanillaBlock: Boolean` | Checks if a Block is a vanilla block (no RebarBlock) |

## Block Base Interfaces (in `block/base/`)

Implement these to add behavior to your block. Rebar auto-registers companion object listeners.

| Interface | File | Key Methods / Behavior |
|-----------|------|---------------------|
| `RebarTickingBlock` | `RebarTickingBlock.kt` | `tick()` — Periodic tick. `setTickInterval(ticks)`, `setAsync(boolean)`. |
| `RebarInventoryBlock` | `RebarInventoryBlock.kt` | `createGui()` — Creates InvUI GUI. Auto-handles inventory persistence. |
| `RebarVirtualInventoryBlock` | `RebarVirtualInventoryBlock.kt` | Virtual inventory persistence. Drops contents on break. |
| `RebarVanillaInventoryBlock` | `RebarVanillaInventoryBlock.kt` | Wraps a vanilla inventory (chest, furnace, etc.). |
| `RebarDirectionalBlock` | `RebarDirectionalBlock.kt` | Directional block behavior. Auto-sets `facing` property. |
| `RebarEntityHolderBlock` | `RebarEntityHolderBlock.kt` | Holds associated entities. Auto-manages entity lifecycle. |
| `RebarFluidBlock` | `RebarFluidBlock.kt` | Fluid input/output point. `getSuppliedFluids()`, `fluidAmountRequested()`, `onFluidAdded()`, `onFluidRemoved()` |
| `RebarFluidBufferBlock` | `RebarFluidBufferBlock.kt` | Fluid storage buffer. `maxCapacity`, `getFluid()`, `setFluid()`, `clearFluid()`. |
| `RebarFluidTank` | `RebarFluidTank.kt` | Fluid tank with capacity. `maxCapacity`, `getFluid()`, `setFluid()`. |
| `RebarCargoBlock` | `RebarCargoBlock.kt` | Cargo/logistics transport block. |
| `RebarLogisticBlock` | `RebarLogisticBlock.kt` | Logistics group management. |
| `RebarProcessor` | `RebarProcessor.kt` | Processing machine with progress. `process()`, `onProcessFinish()`, `onProcessTick()`. |
| `RebarRecipeProcessor` | `RebarRecipeProcessor.kt` | Recipe-based processor. `onCraftFinish()`, `onCraftTick()`. |
| `RebarMultiblock` | `RebarMultiblock.kt` | Multiblock structure. `isFormed()`, `onForm()`, `onBreak()`. |
| `RebarSimpleMultiblock` | `RebarSimpleMultiblock.kt` | Simple multiblock with center block. |
| `RebarCulledBlock` | `RebarCulledBlock.kt` | Culling block (hide when adjacent to same type). |
| `RebarGroupCulledBlock` | `RebarGroupCulledBlock.kt` | Group-based culling. |
| `RebarEntityCulledBlock` | `RebarEntityCulledBlock.kt` | Entity-based culling. |
| `RebarEntityGroupCulledBlock` | `RebarEntityGroupCulledBlock.kt` | Entity group culling. |
| `RebarFallingBlock` | `RebarFallingBlock.kt` | Falling block behavior (like sand/gravel). |
| `RebarFire` | `RebarFire.kt` | Fire block with spread logic. |
| `RebarGrowable` | `RebarGrowable.kt` | Growable block (crops, etc.). |
| `RebarGhostBlockHolder` | `RebarGhostBlockHolder.kt` | Holds a "ghost" block (display only). |
| `RebarFacadeBlock` | `RebarFacadeBlock.kt` | Block facade (display as another block). |
| `RebarBreakHandler` | `RebarBreakHandler.kt` | Custom break handling. |
| `RebarUnloadBlock` | `RebarUnloadBlock.kt` | `onUnload()` — Called when block is unloaded. |
| `RebarInteractBlock` | `RebarInteractBlock.kt` | `onInteract(event, priority)` — Block interaction. |
| `RebarSneakBlock` | `RebarSneakBlock.kt` | Sneak interaction behavior. |
| `RebarRedstoneBlock` | `RebarRedstoneBlock.kt` | Redstone signal behavior. |
| `RebarTargetBlock` | `RebarTargetBlock.kt` | Target block (projectile interaction). |
| `RebarNoteBlock` | `RebarNoteBlock.kt` | Note block behavior. |
| `RebarPiston` | `RebarPiston.kt` | Piston behavior. |
| `RebarSponge` | `RebarSponge.kt` | Sponge behavior (water absorption). |
| `RebarLeaf` | `RebarLeaf.kt` | Leaf behavior (decay). |
| `RebarLog` | `RebarLog.kt` | Log/stem behavior. |
| `RebarShearable` | `RebarShearable.kt` | Shearable block (like vines). |
| `RebarComposter` | `RebarComposter.kt` | Composter behavior. |
| `RebarBrewingStand` | `RebarBrewingStand.kt` | Brewing stand behavior. |
| `RebarCauldron` | `RebarCauldron.kt` | Cauldron behavior. |
| `RebarCampfire` | `RebarCampfire.kt` | Campfire behavior. |
| `RebarFurnace` | `RebarFurnace.kt` | Furnace behavior. |
| `RebarHopper` | `RebarHopper.kt` | Hopper behavior. |
| `RebarDispenser` | `RebarDispenser.kt` | Dispenser behavior. |
| `RebarDropper` | `RebarDropper.kt` | Dropper behavior. |
| `RebarEnchantingTable` | `RebarEnchantingTable.kt` | Enchanting table behavior. |
| `RebarLectern` | `RebarLectern.kt` | Lectern behavior. |
| `RebarCrafter` | `RebarCrafter.kt` | Crafter behavior. |
| `RebarVault` | `RebarVault.kt` | Vault behavior. |
| `RebarBell` | `RebarBell.kt` | Bell behavior. |
| `RebarBeacon` | `RebarBeacon.kt` | Beacon behavior. |
| `RebarFlowerPot` | `RebarFlowerPot.kt` | Flower pot behavior. |
| `RebarTNT` | `RebarTNT.kt` | TNT behavior. |
| `RebarCopperBlock` | `RebarCopperBlock.kt` | Copper oxidation behavior. |
| `RebarJobBlock` | `RebarJobBlock.kt` | Job site block for villagers. |
| `RebarNoJobBlock` | `RebarNoJobBlock.kt` | No-job block for villagers. |
| `RebarJumpBlock` | `RebarJumpBlock.kt` | Jump/launch block behavior. |
| `RebarEntityChangedBlock` | `RebarEntityChangedBlock.kt` | Block that changes when entity interacts. |

## Block Events

| Event | File | Fired When |
|-------|------|------------|
| `RebarBlockPlaceEvent` | `event/RebarBlockPlaceEvent.kt` | Block is placed |
| `RebarBlockBreakEvent` | `event/RebarBlockBreakEvent.kt` | Block is broken |
| `RebarBlockLoadEvent` | `event/RebarBlockLoadEvent.kt` | Block is loaded from disk |
| `RebarBlockUnloadEvent` | `event/RebarBlockUnloadEvent.kt` | Block is unloaded |
| `RebarBlockSerializeEvent` | `event/RebarBlockSerializeEvent.kt` | Block is being saved |
| `RebarBlockDeserializeEvent` | `event/RebarBlockDeserializeEvent.kt` | Block is being loaded |
| `PreRebarBlockPlaceEvent` | `event/PreRebarBlockPlaceEvent.kt` | Before block is placed (cancellable) |
| `RebarBlockWailaEvent` | `event/RebarBlockWailaEvent.kt` | WAILA is being displayed |
