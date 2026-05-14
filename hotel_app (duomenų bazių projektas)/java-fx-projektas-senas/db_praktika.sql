-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Apr 28, 2026 at 01:05 PM
-- Server version: 9.1.0
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_praktika`
--

-- --------------------------------------------------------

--
-- Table structure for table `grades`
--

DROP TABLE IF EXISTS `grades`;
CREATE TABLE IF NOT EXISTS `grades` (
  `GradeID` int NOT NULL AUTO_INCREMENT,
  `StudentID` int DEFAULT NULL,
  `SubjectID` int DEFAULT NULL,
  `grade` decimal(4,2) DEFAULT NULL,
  PRIMARY KEY (`GradeID`),
  KEY `grades_ibfk_1` (`StudentID`),
  KEY `grades_ibfk_2` (`SubjectID`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `grades`
--

INSERT INTO `grades` (`GradeID`, `StudentID`, `SubjectID`, `grade`) VALUES
(2, 4, 1, 7.00),
(5, 4, 1, 9.00),
(8, 8, 1, 8.00),
(9, 4, 4, 10.00),
(10, 12, 7, 6.00),
(11, 12, 7, 4.00),
(13, 13, 7, 2.00),
(15, 13, 4, 7.00),
(16, 10, 4, 2.00),
(17, 4, 9, 6.00),
(18, 4, 9, 10.00),
(19, 10, 9, 6.00),
(21, 19, 12, 8.00),
(22, 19, 12, 6.00),
(23, 19, 1, 5.00),
(27, 21, 7, 6.00),
(28, 21, 7, 10.00),
(29, 22, 15, 6.00),
(30, 22, 15, 7.00),
(31, 8, 15, 9.00);

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
CREATE TABLE IF NOT EXISTS `groups` (
  `GroupID` int NOT NULL,
  `GroupName` varchar(255) COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  PRIMARY KEY (`GroupID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `groups`
--

