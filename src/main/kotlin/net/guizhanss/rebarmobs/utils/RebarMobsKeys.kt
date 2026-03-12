package net.guizhanss.rebarmobs.utils

import net.guizhanss.guizhanlib.common.utils.StringUtil
import net.guizhanss.rebarmobs.RebarMobs
import org.bukkit.NamespacedKey
import java.util.Locale

/**
 * Create a RebarMobs [NamespacedKey] from the string.
 */
fun rmKey(key: String) =
    NamespacedKey(RebarMobs.instance(), StringUtil.dehumanize(key).lowercase(Locale.ENGLISH))


object RebarMobsKeys {
    val SOUL_SHARD = rmKey("soul_shard")
}