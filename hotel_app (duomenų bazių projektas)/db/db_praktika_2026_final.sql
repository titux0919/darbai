-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 02, 2026 at 12:56 PM
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
-- Database: `db_praktika_2026`
--

-- --------------------------------------------------------

--
-- Table structure for table `app_user`
--

DROP TABLE IF EXISTS `app_user`;
CREATE TABLE IF NOT EXISTS `app_user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT NULL,
  `username` varchar(100) COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  `email` varchar(150) COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_lithuanian_ci NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uq_app_user_username` (`username`),
  KEY `fk_app_user_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `app_user`
--

INSERT INTO `app_user` (`user_id`, `role_id`, `username`, `email`, `password_hash`) VALUES
(1, 1, 'admin', 'admin@local.lt', '$2a$12$LYS.0YWsvjvq4TkfxzdGyu1RkFw8CeXVRSlUS8HNP4hkhA9RpWexe'),
(2, 2, 'buhalteris', 'buhalteris@local.lt', '$2a$12$pJVu4arvUBxH5bXh6vbDA.DeDqD4WRYdHijgbMXOeZE3OJjX8/aHW'),
(3, 3, 'user', 'user@local.lt', '$2a$12$bNKVWkjx782nr5q7/9Otr.3ApjSUw5czG.RSMvHLlE2sioM5JXIHC'),
(4, 4, 'registratorius', 'registratorius@hotel.local', '$2a$12$7fOTNSCGPbuqYEsw1ggVQ.fdBUpuN0UD4NTp/DuJUBnxcSvwZLwdK');

-- --------------------------------------------------------

--
-- Table structure for table `availability`
--

DROP TABLE IF EXISTS `availability`;
CREATE TABLE IF NOT EXISTS `availability` (
  `availability_id` int NOT NULL AUTO_INCREMENT,
  `hotel_id` int NOT NULL,
  `room_type_id` int NOT NULL,
  `date` date NOT NULL,
  `available_rooms` int NOT NULL DEFAULT '0',
  `total_rooms` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`availability_id`),
  UNIQUE KEY `uq_availability_hotel_type_date` (`hotel_id`,`room_type_id`,`date`),
  KEY `idx_availability_room_type_id` (`room_type_id`),
  KEY `idx_availability_date` (`date`)
) ;

--
-- Dumping data for table `availability`
--

INSERT INTO `availability` (`availability_id`, `hotel_id`, `room_type_id`, `date`, `available_rooms`, `total_rooms`) VALUES
(101, 1, 104, '2026-04-29', 5, 10),
(102, 1, 104, '2026-04-30', 1, 10),
(103, 1, 103, '2026-05-01', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
CREATE TABLE IF NOT EXISTS `booking` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `room_id` int NOT NULL,
  `check_in` date NOT NULL,
  `check_out` date NOT NULL,
  `guests_count` int NOT NULL DEFAULT '1',
  `booking_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total_price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `status` enum('pending','confirmed','cancelled','completed','no_show') CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL DEFAULT 'pending',
  PRIMARY KEY (`booking_id`),
  KEY `idx_booking_customer_id` (`customer_id`),
  KEY `idx_booking_room_id` (`room_id`),
  KEY `idx_booking_dates` (`check_in`,`check_out`)
) ;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`booking_id`, `customer_id`, `room_id`, `check_in`, `check_out`, `guests_count`, `booking_date`, `total_price`, `status`) VALUES
(102, 102, 103, '2026-04-30', '2026-05-01', 1, '2026-04-29 14:54:45', 0.00, 'confirmed'),
(103, 102, 103, '2026-04-29', '2026-04-30', 2, '2026-04-29 18:06:47', 0.00, 'confirmed'),
(104, 102, 104, '2026-04-29', '2026-04-30', 1, '2026-04-29 18:16:22', 150.00, 'confirmed'),
(105, 102, 104, '2026-04-30', '2026-05-01', 1, '2026-04-30 15:47:30', 132.00, 'confirmed'),
(106, 102, 107, '2026-04-30', '2026-05-01', 1, '2026-04-30 15:56:14', 132.00, 'confirmed'),
(107, 102, 107, '2026-04-30', '2026-05-01', 1, '2026-04-30 15:56:14', 132.00, 'confirmed');

