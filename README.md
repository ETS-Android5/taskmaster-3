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

### View a specific task

- User can view a specific task from the homepage recycle view in detail, to find information about the description and status.
- User can specify which team they belong to. Homepage tasks will be filtered to show only those teams tasks.

### Edit a task

- User can edit a task.
- User can choose to delete a task.




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

![Update Homepage](/images/update_homepage_lab31.png)

#### Updated `View Task`

![Update ViewTask](/images/update_viewTask_lab31.png)


### Lab 32 (3.29.22)

#### Tasks Are Cloudy
- Updated all references to the Task data to instead use AWS Amplify to access data in DynamoDB instead of in Room.

#### Updated `Add Task Form`
- Modified `Add Task form` to save the data entered in as a Task to DynamoDB.

#### Updated `Homepage`
- Refactored homepage’s RecyclerView to display all Task entities in DynamoDB.

![Update Homepage](/images/update_homepage_lab32.png)


### Lab 33 (3.30.22)

#### Tasks Are Owned By Teams
- Updated tasks to be owned by teams.
- Three default teams have been made; elves, robots, and humans.

![Update Homepage](/images/tasksWithTeams_lab33.png)

#### Updated `Add Task Form`
- Spinner used to select a team when creating a task was added.

![Update Homepage](/images/update_addTask_lab33.png)

#### Updated `Settings Page`
- Added ability to allow users to choose a team, and filter the tasks on the homepage corresponding to the chosen team.

![Update Homepage](/images/update_userSettings_lab33.png)


### Lab 34 (3.31.22)

#### Added `Edit a Task` Form
- Added new task form to edit a Task
- Added functionality to delete a Task on the  `Edit Task Form`

![Added Edit A Task](/images/added_editTask_lab34.png)

#### Updated `View Task`
- Added button on page to allow you to edit the viewed task.

![Updated View A Task](/images/update_viewTask_lab34.png)

#### Publish to Google Play

- WIP


## Testing

- `SetUsernameHomeActivityTest` tests to see if the correct username is displayed on the homepage.

- `CorrectTaskDetailsActivityTest` tests to see if correct task title is displayed on the view Task page when
entering from the home screen.

- `AddATaskActivityTest` tests to see if a new added task displays in the recycle view after added in the `add task page`.

## Resources/Citations

- [Android Room](https://developer.android.com/jetpack/androidx/releases/room?gclid=CjwKCAjwrfCRBhAXEiwAnkmKmS76pDHGyIJ2E7n4UyzbyZA3NcjcUHTtf_i4ErfFFc7Eqj7KxJqYEhoCNIoQAvD_BwE&gclsrc=aw.ds)
- [Amplify Docs](https://aws-amplify.github.io/docs/)