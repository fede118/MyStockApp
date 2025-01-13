package com.section11.mystock.framework.resource

import android.content.Context
import com.section11.mystock.common.resources.ResourceProvider

class ResourceProviderImpl(private val context: Context) : ResourceProvider {
    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }
}
