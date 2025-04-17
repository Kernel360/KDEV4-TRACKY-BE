DROP TABLE IF EXISTS `car_event`;
DROP TABLE IF EXISTS `time_distance`;
DROP TABLE IF EXISTS `gpshistory`;
DROP TABLE IF EXISTS `drive`;
DROP TABLE IF EXISTS `location`;
DROP TABLE IF EXISTS `rent`;
DROP TABLE IF EXISTS `car`;
DROP TABLE IF EXISTS `device`;
DROP TABLE IF EXISTS `member`;
DROP TABLE IF EXISTS `biz`;

CREATE TABLE `biz` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`biz_name` varchar(100) NULL,
	`biz_reg_num` varchar(12) NULL,
	`biz_admin`	varchar(100) NULL,
	`biz_phone_num`	varchar(20)	NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	`deleted_at` timestamp NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `member` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`biz_id` bigint	NOT NULL,
	`member_id`	varchar(100) NOT NULL,
	`pwd` varchar(100) NOT NULL,
	`email`	varchar(100) NOT NULL,
	`role`	varchar(10) NOT NULL,
	`lastlogin_at` timestamp NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	`deleted_at` timestamp NULL,
	PRIMARY KEY (`id`),
    CONSTRAINT `fk_member_biz`
        FOREIGN KEY (`biz_id`) REFERENCES `biz`(`id`)
);

CREATE TABLE `device` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`tid` varchar(10) NOT NULL,
	`mid` varchar(10) NOT NULL,
	`did` varchar(10) NOT NULL,
	`pv` varchar(10) NOT NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `car` (
	`mdn` varchar(100) NOT NULL,
	`biz_id` bigint NOT NULL,
	`device_id`	bigint NULL,
	`car_type` varchar(100) NOT NULL,
	`car_plate` varchar(100) NOT NULL,
	`car_year` varchar(100) NOT NULL,
	`purpose` varchar(100) NOT NULL,
	`status` enum('running', 'waiting', 'fixing') NOT NULL,
	`sum` varchar(255) NOT NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	`deleted_at` timestamp NULL,
	PRIMARY KEY (`mdn`),
    CONSTRAINT `fk_car_biz`
        FOREIGN KEY (`biz_id`) REFERENCES `biz`(`id`),
    CONSTRAINT `fk_car_device`
        FOREIGN KEY (`device_id`) REFERENCES `device`(`id`)
);

CREATE TABLE `rent` (
	`rent_uuid`	varchar(100) NOT NULL,
	`mdn`	varchar(100) NOT NULL,
	`rent_stime` timestamp NOT NULL,
	`rent_etime` timestamp NOT NULL,
	`renter_name` varchar(100) NOT NULL,
	`renter_phone` varchar(100) NOT NULL,
	`purpose` varchar(100) NULL,
	`rent_status` varchar(100) NOT NULL,
	`rent_loc` varchar(100) NULL,
	`rent_lat` varchar(100) NULL,
	`rent_lon` varchar(100) NULL,
	`return_loc` varchar(100) NULL,
	`return_lat` varchar(100) NULL,
	`return_lon` varchar(100) NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	PRIMARY KEY (`rent_uuid`),
    CONSTRAINT `fk_rent_car`
        FOREIGN KEY (`mdn`) REFERENCES `car`(`mdn`)
);

CREATE TABLE `location` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`drive_start_lon` varchar(100) NOT NULL,
	`drive_start_lat` varchar(100) NOT NULL,
	`drive_end_lon`	varchar(100) NULL,
	`drive_end_lat`	varchar(100) NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `drive` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`rent_uuid`	varchar(100) NOT NULL,
	`mdn` varchar(100) NOT NULL,
	`drive_loc_id` bigint NOT NULL,
	`drive_distance` varchar(100) NULL,
	`drive_on_time` timestamp NULL,
	`drive_off_time` timestamp NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	`memo` text NULL,
	PRIMARY KEY (`id`),
    CONSTRAINT `fk_drive_rent`
        FOREIGN KEY (`rent_uuid`) REFERENCES `rent`(`rent_uuid`),
    CONSTRAINT `fk_drive_car`
        FOREIGN KEY (`mdn`) REFERENCES `car`(`mdn`),
    CONSTRAINT `fk_drive_location`
        FOREIGN KEY (`drive_loc_id`) REFERENCES `location`(`id`)
);

CREATE TABLE `gpshistory` (
	`seq` bigint NOT NULL,
	`id` bigint NOT NULL,
	`o_time` varchar(100) NOT NULL,
	`gcd` varchar(10) NOT NULL,
	`lat` varchar(100) NOT NULL,
	`lon` varchar(100) NOT NULL,
	`ang` varchar(100) NOT NULL,
	`spd` varchar(100) NOT NULL,
	`sum` varchar(255) NOT NULL,
	`created_at` timestamp	NOT NULL
);

CREATE TABLE `car_event` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`mdn` varchar(100) NOT NULL,
	`type` varchar(100) NOT NULL,
	`event_at` timestamp NOT NULL,
	PRIMARY KEY (`id`),
    CONSTRAINT `fk_car_event_car`
        FOREIGN KEY (`mdn`) REFERENCES `car`(`mdn`)
);

CREATE TABLE `time_distance` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`mdn` varchar(100) NOT NULL,
	`hour` timestamp NOT NULL,
	`distance` varchar(255) NOT NULL,
	PRIMARY KEY (`id`),
    CONSTRAINT `fk_time_distance_car`
        FOREIGN KEY (`mdn`) REFERENCES `car`(`mdn`)
);