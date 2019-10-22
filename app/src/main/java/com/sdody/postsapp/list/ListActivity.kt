package com.sdody.postsapp.list

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.sdody.postsapp.R
import com.sdody.postsapp.application.BaseActivity
import com.sdody.postsapp.commons.data.local.Post
import com.sdody.postsapp.list.viewmodel.ListViewModel
import com.sdody.postsapp.list.viewmodel.ListViewModelFactory
import javax.inject.Inject

class ListActivity : BaseActivity(), Interaction {

    @Inject
    lateinit var viewModelFactory: ListViewModelFactory

    private val viewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun postClicked(post: Post) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //DetailsActivity.start(context, post, tvTitle, tvBody, tvAuthorName, ivAvatar)
    }
//    holder.view.btnDelete.setOnClickListener { showDeleteDialog(holder, movies[position]) }
//    holder.view.btnEdit.setOnClickListener { showUpdateDialog(holder, movies[position]) }
//    fun showUpdateDialog(holder: MovieViewHolder, post: Post) {
//        val dialogBuilder = AlertDialog.Builder(holder.view.context)
//
//        val input = EditText(holder.view.context)
//        val lp = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.MATCH_PARENT)
//        input.layoutParams = lp
//        input.setText(post.postTitle)
//
//        dialogBuilder.setView(input)
//
//        dialogBuilder.setTitle("Update Movie")
//        dialogBuilder.setPositiveButton("Update", { dialog, whichButton ->
//           // updateMovie(Movie(movie.id,input.text.toString()))
//        })
//        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
//            dialog.cancel()
//        })
//        val b = dialogBuilder.create()
//        b.show()
//    }
//
//    fun showDeleteDialog(holder: MovieViewHolder, post: Post) {
//        val dialogBuilder = AlertDialog.Builder(holder.view.context)
//        dialogBuilder.setTitle("Delete")
//        dialogBuilder.setMessage("Confirm delete?")
//        dialogBuilder.setPositiveButton("Delete", { dialog, whichButton ->
//         //   deleteMovie(movie)
//        })
//        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
//            dialog.cancel()
//        })
//        val b = dialogBuilder.create()
//        b.show()
//    }
//
//    fun showNewDialog() {
//        val dialogBuilder = AlertDialog.Builder(this)
//
//        val input = EditText(this)
//        val lp = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.MATCH_PARENT)
//        input.layoutParams = lp
//
//        dialogBuilder.setView(input)
//
//        dialogBuilder.setTitle("New Post")
//        dialogBuilder.setMessage("Enter Tittle Below")
//        dialogBuilder.setPositiveButton("Save", { dialog, whichButton ->
//            //adapter.addMovie(Movie(0,input.text.toString()))
//        })
//        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
//            //pass
//        })
//        val b = dialogBuilder.create()
//        b.show()
//    }
}