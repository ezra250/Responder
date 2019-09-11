-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: Sep 11, 2019 at 04:33 PM
-- Server version: 5.7.25
-- PHP Version: 7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `emergencyresponder`
--

-- --------------------------------------------------------

--
-- Table structure for table `responder`
--

CREATE TABLE `responder` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `token` text NOT NULL,
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `responder`
--

INSERT INTO `responder` (`id`, `username`, `email`, `password`, `token`, `date_created`) VALUES
(1, 'ezra', 'pacsonlose@gmail.com', '12345', '', '0000-00-00 00:00:00'),
(2, 'hospital', 'rubavuhospital@gmail.com', '12345', '', '2019-09-08 16:44:03'),
(3, 'kacyiruhospital', 'kacyiruho@gmail.com', '12345', '', '2019-09-08 17:12:09'),
(4, 'ngomahospital', 'ngomaho@gmail.com', '12345', '', '2019-09-08 18:49:51');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `responder`
--
ALTER TABLE `responder`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `responder`
--
ALTER TABLE `responder`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
