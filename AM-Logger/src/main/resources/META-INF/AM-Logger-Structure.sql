/* ---------------------------------------------------- */
/*  Generated by Enterprise Architect Version 12.1 		*/
/*  Created On : 20-Jan-2018 12:47:39 PM 				*/
/*  DBMS       : MySql 						*/
/* ---------------------------------------------------- */

SET FOREIGN_KEY_CHECKS=0 
;

/* Create Tables */

CREATE TABLE `arc_bus_log`
(
	`arc_log_id` VARCHAR(20) NOT NULL,
	`category` VARCHAR(3) NOT NULL,
	`category_related_id` VARCHAR(30) NOT NULL,
	`log_date` DATETIME NOT NULL,
	`action_user_id` VARCHAR(30) 	,
	`log_type` VARCHAR(1) NOT NULL,
	`new_data_json` LONGTEXT 	,
	`deleted_data_json` LONGTEXT 	,
	`updated_from_json` LONGTEXT 	,
	`updated_to_json` LONGTEXT 	,
	`item_updated_json` LONGTEXT 	,
	CONSTRAINT `PK_arc_bus_log` PRIMARY KEY (`arc_log_id` ASC)
)

;

CREATE TABLE `arc_per_exec_log`
(
	`arc_exec_log_id` VARCHAR(20) NOT NULL,
	`function_id` INT NOT NULL,
	`log_type` VARCHAR(2) NOT NULL,
	`thread_id` INT NOT NULL,
	`exec_time_stamp` TIMESTAMP NOT NULL,
	`source_id` INT NOT NULL,
	`interface_id` INT NOT NULL,
	`phase_id` INT NOT NULL,
	`interface_related_id` VARCHAR(150) 	,
	`warn_id` INT 	,
	`error_id` INT 	,
	`info_id` INT 	,
	`args` LONGTEXT 	,
	`full_msg` TEXT NOT NULL,
	`exec_log_date` DATETIME NOT NULL,
	`processed` BOOL NOT NULL,
	CONSTRAINT `PK_arc_exec_log` PRIMARY KEY (`arc_exec_log_id` ASC)
)

;

CREATE TABLE `arc_per_fn_exec_log`
(
	`log_id` VARCHAR(20) NOT NULL,
	`function_id` INT NOT NULL,
	`thread_id` INT NOT NULL,
	`interface_id` INT NOT NULL,
	`source_id` INT NOT NULL,
	`phase_id` INT NOT NULL,
	`status` VARCHAR(1) NOT NULL,
	`last_exec_id` BIGINT NOT NULL,
	CONSTRAINT `PK_arc_per_fn_exec_log` PRIMARY KEY (`log_id` ASC)
)

;

CREATE TABLE `bus_category`
(
	`category` VARCHAR(3) NOT NULL,
	`description` VARCHAR(100) NOT NULL,
	CONSTRAINT `PK_category` PRIMARY KEY (`category` ASC)
)

;

CREATE TABLE `bus_delete_log`
(
	`log_id` INT NOT NULL AUTO_INCREMENT,
	`category` VARCHAR(3) NOT NULL,
	`category_related_id` VARCHAR(30) NOT NULL,
	`log_date` DATETIME NOT NULL,
	`action_user_id` VARCHAR(30) NOT NULL,
	`deleted_data_json` LONGTEXT NOT NULL,
	CONSTRAINT `PK_delete_log` PRIMARY KEY (`log_id` ASC)
)

;

CREATE TABLE `bus_log_type`
(
	`type` VARCHAR(1) NOT NULL,
	`description` VARCHAR(50) NOT NULL,
	CONSTRAINT `PK_bus_log_type` PRIMARY KEY (`type` ASC)
)

;

CREATE TABLE `bus_new_log`
(
	`log_id` INT NOT NULL AUTO_INCREMENT,
	`category` VARCHAR(3) NOT NULL,
	`category_related_id` VARCHAR(30) NOT NULL,
	`log_date` DATETIME NOT NULL,
	`action_user_id` VARCHAR(30) 	,
	`new_data_json` LONGTEXT NOT NULL,
	CONSTRAINT `PK_new_log` PRIMARY KEY (`log_id` ASC)
)

;

CREATE TABLE `bus_update_attr_log`
(
	`log_id` INT NOT NULL AUTO_INCREMENT,
	`update_log_id` INT NOT NULL,
	`attribute` VARCHAR(50) NOT NULL,
	`from_value` VARCHAR(500) NOT NULL,
	`to_value` VARCHAR(500) NOT NULL,
	CONSTRAINT `PK_update_attribute_log` PRIMARY KEY (`log_id` ASC)
)

