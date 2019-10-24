package com.sdody.postsapp.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sdody.postsapp.R
import com.sdody.postsapp.application.BaseActivity
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.networking.State
import com.sdody.postsapp.list.adapter.Interaction
import com.sdody.postsapp.list.di.PostDH
import com.sdody.postsapp.list.viewmodel.ListViewModel
import com.sdody.postsapp.list.viewmodel.ListViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_post.view.*
import javax.inject.Inject

class ListActivity : BaseActivity(), Interaction, View.OnClickListener {
    override fun onClick(v: View?) {
        showNewDialog()
    }

    private val component by lazy { PostDH.listComponent() }


    @Inject
    lateinit var viewModelFactory: ListViewModelFactory

    private val viewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
        fab.setOnClickListener(this)
        initiateDataListener()

    }

    override fun postClicked(post: Post) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //DetailsActivity.start(context, post, tvTitle, tvBody, tvAuthorName, ivAvatar)
    }


    private fun initiateDataListener() {
        viewModel.PostAddedCallback.observe(this, Observer<State>{state ->
            if (state == State.LOADING)
            {

            }
            if (state == State.DONE)
            {

            }
            if (state == State.ERROR)
            {

            }
        })
        viewModel.PostDeletedCallback.observe(this, Observer<State>{state ->
            if (state == State.LOADING)
            {

            }
            if (state == State.DONE)
            {

            }
            if (state == State.ERROR)
            {

            }
        })
        viewModel.PostUpdatedCallback.observe(this, Observer<State>{state ->
            if (state == State.LOADING)
            {

            }
            if (state == State.DONE)
            {

            }
            if (state == State.ERROR)
            {

            }
        })
    }

    fun showNewDialog() {
        val mBuilder = AlertDialog.Builder(this)

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_post, null)

        mBuilder.setView(mDialogView)

        mBuilder.setTitle("New Post")
        mBuilder.setMessage("Enter Post Details")

        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.dialogLoginBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            //get text from EditTexts of custom layout
            val tittle = mDialogView.tittle.text.toString()
            val body = mDialogView.body.text.toString()
            //set the input text in TextView
            val post = Post(1, 1, tittle, body)
            viewModel.AddPost(post)
        }
        //cancel button click of custom layout
        mDialogView.dialogCancelBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

}