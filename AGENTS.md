# AGENTS.md

## Overview

RebarMobs is a Minecraft Paper plugin (addon) built on the Rebar framework with Kotlin DSL extensions from GuizhanLib-KT.

## Stack

- **Language**: Kotlin (JVM 21+), minimal Java for Paper PluginLoader
- **Framework**: Paper, Rebar addon framework, GuizhanLib-KT DSL extensions
- **Versions**: See `gradle.properties` for all controlled versions (plugin, Minecraft, Rebar, Pylon). Note: Minecraft switched to `YY.major.minor` format in 2026 (e.g. `26.1.2`); the last old-format release was `1.21.11`.
- **Build**: Gradle with Kotlin DSL, Shadow plugin
- **Dependencies**: Rebar, Pylon (optional)

## Commands

- Build and package: `./gradlew clean shadowJar`
- Format code: `./gradlew spotlessApply` (mandatory before finishing any code changes)
- Kotlin lint: ktlint (via Spotless)
- Java lint: Google Java Format AOSP (via Spotless)

## Conventions

### Naming

- Kotlin: PascalCase classes, camelCase functions/properties
- NamespacedKey keys: underscore separator (`soul_shard`, `rebar_mobs`)
- Translation keys: match NamespacedKey format (`item.soul_shard.name`, `guide.page.rebar_mobs`)
- Config keys and translation sub-keys: hyphen separator (`auto-update`, `no-mob-type`)
- Placeholder names: `%mob-type%`, `%tier%`

### Code Style

- Run `./gradlew spotlessApply` before finishing any work involving code changes
- Minimal changes; preserve public APIs
- New functions: single-purpose, colocated with related code

## Architecture & Patterns

### Item Registration

Use `RebarItemRegistry` DSL with delegated properties:

```kotlin
object RebarMobsItems : RebarItemRegistry(RebarMobs.instance()) {
    val ITEM_NAME by item<CustomItem> { key = ...; material = ... }
}
```

### Data Persistence

Use `persistentItemData` delegated property for ItemStack data:

```kotlin
var data: Type by persistentItemData(KEY, DATATYPE) { defaultValue }
```

### Event Listeners

Place Bukkit Listener in the **direct** companion object of the `RebarItem` subclass. The framework only auto-registers the current class's own companion object, not inherited ones from parent classes:

```kotlin
class MyItem(item: ItemStack) : RebarItem(item) {
    companion object : Listener {
        @EventHandler fun onEvent(e: SomeEvent) { ... }
    }
}
```

### Translation System

- No hard-coded player-visible strings; all translatable
- Language files: `src/main/resources/lang/[LOCALE].yml`
- Placeholders use `%name%` format, NOT `{0}`
- Use `RebarArgument.of("name", value)` for named placeholders
- Only modify `en.yml`; never directly edit other language files (e.g., `zh_CN.yml`)

### Command DSL

Use `baseCommand(plugin, name)` from GuizhanLib-KT. Handlers implement `KommandExecutor`:

```kotlin
baseCommand(plugin, "cmd") {
    subCommand("sub") { execute(MyHandler) }
}
```

## Framework API Reference

For the full upstream API reference, call `skill(name="rebar")` when you need to implement new items, blocks, entities, or commands. **Before implementing anything, always check the upstream Rebar source — do not reinvent boilerplate that already exists.**

## Rules

- **Never suppress type errors** with unsafe casts or suppression annotations unless absolutely unavoidable
- **Build verification**: Run `./gradlew clean shadowJar` after code changes to verify type safety
- **No external dependencies** without justification
- **Tests/lint**: Only create when explicitly requested
- **i18n changes**: Only modify `en.yml`; other locales are community-maintained
- **Player heads**: Store in `PlayerHead` enum with the hash part in texture URL
- **Consult upstream source**: Before implementing new items, blocks, or entities, load the `rebar` skill and check the upstream interfaces. Do not reinvent boilerplate that Rebar already provides

## Resources

- Rebar: https://github.com/pylonmc/rebar
- Pylon: https://github.com/pylonmc/pylon
- Soul Shards Despawn (inspiration): https://github.com/0x00002a/Soul-Shards-Despawn
- Mob Lassos (inspiration): https://github.com/Fuzss/mob-lassos
