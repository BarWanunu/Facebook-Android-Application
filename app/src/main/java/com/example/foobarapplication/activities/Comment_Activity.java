package com.example.foobarapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.R;
import com.example.foobarapplication.adapters.CommentListAdapter;
import com.example.foobarapplication.entities.Comment;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.repositories.CommentsRepository;

import java.util.ArrayList;
import java.util.List;

public class Comment_Activity extends AppCompatActivity implements CommentListAdapter.OnItemClickListener {
    private CommentListAdapter commentListAdapter;
    private int POST_ID;
    CommentsRepository repository;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        POST_ID = getIntent().getIntExtra("POST_ID", -1);
        repository = new CommentsRepository(this);
        Intent intentUser = getIntent();
        user = (com.example.foobarapplication.entities.User) intentUser.getSerializableExtra("userDetails");

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
                // Fetch the latest comments
                List<Comment> currentComments = repository.getCommentsOfPost(POST_ID);
                int id = currentComments.size() + 1;
                Comment newComment = new Comment(id, POST_ID, user.getUserName(), commentText, user.getPhoto());
                repository.add(newComment);
                // Refresh adapter with updated list
                commentListAdapter.setComments(repository.getCommentsOfPost(POST_ID));
                commentListAdapter.notifyItemInserted(currentComments.size() - 1);
                Toast.makeText(this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                editTextComment.setText("");
            } else {
                Toast.makeText(this, "Please enter text before adding a comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Happens to handle the GlobalCommentsHolder list
    protected void onResume() {
        super.onResume();
        List<Comment> commentsForThisPost = repository.getCommentsOfPost(POST_ID);
        // Update your adapter with commentsForThisPost
        commentListAdapter.setComments(commentsForThisPost);
        commentListAdapter.notifyDataSetChanged();
    }

    //edit comment button was pressed
    @Override
    public void onEditCommentClick(int position) {
        Comment commentToEdit = commentListAdapter.getCommentAt(position);
        if (user.getUserName().equals(commentToEdit.getCommentAuthor())){
            final EditText editTextField = new EditText(this);
            editTextField.setText(commentToEdit.getCommentContent());

            new AlertDialog.Builder(this)
                    .setTitle("Edit Comment")
                    .setView(editTextField)
                    .setPositiveButton("Update", (dialog, which) -> {
                        String updatedCommentText = editTextField.getText().toString();
                        if (updatedCommentText.isEmpty()) {
                            // Show error message if updated comment text is empty
                            Toast.makeText(Comment_Activity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                        } else if (updatedCommentText.equals(commentToEdit.getCommentContent())) {
                            // Show message if comment is not edited
                            Toast.makeText(Comment_Activity.this, "Comment did not edit", Toast.LENGTH_SHORT).show();
                        } else {
                            // Refresh the adapter
                            commentToEdit.setCommentContent(updatedCommentText);
                            commentListAdapter.notifyItemChanged(position);
                            repository.update(commentToEdit);
                            Toast.makeText(Comment_Activity.this, "Comment edited", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
        else{
            // Show message if user is not authorized to edit the comment
            new AlertDialog.Builder(this).setMessage("Can't edit comments of other users").show();
        }

    }

    // Delete comment button was pressed
    @Override
    public void onDeleteCommentClick(int position) {
        Comment commentToDelete = commentListAdapter.getCommentAt(position);
        if (user.getUserName().equals(commentToDelete.getCommentAuthor())) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Comment")
                    .setMessage("Are you sure you want to delete this comment?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        // Remove comment from room
                        repository.delete(commentToDelete);
                        // Refresh the adapter
                        commentListAdapter.setComments(repository.getCommentsOfPost(POST_ID));
                        commentListAdapter.notifyItemChanged(position);
                        Toast.makeText(Comment_Activity.this, "Comment deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
        else{
            // Show message if user is not authorized to delete the comment
            new AlertDialog.Builder(this).setMessage("Can't delete comments of other users").show();
        }
    }
}