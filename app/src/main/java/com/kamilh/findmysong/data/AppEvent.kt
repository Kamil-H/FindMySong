package com.kamilh.findmysong.data

sealed class AppEvent

sealed class Navigation: AppEvent()

class Loading(val isLoading: Boolean): AppEvent()

data class Alert(
    val title: String,
    val message: String,
    val isCancelable: Boolean = true,
    val positiveButton: Action? = null,
    val negativeButton: Action? = null,
    val cancelAction: (() -> (Unit))? = null
) : AppEvent() {

    fun addOkAction(callback: (() -> Unit) = {}): Alert {
        return this.copy(positiveButton = Action("OK", callback))
    }

    data class Action(
        val title: String,
        val callback: () -> (Unit)
    )
}
