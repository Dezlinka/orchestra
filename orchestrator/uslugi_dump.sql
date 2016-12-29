-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               5.7.15-log - MySQL Community Server (GPL)
-- ОС Сервера:                   Win64
-- HeidiSQL Версия:              9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Дамп структуры базы данных uslugi_crap
CREATE DATABASE IF NOT EXISTS `uslugi_crap` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `uslugi_crap`;


-- Дамп структуры для таблица uslugi_crap.claims
CREATE TABLE IF NOT EXISTS `claims` (
  `id` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.


-- Дамп структуры для процедура uslugi_crap.create_claim
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `create_claim`(IN `id` INT, IN `author` VARCHAR(50), IN `text` VARCHAR(250), IN `address` VARCHAR(100), IN `category` VARCHAR(100), IN `department` VARCHAR(100), IN `status` INT, IN `levelOfSolution` VARCHAR(100))
BEGIN
	INSERT INTO claims (id, author, text, address, category, department, status, levelOfSolution) 
	VALUES(id, author, text, address, category, department, status, levelOfSolution);
END//
DELIMITER ;


-- Дамп структуры для таблица uslugi_crap.statuses
CREATE TABLE IF NOT EXISTS `statuses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status_text` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
