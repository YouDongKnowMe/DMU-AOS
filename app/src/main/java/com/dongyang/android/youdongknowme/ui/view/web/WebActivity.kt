package com.dongyang.android.youdongknowme.ui.view.web

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import com.dongyang.android.youdongknowme.R
import com.dongyang.android.youdongknowme.databinding.ActivityWebBinding
import com.dongyang.android.youdongknowme.standard.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class WebActivity : BaseActivity<ActivityWebBinding, WebViewModel>() {

    override val layoutResourceId: Int = R.layout.activity_web
    override val viewModel: WebViewModel by viewModel()

    override fun initStartView() {
        val url = intent.getStringExtra(KEY_URL)
        binding.wvWeb.loadUrl(url.toString())
        binding.btnWebClose.setOnClickListener {
            finish()
        }
    }

    override fun initDataBinding() = Unit

    override fun initAfterBinding() = Unit

    companion object {
        private const val KEY_URL = "url"

        fun newIntent(context: Context, url: String): Intent {
            return Intent(context, WebActivity::class.java).apply {
                putExtra(KEY_URL, url)
                flags = FLAG_ACTIVITY_SINGLE_TOP
            }
        }
    }
}