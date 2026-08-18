package net.guizhanss.rebarmobs.utils

import io.github.pylonmc.rebar.i18n.RebarTranslator.Companion.translator
import net.guizhanss.rebarmobs.RebarMobs
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.translation.Translator
import java.text.MessageFormat
import java.util.Locale

/**
 * Fallback [Translator] that serves English entries when a RebarMobs locale file is missing a key.
 *
 * Delegates to Rebar's translator with the requested locale first, then English, so it works
 * regardless of GlobalTranslator source registration order.
 * Remove when Rebar supports per-key default-language fallback.
 */
object RebarMobsTranslator : Translator {
    override fun name(): Key = RebarMobsKeys.REBAR_MOBS_TRANSLATOR

    override fun canTranslate(key: String, locale: Locale): Boolean {
        if (!key.startsWith("${RebarMobs.instance().key.namespace}.")) return false

        val translator = RebarMobs.instance().translator
        return translator.canTranslate(key, locale) || translator.canTranslate(key, Locale.ENGLISH)
    }

    override fun translate(component: TranslatableComponent, locale: Locale): Component? {
        if (!component.key().startsWith("${RebarMobs.instance().key.namespace}.")) return null

        val translator = RebarMobs.instance().translator
        return translator.translate(component, locale) ?: translator.translate(component, Locale.ENGLISH)
    }

    override fun translate(key: String, locale: Locale): MessageFormat? = null
}
