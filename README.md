# taskmaster


## Contributors

- Shane Roach

## Description

An android application that allows you to keep track of all your tasks.


## Features

### View current tasks

- User can view all tasks in the `homepage`, displayed in a recycle view.

### Add a task

- User can add a task with a given title, description, status, and team. This will be added to the users current task list.
- User can optionally upload an image file when creating a task.
- Users can can choose to upload a file to a new task page when browsing in other applications.
- When the user adds a task, their location should is retrieved and included as part of the saved Task.


### View a specific task

- User can view a specific task from the homepage recycle view in detail, to find information about the description and status.
- User can specify which team they belong to. Homepage tasks will be filtered to show only those teams tasks.
- User can view image associated with task if applicable.

#### Accessibility
- User can choose to translate the task description to Spanish.
- User can hear task description spoken aloud.

### Edit a task

- User can edit a task.
- User can choose to delete a task.

### User login

- User can signup or login with an existing account.
- User can see their username on `homepage`
- User can signout once logged in.

### All Tasks

- User can now view ads associated with Google.




## Change Log

### Lab 26 (3.21.22)

- Added `Homepage` Page

- Added `Add a Task` Page

- Added `All Tasks` Page

### Lab 27 (3.22.22)

- Added `Settings Page`

- Added `Task Detail Page`

- Updated `Homepage`

### Lab 28 (3.23.22)

- Added Task Model: A Task has a title, body, and taskStatusEnum.

- Updated Homepage: Added a RecyclerView for displaying Task data. This has hardcoded tasks for now.

### Lab 29 (3.24.22)

#### Added `Add Task Form`
- Users can now add tasks with a title, description, and status.

#### Updated `Homepage`


#### Updated `View Task Page`
- Users can now view a given task with a title, description, and status.

### Lab 31 (3.28.22)

#### General Application Maintenance
- Ensured espresso tests are functional.
- Refactored Main Activity.
- Added styling to UI.

#### Updated `Homepage`

#### Updated `View Task`

### Lab 32 (3.29.22)

#### Tasks Are Cloudy
- Updated all references to the Task data to instead use AWS Amplify to access data in DynamoDB instead of in Room.

#### Updated `Add Task Form`
- Modified `Add Task form` to save the data entered in as a Task to DynamoDB.

#### Updated `Homepage`
- Refactored homepage’s RecyclerView to display all Task entities in DynamoDB.

### Lab 33 (3.30.22)

#### Tasks Are Owned By Teams
- Updated tasks to be owned by teams.
- Three default teams have been made; elves, robots, and humans.

#### Updated `Add Task Form`
- Spinner used to select a team when creating a task was added.

#### Updated `Settings Page`
- Added ability to allow users to choose a team, and filter the tasks on the homepage corresponding to the chosen team.


### Lab 34 (3.31.22)

#### Added `Edit a Task` Form
- Added new task form to edit a Task
- Added functionality to delete a Task on the  `Edit Task Form`

#### Updated `View Task`
- Added button on page to allow you to edit the viewed task.

### Lab 36 (4.4.22)

#### Added `User Login`

- Cognito has been added to the Amplify setup. A user login and signup flow has been
integrated into th app. Once the user has succesfully logged in, their username is displayed
at the top of the `homepage`.

![Updated View A Task](/images/userLoginFlow_lab36.png)

#### Added `User Logout`

- Users can logout of the application.


### Lab 37 (4.5.22)

#### Updated `Task` model

- Users can optionally select a file to attach to a task.
If a user attaches a file to a task, the file is uploaded to S3, and associated with that task.

![Updated View A Task](/images/fileupload_lab37.png)

#### Update `View Task` page

- Users can now view the image associated with that Task.

![Updated View A Task](/images/viewTask_lab37.png)


### Lab 38 (4.6.22)

#### Feature Added: Adding a Task from Another Application

- An intent filter was added to the application such that a user can hit the “share” button on an image
in another application, choose TaskMaster as the app to share
that image with, and be taken directly to the Add a Task activity with that image pre-selected.

![Updated View A Task](/images/uploadImage_intentFilter_lab38.png)

### Lab 39 (4.7.22)

#### Feature Added: Location Integration with Task Creation
- When the user adds a task, their location is retrieved and included as part of the saved Task.

#### Updated `View Task` Page
- The user can now see the location associated with a given task.
![Updated View A Task](/images/viewTask_lab39.png)

### Lab 41 (4.11.22)

#### Added Analytics
- Events are now being tracked using Amazon Pinpoint. Everytime a user enters the Home Activity, it is recorded.
![Analytics](/images/pinpoint_lab41.png)

#### Added Predictions

##### Text to Speech
- Added ability to hear Task description spoken aloud on `View Task` page.

##### Translate Text
- Added ability to view a translated Task description to Spanish on `View Task` page.
![Translate A Description](/images/translate_lab41.png)

### Lab 42 (4.12.22)

#### Updated `All Tasks`
- Google ads (AdMob) has been incorporated into activity.
- A banner ad is now displayed at top of page.
- An interstitial ad can be viewed upon clicking `Interstitial` button.
- An rewarded ad can be viewed after clicking `Rewarded` button.

![Ads](/images/adds_lab42.png)




## Testing

- `SetUsernameHomeActivityTest` tests to see if the correct username is displayed on the homepage.

- `CorrectTaskDetailsActivityTest` tests to see if correct task title is displayed on the view Task page when
entering from the home screen.

- `AddATaskActivityTest` tests to see if a new added task displays in the recycle view after added in the `add task page`.

## Resources/Citations

- [Android Room](https://developer.android.com/jetpack/androidx/releases/room?gclid=CjwKCAjwrfCRBhAXEiwAnkmKmS76pDHGyIJ2E7n4UyzbyZA3NcjcUHTtf_i4ErfFFc7Eqj7KxJqYEhoCNIoQAvD_BwE&gclsrc=aw.ds)
- [Amplify Docs](https://aws-amplify.github.io/docs/)