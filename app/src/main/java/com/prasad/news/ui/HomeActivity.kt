package com.prasad.news.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.prasad.news.R
import com.prasad.news.core.ui.ConnectivityChecker
import com.prasad.news.core.ui.model.NewsProgressUiModel
import com.prasad.news.core.ui.model.NewsUiModel
import com.prasad.news.dagger.inject
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {
    private lateinit var snackBar: Snackbar

    @Inject
    @JvmField
    var connectivityChecker: ConnectivityChecker? = null

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)
        setContentView(R.layout.activity_main)
        noInternetConnectionSnackbar()
        connectivityChecker?.apply {
            lifecycle.addObserver(this)
            connectedStatus.observe(this@HomeActivity, Observer<Boolean> {
                if (it) {
                    handleNetworkConnected()
                } else {
                    handleNoNetworkConnection()
                }
            })
        } ?: handleNoNetworkConnection()
        initializeViews()
    }

    private fun initializeViews() {

        viewModel.newsProgress.observe(this@HomeActivity, Observer<NewsProgressUiModel> {
            if (it.isLoading) {
                // TODO: "Loading Progressbar"
            } else {
                // TODO: "Loading Progressbar Gone"
            }
        })

        viewModel.getNewsItem().observe(this@HomeActivity, Observer<NewsUiModel> {
            Log.d("Response:",it.items.toString())
            dataText.text = it.items.articles[2].content
        })

    }

    private fun handleNoNetworkConnection() {
        snackBar.show()
    }

    private fun handleNetworkConnected() {
        if (snackBar.isShown) {
            snackBar.dismiss()
        }
    }

    private fun noInternetConnectionSnackbar() {
        snackBar = Snackbar.make(
            findViewById(android.R.id.content),
            "No Internet connecion",
            Snackbar.LENGTH_INDEFINITE
        )
    }
}