--
-- Triggers `booking`
--
DROP TRIGGER IF EXISTS `trg_after_booking_insert`;
DELIMITER $$
CREATE TRIGGER `trg_after_booking_insert` AFTER INSERT ON `booking` FOR EACH ROW BEGIN
    IF NEW.status IN ('confirmed', 'completed') THEN
        UPDATE availability a
        JOIN room r 
          ON r.hotel_id = a.hotel_id 
         AND r.room_type_id = a.room_type_id
        SET a.available_rooms = a.available_rooms - 1
        WHERE r.room_id = NEW.room_id
          AND a.date >= NEW.check_in
          AND a.date < NEW.check_out
          AND a.available_rooms > 0;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `competitor_price`
--

DROP TABLE IF EXISTS `competitor_price`;
CREATE TABLE IF NOT EXISTS `competitor_price` (
  `competitor_price_id` int NOT NULL AUTO_INCREMENT,
  `hotel_id` int NOT NULL,
  `room_type_id` int NOT NULL,
  `date` date NOT NULL,
  `competitor_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL DEFAULT 'EUR',
  PRIMARY KEY (`competitor_price_id`),
  KEY `idx_competitor_price_hotel_id` (`hotel_id`),
  KEY `idx_competitor_price_room_type_id` (`room_type_id`),
  KEY `idx_competitor_price_date` (`date`)
) ;

--
-- Dumping data for table `competitor_price`
--

INSERT INTO `competitor_price` (`competitor_price_id`, `hotel_id`, `room_type_id`, `date`, `competitor_name`, `price`, `currency`) VALUES
(101, 1, 104, '2026-04-29', 'UAB Smart', 300.22, 'EUR'),
(102, 1, 104, '2026-04-29', 'UAB SMART', 200.55, 'EUR'),
(103, 1, 104, '2026-04-29', 'TEST', 200.00, 'EUR'),
(104, 1, 104, '2026-04-29', 'UAB TEST', 200.00, 'EUR'),
(105, 1, 104, '2026-04-30', 'Andrei', 300.00, 'EUR'),
(106, 1, 103, '2026-05-01', '250', 200.00, 'EUR'),
(107, 1, 104, '2026-05-01', '100', 100.00, 'EUR'),
(108, 1, 104, '2026-05-01', 'Andrei', 150.00, 'EUR');

-- --------------------------------------------------------

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
CREATE TABLE IF NOT EXISTS `country` (
  `country_id` int NOT NULL AUTO_INCREMENT,
  `country_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `country_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  `timezone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  PRIMARY KEY (`country_id`),
  UNIQUE KEY `uq_country_code` (`country_code`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `country`
--

INSERT INTO `country` (`country_id`, `country_name`, `country_code`, `currency`, `timezone`) VALUES
(1, 'Lithuania', 'LT', 'EUR', 'GMT +2'),
(101, 'Australija', 'AU', NULL, NULL),
(102, 'Anglija', 'UK', 'GBP', 'GMT +0');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
CREATE TABLE IF NOT EXISTS `customer` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `country_id` int DEFAULT NULL,
  `first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `last_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `uq_customer_email` (`email`),
  KEY `idx_customer_country_id` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customer_id`, `country_id`, `first_name`, `last_name`, `email`, `phone`, `created_at`, `balance`) VALUES
(102, 1, 'Lukas', 'Lukasas', 'user@local.lt', '+37060945440', '2026-04-29 14:12:17', 120.00),
(103, 1, 'Buhalteris', 'Buhalter', 'buhalteris@local.lt', '+37000000', '2026-04-30 15:28:57', 0.00),
(104, 102, 'Registratorius', 'Registratoir', 'registratorius@hotel.local', '+34000000', '2026-05-02 12:51:16', 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `facility`
--

DROP TABLE IF EXISTS `facility`;
CREATE TABLE IF NOT EXISTS `facility` (
  `facility_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci,
  PRIMARY KEY (`facility_id`),
  UNIQUE KEY `uq_facility_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `facility`
--

INSERT INTO `facility` (`facility_id`, `name`, `description`) VALUES
(107, 'Wifi', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `holiday`
--

DROP TABLE IF EXISTS `holiday`;
CREATE TABLE IF NOT EXISTS `holiday` (
  `holiday_id` int NOT NULL AUTO_INCREMENT,
  `country_id` int NOT NULL,
  `holiday_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `holiday_date` date NOT NULL,
  `is_public_holiday` tinyint(1) NOT NULL DEFAULT '1',
  `price_multiplier` decimal(5,2) NOT NULL DEFAULT '1.00',
  PRIMARY KEY (`holiday_id`),
  UNIQUE KEY `uq_holiday_country_date_name` (`country_id`,`holiday_date`,`holiday_name`),
  KEY `idx_holiday_date` (`holiday_date`)
) ;

--
-- Dumping data for table `holiday`
--

INSERT INTO `holiday` (`holiday_id`, `country_id`, `holiday_name`, `holiday_date`, `is_public_holiday`, `price_multiplier`) VALUES
(101, 1, 'Vasario 16', '2026-02-16', 1, 1.10);

-- --------------------------------------------------------

--
-- Table structure for table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
CREATE TABLE IF NOT EXISTS `hotel` (
  `hotel_id` int NOT NULL AUTO_INCREMENT,
  `country_id` int NOT NULL,
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci DEFAULT NULL,
  `star_rating` tinyint DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`hotel_id`),
  KEY `idx_hotel_country_id` (`country_id`),
  KEY `idx_hotel_city` (`city`)
) ;

