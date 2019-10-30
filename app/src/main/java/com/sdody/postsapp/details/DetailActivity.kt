package com.sdody.postsapp.details

import android.os.Bundle
import com.sdody.postsapp.R
import com.sdody.postsapp.application.BaseActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (intent.hasExtra("tittle")) {
            if (!(intent.getStringExtra("tittle")).isNullOrEmpty()) {
                val tittletxt: String = intent.getStringExtra("tittle")
                tittle.text = tittletxt
            }
        }
        if (intent.hasExtra("body")) {
            if (!(intent.getStringExtra("body")).isNullOrEmpty()) {
                val bodytxt: String = intent.getStringExtra("body")
                body.text = bodytxt
            }
        }
    }
}