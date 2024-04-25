package com.example.foobarapplication.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Post implements Comparable<Post>{
    @PrimaryKey
    private int id;
    private String profile;
    private String text;
    private String date;
    private int likes;
    private String img;
    private String profileImg;
    private boolean isLiked = false;

    public Post() {
    }

    // Constructor with essential fields
    public Post(String author, String content, String date, int likes) {
        id = 0;
        this.profile = author;
        this.text = content;
        this.date = date;
        this.likes = likes;
        if (this.date.endsWith("\""))
            this.date = this.date.substring(0, this.date.length()-1);
    }

    // Constructor with image URLs
    public Post(String author, String content, String date, int likes, String sPic, String sProfilePicture) {
        this(author, content, date, likes);
        this.img = sPic;
        this.profileImg = sProfilePicture;
    }

    // Constructor with profile image URL
    public Post(String author, String content, String date, int likes, String sProfilePicture) {
        this(author, content, date, likes);
        this.profileImg = sProfilePicture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return profile;
    }

    public void setAuthor(String author) {
        this.profile = author;
    }

    public String getContent() {
        return text;
    }

    public void setContent(String content) {
        this.text = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getCurrentDate() {
        Date d = new Date();
        try {
            d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(date);
        } catch (ParseException ignored) {
        }
        return new SimpleDateFormat("dd-MM-yyyy").format(d);
    }

    public String getsPic(){
        return img;
    }
    public String getsProfilePicture(){
        return profileImg;
    }

    public boolean getIsLiked() {
        return isLiked;
    }
    public void setLiked(boolean liked) {
        this.isLiked = liked;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.post_layout);
//        ImageButton likeBtn = findViewById(R.id.likeButton);
//        likeBtn.setOnClickListener(v -> {
//            if (isLiked == false) {
//                isLiked = true;
//                addLike();
//            } else {
//                isLiked = false;
//                unLike();
//            }
//        });
//    }
    public String getProfile() {
        return profile;
    }

    public String getImg() {
        return img;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getText() {
        return text;
    }
    public void setImg(String img) {
        this.img = img;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        if (date.endsWith("\""))
            date = date.substring(0, date.length()-1);
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(Post o) {
        Date numberDate = null;
        try {
            numberDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(date);
            Date oNumberDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(o.date);
            if (numberDate.getTime() > oNumberDate.getTime())
                return -1;
            if (numberDate.getTime() == oNumberDate.getTime())
                return 0;
            return 1;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }



    /*
    public static List<Post> fromServerModel(List<Post2> serverPosts) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        List<Post> posts = new ArrayList<>();
        for (Post2 serverPost : serverPosts) {
            Post post = new Post();
            post.setId(serverPost.getId());
            post.setAuthor(serverPost.getProfile());
            post.setContent(serverPost.getText());
            outputDateFormat.format(serverPost.getDate());
            post.setLikes(serverPost.getLikes());
            // Set other fields accordingly based on the server model
            posts.add(post);
        }
        return posts;
    }

     */
}