--
-- Dumping data for table `hotel`
--

INSERT INTO `hotel` (`hotel_id`, `country_id`, `name`, `city`, `address`, `star_rating`, `created_at`) VALUES
(1, 1, 'Svajoniu hotelis', 'Vilnius', 'Grazioji g. 13', 2, '2026-04-29 13:37:46');

-- --------------------------------------------------------

--
-- Table structure for table `hotel_facility`
--

DROP TABLE IF EXISTS `hotel_facility`;
CREATE TABLE IF NOT EXISTS `hotel_facility` (
  `hotel_facility_id` int NOT NULL AUTO_INCREMENT,
  `hotel_id` int NOT NULL,
  `facility_id` int NOT NULL,
  PRIMARY KEY (`hotel_facility_id`),
  UNIQUE KEY `uq_hotel_facility` (`hotel_id`,`facility_id`),
  KEY `idx_hotel_facility_facility_id` (`facility_id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `hotel_facility`
--

INSERT INTO `hotel_facility` (`hotel_facility_id`, `hotel_id`, `facility_id`) VALUES
(101, 1, 107);

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
CREATE TABLE IF NOT EXISTS `payment` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `payment_method` enum('cash','card','bank_transfer','paypal','other') CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL DEFAULT 'card',
  `status` enum('pending','paid','failed','refunded') CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL DEFAULT 'pending',
  PRIMARY KEY (`payment_id`),
  KEY `idx_payment_booking_id` (`booking_id`)
) ;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`payment_id`, `booking_id`, `amount`, `payment_date`, `payment_method`, `status`) VALUES
(1, 103, 0.00, '2026-04-29 18:06:47', 'card', 'paid'),
(2, 104, 150.00, '2026-04-29 18:16:22', 'card', 'paid'),
(3, 105, 132.00, '2026-04-30 15:47:30', 'card', 'paid'),
(4, 106, 132.00, '2026-04-30 15:56:14', 'card', 'paid');

-- --------------------------------------------------------

--
-- Table structure for table `price_prediction`
--

DROP TABLE IF EXISTS `price_prediction`;
CREATE TABLE IF NOT EXISTS `price_prediction` (
  `prediction_id` int NOT NULL AUTO_INCREMENT,
  `hotel_id` int NOT NULL,
  `room_type_id` int NOT NULL,
  `prediction_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `target_date` date NOT NULL,
  `base_price` decimal(10,2) DEFAULT NULL,
  `season_multiplier` decimal(5,2) DEFAULT NULL,
  `holiday_multiplier` decimal(5,2) DEFAULT NULL,
  `avg_competitor_price` decimal(10,2) DEFAULT NULL,
  `predicted_price` decimal(10,2) NOT NULL,
  `confidence_score` decimal(5,2) DEFAULT NULL,
  `model_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL DEFAULT 'rule_based_v1',
  PRIMARY KEY (`prediction_id`),
  KEY `idx_price_prediction_hotel_id` (`hotel_id`),
  KEY `idx_price_prediction_room_type_id` (`room_type_id`),
  KEY `idx_price_prediction_target_date` (`target_date`)
) ;

--
-- Dumping data for table `price_prediction`
--

INSERT INTO `price_prediction` (`prediction_id`, `hotel_id`, `room_type_id`, `prediction_date`, `target_date`, `base_price`, `season_multiplier`, `holiday_multiplier`, `avg_competitor_price`, `predicted_price`, `confidence_score`, `model_version`) VALUES
(135, 1, 103, '2026-04-30 17:34:48', '2026-04-30', 200.00, NULL, NULL, 200.00, 200.00, NULL, 'rule_based_v1'),
(136, 1, 104, '2026-04-30 17:34:48', '2026-04-30', 120.00, NULL, NULL, 300.00, 210.00, NULL, 'rule_based_v1'),
(137, 1, 104, '2026-04-30 17:34:48', '2026-04-30', 120.00, NULL, NULL, 225.19, 172.60, NULL, 'rule_based_v1'),
(138, 1, 104, '2026-04-30 17:34:48', '2026-04-30', 120.00, NULL, NULL, 225.20, 172.60, NULL, 'rule_based_v1');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uq_role_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`role_id`, `name`) VALUES
(1, 'admin'),
(2, 'buhalteris'),
(4, 'registratorius'),
(3, 'user');

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
CREATE TABLE IF NOT EXISTS `room` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `hotel_id` int NOT NULL,
  `room_type_id` int NOT NULL,
  `room_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `floor` int DEFAULT NULL,
  `status` enum('available','occupied','maintenance','inactive') CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL DEFAULT 'available',
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `uq_room_hotel_number` (`hotel_id`,`room_number`),
  KEY `idx_room_hotel_id` (`hotel_id`),
  KEY `idx_room_room_type_id` (`room_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_lithuanian_ci;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`room_id`, `hotel_id`, `room_type_id`, `room_number`, `floor`, `status`) VALUES
(103, 1, 102, '103', 1, 'available'),
(104, 1, 102, '302', 3, 'available'),
(105, 1, 104, '304', 3, 'available'),
(107, 1, 104, '303', 3, 'available');

-- --------------------------------------------------------

--
-- Table structure for table `room_price`
--

DROP TABLE IF EXISTS `room_price`;
CREATE TABLE IF NOT EXISTS `room_price` (
  `price_id` int NOT NULL AUTO_INCREMENT,
  `hotel_id` int NOT NULL,
  `room_type_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL DEFAULT 'EUR',
  `valid_from` date DEFAULT NULL,
  `valid_to` date DEFAULT NULL,
  PRIMARY KEY (`price_id`),
  UNIQUE KEY `uq_room_price_current` (`hotel_id`,`room_type_id`,`valid_from`),
  KEY `idx_room_price_hotel_id` (`hotel_id`),
  KEY `idx_room_price_room_type_id` (`room_type_id`)
) ;

--
-- Dumping data for table `room_price`
--

INSERT INTO `room_price` (`price_id`, `hotel_id`, `room_type_id`, `price`, `currency`, `valid_from`, `valid_to`) VALUES
(101, 1, 104, 120.00, 'EUR', '2026-04-29', '2026-04-30'),
(102, 1, 103, 200.00, 'EUR', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `room_type`
--

DROP TABLE IF EXISTS `room_type`;
CREATE TABLE IF NOT EXISTS `room_type` (
  `room_type_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci,
  `base_capacity` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`room_type_id`),
  UNIQUE KEY `uq_room_type_name` (`name`)
) ;

--
-- Dumping data for table `room_type`
--

INSERT INTO `room_type` (`room_type_id`, `name`, `description`, `base_capacity`) VALUES
(101, 'Standartinis', 'Standartinis kambarys', 2),
(102, 'Liuksas', 'Liukso kambarys', 2),
(103, 'Šeimyninis', 'Šeimyninis kambarys', 4),
(104, 'Dvivietis', 'Puikus', 3),
(105, 'Extreme', 'Lorem ipsuma', 2);

-- --------------------------------------------------------

--
-- Table structure for table `season`
--

DROP TABLE IF EXISTS `season`;
CREATE TABLE IF NOT EXISTS `season` (
  `season_id` int NOT NULL AUTO_INCREMENT,
  `hotel_id` int DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_lithuanian_ci NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `price_multiplier` decimal(5,2) NOT NULL DEFAULT '1.00',
  PRIMARY KEY (`season_id`),
  KEY `idx_season_hotel_id` (`hotel_id`),
  KEY `idx_season_dates` (`start_date`,`end_date`)
) ;

--
-- Dumping data for table `season`
--

INSERT INTO `season` (`season_id`, `hotel_id`, `name`, `start_date`, `end_date`, `price_multiplier`) VALUES
(101, 1, 'Vasara', '2026-06-01', '2026-08-31', 1.20),
(102, NULL, 'test', '2026-04-01', '2026-04-30', 1.10);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_price_prediction_inputs`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `v_price_prediction_inputs`;
CREATE TABLE IF NOT EXISTS `v_price_prediction_inputs` (
`available_rooms` bigint
,`avg_competitor_price` decimal(14,6)
,`base_price` decimal(10,2)
,`country_id` int
,`currency` varchar(10)
,`holiday_multiplier` decimal(5,2)
,`hotel_id` int
,`hotel_name` varchar(150)
,`room_type_id` int
,`room_type_name` varchar(100)
,`season_multiplier` decimal(5,2)
,`target_date` date
,`total_rooms` bigint
);

-- --------------------------------------------------------

--
-- Structure for view `v_price_prediction_inputs`
--
DROP TABLE IF EXISTS `v_price_prediction_inputs`;

DROP VIEW IF EXISTS `v_price_prediction_inputs`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_price_prediction_inputs`  AS SELECT `h`.`hotel_id` AS `hotel_id`, `h`.`name` AS `hotel_name`, `h`.`country_id` AS `country_id`, `rt`.`room_type_id` AS `room_type_id`, `rt`.`name` AS `room_type_name`, `rp`.`price` AS `base_price`, `rp`.`currency` AS `currency`, `a`.`date` AS `target_date`, coalesce(max(`s`.`price_multiplier`),1.00) AS `season_multiplier`, coalesce(max(`hol`.`price_multiplier`),1.00) AS `holiday_multiplier`, avg(`cp`.`price`) AS `avg_competitor_price`, coalesce(`a`.`available_rooms`,0) AS `available_rooms`, coalesce(`a`.`total_rooms`,0) AS `total_rooms` FROM ((((((`hotel` `h` join `room_type` `rt`) join `room_price` `rp` on(((`rp`.`hotel_id` = `h`.`hotel_id`) and (`rp`.`room_type_id` = `rt`.`room_type_id`)))) left join `availability` `a` on(((`a`.`hotel_id` = `h`.`hotel_id`) and (`a`.`room_type_id` = `rt`.`room_type_id`)))) left join `season` `s` on((((`s`.`hotel_id` is null) or (`s`.`hotel_id` = `h`.`hotel_id`)) and (`a`.`date` between `s`.`start_date` and `s`.`end_date`)))) left join `holiday` `hol` on(((`hol`.`country_id` = `h`.`country_id`) and (`hol`.`holiday_date` = `a`.`date`)))) left join `competitor_price` `cp` on(((`cp`.`hotel_id` = `h`.`hotel_id`) and (`cp`.`room_type_id` = `rt`.`room_type_id`) and (`cp`.`date` = `a`.`date`)))) GROUP BY `h`.`hotel_id`, `h`.`name`, `h`.`country_id`, `rt`.`room_type_id`, `rt`.`name`, `rp`.`price`, `rp`.`currency`, `a`.`date`, `a`.`available_rooms`, `a`.`total_rooms` ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `app_user`
--
ALTER TABLE `app_user`
  ADD CONSTRAINT `fk_app_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`);

--
-- Constraints for table `availability`
--
ALTER TABLE `availability`
  ADD CONSTRAINT `fk_availability_hotel` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_availability_room_type` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `fk_booking_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_booking_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `competitor_price`
--
ALTER TABLE `competitor_price`
  ADD CONSTRAINT `fk_competitor_price_hotel` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_competitor_price_room_type` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `fk_customer_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `holiday`
--
ALTER TABLE `holiday`
  ADD CONSTRAINT `fk_holiday_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `hotel`
--
ALTER TABLE `hotel`
  ADD CONSTRAINT `fk_hotel_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `hotel_facility`
--
ALTER TABLE `hotel_facility`
  ADD CONSTRAINT `fk_hotel_facility_facility` FOREIGN KEY (`facility_id`) REFERENCES `facility` (`facility_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_hotel_facility_hotel` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `fk_payment_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `price_prediction`
--
ALTER TABLE `price_prediction`
  ADD CONSTRAINT `fk_price_prediction_hotel` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_price_prediction_room_type` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `fk_room_hotel` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_room_room_type` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `room_price`
--
ALTER TABLE `room_price`
  ADD CONSTRAINT `fk_room_price_hotel` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_room_price_room_type` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `season`
--
ALTER TABLE `season`
  ADD CONSTRAINT `fk_season_hotel` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`hotel_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
