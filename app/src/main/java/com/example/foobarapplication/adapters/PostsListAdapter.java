package com.example.foobarapplication.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foobarapplication.R;
import com.example.foobarapplication.entities.Post;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class PostsListAdapter  extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    private OnItemClickListener listener;
    private boolean isDarkMode;
    private final LayoutInflater mInflater;
    private List<Post> posts;
    private Boolean isLiked= false;
    public interface OnItemClickListener{

        void onShareClick();

        void onLikeClick(Post id, Boolean isLiked);

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
        private final TextView likesTextView;
        private final ImageView profilePicture;
        private boolean isDarkMode;


        private PostViewHolder(View itemView, boolean isDarkMode){
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            shareButton = itemView.findViewById(R.id.shareButton);
            likeButton= itemView.findViewById(R.id.likeButton);
            commentButton= itemView.findViewById(R.id.commentButton);
            post_option= itemView.findViewById(R.id.post_options);
            likesTextView = itemView.findViewById(R.id.likes);
            this.isDarkMode = isDarkMode;
            this.profilePicture = itemView.findViewById(R.id.profile_picture);

        }
    }


    public PostsListAdapter(Context context, boolean isDarkMode) {
        mInflater = LayoutInflater.from(context);
        this.isDarkMode = isDarkMode;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(itemView, isDarkMode);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        if (posts != null){
            final Post current = posts.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
            Log.d("Image URIs", "Post Image URI: " + current.getPic()+ position);
            Log.d("Image URIs", "Profile Picture URI: " + current.getuProfilePicture());
            int currentpic=current.getPic();
            int currentprofilepic= current.getProfilePicture();
            if(currentpic==-1&&currentprofilepic==-1){
                Picasso.get().load(current.getuPic()).into(holder.ivPic);
                Picasso.get().load(current.getuProfilePicture()).into(holder.profilePicture);
            } else if (currentpic != -1 && currentprofilepic == -1) {
                Picasso.get().load(current.getPic()).into(holder.ivPic);
                Picasso.get().load(current.getuProfilePicture()).into(holder.profilePicture);
            }
            else{
                holder.ivPic.setImageResource(current.getPic());
                holder.profilePicture.setImageResource(current.getProfilePicture());
            }

            holder.likesTextView.setText(String.format(Locale.getDefault(), "%d likes", current.getLikes()));;

            // Use the isDarkMode parameter from the ViewHolder constructor
            if (isDarkMode) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grey));
                holder.post_option.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grey));
                holder.tvAuthor.setTextColor(Color.WHITE);
                holder.tvContent.setTextColor(Color.WHITE);
                // Set other dark mode styles
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
                holder.post_option.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));

                holder.tvAuthor.setTextColor(Color.BLACK);
                holder.tvContent.setTextColor(Color.BLACK);
            }
        }

        //check if the shareButton was pressed
        holder.shareButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onShareClick();
            }
        });

        //check if the likeButton was pressed
        holder.likeButton.setOnClickListener(v -> {
            if (listener != null) {
                int adapterPosition = holder.getAdapterPosition();
                if(adapterPosition != RecyclerView.NO_POSITION) {

                    Post currentPost = posts.get(adapterPosition);
                    int id=currentPost.getId();
                    listener.onLikeClick(currentPost,isLiked);
                    isLiked=!isLiked;
                }
            }
        });

        //check if the commentButton was pressed
        holder.commentButton.setOnClickListener(v -> {
            if (listener != null) {
                int adapterPosition = holder.getAdapterPosition(); // Use a different variable name if necessary
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Post currentPost = posts.get(adapterPosition);
                    listener.onCommentClick(currentPost.getId()); // Correctly reference the position
                }
            }
        });

        //check if the post_option (edit or delete) was pressed
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

    public void setDarkMode(boolean darkMode) {
        isDarkMode = darkMode;
        notifyDataSetChanged();
    }
}
