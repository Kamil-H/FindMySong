package com.kamilh.findmysong.utils

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(private val context: Context) {

    fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }

    fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
        return context.getString(id, *formatArgs)
    }

    fun getQuantityString(@PluralsRes id: Int, value: Int): String {
        return context.resources.getQuantityString(id, value)
    }

    fun getQuantityString(@PluralsRes id: Int, value: Int, vararg formatArgs: Any): String {
        return context.resources.getQuantityString(id, value, *formatArgs)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return AppCompatResources.getDrawable(context, id)
    }

    fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(context, id)
    }

    fun getFont(@FontRes id: Int): Typeface? {
        return ResourcesCompat.getFont(context, id)
    }
}