;

CREATE TABLE `bus_update_log`
(
	`log_id` INT NOT NULL AUTO_INCREMENT,
	`category` VARCHAR(3) NOT NULL,
	`category_related_id` VARCHAR(30) NOT NULL,
	`log_date` DATETIME NOT NULL,
	`action_user_id` VARCHAR(30) NOT NULL,
	`update_from_json` LONGTEXT NOT NULL,
	`update_to_json` LONGTEXT NOT NULL,
	CONSTRAINT `PK_update_log` PRIMARY KEY (`log_id` ASC)
)

;

CREATE TABLE `per_error`
(
	`error_id` INT NOT NULL AUTO_INCREMENT,
	`error_code` VARCHAR(10) NOT NULL,
	`first_date_log` DATETIME NOT NULL,
	`first_exec_log` VARCHAR(20) 	,
	CONSTRAINT `PK_per_error` PRIMARY KEY (`error_id` ASC)
)

;

CREATE TABLE `per_exec_log`
(
	`execution_log_id` BIGINT NOT NULL AUTO_INCREMENT,
	`function_id` INT NOT NULL,
	`log_type` VARCHAR(2) NOT NULL,
	`thread_id` INT NOT NULL,
	`exec_time_stamp` TIMESTAMP NOT NULL,
	`source_id` INT NOT NULL,
	`interface_id` INT NOT NULL,
	`phase_id` INT NOT NULL,
	`interface_related_id` VARCHAR(150) 	,
	`warn_id` INT 	,
	`error_id` INT 	,
	`info_id` INT 	,
	`args` LONGTEXT 	,
	`full_msg` TEXT NOT NULL,
	`exec_log_date` DATETIME NOT NULL,
	`processed` BOOL NOT NULL,
	CONSTRAINT `PK_execution_log` PRIMARY KEY (`execution_log_id` ASC)
)

;

CREATE TABLE `per_fn_exec_log`
(
	`log_id` BIGINT NOT NULL AUTO_INCREMENT,
	`function_id` INT NOT NULL,
	`thread_id` INT NOT NULL,
	`interface_id` INT NOT NULL,
	`source_id` INT NOT NULL,
	`phase_id` INT NOT NULL,
	`status` VARCHAR(1) NOT NULL,
	`last_exec_id` VARCHAR(20) NOT NULL,
	CONSTRAINT `PK_per_fn_exec_log` PRIMARY KEY (`log_id` ASC)
)

;

CREATE TABLE `per_fn_log`
(
	`function_id` INT NOT NULL AUTO_INCREMENT,
	`fn_name` VARCHAR(100) NOT NULL,
	`class_name` VARCHAR(50) NOT NULL,
	`first_date_log` DATETIME NOT NULL,
	`first_exec_log` VARCHAR(20) 	,
	`num_start_debug` INT NOT NULL,
	`num_end_debug` INT NOT NULL,
	`num_error` INT NOT NULL,
	`num_info` INT NOT NULL,
	`num_warn` INT NOT NULL,
	CONSTRAINT `PK_function_class` PRIMARY KEY (`function_id` ASC)
)

;

CREATE TABLE `per_fn_status`
(
	`status` VARCHAR(1) NOT NULL,
	`description` VARCHAR(50) NOT NULL,
	CONSTRAINT `PK_per_function_status` PRIMARY KEY (`status` ASC)
)

;

CREATE TABLE `per_info`
(
	`info_id` INT NOT NULL AUTO_INCREMENT,
	`info_code` VARCHAR(10) NOT NULL,
	`first_date_log` DATETIME NOT NULL,
	`first_exec_log` VARCHAR(20) 	,
	CONSTRAINT `PK_per_info` PRIMARY KEY (`info_id` ASC)
)

;

CREATE TABLE `per_interface`
(
	`interface_id` INT NOT NULL AUTO_INCREMENT,
	`interface_name` VARCHAR(50) NOT NULL,
	`first_date_log` DATETIME NOT NULL,
	`first_exec_log` VARCHAR(20) 	,
	CONSTRAINT `PK_per_interface` PRIMARY KEY (`interface_id` ASC)
)

;

CREATE TABLE `per_log_type`
(
	`type` VARCHAR(2) NOT NULL,
	`description` VARCHAR(50) NOT NULL,
	CONSTRAINT `PK_per_log_type` PRIMARY KEY (`type` ASC)
)

