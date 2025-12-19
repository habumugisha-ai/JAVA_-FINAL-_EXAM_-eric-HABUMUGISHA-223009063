-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Dec 19, 2025 at 08:54 PM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `security_portal`
--

-- --------------------------------------------------------

--
-- Table structure for table `accesscontrol`
--

DROP TABLE IF EXISTS `accesscontrol`;
CREATE TABLE IF NOT EXISTS `accesscontrol` (
  `AccessControlID` int NOT NULL AUTO_INCREMENT,
  `AccessLevel` enum('Full','Limited','Restricted') NOT NULL,
  `Description` text,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`AccessControlID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `alert`
--

DROP TABLE IF EXISTS `alert`;
CREATE TABLE IF NOT EXISTS `alert` (
  `AlertID` int NOT NULL AUTO_INCREMENT,
  `ReferenceID` int NOT NULL,
  `Description` text NOT NULL,
  `Date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `Status` enum('Pending','Acknowledged','Resolved') NOT NULL,
  `Remarks` text,
  PRIMARY KEY (`AlertID`),
  KEY `ReferenceID` (`ReferenceID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `alert`
--

INSERT INTO `alert` (`AlertID`, `ReferenceID`, `Description`, `Date`, `Status`, `Remarks`) VALUES
(1, 1, 'High temperature in server room', '2025-10-25 09:40:03', 'Pending', 'Immediate attention required'),
(2, 2, 'Camera offline', '2025-10-25 09:40:03', 'Acknowledged', 'Maintenance scheduled'),
(3, 3, 'Multiple failed login attempts', '2025-10-25 09:40:03', 'Resolved', 'False positive');

-- --------------------------------------------------------

--
-- Table structure for table `camera`
--

DROP TABLE IF EXISTS `camera`;
CREATE TABLE IF NOT EXISTS `camera` (
  `CameraID` int NOT NULL AUTO_INCREMENT,
  `CameraName` varchar(100) NOT NULL,
  `Location` varchar(200) NOT NULL,
  `Status` enum('Active','Inactive','Maintenance') NOT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CameraID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `camera`
--

INSERT INTO `camera` (`CameraID`, `CameraName`, `Location`, `Status`, `CreatedAt`) VALUES
(1, 'Main Entrance', 'Building A - Front Door', 'Active', '2025-10-25 09:40:03'),
(2, 'Parking Lot', 'Underground Parking B1', 'Active', '2025-10-25 09:40:03'),
(3, 'Server Room', 'Floor 3 - Room 301', 'Active', '2025-10-25 09:40:03'),
(4, 'Back Exit', 'Building A - Rear Door', 'Inactive', '2025-10-25 09:40:03');

-- --------------------------------------------------------

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
CREATE TABLE IF NOT EXISTS `event` (
  `EventID` int NOT NULL AUTO_INCREMENT,
  `ReferenceID` int NOT NULL,
  `Description` text NOT NULL,
  `Date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `Status` enum('Open','Closed','Investigating') NOT NULL,
  `Remarks` text,
  PRIMARY KEY (`EventID`),
  KEY `ReferenceID` (`ReferenceID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `event`
--

INSERT INTO `event` (`EventID`, `ReferenceID`, `Description`, `Date`, `Status`, `Remarks`) VALUES
(1, 1, 'Unauthorized access attempt', '2025-10-25 09:40:03', 'Open', 'Investigation pending'),
(2, 2, 'Door left open', '2025-10-25 09:40:03', 'Closed', 'Resolved by security team'),
(3, 1, 'Motion detected in restricted area', '2025-10-25 09:40:03', 'Investigating', 'Reviewing camera footage');

-- --------------------------------------------------------

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
CREATE TABLE IF NOT EXISTS `log` (
  `LogID` int NOT NULL AUTO_INCREMENT,
  `UserID` int NOT NULL,
  `Action` varchar(200) NOT NULL,
  `Timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `Details` text,
  PRIMARY KEY (`LogID`),
  KEY `UserID` (`UserID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `log`
--

INSERT INTO `log` (`LogID`, `UserID`, `Action`, `Timestamp`, `Details`) VALUES
(1, 1, 'User login', '2025-10-25 09:40:05', 'Successful login from IP 192.168.1.100'),
(2, 2, 'Camera viewed', '2025-10-25 09:40:05', 'Accessed camera feed: Main Entrance'),
(3, 1, 'User management', '2025-10-25 09:40:05', 'Updated user permissions for operator1');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL,
  `PasswordHash` varchar(255) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `FullName` varchar(100) NOT NULL,
  `Role` enum('Admin','Security','Operator') NOT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `LastLogin` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `Username` (`Username`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`UserID`, `Username`, `PasswordHash`, `Email`, `FullName`, `Role`, `CreatedAt`, `LastLogin`) VALUES
(1, 'admin', '$2a$10$8K1p/a0dRaP/dOZ4OeYzAuQdZ6MYZ.lMqA7k2QpKZzN9qJY1WzJyW', 'admin@security.com', 'HABUMUGISHA ERIC', 'Admin', '2025-10-25 09:40:03', '2025-10-27 07:38:39'),
(2, 'security1', '$2a$10$8K1p/a0dRaP/dOZ4OeYzAuQdZ6MYZ.lMqA7k2QpKZzN9qJY1WzJyW', 'security1@company.com', 'tuyizere ely', 'Security', '2025-10-25 09:40:03', '2025-10-26 14:09:57'),
(3, 'operator1', '$2a$10$8K1p/a0dRaP/dOZ4OeYzAuQdZ6MYZ.lMqA7k2QpKZzN9qJY1WzJyW', 'operator@company.com', 'M.CLOUDINE', 'Operator', '2025-10-25 09:40:03', '2025-10-26 14:11:40'),
(4, 'auditor1', '$2a$10$8K1p/a0dRaP/dOZ4OeYzAuQdZ6MYZ.lMqA7k2QpKZzN9qJY1WzJyW', 'eric@security.com', 'NEZERWA GIKUNDIRO sandrine', 'Security', '2025-10-25 10:21:03', NULL);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
