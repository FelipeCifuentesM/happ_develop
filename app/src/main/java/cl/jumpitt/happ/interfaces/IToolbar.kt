package cl.jumpitt.happ.interfaces

import androidx.appcompat.widget.Toolbar


interface IToolbar {
    fun toolbarToLoad(toolbar: Toolbar?, title: String?)
    fun enableHomeDisplay(value: Boolean)
    fun toolbarToLoadFragment(toolbar: Toolbar?, title: String?)
}