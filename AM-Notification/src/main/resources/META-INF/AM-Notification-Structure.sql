/* ---------------------------------------------------- */
/*  Generated by Enterprise Architect Version 12.1 		*/
/*  Created On : 09-Feb-2018 1:15:14 AM 				*/
/*  DBMS       : MySql 						*/
/* ---------------------------------------------------- */

SET FOREIGN_KEY_CHECKS=0 
;

/* Create Tables */

CREATE TABLE `application_quota`
(
	`quota_id` INT NOT NULL AUTO_INCREMENT,
	`app_id` VARCHAR(3) NOT NULL,
	`quota_date` VARCHAR(10) NOT NULL,
	`number_of_notification` INT NOT NULL,
	`event_counter` INT NOT NULL,
	CONSTRAINT `PK_application_qouta` PRIMARY KEY (`quota_id` ASC)
)

;

CREATE TABLE `category`
(
	`category_id` VARCHAR(3) NOT NULL,
	`app_id` VARCHAR(3) NOT NULL,
	`description` VARCHAR(100) NOT NULL,
	CONSTRAINT `PK_notification_category` PRIMARY KEY (`category_id` ASC)
)

;

CREATE TABLE `event`
(
	`event_id` VARCHAR(5) NOT NULL,
	`event_type` VARCHAR(3) NOT NULL,
	`category_id` VARCHAR(3) NOT NULL,
	`description` VARCHAR(150) 	,
	CONSTRAINT `PK_event` PRIMARY KEY (`event_id` ASC)
)

;

CREATE TABLE `event_notification`
(
	`event_ntf_id` VARCHAR(9) NOT NULL,
	`event_id` VARCHAR(5) NOT NULL,
	`notification` VARCHAR(3) NOT NULL,
	`description` VARCHAR(200) NOT NULL,
	CONSTRAINT `PK_event_notification` PRIMARY KEY (`event_ntf_id` ASC)
)

;

CREATE TABLE `event_ntf_start`
(
	`valid_event_id` VARCHAR(25) NOT NULL,
	`start_ntf_id` INT NOT NULL,
	CONSTRAINT `PK_event_ntf_start_id` PRIMARY KEY (`valid_event_id` ASC)
)

;

CREATE TABLE `event_parameter`
(
	`parameter_id` INT NOT NULL AUTO_INCREMENT,
	`event_id` VARCHAR(5) NOT NULL,
	`parameter` VARCHAR(50) NOT NULL,
	CONSTRAINT `PK_email_params` PRIMARY KEY (`parameter_id` ASC)
)

;

CREATE TABLE `java_mail_config`
(
	`mail_config_id` VARCHAR(10) NOT NULL,
	`smtp` VARCHAR(50) NOT NULL,
	CONSTRAINT `PK_java_mail_config` PRIMARY KEY (`mail_config_id` ASC)
)

;

CREATE TABLE `notification`
(
	`notification_id` VARCHAR(25) NOT NULL,
	`valid_event_id` VARCHAR(25) NOT NULL,
	`sender_id` INT 	,
	`destination_address` VARCHAR(60) NOT NULL,
	`message_subject` VARCHAR(50) 	,
	`message_body` LONGTEXT NOT NULL,
	`is_sent` BOOL 	,
	`sent_date` DATETIME 	,
	`num_of_trails` INT 	,
	`failure_reason` TEXT 	,
	`creation_date` DATETIME NOT NULL,
	`ntf_type` VARCHAR(3) NOT NULL,
	CONSTRAINT `PK_email` PRIMARY KEY (`notification_id` ASC)
)

;

CREATE TABLE `notification_type`
(
	`ntf_type` VARCHAR(3) NOT NULL,
	`description` VARCHAR(100) NOT NULL,
	CONSTRAINT `PK_notification_type` PRIMARY KEY (`ntf_type` ASC)
)

;

CREATE TABLE `received_event`
(
	`received_event_id` INT NOT NULL AUTO_INCREMENT,
	`app_id` VARCHAR(3) NOT NULL,
	`event_content` LONGBLOB NOT NULL,
	`valid` BOOL 	,
	`received_date` DATETIME NOT NULL,
	CONSTRAINT `PK_received_event` PRIMARY KEY (`received_event_id` ASC)
)

;

CREATE TABLE `registered_application`
(
	`app_id` VARCHAR(3) NOT NULL,
	`app_name` VARCHAR(50) NOT NULL,
	`description` VARCHAR(100) NOT NULL,
	`username` VARCHAR(50) NOT NULL,
	`user_pass` VARCHAR(50) NOT NULL,
	`creation_date` DATETIME NOT NULL,
	`quota_per_day` INT NOT NULL,
	`reached_max_quota` BOOL NOT NULL,
	CONSTRAINT `PK_Table1` PRIMARY KEY (`app_id` ASC)
)

;

CREATE TABLE `system_address`
(
	`address_id` INT NOT NULL AUTO_INCREMENT,
	`app_id` VARCHAR(3) NOT NULL,
	`address` VARCHAR(60) NOT NULL,
	`type` VARCHAR(3) NOT NULL,
	`address_password` VARCHAR(50) NOT NULL,
	`mail_config_id` VARCHAR(10) NOT NULL,
	CONSTRAINT `PK_system_address` PRIMARY KEY (`address_id` ASC)
)

;

