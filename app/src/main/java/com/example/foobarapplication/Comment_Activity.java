package com.example.foobarapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.Globals.GlobalCommentsHolder;
import com.example.foobarapplication.adapters.CommentListAdapter;
import com.example.foobarapplication.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class Comment_Activity extends AppCompatActivity implements CommentListAdapter.OnItemClickListener {
    private CommentListAdapter commentListAdapter;
    private int POST_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        POST_ID = getIntent().getIntExtra("POST_ID", -1);

        Intent intentUser = getIntent();
        UserCred User = (UserCred) intentUser.getSerializableExtra("userDetails");

        RecyclerView lstComments = findViewById(R.id.lstComments);
        final EditText editTextComment = findViewById(R.id.editTextComment); // For entering new comments
        Button submitCommentButton = findViewById(R.id.submitCommentButton); // The submit button

        commentListAdapter = new CommentListAdapter(this);

        lstComments.setLayoutManager(new LinearLayoutManager(this));
        lstComments.setAdapter(commentListAdapter);
        commentListAdapter.setOnItemClickListener(this);

        List<Comment> comments = new ArrayList<>();

        commentListAdapter.setComments(comments);

        submitCommentButton.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                List<Comment> currentComments = GlobalCommentsHolder.getCommentsByPostId(POST_ID); // Fetch the latest comments
                int id = currentComments.size() + 1; // This might need adjustment based on how you manage IDs
                Comment newComment = new Comment(id, POST_ID, User.getUsername(), commentText, Uri.parse(User.getImagePath()));
                GlobalCommentsHolder.addComment(newComment); // Add to global holder
                commentListAdapter.setComments(GlobalCommentsHolder.getCommentsByPostId(POST_ID)); // Refresh adapter with updated list
                commentListAdapter.notifyItemInserted(currentComments.size() - 1);
                Toast.makeText(this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                editTextComment.setText("");
            } else {
                Toast.makeText(this, "Please enter text before adding a comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //happens to handle the GlobalCommentsHolder list
    protected void onResume() {
        super.onResume();
        List<Comment> commentsForThisPost = GlobalCommentsHolder.getCommentsByPostId(POST_ID);
        // Update your adapter with commentsForThisPost
        commentListAdapter.setComments(commentsForThisPost);
        commentListAdapter.notifyDataSetChanged();
    }

    //edit comment button was pressed
    @Override
    public void onEditCommentClick(int position) {
        Comment commentToEdit = commentListAdapter.getCommentAt(position);
        final EditText editTextField = new EditText(this);
        editTextField.setText(commentToEdit.getCommentContent());

        new AlertDialog.Builder(this)
                .setTitle("Edit Comment")
                .setView(editTextField)
                .setPositiveButton("Update", (dialog, which) -> {
                    String updatedCommentText = editTextField.getText().toString();
                    if (updatedCommentText.isEmpty()) {
                        Toast.makeText(Comment_Activity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if (updatedCommentText.equals(commentToEdit.getCommentContent())) {
                        Toast.makeText(Comment_Activity.this, "Comment did not edit", Toast.LENGTH_SHORT).show();
                    } else {
                        commentToEdit.setCommentContent(updatedCommentText);
                        commentListAdapter.notifyItemChanged(position);
                        GlobalCommentsHolder.getInstance().updateComment(commentToEdit.getCommentId(), updatedCommentText);
                        Toast.makeText(Comment_Activity.this, "Comment edited", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    //delete comment button was pressed
    @Override
    public void onDeleteCommentClick(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Comment")
                .setMessage("Are you sure you want to delete this comment?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Get the comment to delete
                    Comment commentToDelete = commentListAdapter.getCommentAt(position);
                    // Remove from GlobalCommentsHolder
                    GlobalCommentsHolder.getInstance().deleteComment(commentToDelete.getCommentId());
                    // Refresh the adapter
                    commentListAdapter.setComments(GlobalCommentsHolder.getCommentsByPostId(POST_ID));
                    commentListAdapter.notifyItemChanged(position);
                    Toast.makeText(Comment_Activity.this, "Comment deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}