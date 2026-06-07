package net.guizhanss.rebarmobs.utils

import net.guizhanss.rebarmobs.RebarMobs
import java.util.logging.Level

inline fun tryCatch(message: String, block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        RebarMobs.instance().logger.log(Level.SEVERE, message, e)
    }
}
