package com.kamilh.findmysong.views.main

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kamilh.findmysong.R
import com.kamilh.findmysong.base.BaseActivity
import com.kamilh.findmysong.data.Alert
import com.kamilh.findmysong.extensions.observeNotNull
import com.kamilh.findmysong.views.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        show(SearchFragment.newInstance())

        setUpObservables()

        setSupportActionBar(toolbar)
    }

    private fun show(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commitAllowingStateLoss()
    }

    private fun setUpObservables() {
        observeNotNull(viewModel.alert, this::showDialog)
    }

    private fun dialog(alert: Alert): AlertDialog {
        val builder = AlertDialog.Builder(this)
            .setTitle(alert.title)
            .setMessage(alert.message)
            .setCancelable(alert.isCancelable)

        alert.positiveButton?.let {
            builder.setPositiveButton(it.title) { _, _ ->
                it.callback()
            }
        }

        alert.negativeButton?.let {
            builder.setNegativeButton(it.title) { _, _ ->
                it.callback()
            }
        }

        alert.cancelAction?.let { action ->
            builder.setOnCancelListener { action() }
        }

        return builder.create()
    }

    private fun showDialog(alert: Alert) {
        dialog(alert).show()
    }
}
