-- -------------------------------------------
-- version 1.0
-- -------------------------------------------
CREATE DATABASE IF NOT EXISTS `member_master` CHARACTER SET utf8 COLLATE utf8_general_ci;
USE member_master;
CREATE TABLE IF NOT EXISTS rec_status_ref (

	rec_status_id INT NOT NULL PRIMARY KEY,
	description VARCHAR(128) DEFAULT '' NOT NULL
);

INSERT INTO rec_status_ref VALUES
(-1, 'Deleted'),
(0, 'Inactive'),
(1, 'Active');

CREATE TABLE IF NOT EXISTS users (

	user_id VARCHAR(36) NOT NULL PRIMARY KEY,
	descr VARCHAR(100) DEFAULT ''
);

INSERT INTO users VALUES ('a9998ce6-da2d-11e5-b5d2-0a1d41d68578', 'System users');

CREATE TABLE IF NOT EXISTS db_info (

	info_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	db_version INT NOT NULL,
	app_version INT,
	rec_created_by VARCHAR(36) NOT NULL,
	rec_created_when VARCHAR(128) NOT NULL,
	rec_modified_by VARCHAR(36) DEFAULT NULL,
	rec_modified_when VARCHAR(128) DEFAULT NULL,
	rec_status INT NOT NULL DEFAULT 0,
	FOREIGN KEY (rec_status) REFERENCES rec_status_ref (rec_status_id),
	FOREIGN KEY (rec_created_by) REFERENCES users(user_id),
	FOREIGN KEY (rec_modified_by) REFERENCES users(user_id)
);

INSERT INTO db_info (db_version, app_version, rec_created_by, rec_created_when, rec_status)
VALUES (1, 1.0, 'a9998ce6-da2d-11e5-b5d2-0a1d41d68578', NOW(), 1);

CREATE TABLE IF NOT EXISTS member (

	member_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	first_name NVARCHAR(100) NOT NULL,
	last_name NVARCHAR(100) NOT NULL,
	id BIGINT NOT NULL,
	tel VARCHAR(50) NOT NULL,
	address NVARCHAR(500) NOT NULL,
	sex VARCHAR(1) NOT NULL,
	birth VARCHAR(128) NOT NULL,
	rec_created_by VARCHAR(36) NOT NULL,
	rec_created_when VARCHAR(128) NOT NULL,
	rec_modified_by VARCHAR(36),
	rec_modified_when VARCHAR(128),
	rec_status INT NOT NULL DEFAULT 0,
	FOREIGN KEY (rec_status) REFERENCES rec_status_ref (rec_status_id),
	FOREIGN KEY (rec_created_by) REFERENCES users(user_id),
	FOREIGN KEY (rec_modified_by) REFERENCES users(user_id)
);

CREATE OR REPLACE VIEW member_vu AS (SELECT * FROM member);

-- -------------------------------------------
-- version 2.0
-- -------------------------------------------
UPDATE db_info SET db_version = 2;

ALTER TABLE member
ADD COLUMN point BIGINT NOT NULL DEFAULT 0 AFTER birth;

CREATE OR REPLACE VIEW member_vu AS (SELECT * FROM member);

-- -------------------------------------------
-- version 3.0
-- -------------------------------------------
UPDATE db_info SET db_version = 3;

CREATE TABLE IF NOT EXISTS point_activity_type_ref (
	type_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	activity_type VARCHAR(1) NOT NULL UNIQUE,
	description VARCHAR(32) NOT NULL
);

INSERT INTO point_activity_type_ref  (activity_type, description) VALUES ('A', 'Add points'), ('R', 'Redeem points'),('U','Update points available');

CREATE TABLE IF NOT EXISTS point_history (
	history_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	member_id INT NOT NULL,
	activity_type VARCHAR(1) NOT NULL,
	point INT NOT NULL,
	rec_created_by VARCHAR(36) NOT NULL,
	rec_created_when VARCHAR(128) NOT NULL,
	rec_modified_by VARCHAR(36),
	rec_modified_when VARCHAR(128),
	rec_status INT NOT NULL DEFAULT 0,
	FOREIGN KEY (activity_type) REFERENCES point_activity_type_ref(activity_type),
	FOREIGN KEY (member_id) REFERENCES member(member_id),
	FOREIGN KEY (rec_status) REFERENCES rec_status_ref (rec_status_id),
	FOREIGN KEY (rec_created_by) REFERENCES users(user_id),
	FOREIGN KEY (rec_modified_by) REFERENCES users(user_id)
	);

CREATE OR REPLACE VIEW point_history_vu AS (SELECT * FROM point_history);

SHOW ENGINE INNODB STATUS;