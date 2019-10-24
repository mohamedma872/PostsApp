package com.sdody.postsapp.details

import android.os.Bundle
import com.sdody.postsapp.R
import com.sdody.postsapp.application.BaseActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        var tittletxt: String = intent.getStringExtra("tittle")
        var bodytxt: String = intent.getStringExtra("body")
        tittle.setText(tittletxt)
        body.setText(bodytxt)
    }
}