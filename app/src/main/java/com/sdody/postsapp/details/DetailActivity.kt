package com.sdody.postsapp.details

import android.os.Bundle
import com.sdody.postsapp.R
import com.sdody.postsapp.application.BaseActivity
import com.sdody.postsapp.constants.Constants
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        when {
            validate(Constants.POSTTITTLE) -> {
                tittletxt.text = intent.getStringExtra(Constants.POSTTITTLE)
                bodytxt.text = intent.getStringExtra(Constants.POSTBody)
            }
        }
    }

    private fun validate(extra: String): Boolean {
        when {
            intent.hasExtra(extra) -> when {
                !(intent.getStringExtra(extra)).isNullOrEmpty() ->
                    return true
            }
        }
        return false
    }
}