INSERT INTO `groups` (`GroupID`, `GroupName`) VALUES
(1, 'admin'),
(2, 'teacher'),
(3, 'student');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
CREATE TABLE IF NOT EXISTS `students` (
  `StudentID` int NOT NULL AUTO_INCREMENT,
  `UserID` int DEFAULT NULL,
  `GroupID` int DEFAULT NULL,
  PRIMARY KEY (`StudentID`),
  KEY `FK_students_groups_GroupID` (`GroupID`),
  KEY `FK_students_users_UserID` (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`StudentID`, `UserID`, `GroupID`) VALUES
(4, 8, 3),
(8, 12, 3),
(10, 29, 3),
(12, 31, 3),
(13, 34, 3),
(16, 40, 3),
(19, 45, 3),
(21, 51, 3),
(22, 54, 3);

-- --------------------------------------------------------

--
-- Table structure for table `subjects`
--

DROP TABLE IF EXISTS `subjects`;
CREATE TABLE IF NOT EXISTS `subjects` (
  `SubjectID` int NOT NULL AUTO_INCREMENT,
  `SubjectName` varchar(255) COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `TeacherID` int DEFAULT NULL,
  PRIMARY KEY (`SubjectID`),
  KEY `FK_subjects_teachers` (`TeacherID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `subjects`
--

INSERT INTO `subjects` (`SubjectID`, `SubjectName`, `TeacherID`) VALUES
(1, 'Info', 3),
(4, 'Fizika', 27),
(7, 'Geografija', 52),
(8, 'Chemija', 28),
(9, 'Matematika', 32),
(10, 'Daile', 43),
(12, 'Lietuviu kalba', 28),
(15, 'Programavimo praktika', 55);

-- --------------------------------------------------------

--
-- Table structure for table `teachers`
--

DROP TABLE IF EXISTS `teachers`;
CREATE TABLE IF NOT EXISTS `teachers` (
  `UserID` int NOT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `teachers`
--

INSERT INTO `teachers` (`UserID`) VALUES
(3),
(21),
(27),
(28),
(32),
(43),
(52),
(55);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  `lastname` varchar(255) COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  `email` varchar(50) COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  `role` varchar(50) COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`UserID`, `name`, `lastname`, `email`, `role`, `password`) VALUES
(1, 'admin', 'Adminelis', 'info@admin.lt', 'admin', 'admin123'),
(3, 'Mokytojas', 'Mokytojelis', 'moko@moko.lt', 'teacher', '123'),
(4, 'Mokinys', 'Mokiniukas', 'mokinys.mokiniukas@stud.viko.lt', 'student', 'mokiniukas'),
(5, 'Lukas', 'Lukanskas', 'lukas.lukanskas@stud.viko.lt', 'student', 'lukanskas'),
(8, 'Lukas', 'Lukasas', 'lukas.lukasas@stud.viko.lt', 'student', 'lukasas'),
(12, 'Tomas', 'Tomauskas', 'tomas.tomauskas@stud.viko.lt', 'student', 'tomauskas'),
(23, 'Testas', 'Testauskas', 'testas.testauskas@stud.viko.lt', 'admin', 'testauskas'),
(26, 'Kalmaras', 'Kalmarauskas', 'kalmaras.kalmarauskas@stud.viko.lt', 'admin', 'kalmarauskas'),
(27, 'Vardenis', 'Pavardenis', 'vardenis.pavardenis@stud.viko.lt', 'teacher', 'pavardenis'),
(28, 'Gintaras', 'Sakalauskas', 'gintaras.sakalauskas@stud.viko.lt', 'teacher', 'sakalauskas'),
(29, 'Gintaras', 'Gintarauskas', 'gintaras.gintarauskas@stud.viko.lt', 'student', 'gintarauskas'),
(31, 'Studentas', 'Studentelis', 'studentas.studentelis@stud.viko.lt', 'student', 'studentelis'),
(32, 'Vaidas', 'Vaidelis', 'vaidas.vaidelis@stud.viko.lt', 'teacher', 'vaidelis'),
(33, 'testadminas', 'adminas', 'testadminas.adminas@stud.viko.lt', 'admin', 'adminas'),
(34, 'Pranas', 'Pranauskas', 'pranas.pranauskas@stud.viko.lt', 'student', 'pranauskas'),
(35, 'Ernestas', 'Ernestauskas', 'ernestas.ernestauskas@stud.viko.lt', 'admin', 'ernestauskas'),
(40, 'Viktoras', 'Viktorauskas', 'viktoras.viktorauskas@stud.viko.lt', 'student', 'viktorauskas'),
(43, 'Dainius', 'Dainauskas', 'dainius.dainauskas@stud.viko.lt', 'teacher', 'dainauskas'),
(44, 'Adminas', 'Adminelis', 'adminas.adminelis@stud.viko.lt', 'admin', 'adminelis'),
(45, 'Svajunas', 'Svajunelis', 'svajunas.svajunelis@stud.viko.lt', 'student', 'svajunelis'),
(46, 'Adminas', 'Adminavicius', 'adminas.adminavicius@stud.viko.lt', 'admin', 'adminavicius'),
(51, 'Faustas', 'Faustauskas', 'faustas.faustauskas@stud.viko.lt', 'student', 'faustauskas'),
(52, 'Mykolas', 'Mykolauskas', 'mykolas.mykolauskas@stud.viko.lt', 'teacher', 'mykolauskas'),
(53, 'Adminas', 'Adinskas', 'adminas.adinskas@stud.viko.lt', 'admin', 'adinskas'),
(54, 'Kristupas', 'Kristupauskas', 'kristupas.kristupauskas@stud.viko.lt', 'student', 'kristupauskas'),
(55, 'Vaidas', 'Liubinas', 'vaidas.liubinas@stud.viko.lt', 'teacher', 'liubinas');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `grades`
--
ALTER TABLE `grades`
  ADD CONSTRAINT `grades_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `students` (`StudentID`) ON DELETE CASCADE,
  ADD CONSTRAINT `grades_ibfk_2` FOREIGN KEY (`SubjectID`) REFERENCES `subjects` (`SubjectID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `FK_students_groups_GroupID` FOREIGN KEY (`GroupID`) REFERENCES `groups` (`GroupID`),
  ADD CONSTRAINT `FK_students_users_UserID` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Constraints for table `subjects`
--
ALTER TABLE `subjects`
  ADD CONSTRAINT `FK_subjects_teachers` FOREIGN KEY (`TeacherID`) REFERENCES `teachers` (`UserID`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
