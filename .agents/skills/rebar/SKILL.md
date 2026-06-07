---
name: rebar
description: Rebar Minecraft Paper plugin framework API reference. Use when implementing new items, blocks, entities, or any other Rebar addon content. Covers Rebar core (RebarItem, RebarBlock, RebarEntity, RebarRegistry, i18n). Always consult upstream source before writing code to avoid reinventing boilerplate.
---

# Rebar Framework API Reference

## How to use

1. Click the GitHub links below to inspect the upstream source before writing code
2. For detailed interface lists, read the reference files in this skill
3. Do not write code that duplicates what Rebar already provides

## Reference Files

| Topic | File | When to read |
|-------|------|-------------|
| Items | [`references/items.md`](references/items.md) | Implementing new items, tools, weapons, consumables |
| Blocks | [`references/blocks.md`](references/blocks.md) | Implementing new blocks, machines, multiblocks, ticking blocks |
| Entities | [`references/entities.md`](references/entities.md) | Implementing new entities, projectiles, mobs |

## Core Architecture

| Class | File | Purpose                                                                                                                                                                                             |
|-------|------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `RebarItem` | [`item/RebarItem.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/item/RebarItem.kt) | Base class for all custom items. Each instance wraps exactly one `ItemStack` provided in constructor.                                                                                               |
| `RebarBlock` | [`block/RebarBlock.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/block/RebarBlock.kt) | Base class for all custom blocks. Each instance wraps exactly one `Block` provided in constructor. Two constructors required: `(Block, BlockCreateContext)` and `(Block, PersistentDataContainer)`. |
| `RebarEntity` | [`entity/RebarEntity.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/entity/RebarEntity.kt) | Base class for all custom entities. Each instance wraps exactly one `Entity` provided in constructor. You are responsible for spawning.                                                             |
| `RebarItemSchema` | [`item/RebarItemSchema.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/item/RebarItemSchema.kt) | Static item definition (key, template, class, optional block key).                                                                                                                                  |
| `RebarBlockSchema` | [`block/RebarBlockSchema.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/block/RebarBlockSchema.kt) | Static block definition (key, material, class).                                                                                                                                                     |
| `RebarEntitySchema` | [`entity/RebarEntitySchema.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/entity/RebarEntitySchema.kt) | Static entity definition (key, entity class, RebarEntity class, persistence).                                                                                                                       |
| `RebarAddon` | [`addon/RebarAddon.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/addon/RebarAddon.kt) | Addon interface all add-ons must implement.                                                                                                                                                         |
| `RebarRegistry` | [`registry/RebarRegistry.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/registry/RebarRegistry.kt) | Central registry for all Rebar content (items, blocks, entities, recipes, researches).                                                                                                              |

## i18n / Translation

| Class | File | Purpose                                                                                                                                              |
|-------|------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| `RebarArgument` | [`i18n/RebarArgument.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/i18n/RebarArgument.kt) | Named placeholders for translations. Use `RebarArgument.of("name", value)`. Value can be primitive types and they eventually converted to `Component`. |
| `RebarMiniMessage` | [`i18n/RebarMiniMessage.kt`](https://github.com/pylonmc/rebar/blob/master/rebar/src/main/kotlin/io/github/pylonmc/rebar/i18n/RebarMiniMessage.kt) | Custom extra MiniMessage tags added by Rebar.                                                                                                        |
