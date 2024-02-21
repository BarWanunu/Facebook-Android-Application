package com.example.foobarapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.R;
import com.example.foobarapplication.entities.Post;

import java.util.List;

public class PostsListAdapter  extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    private PostsListAdapter.OnItemClickListener listener;



    public interface OnItemClickListener{

        void onShareClick();

        void onLikeClick();

        void onCommentClick(int postId);

        void onOptionClick(int id);
    }
    class PostViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final ImageButton shareButton;
        private final ImageButton likeButton;
        private final ImageButton commentButton;
        private final ImageView post_option;

        private PostViewHolder(View itemView){
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            shareButton = itemView.findViewById(R.id.shareButton);
            likeButton= itemView.findViewById(R.id.likeButton);
            commentButton= itemView.findViewById(R.id.commentButton);
            post_option= itemView.findViewById(R.id.post_options);

        }
    }

    private final LayoutInflater mInflater;
    private List<Post> posts;

    public PostsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {

        if (posts != null){
            final Post current = posts.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
            holder.ivPic.setImageResource(current.getPic());
        }

        //share button was pressed
        holder.shareButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onShareClick();
            }
        });

        //like button was pressed
        holder.likeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLikeClick();
            }
        });

        //comment button was pressed
        holder.commentButton.setOnClickListener(v -> {
            if (listener != null) {
                int adapterPosition = holder.getAdapterPosition(); // Use a different variable name if necessary
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Post currentPost = posts.get(adapterPosition);
                    listener.onCommentClick(currentPost.getId()); // Correctly reference the position
                }
            }
        });
        holder.post_option.setOnClickListener(v -> {
            if (listener != null) {
                int adapterPosition = holder.getAdapterPosition(); // Use a different variable name if necessary
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Post currentPost = posts.get(adapterPosition);
                    listener.onOptionClick(currentPost.getId());
                }
            }
        });





    }
    public void setOnItemClickListener(PostsListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    public void setPosts(List<Post> s){
        posts =s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (posts!=null)
            return posts.size();
        return 0;
    }

    public List<Post> getPosts() {
        return posts;
    }



}
