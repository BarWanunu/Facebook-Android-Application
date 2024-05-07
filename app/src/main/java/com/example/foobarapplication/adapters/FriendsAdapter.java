package com.example.foobarapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.R;
import com.example.foobarapplication.entities.User;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private Context context;
    private List<User> friendsList;

    public FriendsAdapter(Context context, List<User> friendsList) {
        this.context = context;
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User friend = friendsList.get(position);
        // Bind data to views in the ViewHolder
        holder.friendNameTextView.setText(friend.getUserName());
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView friendNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendNameTextView = itemView.findViewById(R.id.friendNameTextView);
        }
    }
}