;

CREATE TABLE `per_phase`
(
	`phase_id` INT NOT NULL AUTO_INCREMENT,
	`phase_name` VARCHAR(50) NOT NULL,
	`first_date_log` DATETIME NOT NULL,
	`first_exec_log` VARCHAR(20) 	,
	CONSTRAINT `PK_per_phase` PRIMARY KEY (`phase_id` ASC)
)

;

CREATE TABLE `per_source`
(
	`source_id` INT NOT NULL,
	`source_name` VARCHAR(50) NOT NULL,
	`first_date_log` DATETIME NOT NULL,
	`first_exec_log` VARCHAR(20) 	,
	CONSTRAINT `PK_per_source` PRIMARY KEY (`source_id` ASC)
)

;

CREATE TABLE `per_thread`
(
	`thread_id` INT NOT NULL AUTO_INCREMENT,
	`thread_name` VARCHAR(100) NOT NULL,
	`first_date_log` DATETIME NOT NULL,
	`first_exec_log` VARCHAR(20) 	,
	CONSTRAINT `PK_per_thread` PRIMARY KEY (`thread_id` ASC)
)

;

CREATE TABLE `per_warn`
(
	`warn_id` INT NOT NULL AUTO_INCREMENT,
	`warn_code` VARCHAR(10) NOT NULL,
	`first_date_log` DATETIME NOT NULL,
	`first_exec_log` VARCHAR(20) 	,
	CONSTRAINT `PK_per_warn` PRIMARY KEY (`warn_id` ASC)
)

;

CREATE TABLE `users`
(
	`username` VARCHAR(50) NOT NULL,
	`password` VARCHAR(100) NOT NULL,
	CONSTRAINT `PK_users` PRIMARY KEY (`username` ASC)
)

;

/* Create Foreign Key Constraints */