CREATE TABLE `template`
(
	`template_id` INTEGER NOT NULL AUTO_INCREMENT,
	`event_ntf_id` VARCHAR(9) NOT NULL,
	`ntf_type` VARCHAR(3) NOT NULL,
	`template_subject` VARCHAR(50) NOT NULL,
	`file_name` VARCHAR(100) NOT NULL,
	CONSTRAINT `PK_email_templates` PRIMARY KEY (`template_id` ASC)
)

;

CREATE TABLE `valid_event`
(
	`valid_event_id` VARCHAR(25) NOT NULL,
	`app_notification_id` VARCHAR(30) NOT NULL,
	`jms_id` VARCHAR(150) NOT NULL,
	`event_id` VARCHAR(5) NOT NULL,
	`app_id` VARCHAR(3) NOT NULL,
	`category_id` VARCHAR(3) NOT NULL,
	`category_related_id` VARCHAR(30) 	,
	`receive_date` DATETIME NOT NULL,
	`creation_date` DATETIME NOT NULL,
	`process_date` DATETIME 	,
	`is_completed` BOOL 	,
	`completion_date` DATETIME 	,
	`is_quarantined` BOOL NOT NULL,
	CONSTRAINT `PK_valid_event` PRIMARY KEY (`valid_event_id` ASC)
)

;

CREATE TABLE `valid_event_destination`
(
	`destination_id` INT NOT NULL AUTO_INCREMENT,
	`valid_event_id` VARCHAR(25) NOT NULL,
	`event_ntf_id` VARCHAR(9) NOT NULL,
	`email` VARCHAR(60) 	,
	`phone` VARCHAR(20) 	,
	`user_id` VARCHAR(25) 	,
	`full_name` VARCHAR(50) 	,
	CONSTRAINT `PK_input_event_destination` PRIMARY KEY (`destination_id` ASC)
)

;

CREATE TABLE `valid_event_parameter`
(
	`parameter_id` INT NOT NULL AUTO_INCREMENT,
	`valid_event_id` VARCHAR(25) NOT NULL,
	`key` VARCHAR(50) 	,
	`value` VARCHAR(200) 	,
	CONSTRAINT `PK_input_event_parameter` PRIMARY KEY (`parameter_id` ASC)
)

;

/* Create Foreign Key Constraints */

ALTER TABLE `application_quota` 
 ADD CONSTRAINT `FK_app_quota_reg_app`
	FOREIGN KEY (`app_id`) REFERENCES `registered_application` (`app_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `category` 
 ADD CONSTRAINT `FK_category_reg_app`
	FOREIGN KEY (`app_id`) REFERENCES `registered_application` (`app_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `event` 
 ADD CONSTRAINT `FK_event_category`
	FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `event_notification` 
 ADD CONSTRAINT `FK_event_notification_event`
	FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `event_parameter` 
 ADD CONSTRAINT `FK_event_parameter_event`
	FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `notification` 
 ADD CONSTRAINT `FK_ntf_ntf_type`
	FOREIGN KEY (`ntf_type`) REFERENCES `notification_type` (`ntf_type`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `notification` 
 ADD CONSTRAINT `FK_ntf_system_address`
	FOREIGN KEY (`sender_id`) REFERENCES `system_address` (`address_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `notification` 
 ADD CONSTRAINT `FK_ntf_valid_event`
	FOREIGN KEY (`valid_event_id`) REFERENCES `valid_event` (`valid_event_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `received_event` 
 ADD CONSTRAINT `FK_received_event_registered_application`
	FOREIGN KEY (`app_id`) REFERENCES `registered_application` (`app_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `system_address` 
 ADD CONSTRAINT `FK_sys_addr_java_mail_config`
	FOREIGN KEY (`mail_config_id`) REFERENCES `java_mail_config` (`mail_config_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `system_address` 
 ADD CONSTRAINT `FK_sys_addr_ntf_type`
	FOREIGN KEY (`type`) REFERENCES `notification_type` (`ntf_type`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `system_address` 
 ADD CONSTRAINT `FK_sys_addr_reg_app`
	FOREIGN KEY (`app_id`) REFERENCES `registered_application` (`app_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `template` 
 ADD CONSTRAINT `FK_temp_event_ntf`
	FOREIGN KEY (`event_ntf_id`) REFERENCES `event_notification` (`event_ntf_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `template` 
 ADD CONSTRAINT `FK_temp_ntf_type`
	FOREIGN KEY (`ntf_type`) REFERENCES `notification_type` (`ntf_type`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `valid_event` 
 ADD CONSTRAINT `FK_valid_event_category`
	FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `valid_event` 
 ADD CONSTRAINT `FK_valid_event_event`
	FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `valid_event` 
 ADD CONSTRAINT `FK_valid_event_reg_app`
	FOREIGN KEY (`app_id`) REFERENCES `registered_application` (`app_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `valid_event_destination` 
 ADD CONSTRAINT `FK_event_dest_event_ntf`
	FOREIGN KEY (`event_ntf_id`) REFERENCES `event_notification` (`event_ntf_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `valid_event_destination` 
 ADD CONSTRAINT `FK_valid_event_dest`
	FOREIGN KEY (`valid_event_id`) REFERENCES `valid_event` (`valid_event_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `valid_event_parameter` 
 ADD CONSTRAINT `FK_event_parameter_valid_event`
	FOREIGN KEY (`valid_event_id`) REFERENCES `valid_event` (`valid_event_id`) ON DELETE Restrict ON UPDATE Restrict
;

SET FOREIGN_KEY_CHECKS=1 
;