# OptaJanterm
Program for placing students into Janterm classes.

## Running the program
Run [OptaJanterm.jar](https://github.com/ejkaplan/OptaJanterm/blob/master/OptaJanterm/OptaJanterm.jar), select the 5 files (4 for the preferences for each grade and 1 for max course sizes.) When you hit the run button, it will start placing students into classes by the following criteria, with the following priorities:

1. Make sure that each class has no more than its maximum number of students
2. Make sure that each student is in a course in their top 5
3. Other criteria, determined by the sliders.

The sliders reflect how much importance will be put on getting students into courses higher on their preference lists, keeping class sizes even across courses, keeping classes balanced by gender and keeping classes balanced by grade level. If you set any slider to 0, that criteria will not be considered. Optimizations based on the sliders will never break priorities 1 or 2.

The program will end either when you press the kill button or when the program has gone 1 hour without finding any improvements. At that point, it will output two csv files, one indicating which course each student has been assigned to and one summarizing statistics about each course.

## Input Files

### Preference Data
Download survey results from surveymonkey as 4 separate csv files for 9th, 10th, 11th and 12 graders' preferences. An exerpt from the 10th grade csv file might look something like this:

|Email Address   |First Name|Last Name|Gender|Grade|Student ID|Underwater Basket Weaving|Giant Gnome Construction|...|
|----------------|----------|---------|------|-----|----------|-------------------------|------------------------|---|
|alice@school.net|alice     |aliceton |F     |10   |ALI321    |3                        |                        |...|
|bob@school.net  |bob       |bobstein |M     |10   |BOB123    |2                        |1                       |...|

A given .csv file should only contain columns for the janterm courses that grade level is eligible to sign up for. Each row should represent one student, with the appropriate info filled in for each column. Gender should be indicated by the letter M or F, and grade should be indicated by an integer (9,10,11,12). In the columns for the different courses, there should be integers representing how much the students' preference level, where 1 indicates first choice, 2 indicates 2nd choice and so on. The program only considers top 5 choices, so the columns that are not in a students' top 5 may optionally be left blank - it makes no difference. In the above example, underwater basket weaving is alice's 3rd choice and giant gnome construction is not in her top 5. (Of course, the actual csv file would have more columns for more courses.)

### Course Size Data
Course sizes should be indicated in a csv file. The csv file should have 2 columns, one for course titles and one for maximum sizes. An example csv might look like this:

|default                  |22|
|-------------------------|--|
|underwater basket weaving|18|
|giant gnome construction |15|

This would indicate that basket weaving can take 18 students, gnome construction can take 15 and all other classes can take 22. You must include at least the one line for default, and you can include as many exceptions as you want.
