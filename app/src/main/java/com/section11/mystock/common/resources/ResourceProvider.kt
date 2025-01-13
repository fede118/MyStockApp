package com.section11.mystock.common.resources

interface ResourceProvider {
    fun getString(resId: Int, vararg args: Any): String
}
