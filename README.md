## Build tools & versions used
minSDK 30
compile/ target SDK 32
JDK 1.8
gradle 7.2

## Steps to run the app
Built on Android Studio using gradle.
Any environment that supports gradle builds should work fine.

## What areas of the app did you focus on?
The primary focus on this app was to use best practices while implementing an event-based interface.  I tried to create a robust application using my knowledge in the Web as a foundation, leveraging my previous project experiences. I recognize, however, that best practices in React may not necessarily translate well to Android.  Hence, I researched industry-standard practices for subject areas like image caching and memory management. I made an effort to follow Clean architecture and use MVVM while also using libraries that have been recognized to be the best for these tasks. I aimed to have the app pass benchmarks set for production-ready code at Square and so a large focus of my work was to find the best approaches, beyond just something that is functional.

## What was the reason for your focus? What problems were you trying to solve?
Focusing on best practices helped me write an application that had the minimum level of features that you would expect from this application (non blocking UI, effective error handling and scalability)  with reasonable speed. I have found that focusing on functionality alone while being agnostic to software design can lead to a lot of time spent on bug fixes and work arounds. Thankfully, my approach helped this significantly, reducing time spent on development Additionally, this focus made adding and testing features a lot quicker. This is something I emphasize quite a bit in my personal and professional work and so, I wanted to make an effort in discovering best designs for this paradigm.

## How long did you spend on this project?
I spent some time casually reading up on approaches to this problem during the weeknights and completed implementation on Sunday night. I would estimate a bit over 5 hours for the entire projet.

## Did you make any trade-offs for this project? What would you have done differently with more time?
I would say that given more time, I would improve the UI. I tried to avoid using libraries I wasn't a 100% comfortable with yet like Compose, which could've improved the application. I wanted to focus on the lower levels a bit more but if I had the time, implementing this project in Compose would have been a fun challenge to undertake.


## What do you think is the weakest part of your project?
Tying back to the previous question, I believe the UI to be the weakest part of this project. Ignoring subjective opinions on color choices and design, I know the layouts need to designed with different phone sizes in mind and Strings need to be organized to be easily translatable. This is something that I could not emphasize in this project and is something that I believe to be necessary when designing layouts. One additional thing I would've improved is the amount of responsibility given to the EmployeesListAdapter.


## Did you copy any code or dependencies? Please make sure to attribute them here!
1. Sanket Berde from https://stackoverflow.com/questions/23978828/how-do-i-use-disk-caching-in-picasso with some details on Caching in Picasso (Didn't directly use the code but was instrumental in testing Disk Cache code ).
2. Jake Wharton from https://twitter.com/JakeWharton/status/679403330809028608. Helped in best approach to check for image in disk cache alone.

Reference Materials:
man pages for Picasso, okhttp & SwipeRefreshView

## Is there any other information you’d like us to know?

- I could not decide how to sort the Employee list, so I am letting the user control sort order.

Design decisions made ->
1. When the User changes sort type, I decided against scrolling to the first position. I figured with larger lists the user might not want to lose the current employee
2. All error messages are shown as Toasts
3. When the Application has a list of employees and the next refresh returns an error, I decided to show a toast with the error but maintain the downloaded data from the previous request
4. In an effort to minimize network usage, I decide to add a button for each user to manually request photo if its not already in Disk Cache. So if it is in Disk Cache, we load the image into the ImageView and if it isn't, we let the user manually make a request for the image using their network.

