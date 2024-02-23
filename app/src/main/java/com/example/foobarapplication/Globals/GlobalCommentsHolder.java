package com.example.foobarapplication.Globals;

import com.example.foobarapplication.entities.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalCommentsHolder {
    private static final List<Comment> comments = new ArrayList<>();
    private static GlobalCommentsHolder instance;

    private GlobalCommentsHolder(){}

    public static synchronized GlobalCommentsHolder getInstance() {
        if (instance == null) {
            instance = new GlobalCommentsHolder();
        }
        return instance;
    }

    public static void addComment(Comment comment) {
        comments.add(comment);
    }

    public static List<Comment> getCommentsByPostId(int postId) {
        return comments.stream()
                .filter(comment -> comment.getPostId() == postId)
                .collect(Collectors.toList());
    }

    public void updateComment(int commentId, String updatedCommentText) {
        for (Comment comment : comments) {
            if (comment.getCommentId() == commentId) {
                comment.setCommentContent(updatedCommentText);
                break;
            }
        }
    }

    public void deleteComment(int commentId) {
        comments.removeIf(comment -> comment.getCommentId() == commentId);
    }
}
