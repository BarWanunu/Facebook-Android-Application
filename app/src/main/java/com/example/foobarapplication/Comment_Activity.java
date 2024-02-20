package com.example.foobarapplication;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.adapters.CommentListAdapter;
import com.example.foobarapplication.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class Comment_Activity extends AppCompatActivity implements CommentListAdapter.OnItemClickListener {
    private CommentListAdapter commentListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        RecyclerView lstComments = findViewById(R.id.lstComments);

        commentListAdapter = new CommentListAdapter(this);

        lstComments.setLayoutManager(new LinearLayoutManager(this));
        lstComments.setAdapter(commentListAdapter);
        commentListAdapter.setOnItemClickListener(this);

        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("itay", "comment"));
        commentListAdapter.setComments(comments);

    }


    @Override
    public void onSubmitCommentClick(List<Comment> comments) {
        EditText editTextComment = findViewById(R.id.editTextComment);
        String userComment = editTextComment.getText().toString();

        if (!userComment.isEmpty()) {
            Comment newComment = new Comment("itay", userComment);
            commentListAdapter.addComment(newComment); // Add comment through adapter
            editTextComment.setText(""); // Clear input field
        }
    }
}