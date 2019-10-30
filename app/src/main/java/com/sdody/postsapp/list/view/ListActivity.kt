package com.sdody.postsapp.list.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sdody.postsapp.R
import com.sdody.postsapp.application.BaseActivity
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.networking.State
import com.sdody.postsapp.constants.Constants
import com.sdody.postsapp.details.DetailActivity
import com.sdody.postsapp.list.adapter.Interaction
import com.sdody.postsapp.list.adapter.PostListAdapter
import com.sdody.postsapp.list.adapter.PostViewHolder
import com.sdody.postsapp.list.di.PostDH
import com.sdody.postsapp.list.viewmodel.ListViewModel
import com.sdody.postsapp.list.viewmodel.ListViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_post.view.*
import javax.inject.Inject

class ListActivity : BaseActivity(), Interaction, View.OnClickListener {

    private val component by lazy { PostDH.listComponent() }

    @Inject
    lateinit var viewModelFactory: ListViewModelFactory

    private val viewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    @Inject
    lateinit var postadapter: PostListAdapter

    private val context: Context by lazy { this }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
        postadapter.interaction = this
        rvPosts.adapter = postadapter
        viewModel.fetchPosts()
        fabbtn.setOnClickListener(this)
        initiateDataListener()
        //sync posts
        when {
            isConnected() -> {
                Toast.makeText(
                    context,
                    getString(R.string.sync),
                    Toast.LENGTH_LONG
                ).show()
                viewModel.getPostsNotSynced()
            }
            else -> Toast.makeText(
                context,
                getString(R.string.nointernet),
                Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun onClick(v: View?) {
        buildAlert(post = null)
    }

    private fun initiateDataListener() {

        //paging
        viewModel.postList.observe(this, Observer {
            postadapter.submitList(it)
        })
        //Observe the outcome and update state of the screen  accordingly
        viewModel.getpostCrudCallback().observe(this, Observer<State> { state ->
            if (state == State.DONE) {
                Toast.makeText(
                    context,
                    getString(R.string.succefuly),
                    Toast.LENGTH_LONG
                ).show()
            }
            if (state == State.ERROR) {
                Toast.makeText(
                    context,
                    getString(R.string.failopertaion),
                    Toast.LENGTH_LONG
                ).show()
            }
        })


    }

    private fun buildAlert(post: Post?) {
        val mBuilder = AlertDialog.Builder(this)

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_post, null)
        mBuilder.setView(mDialogView)
        if (post != null) {
            mBuilder.setTitle(getString(R.string.updatepost))
            mDialogView.tittletxt.setText(post.postTitle)
            mDialogView.bodytxt.setText(post.postBody)
        } else {
            mBuilder.setTitle(getString(R.string.newposttittle))
            mBuilder.setMessage(getString(R.string.postdetails))
        }
        //show dialog
        val mAlertDialog = mBuilder.show()
        mDialogView.dialogLoginBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            //get text from EditTexts of tittletxt layout
            val tittle = mDialogView.tittletxt.text.toString()
            val body = mDialogView.bodytxt.text.toString()
            //set the input text in TextView
            if (tittle.isEmpty() || body.isEmpty()) {
                Toast.makeText(
                    context,
                    getString(R.string.tittlecannotbeempty),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (post != null) {
                    val localpost = Post(1, post.postId, tittle, body, false, 2)
                    viewModel.updatePost(localpost)
                } else {
                    val localpost = Post(1, System.currentTimeMillis(), tittle, body, false, 1)
                    viewModel.addPost(localpost)
                }

            }

        }
        //cancel button click of custom layout
        mDialogView.dialogCancelBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }

    }
    @SuppressLint("InflateParams")


    private fun showDeleteDialog(post: Post?) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(getString(R.string.deletetittle))
        dialogBuilder.setMessage(getString(R.string.confirmdelete))
        dialogBuilder.setPositiveButton(getString(R.string.deletebtn)) { dialog, whichButton ->
            if (post != null) {
               // adapter.deleteItem(position)
                viewModel.deletePost(post)
               // adapter.notifyItemRemoved(position)

            }
        }
        dialogBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, whichButton ->
            dialog.cancel()
        }
        val b = dialogBuilder.create()
        b.show()
    }

    override fun postClicked(holder: PostViewHolder) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(
            Constants.POSTTITTLE,
            postadapter.getElementItem(holder.adapterPosition).postTitle
        )
        intent.putExtra(
            Constants.POSTBody,
            postadapter.getElementItem(holder.adapterPosition).postBody
        )
        startActivity(intent)
    }

    override fun postEdit(holder: PostViewHolder) {
        buildAlert(postadapter.getElementItem(holder.adapterPosition))
    }

    override fun postDeleted(holder: PostViewHolder) {
        showDeleteDialog(postadapter.getElementItem(holder.adapterPosition))
    }
}