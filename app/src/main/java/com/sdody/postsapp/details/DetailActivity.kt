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

    fun validate(extraname: String): Boolean {
        when {
            intent.hasExtra(extraname) -> if (!(intent.getStringExtra(extraname)).isNullOrEmpty()) {
                return true
            }
        }
        return false
    }
}