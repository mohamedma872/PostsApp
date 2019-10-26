package com.sdody.postsapp.list.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.AsyncPagedListDiffer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sdody.postsapp.commons.data.local.Post;

class UserAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private final AsyncPagedListDiffer<Post> mDiffer
            = new AsyncPagedListDiffer<>(this, DIFF_CALLBACK);
    @Override
    public int getItemCount() {
        return mDiffer.getItemCount();
    }
    public void submitList(PagedList<Post> pagedList) {
        mDiffer.submitList(pagedList);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post user = mDiffer.getItem(position);
        if (user != null) {
           // holder.bindTo(user);
            holder.bind(user);
        } else {
            // Null defines a placeholder item - AsyncPagedListDiffer will automatically
            // invalidate this row when the actual object is loaded from the database
           // holder.clear();
        }
    }
    public static final DiffUtil.ItemCallback<Post> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Post>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Post oldUser, @NonNull Post newUser) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldUser.getPostId().equals(newUser.getPostId());
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull Post oldUser, @NonNull Post newUser) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldUser.equals(newUser);
                }
            };
}