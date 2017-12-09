-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 09, 2017 at 12:56 AM
-- Server version: 10.1.26-MariaDB
-- PHP Version: 7.1.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `project_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `Fname` varchar(50) NOT NULL,
  `Lname` varchar(50) NOT NULL,
  `CellNum` int(10) NOT NULL,
  `Email` varchar(75) NOT NULL,
  `NotifyFreq` varchar(15) NOT NULL,
  `ReasonForVisit` varchar(15) NOT NULL,
  `TicketID` varchar(14) NOT NULL,
  `Preference` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`Fname`, `Lname`, `CellNum`, `Email`, `NotifyFreq`, `ReasonForVisit`, `TicketID`, `Preference`) VALUES
('Jessie', 'Wilkins', 911, 'ProbablySomethingRelatedToVideoGames@gmail.com', '5', 'Billing', 'VWXYZ123456789', 1),
('Samantha', 'Eidson', 12345, 'SomethingAboutChickensAndDatabases@gmail.com', '5', 'Billing', 'QWERT123456789', 1),
('Yuri', 'Khechoyan', 2147483647, 'SomethingWithPandasInIt@gmail.com', '5', 'Phone_Repair', 'ABCDE123456789', 0);

-- --------------------------------------------------------

--
-- Table structure for table `device`
--

CREATE TABLE `device` (
  `Make` varchar(25) NOT NULL,
  `Model` varchar(75) NOT NULL,
  `Damaged` int(2) NOT NULL COMMENT '1 for yes, 2 for no, 3 for I dont know',
  `Serial_Number` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `EMPID` varchar(10) NOT NULL,
  `ticket_assigned` varchar(14) NOT NULL,
  `fname` varchar(50) NOT NULL,
  `lname` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`EMPID`, `ticket_assigned`, `fname`, `lname`) VALUES
('A123456789', 'QWERT123456789', 'Steven', 'Olson'),
('B123456789', 'ABCDE123456789', 'John', 'McQuaide');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`CellNum`),
  ADD KEY `FK_TicketConstraint` (`TicketID`);

--
-- Indexes for table `device`
--
ALTER TABLE `device`
  ADD PRIMARY KEY (`Serial_Number`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`EMPID`),
  ADD KEY `ticket_assigned` (`ticket_assigned`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `FK_TicketConstraint` FOREIGN KEY (`TicketID`) REFERENCES `employee` (`ticket_assigned`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