ALTER TABLE `arc_bus_log` 
 ADD CONSTRAINT `FK_arc_bus_log_bus_category`
	FOREIGN KEY (`category`) REFERENCES `bus_category` (`category`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_bus_log` 
 ADD CONSTRAINT `FK_arc_bus_log_bus_log_type`
	FOREIGN KEY (`log_type`) REFERENCES `bus_log_type` (`type`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_exec_log` 
 ADD CONSTRAINT `FK_arc_per_exec_log_per_info`
	FOREIGN KEY (`info_id`) REFERENCES `per_info` (`info_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_exec_log` 
 ADD CONSTRAINT `FK_arc_per_exec_log_per_fn_log`
	FOREIGN KEY (`function_id`) REFERENCES `per_fn_log` (`function_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_exec_log` 
 ADD CONSTRAINT `FK_arc_per_exec_log_per_log_type`
	FOREIGN KEY (`log_type`) REFERENCES `per_log_type` (`type`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_exec_log` 
 ADD CONSTRAINT `FK_arc_per_exec_log_per_interface`
	FOREIGN KEY (`interface_id`) REFERENCES `per_interface` (`interface_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_exec_log` 
 ADD CONSTRAINT `FK_arc_per_exec_log_per_thread`
	FOREIGN KEY (`thread_id`) REFERENCES `per_thread` (`thread_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_exec_log` 
 ADD CONSTRAINT `FK_arc_per_exec_log_per_source`
	FOREIGN KEY (`source_id`) REFERENCES `per_source` (`source_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_exec_log` 
 ADD CONSTRAINT `FK_arc_per_exec_log_per_phase`
	FOREIGN KEY (`phase_id`) REFERENCES `per_phase` (`phase_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_exec_log` 
 ADD CONSTRAINT `FK_arc_per_exec_log_per_error`
	FOREIGN KEY (`error_id`) REFERENCES `per_error` (`error_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_exec_log` 
 ADD CONSTRAINT `FK_arc_per_exec_log_per_warn`
	FOREIGN KEY (`warn_id`) REFERENCES `per_warn` (`warn_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_fn_exec_log` 
 ADD CONSTRAINT `FK_arc_per_fn_exec_log_per_fn_log`
	FOREIGN KEY (`function_id`) REFERENCES `per_fn_log` (`function_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_fn_exec_log` 
 ADD CONSTRAINT `FK_arc_per_fn_exec_log_per_thread`
	FOREIGN KEY (`thread_id`) REFERENCES `per_thread` (`thread_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_fn_exec_log` 
 ADD CONSTRAINT `FK_arc_per_fn_exec_log_per_exec_log`
	FOREIGN KEY (`last_exec_id`) REFERENCES `per_exec_log` (`execution_log_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_fn_exec_log` 
 ADD CONSTRAINT `FK_arc_per_fn_exec_log_per_fn_status`
	FOREIGN KEY (`status`) REFERENCES `per_fn_status` (`status`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_fn_exec_log` 
 ADD CONSTRAINT `FK_arc_per_fn_exec_log_per_interface`
	FOREIGN KEY (`interface_id`) REFERENCES `per_interface` (`interface_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_fn_exec_log` 
 ADD CONSTRAINT `FK_arc_per_fn_exec_log_per_phase`
	FOREIGN KEY (`phase_id`) REFERENCES `per_phase` (`phase_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `arc_per_fn_exec_log` 
 ADD CONSTRAINT `FK_arc_per_fn_exec_log_per_source`
	FOREIGN KEY (`source_id`) REFERENCES `per_source` (`source_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `bus_delete_log` 
 ADD CONSTRAINT `FK_delete_log_category`
	FOREIGN KEY (`category`) REFERENCES `bus_category` (`category`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `bus_new_log` 
 ADD CONSTRAINT `FK_new_log_category`
	FOREIGN KEY (`category`) REFERENCES `bus_category` (`category`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `bus_update_attr_log` 
 ADD CONSTRAINT `FK_update_attribute_log`
	FOREIGN KEY (`update_log_id`) REFERENCES `bus_update_log` (`log_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `bus_update_log` 
 ADD CONSTRAINT `FK_update_log_category`
	FOREIGN KEY (`category`) REFERENCES `bus_category` (`category`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_exec_log` 
 ADD CONSTRAINT `FK_per_exec_log_per_error`
	FOREIGN KEY (`error_id`) REFERENCES `per_error` (`error_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_exec_log` 
 ADD CONSTRAINT `FK_per_exec_log_per_fn_log`
	FOREIGN KEY (`function_id`) REFERENCES `per_fn_log` (`function_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_exec_log` 
 ADD CONSTRAINT `FK_per_exec_log_per_info`
	FOREIGN KEY (`info_id`) REFERENCES `per_info` (`info_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_exec_log` 
 ADD CONSTRAINT `FK_per_exec_log_per_interface`
	FOREIGN KEY (`interface_id`) REFERENCES `per_interface` (`interface_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_exec_log` 
 ADD CONSTRAINT `FK_per_exec_log_per_log_type`
	FOREIGN KEY (`log_type`) REFERENCES `per_log_type` (`type`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_exec_log` 
 ADD CONSTRAINT `FK_per_exec_log_per_phase`
	FOREIGN KEY (`phase_id`) REFERENCES `per_phase` (`phase_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_exec_log` 
 ADD CONSTRAINT `FK_per_exec_log_per_source`
	FOREIGN KEY (`source_id`) REFERENCES `per_source` (`source_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_exec_log` 
 ADD CONSTRAINT `FK_per_exec_log_per_thread`
	FOREIGN KEY (`thread_id`) REFERENCES `per_thread` (`thread_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_exec_log` 
 ADD CONSTRAINT `FK_per_exec_log_per_warn`
	FOREIGN KEY (`warn_id`) REFERENCES `per_warn` (`warn_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_fn_exec_log` 
 ADD CONSTRAINT `FK_per_fn_exec_log_per_fn_log`
	FOREIGN KEY (`function_id`) REFERENCES `per_fn_log` (`function_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_fn_exec_log` 
 ADD CONSTRAINT `FK_per_fn_exec_log_per_fn_status`
	FOREIGN KEY (`status`) REFERENCES `per_fn_status` (`status`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_fn_exec_log` 
 ADD CONSTRAINT `FK_per_fn_exec_log_per_interface`
	FOREIGN KEY (`interface_id`) REFERENCES `per_interface` (`interface_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_fn_exec_log` 
 ADD CONSTRAINT `FK_per_fn_exec_log_per_phase`
	FOREIGN KEY (`phase_id`) REFERENCES `per_phase` (`phase_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_fn_exec_log` 
 ADD CONSTRAINT `FK_per_fn_exec_log_per_source`
	FOREIGN KEY (`source_id`) REFERENCES `per_source` (`source_id`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `per_fn_exec_log` 
 ADD CONSTRAINT `FK_per_fn_exec_log_per_thread`
	FOREIGN KEY (`thread_id`) REFERENCES `per_thread` (`thread_id`) ON DELETE Restrict ON UPDATE Restrict
;

SET FOREIGN_KEY_CHECKS=1 
;
