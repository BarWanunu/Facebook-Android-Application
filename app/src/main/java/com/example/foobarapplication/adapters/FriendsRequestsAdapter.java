package com.example.foobarapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.R;
import com.example.foobarapplication.entities.User;

import java.util.List;

public class FriendsRequestsAdapter extends RecyclerView.Adapter<FriendsRequestsAdapter.ViewHolder> {


    private Context context;
    private List<User> friendRequests;
    private OnRequestActionClickListener listener;

    public FriendsRequestsAdapter(Context context, OnRequestActionClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setFriendRequests(List<User> friendRequests) {
        this.friendRequests = friendRequests;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (friendRequests.get(0).getUserName().equals("You don't have any friends requests")) {
            view = LayoutInflater.from(context).inflate(R.layout.no_friends_request, parent, false);
            return new NoFriendRequestViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.friend_request_item_layout, parent, false);
            return new ViewHolder(view);
        }
    }

    // ViewHolder for the "no friend request" layout
    private static class NoFriendRequestViewHolder extends ViewHolder {
        public NoFriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize any views specific to the "no friend request" layout here
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (friendRequests.get(0).getUserName().equals("You don't have any friends requests")) {
            // Set a message indicating no friend requests
            holder.friendNameTextView.setText("You don't have any friends requests");
        } else {
            // Bind friend request data
            User friendRequest = friendRequests.get(position);
            holder.friendNameTextView.setText(friendRequest.getUserName());

            // Show action buttons
            holder.approveButton.setVisibility(View.VISIBLE);
            holder.declineButton.setVisibility(View.VISIBLE);

            // Set click listeners for the action buttons
            holder.approveButton.setOnClickListener(v -> listener.onApproveClick(friendRequest));
            holder.declineButton.setOnClickListener(v -> listener.onDeclineClick(friendRequest));
        }
    }




    @Override
    public int getItemCount() {
        return friendRequests == null || friendRequests.isEmpty() ? 1 : friendRequests.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView friendNameTextView;
        ImageButton approveButton;
        ImageButton declineButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendNameTextView = itemView.findViewById(R.id.friendNameTextView);
            approveButton = itemView.findViewById(R.id.approveButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }

    // Interface for handling click events on action buttons
    public interface OnRequestActionClickListener {
        void onApproveClick(User user);

        void onDeclineClick(User user);
    }
}
