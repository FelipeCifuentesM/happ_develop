package cl.jumpitt.happ.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import cl.jumpitt.happ.R
import cl.jumpitt.happ.interfaces.IToolbar

open class ToolbarActivity : AppCompatActivity(), IToolbar {
    private var _toolbar: Toolbar? = null

    override fun toolbarToLoad(toolbar: Toolbar?, title: String?) {
        _toolbar = toolbar
        _toolbar?.title = title
        _toolbar?.let { setSupportActionBar(_toolbar) }
        _toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun toolbarToLoadFragment(toolbar: Toolbar?, title: String?) {
        _toolbar = toolbar
        _toolbar?.title = title
        _toolbar?.setNavigationIcon(R.drawable.ic_close)
        _toolbar?.let { setSupportActionBar(_toolbar) }
    }

    override fun enableHomeDisplay(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }

}