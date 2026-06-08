package net.guizhanss.rebarmobs.datatypes.cmd

import net.guizhanss.guizhanlib.kt.rebar.utils.delegates.CustomModelDataType

object BooleanDataType : CustomModelDataType<Boolean> {
    override fun fromString(value: String) = value.toBoolean()
    override fun toString(value: Boolean) = value.toString()
}
