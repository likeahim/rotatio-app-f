# ROTATIO Vaadin

## Description
This is the frontend part of the application built using the Vaadin framework. It communicates with the backend application through a REST API.

## System Requirements
- Java 17
- Maven or Gradle
- Node.js (version defined in `build.gradle`)
- pnpm (installed globally)

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/likeahim/rotatio-app-f.git
   cd vaadin-frontend
2. Make sure Node.js and pnpm are correctly installed:
   You can check the Node.js version with the command:
   `node -v`
   If pnpm is not installed, you can install it globally with:
   `npm install -g pnpm`
3. Install dependencies and run the application:
   `./gradlew bootRun`

## Testing
To run tests, use:
`./gradlew test`

## Related Projects
To run the full application, clone and run the REST API application repository as well - [Rotatio App backend](https://github.com/likeahim/rotatio-app-b)

## License
This project is licensed under the MIT License. Details can be found in the LICENSE file.

## View Navigation

After starting the REST project and the Vaadin project, your default browser should open at the [localhost](http://localhost:8081/). 
If the browser does not open automatically, enter the address http://localhost:8081/ into the browser window and press Enter.

The start page opens from the mentioned address. It includes:

The ability to `log in`
The ability to `register`
The ability to `reset your password`.
To `log in`, you must use the *email address* associated with an existing account 
and a valid *password*. If you don’t have an account, you need to create one by clicking the **Sign in** button 
and registering a new user. If the password for an existing account is forgotten, it must be reset — a reset 
link will be sent to the provided email address (which must be a valid user email address).
After a successful login, the view navigates to the main page, which displays the current date, 
brief information about the **Rotatio App**, the current number of employees, and the number of employed employees 
with the status "PRESENT." On this page, there is a navigation bar on the left side with options to choose from:

Main
Plans (**1**) / (**5**)
Tasks (**2**)
Workplaces (**3**)
Workers (**4**)
Archives (**6**)
User data (**7**)

1. **Plans** - Here, the user can add a new plan to the list, view a list of all plans from all users, 
or only view their own plans. The showed plans can be also filtered by execute date and value "Planned".
When adding a plan, be careful with the "execute date" field,
as only one plan can exist for a given date. A created plan has default values of "false" for the 
"Planned" and "Archived" columns. The "Archived" column updates when a plan is archived, while the "Planned"
column must be proactively changed by clicking the "Edit" button, setting the "planned" field in the dialog
window as desired, and clicking the "Save" button. Only when a plan has the status "Planned = true" can it be archived.
Once plan is archived, there is no option to editing it.
Of course, archiving an empty plan defeats the purpose of this application, so after creating a plan, 
*you should move on to the other tabs.*

2. **Tasks** - Here, the user can add, edit and view tasks. Tasks should have a unique name. 
There are several buttons available for displaying tasks and an edit button for editing tasks. 
For statistical and archival purposes, the view does not provide the ability to permanently delete tasks. 
You can only change the task's status in the "Performed" column to "false". 
If the user wants to permanently delete a task, this can only be done in the query console, 
which may lead to unforeseen errors due to relationships based on foreign keys.

3. **Workplaces** - Here, the user can add, edit and view workplaces. Workplaces should have a unique destination. 
Several buttons are available for displaying workplaces and an edit button for editing workplaces. 
Similar to tasks, the view does not provide a direct way to delete a workplace from the database; 
you can only deactivate a workplace by changing the "Active" field or temporarily deactivate it 
by changing the "Now used" field.

4. **Workers** - In this view, the user can add, edit and view employees. The Worker Number field must be a number
and cannot be empty. **Make sure to fill in all fields.** 
There are several buttons available for displaying employees and an edit button for editing employee data. 
There is also "delete" button available, but similar to tasks and workplaces, the view does not provide
a direct way to delete a worker. This button changes worker's "Status" on *unemployed*. To clear view
can be used *Show by status* option.
To test the application, the user must create a plan, at least one task, at least one workplace, 
and at least one employee. It is recommended to create these components according to this description, 
as when editing a plan and filling it in with values in the "Workers" view before archiving, 
items such as the plan, tasks, and workplace must already exist in the database.

*Selecting a plan - **Click** the field in the "plans" column and choose a plan from the list. 
The form only contains plans with a "false" value in the "planned" and "archived" fields.*
*Selecting a task - **Click** the field in the "tasks" column and choose a task from the list. 
The form only contains tasks with a "true" value in the "performed" field.*
*Selecting a workplace - **Click** the field in the "workplaces" column and choose a workplace from the list. 
The form only contains workplaces with a "true" value in the "active" and "now used" fields.*

**In each of these three cases, clicking on a selection automatically saves the employee with the new settings in the database.**

5. **Plans** - When the plan, workplaces, and tasks have been assigned to employees, 
go back to the Plans view, click the "Edit" icon on the relevant plan, set the "planned" field to "false"
and save the plan in the database. Then click on the "archive" icon to archive the plan.

6. **Archives** view contains all archived plans. Here, you can view a list of employees and use the "print" button, 
which opens a dialog window with a link address. The application allows you to copy the link to the clipboard 
and/or close the window. You can then paste the copied address into a new browser tab to view the 
created and archived plan or print the plan. After printing, the user can click the "delete" button, 
which removes the archive from the database.

7. **User data** view allows viewing and editing the logged-in user's data. It allows also deleting account 
on extern server - this action logs user out. In local database deleted users status changes, what causes failure in
future log in attempts.

In addition to freely navigating the views, the user has continuous access to the "log out" button, 
which logs the user out and returns the view to the start page.