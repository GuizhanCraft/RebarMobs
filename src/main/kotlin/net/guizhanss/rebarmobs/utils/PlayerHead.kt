package net.guizhanss.rebarmobs.utils

import net.guizhanss.guizhanlib.kt.minecraft.items.edit
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.net.URI
import java.util.UUID

/**
 * Construct a player head [ItemStack] with the given hash string.
 */
fun createPlayerHead(texture: String): ItemStack {
    val textureUrl = "https://textures.minecraft.net/texture/$texture"
    return ItemStack(Material.PLAYER_HEAD).edit {
        meta {
            val profile = Bukkit.createProfile(UUID.randomUUID())
            val profileTextures = profile.textures
            profileTextures.skin = URI(textureUrl).toURL()
            profile.setTextures(profileTextures)
            (this as SkullMeta).playerProfile = profile
        }
    }
}

enum class PlayerHead(val texture: String) {
    ENTITY_(""),

    ;

    fun createHeadItem() = createPlayerHead(texture)
}
