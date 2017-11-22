-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 03, 2017 at 05:13 AM
-- Server version: 10.1.25-MariaDB
-- PHP Version: 7.1.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shampoo_account`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `name` varchar(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_no` varchar(12) NOT NULL,
  `profile_picture` varchar(255) NOT NULL,
  `is_driver` smallint(6) NOT NULL,
  `expired_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `access_token` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `name`, `email`, `password`, `phone_no`, `profile_picture`, `is_driver`, `expired_time`, `access_token`) VALUES
(1, 'aa', 'agus', 'ago', 'lala', '023', 'lala.png', 1, '2017-11-02 14:50:46', '821f6f4dd2460bfab91e68310d44dbe5'),
(3, 'ab', 'agus', 'ago', 'lala', '023', 'lala.png', 1, '2017-11-02 13:17:37', 'AW\n'),
(5, 'ad', 'cc', 'bb', 'dd', 'ee', 'haaha', 1, '2017-11-02 13:17:37', 'AW\n'),
(6, 'hahaha', '', 'hehehe@gmail.com', 'hahaha', '', 'images/default', 0, '2017-11-03 03:21:02', 'db228f294006a44dd2311374c9b46015'),
(7, 'jojoo', '', 'joojo@gmail.com', 'jojo', '', 'images/default', 0, '2017-11-02 16:19:05', '1eb57db6ec1c3186d26830731e1bd69b'),
(8, 'hahah', 'a', 'babi@hehe.com', '', 'ja', 'images/default', 0, '2017-11-02 16:25:56', '81a56d0465164c751bf540cd818c59c7'),
(9, 'hahahab', 'a', 'babi@h2ehe.com', 'la', 'ja', 'images/default', 0, '2017-11-03 02:20:59', ''),
(10, 'haha', 'agusg', 'lala@gmail.com', 'ha', '0102', 'images/default', 1, '2017-11-03 02:17:02', ''),
(11, 'haha2', 'agusg', 'lala2@gmail.com', 'la', '0102', 'images/default', 0, '2017-11-03 02:22:43', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;COMMIT;

--
-- Modify access_token column to accomodate longer tokens
--
ALTER TABLE `users`
  MODIFY `access_token` varchar(1000) NOT NULL;


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
