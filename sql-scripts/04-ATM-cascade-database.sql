CREATE DATABASE  IF NOT EXISTS `04-ATM-cascade-database`;
USE `04-ATM-cascade-database`;


SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `members`;
DROP TABLE IF EXISTS `withdrawals`;

--
-- Table structure for table `members`
--

CREATE TABLE `members` (
  `user_id` varchar(50) NOT NULL,
  `pw` char(68) NOT NULL,
  `active` tinyint NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Inserting data for table `members`
--
-- NOTE: The passwords are encrypted using BCrypt
--
-- A generation tool is avail at: https://www.luv2code.com/generate-bcrypt-password
--
-- Default passwords here are: 1234, 2222, 3333

INSERT INTO `members`
VALUES
('1111111111111111','{bcrypt}$2a$10$RASFcoym/iLBMHGQPM51T.JrI.RdCT9O/VFabyva7eP4bWJJrWbti',1),
('2222222222222222','{bcrypt}$2a$10$9mi119MYPnsVwyyYMlTfGeLSKIPj5Lhc.TDtj1vTLaYi4V/7neV36',1),
('3333333333333333','{bcrypt}$2a$10$hWJQ7d9Nnu8U6lO7nndwLOv47foGKeXTRnw5RG4oN4gpt30TxNHlC',1);

--
-- Table structure for table `authorities`
--

CREATE TABLE `roles` (
  `user_id` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  UNIQUE KEY `authorities8_idx_1` (`user_id`, `role`),
  CONSTRAINT `authorities8_ibfk_1` 
    FOREIGN KEY (`user_id`) 
    REFERENCES `members` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `roles`
--

INSERT INTO `roles`
VALUES
('1111111111111111','ROLE_EMPLOYEE'),
('2222222222222222','ROLE_EMPLOYEE'),
('2222222222222222','ROLE_MANAGER'),
('3333333333333333','ROLE_EMPLOYEE'),
('3333333333333333','ROLE_MANAGER'),
('3333333333333333','ROLE_ADMIN');

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` varchar(50) NOT NULL,
  `amount` int DEFAULT 0,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `user_ibfk_1` 
    FOREIGN KEY (`user_id`) 
    REFERENCES `members` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Data for table `user`
--

INSERT INTO `user` VALUES 
	('1111111111111111',1000),
	('2222222222222222',2000),
	('3333333333333333',3000);

--
-- Table structure for table `withdrawals`
--

CREATE TABLE `withdrawals` (
	`id` CHAR(36) PRIMARY KEY,
  `user_id` VARCHAR(50) NOT NULL,
  `amount` INT NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (`user_id`)
	REFERENCES `members` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=latin1;
    
    SET FOREIGN_KEY_CHECKS = 1;