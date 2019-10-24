package com.sdody.postsapp.list.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sdody.postsapp.R
import com.sdody.postsapp.application.BaseActivity
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.commons.networking.Outcome
import com.sdody.postsapp.commons.networking.State
import com.sdody.postsapp.details.DetailActivity
import com.sdody.postsapp.list.adapter.Interaction
import com.sdody.postsapp.list.adapter.PostListAdapter
import com.sdody.postsapp.list.di.PostDH
import com.sdody.postsapp.list.viewmodel.ListViewModel
import com.sdody.postsapp.list.viewmodel.ListViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_post.view.*
import java.io.IOException
import javax.inject.Inject

class ListActivity : BaseActivity(), Interaction, View.OnClickListener {


    private val component by lazy { PostDH.listComponent() }


    @Inject
    lateinit var viewModelFactory: ListViewModelFactory

    private val viewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    @Inject
    lateinit var adapter: PostListAdapter

    private val context: Context by lazy { this }


    private val TAG = "ListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
        adapter.interaction = this
        rvPosts.adapter = adapter
        viewModel.fetchPosts()
        fab.setOnClickListener(this)
        initiateDataListener()


    }

    override fun onClick(v: View?) {
        showNewDialog()
    }


    private fun initiateDataListener() {

        //paging
        viewModel.postList.observe(this, Observer {
            adapter.submitList(it)
        })
        //Observe the outcome and update state of the screen  accordingly
        // i will use it when request data on demand
        viewModel.postsOutcome.observe(this, Observer<Outcome<List<Post>>> { outcome ->
            Log.d(TAG, "initiateDataListener: $outcome")
            when (outcome) {

                //is Outcome.Progress -> srlPosts.isRefreshing = outcome.loading

                is Outcome.Success -> {
                    Log.d(TAG, "initiateDataListener: Successfully loaded data")
                    // adapter.submitList(outcome.data)
                }

                is Outcome.Failure -> {

                    if (outcome.e is IOException)
                        Toast.makeText(
                            context,
                            "Need internet to fetch latest posts!",
                            Toast.LENGTH_LONG
                        ).show()
                    else
                        Toast.makeText(
                            context,
                            "Failed to load posts. Please try again later.",
                            Toast.LENGTH_LONG
                        ).show()
                }

            }
        })
        viewModel.getAddedCallback().observe(this, Observer<State> { state ->
            //            if (state == State.LOADING) {
//
//            }
            if (state == State.DONE) {
                Toast.makeText(
                    context,
                    "Adding succefuly",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (state == State.ERROR) {
                Toast.makeText(
                    context,
                    "Adding fail",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        viewModel.getDeletedCallback().observe(this, Observer<State> { state ->
            //            if (state == State.LOADING) {
//
//            }
            if (state == State.DONE) {
                Toast.makeText(
                    context,
                    "Deleting succefuly",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (state == State.ERROR) {
                Toast.makeText(
                    context,
                    "Deleting fail",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        viewModel.getUpdatedCallback().observe(this, Observer<State> { state ->
            //            if (state == State.LOADING) {
//
//            }
            if (state == State.DONE) {
                Toast.makeText(
                    context,
                    "Updating succefuly",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (state == State.ERROR) {
                Toast.makeText(
                    context,
                    "Updating fail",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun showNewDialog() {
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
            val post = Post(1, System.currentTimeMillis(), tittle, body,false)
            viewModel.addPost(post)
        }
        //cancel button click of custom layout
        mDialogView.dialogCancelBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    private fun showDeleteDialog(position: Int, post: Post?) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Delete")
        dialogBuilder.setMessage("Confirm delete?")
        dialogBuilder.setPositiveButton("Delete") { dialog, whichButton ->
            if (post != null) {
                viewModel.deletePost(post)
                adapter.deleteItem(position)
            }
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, whichButton ->
            dialog.cancel()
        }
        val b = dialogBuilder.create()
        b.show()
    }

    private fun showUpdateDialog(position: Int, post: Post?) {
        val mBuilder = AlertDialog.Builder(this)

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_post, null)

        mBuilder.setView(mDialogView)

        mBuilder.setTitle("Update Post")
        if (post != null) {
            mDialogView.tittle.setText(post.postTitle)
            mDialogView.body.setText(post.postBody)
        }
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
            if (post != null) {

                val localpost = Post(1, post.postId, tittle, body,false)
                viewModel.updatePost(localpost)
                adapter.updateItem(localpost, position)
            }

        }
        //cancel button click of custom layout
        mDialogView.dialogCancelBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    override fun postClicked(post: Post) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("tittle", post.postTitle)
        intent.putExtra("body", post.postBody)
        startActivity(intent)
    }

    override fun postEdit(post: Post?, position: Int) {
        showUpdateDialog(position, post)
    }

    override fun postDeleted(post: Post?, position: Int) {
        showDeleteDialog(position, post)
    }
}