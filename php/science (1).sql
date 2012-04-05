-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 05, 2011 at 06:01 
-- Server version: 5.5.8
-- PHP Version: 5.3.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `science`
--

-- --------------------------------------------------------

--
-- Table structure for table `calls_data`
--

CREATE TABLE IF NOT EXISTS `calls_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `date` varchar(100) DEFAULT NULL,
  `duration` varchar(100) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=98 ;

--
-- Dumping data for table `calls_data`
--

INSERT INTO `calls_data` (`id`, `user_id`, `date`, `duration`, `type`) VALUES
(1, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(2, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(3, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(4, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(5, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(6, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(7, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(8, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(9, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(10, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(11, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(12, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(13, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(14, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(15, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(16, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(17, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(18, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(19, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(20, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(21, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(22, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(23, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(24, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(25, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(26, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(27, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(28, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(29, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(30, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(31, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(32, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(33, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(34, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(35, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(36, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(37, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(38, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(39, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(40, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(41, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(42, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(43, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(44, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(45, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(46, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(47, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(48, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(49, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(50, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(51, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(52, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(53, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(54, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(55, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(56, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(57, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(58, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(59, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(60, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(61, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(62, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(63, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(64, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(65, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(66, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(67, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(68, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(69, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(70, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(71, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(72, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(73, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(74, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(75, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(76, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(77, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(78, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(79, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(80, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(81, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(82, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(83, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(84, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(85, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(86, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(87, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(88, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(89, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(90, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(91, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(93, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(94, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(95, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(96, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1'),
(97, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', '0', '1');

-- --------------------------------------------------------

--
-- Table structure for table `location_data`
--

CREATE TABLE IF NOT EXISTS `location_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `coordinates` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `location_data`
--


-- --------------------------------------------------------

--
-- Table structure for table `sms_data`
--

CREATE TABLE IF NOT EXISTS `sms_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `date` varchar(100) DEFAULT NULL,
  `body` varchar(100) DEFAULT NULL,
  `destination` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `sms_data`
--

INSERT INTO `sms_data` (`id`, `user_id`, `date`, `body`, `destination`) VALUES
(6, 1, '1310760014534', 'holaholitahola', NULL),
(7, 1, '1310760014534', 'holaholitahola', NULL),
(8, 1, '1310760014534', 'holaholitahola', '0034619653851'),
(9, 1, 'Tue Mar 01 17:55:15 GMT+00:00 2011', 'holaholitahola', '0034619653851');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(1, 'manu', 'manu');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `calls_data`
--
ALTER TABLE `calls_data`
  ADD CONSTRAINT `calls_data_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `location_data`
--
ALTER TABLE `location_data`
  ADD CONSTRAINT `location_data_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `sms_data`
--
ALTER TABLE `sms_data`
  ADD CONSTRAINT `sms_data_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
