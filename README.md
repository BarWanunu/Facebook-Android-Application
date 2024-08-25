This page is refering to the facebook app on android. The link to the repository is : https://github.com/shakedg1234/Foobar_Application/tree/ex3_branch

## Before we begin
In oreder to demonstrate all of the functions and actions you can do in our app you can check out the next video if you would like to : <br>
https://www.youtube.com/watch?v=9AOOTYsxuxs

This facebook app was developed in android studio envionement and it's linked to the Node Js server and the TCP, so please make sure they're both running before you work on this app. <br>
The work on this app in android studio is divided to 2 parts. In the first part we worked only on the basic functions as login screen, and sign in screen of course with all of the functionality. <br>
We had hard coded users we could log in with and inside the main page you could scroll down and see hard coded feed from a json list of posts. <br>
In the second part, we developed the app to make all of the required functions with the server when we need to fetch data or to post data.
If you want to read about each part you can go to specific repositories. <br>
Use the link we gave above for the second part, and for the first part just switch to the maim branch.

## Running the app
Now let's strat the app ! <br>
We will guide you with several steps in order to run the android app. <br>
First, make the Node Js server is running as required. <br>
Afterwards, make sure you download the Pixel 2 device and then press "Sync Project with Gradle Files" button. <br>
Then, make sure the configuration is App and you can start running our facebook app by pressing the green triangle !! <br>
We hope you'll enjoy your experience !

## Login and Sign in
The first page you'll encounter after starting the app is the Sign in page. <br>
In order to sign in to our facebook app you can take one of the users from the mongoDB or to create one for yourself. <br>
If you want to make your own profile you can press the sign up button. <br>
<img width="191" alt="image" src="https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/34c623cf-f02e-440e-9fac-6feaabbc3e3b"> <br>
Afterwards, you'll be transferd to the sign up page where you'll need to fill the required fields. <br>
<img width="191" alt="image" src="https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/a7cac971-b2e9-4151-bcf3-67c5e7a18142"> <br>
Be aware, you'll have to fill all of the fields in order to finish the form. <br>
For example - the email address you sign up with must be from the pattern of name@example.com. <br>
For the password choosing it must be in the length of minimum 8 characters and contain letters and numbers. Afterwards, you will have to confirm your password and the values must be identical of course. <br>
After you'll choose your display name for the application and the photo that will be shown you'll get a massage that the form was submmited successfuly and you'll find yourself back in the sign in page. <br>
After you sign in with us you'll get a spesific token that will allow you to make some of the activities we offer for our users by making sure you are indeed the person who are authorized to make the action. <br>

## Feed Page Activity
Welcome to the main center of our app ! <br>
In here, you can see the posts of your friends and also people who are not your friends. <br>
So, what you can do in this page ? Let's walkthrough all of the things we can do in this app. <br>
On the top of the phone you'll see the facebook logo to the right and 3 icons.
Underneath, there's the box where you as the user can let us know what's on your mind. All you need to do is write it and click add. Be aware you can also add a photo with your post, if you'll choose to post only a text, there won't be a photo that will attach automaticlly to the post. <br>
![image](https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/574b07b7-fb43-48d1-9215-988cb12f70ee) <br>


At the bottom of the page you'll see our ruler of actions.
We have 2 ImageButtons that will respond.
The first one, is the most left one - the menu button. In one click you'll see a few options pop up. <br>

![image](https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/d64a81cb-5d0f-4ad3-b155-026cd8321455) <br>


1) Light/Dark mode - you can adjust the visuality of the phone as you like by making it dark mode or light mode.
2) Edit name, Edit Image, Delete User - all of this functions will change/erase your profile as you would choose.
3) My Profile - you'll be transfered in to your profile and you'll be able to see all of your posts.
4) Log Out - you'll log out from your account back to the sign in page.

The second option, is clicking on the friends button where it will pop up 2 options : <br>

![image](https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/884f45b3-75e1-4a98-b08b-b6db9a417697) <br>


1) My Friends - by choosing that you'll see a list that contains all of your friends. <br>
<img width="193" alt="image" src="https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/f592249a-c399-4b98-aa64-617ba2d9c853"> <br>

2) Friends Requests - A list of all your friend requests will pop up, and you can choose whom to accept and whom to decline. <br>

![image](https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/63adc398-865a-4fc4-a8dc-6ada7c9b8e61) <br>

In our facebook as mentions above you'll be able to see who are your friends at any time by cliking My Friends button.
Moreover, when you'll see a post of your friend, you can press on his photo and you'll be able to choose if you want to remove this friend from your friends list or to enter his profile and see all of his posts. <br>
![image](https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/10089344-d168-4ea0-a136-4cbd4bc302a9) <br>
If you suddenly, encounter with a post of someome who isn't your friend and you want to add him as a friend, just click on his photo and you'll have the option to add him as a friend by seding him a friend request and wait until he will either approve it or reject. <br>
<img width="188" alt="image" src="https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/1ffccb18-4494-47ca-9d52-e5caf443156b"> <br>

In our app you have the opportunity to enter to every one of your friends profile. Overthere, you'll see all of the spesific friend posts. You are not able to edit or delete any of them, an appropriate message will be displayed. <br>
![image](https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/f693be15-3579-4fdf-b541-2e78aeb3faa8) <br>

Also, if you would like to see all of your friend friends just click on the friends icon and select Profile Friends. <br>
![image](https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/d771fd7a-823f-4f29-a217-4314850dc523) <br>

If you'll enter your own profile you'll be able to edit or delete your posts. <br>
![image](https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/3023f415-46cc-4750-abe9-c962c71e759a) <br>

When entering to profile (even if it is your profile) you won't be able to edit your user details or delete your user, you can do that only from the home page.
If you want to get back to the main feed please return from the android back button.

About the posts in the Feed Page, each post has its own data: author, content, number of likes and comments, you can add like by presing the like button, you can add comments by pressing the comments button and right after to enter you comment and submit it. <br>
you can also delete and edit your comment. <br>
When you add a post the home page will get updated and your post will be added at the end of the page. <br> 
You can not edit/delete post that is not your post, an appropriate message will be displayed. <br>
![image](https://github.com/BarWanunu/Advanced-programming-project/assets/139462169/d38ad6e1-68ac-43c8-b10d-86e46a069bb7) <br>

## Important Thing when you ran the app
In order, for the application to run successfully without sudden crushes, please make sure you don't upload any photo that has a size larger than 1023 KB either for profile image or for a post.



