package com.kamilh.findmysong.views.search

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kamilh.findmysong.R

class SourceChipGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ChipGroup(context, attrs, defStyleAttr) {

    private var configuration: Configuration? = null
    var sourceChipCheckedListener: ((Source?) -> Unit)? = null

    init {
        isSingleSelection = true
        setOnCheckedChangeListener { chipGroup, i ->
            if (configuration != null) {
                configuration?.let {
                    sourceChipCheckedListener?.invoke(it.list.elementAtOrNull(i))
                }
            } else {
                sourceChipCheckedListener?.invoke(null)
            }
        }
    }

    fun set(configuration: Configuration) {
        this.configuration = configuration
        removeAllViews()
        configuration.list.forEachIndexed { index, source ->
            addView(
                Chip(context).apply {
                    text = source.name()
                    isCheckable = true
                    isChecked = index == configuration.selectedIndex
                    id = index
                    chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.chip_state)
                    setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                }
            )
        }
    }

    private fun Source.name(): String = when (this) {
        Source.Remote -> context.getString(R.string.Source_remote)
        Source.Local -> context.getString(R.string.Source_local)
        Source.All -> context.getString(R.string.Source_all)
    }

    data class Configuration(
        val list: List<Source>,
        val selectedIndex: Int = 0
    )
}
