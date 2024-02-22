package com.example.foobarapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.R;
import com.example.foobarapplication.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {

    private CommentListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditCommentClick(int position);

        void onDeleteCommentClick(int position);
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView commentAuthor;
        private final TextView commentContent;

        private final ImageButton editCommentButton;
        private final ImageButton deleteCommentButton;

        private CommentViewHolder(View itemView) {
            super(itemView);
            commentAuthor = itemView.findViewById(R.id.commentAuthor);
            commentContent = itemView.findViewById(R.id.commentContent);
            editCommentButton = itemView.findViewById(R.id.editCommentButton);
            deleteCommentButton = itemView.findViewById(R.id.deleteCommentButton);
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

        holder.editCommentButton.setOnClickListener(v -> {
            if (listener != null){
                listener.onEditCommentClick(position);
            }
        });

        holder.deleteCommentButton.setOnClickListener(v -> {
            if (listener != null){
                listener.onDeleteCommentClick(position);
            }
        });

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

    public Comment getCommentAt(int position) {
        return comments.get(position);
    }
}
