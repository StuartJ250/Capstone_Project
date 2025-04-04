
# WESTERN GOVERNORS UNIVERSITY 


## D424 – SOFTWARE ENGINEERING CAPSTONE


<strong>**Submit a copy of the GitLab repository URL and a copy of the repository branch history retrieved from your repository, which must include the commit messages and dates.**</strong>
<strong>**Note: Wait until you have completed all the following prompts before you create your copy of the repository branch history.**</strong>


### B.  Design and develop a fully functional full stack (mobile or web) software product that addresses your identified business problem or organizational need. Include each of the following attributes, as they are the minimum required elements for the application:

●  code including inheritance, polymorphism, and encapsulation

●  search functionality with multiple row results and displays

●  a database component with the functionality to securely add, modify, and delete the data

●  ability to generate reports with multiple columns, multiple rows, date-time stamps, and title

●  validation functionality

●  industry-appropriate security features

●  design elements that make the application scalable

●  a user-friendly, functional GUI


### C.  Create each of the following forms of documentation for the software product you have developed:

●  a design document including a class diagram and design diagram

●  link to where the web app is hosted with HTML code (if applicable)

●  link to the GitLab repository of the code indicating the version included in this submission

●  user guide for setting up and running the application for maintenance purposes

●  user guide for running the application from a user perspective


### D.  Explain how the software product was tested, including the following:

●  a test plan for a unit test, including screenshots

●  unit test scripts

●  the results of the unit tests based on the provided test plan, including screenshots

●  summaries of changes resulting from completed tests


### E.  Provide a Panopto video recording that includes a demonstration of the functionality of the software application and a summary of the tool or tools used.


Note: For instructions on how to access and use Panopto, use the "Panopto How-To Videos" web link provided below. To access Panopto's website, navigate to the web link titled "Panopto Access" and then choose to log in using the “WGU” option. If prompted, log in using your WGU student portal credentials, and then it will forward you to Panopto’s website.

To submit your recording, upload it to the Panopto drop box titled Software Engineering Capstone EQN1 | D424 (Student Creators).” Once the recording has been uploaded and processed in Panopto's system, retrieve the URL of the recording from Panopto and copy and paste it into the Links option. Upload the remaining task requirements using the Attachments option.


### Work history

- initial commit
- created new project with "Navigation Drawer views activity"
- Updated xml to show home, expense, and report
- updated fragment_expense.xml to have form for saving fragments
- Created all Fragments and View Models for each page
- Created database, DAO and repository for Expense database


- 2nd commit
- Added timestamp and expenseDate to Expense.java entity on lines 15-16,33-34, 98-105, 110-114
- Added logic for timestamp and expenseDate to ExpenseFragment.java on lines 83-87, 93-114, 196,204
- Updated receiptImagePath logic as it was not saving correctly and showing as null on ExpenseFragment.java on lines 163-169
- added expensedate EditText to fragment_expense.xml and added scroll view for smaller devices


- 3rd commit
- Created login package in UI package
- Created register package in UI package
- Created LoginActivity.java in login package
- Created RegisterActivity.java in register package
- Created activity_login.xml in layout package
- Created activity_register.xml in layout package
- Created Login.java in entities package
- Created LoginDAO in dao package
- Added foreign key userID to Expense.java on lines 23, 37, 110-113
- Added @Query method to ExpenseDAO.java on lines 29-31
- Added Login and LoginDAO to ExpenseDatabase.java on lines 15,21
- Added code to ExpenseFragment.java on lines 200-207, 215, 218-219
- Changed main activity in AndroidManifest.xml on line 16
- Added back ".MainActivity" activity to AndroidManifest.xml on lines 25-27 due to crashes once logged in
- Removed ImageView and text view from nav_header_main.xml
- Updated remaining TextView on nav_header_main.xml on line 15-20
- Added button to nav_header_main.xml on lines 22-29
- Added code to MainActivity.java for new addition functionality on lines 28-30, 51-73, 85-94


- 4th commit
- Added code to ReportFragment.java for search and recyclerview on lines 29-82
- Added code to ReportView.java on lines 16-37
- Created ReportAdapter.java in report package
- Created item_expense.xml in layout package
- Added code to fragment_report.xml to add search bar and recyclerview on lines 9-61
- Added query to ExpenseDAO.java on lines 32-35
- Updated Expense.java to add userID as an Index on line 21

- 5th commit
- Cleaned up code on ReportAdapter.java to only show pertinent information
- Created expensedetails package
- Created ExpenseDetailsActivity.java in expensedetails package 
- Created activity_expense_details.xml in layout package
- Removed floating action button from app_bar_main.xml as it is unused
- Added getExpense method to ExpenseDAO.java on lines 36-38
- Updated recyclerView on fragment_report.xml on lines 58-60 to solve formatting issues
- Removed some TextViews from item_expense.xml to clean up look of search functionality
- Added onItemClick to recyclerView items in ReportFragment.java on lines 59-68, 79-84
- Added getExpenseUser method to ReportViewModel.java on lines 38-43
- Added expenseDetailsActivity to AndroidManifest.xml on line 28
- Added code to HomeFragment.java on lines 23-30, 33-44 to display welcome message
- Added code to HomeViewModel.java on lines 15-29 to accommodate welcome message
- Added firstName and lastName to shared preferences in LoginActivity.java  on lines 58-59
- Removed 1 item from main.xml and replaced with "send_report"
- Added code to MainActivity.java to build functionality for exporting a report on lines 129-234
- Added provider item to AndroidManifest.xml on lines 30-45
- Added itext7 dependency to build.gradle.kts on line 52
- Created file_paths.xml to xml package as not having a path was causing the export of report to fail
- Added meta-data item to AndroidManifest.xml on lines 36-38 to link to file_paths.xml
- Synced gradle files to verify everything is working correctly
- Added validation to RegisterActivity.java on lines 47, 70-94

- 6th commit
- Fixed code on multiple xml files due to menu text not changing colors with theme change
  - themes.xml on line 25
  - strings.xml on line 13
  - main.xml on line 7
  - colors.xml on line 10
  - created colors.xml (night) in colors package 
  - deleted README.md duplicate as to not cause confusion

- Final Commit
  - Added LoginActivityTest.java to expense package in androidTest
  - Added RegisterActivityTest.java to expense package in androidTest
  - Added ExpenseFragmentTest.java to expense package in androidTest
  - Changed pop up notifications from TOAST to Snackbar in:
    - ExpenseFragment.java on lines 205,211,222
    - LoginActivity.java on lines 74-79, 84
    - RegisterActivity.java on lines 66-74, 83, 88, 93, 98
  - Added versions and libraries to libs.versions.toml file on lines 18-21, 24, 39-42
  - Added setTestInstance method to ExpenseDatabase.java on lines 36-39
  - Added configurations to build.gradle.kts on lines 49-50, 53-55, 58-64