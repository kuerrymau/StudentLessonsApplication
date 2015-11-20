DROP TABLE IF EXISTS Lessons;
 CREATE TABLE Lessons(id INT PRIMARY KEY,
    name VARCHAR(255),
    faculty VARCHAR(255),
    totalNoOfStudentss INT);
 INSERT INTO Lessons VALUES(1, 'Computer Science 101', 'Science', 2);
 INSERT INTO Lessons VALUES(2, 'Software Engineering 102', 'Science', 2);
 
 
 DROP TABLE IF EXISTS Students;
 CREATE TABLE Students(id INT PRIMARY KEY,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    studentID VARCHAR(255));
 INSERT INTO Students VALUES(1, 'Crimore', 'Chingwenje', '2A25F');
 INSERT INTO Students VALUES(2, 'Chikomo', 'Chitondo', '3B36G');
 INSERT INTO Students VALUES(3, 'Mayibongwe', 'Nkosi', '4C47H');
 
 
 DROP TABLE IF EXISTS LessonDays;
 CREATE TABLE LessonDays(id INT PRIMARY KEY,
    day VARCHAR(255),
    lessonID INT,
FOREIGN KEY(lessonID) REFERENCES Lessons(id) ON UPDATE CASCADE);
 INSERT INTO LessonDays VALUES(1, 'Monday', 1);
 INSERT INTO LessonDays VALUES(2, 'Tuesday', 1);
 INSERT INTO LessonDays VALUES(3, 'Wednesday', 0);
 INSERT INTO LessonDays VALUES(4, 'Thursday', 1);
 INSERT INTO LessonDays VALUES(5, 'Friday', 1);
 INSERT INTO LessonDays VALUES(6, 'Tuesday', 2);
 INSERT INTO LessonDays VALUES(7, 'Thursday', 2);

-- DROP TABLE IF EXISTS LessonStudents;
--  CREATE TABLE LessonStudents(
--     id INT PRIMARY KEY
--     lessonID INT,
--     studentID INT,
-- FOREIGN KEY(lessonID) REFERENCES Lessons(id) ON UPDATE CASCADE,
-- FOREIGN KEY(studentID) REFERENCES Students(id) ON UPDATE CASCADE);
