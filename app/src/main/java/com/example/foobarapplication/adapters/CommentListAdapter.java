package com.example.foobarapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.R;
import com.example.foobarapplication.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {

    private CommentListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onSubmitCommentClick(List<Comment> comments);
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView commentAuthor;
        private final TextView commentContent;
        private final Button submitCommentButton;

        private CommentViewHolder(View itemView) {
            super(itemView);
            commentAuthor = itemView.findViewById(R.id.commentAuthor);
            commentContent = itemView.findViewById(R.id.commentContent);
            submitCommentButton = itemView.findViewById(R.id.submitCommentButton);
        }
    }

    private final LayoutInflater mInflater;
    private List<Comment> comments; // Initialize the list as empty

    public CommentListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.comments = new ArrayList<>(); // Ensure the list is initialized as empty
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.comment_layout, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        if (!comments.isEmpty()) { // Check if comments list is not empty
            final Comment current = comments.get(position);
            holder.commentAuthor.setText(current.getCommentAuthor());
            holder.commentContent.setText(current.getCommentContent());
        }

        holder.submitCommentButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSubmitCommentClick(comments);
            }
        });
    }

    public void addComment(Comment comment) {
        comments.add(comment); // Add the new comment to the list
        notifyItemInserted(comments.size() - 1); // Notify the adapter that an item is inserted
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setOnItemClickListener(CommentListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setComments(List<Comment> s){
        comments =s;
        notifyDataSetChanged();
    }
}
