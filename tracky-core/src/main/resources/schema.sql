CREATE TABLE IF NOT EXISTS `biz` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`biz_uuid` varchar(100) NOT NULL,
	`biz_name` varchar(100) NOT NULL,
	`biz_reg_num` varchar(20) NOT NULL,
	`biz_admin`	varchar(100) NOT NULL,
	`biz_phone_num`	varchar(20)	NOT NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	`deleted_at` timestamp NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `member` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`biz_id` bigint	NOT NULL,
	`member_id`	varchar(100) NOT NULL,
	`pwd` varchar(100) NOT NULL,
	`email`	varchar(100) NOT NULL,
	`role`	enum('ADMIN', 'USER') NOT NULL,
	`status` enum('ACTIVE', 'DEACTIVE', 'WAIT', 'DELETED') NOT NULL,
	`lastlogin_at` timestamp NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	`deleted_at` timestamp NULL,
	PRIMARY KEY (`id`),
    CONSTRAINT `fk_member_biz`
        FOREIGN KEY (`biz_id`) REFERENCES `biz`(`id`)
);

CREATE TABLE IF NOT EXISTS `device` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`tid` varchar(10) NOT NULL,
	`mid` varchar(10) NOT NULL,
	`did` varchar(10) NOT NULL,
	`pv` varchar(10) NOT NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `car` (
	`mdn` varchar(100) NOT NULL,
	`biz_id` bigint NOT NULL,
	`device_id`	bigint NOT NULL,
	`car_type` enum('MINI', 'SEDAN', 'VAN', 'SUV', 'TRUCK', 'BUS', 'SPORTS', 'ETC') NOT NULL,
	`car_name` varchar(100) NOT NULL,
	`car_plate` varchar(100) NOT NULL,
	`car_year` varchar(100) NOT NULL,
	`purpose` varchar(100) NOT NULL,
	`status` enum('RUNNING', 'WAITING', 'FIXING', 'DELETED', 'CLOSED') NOT NULL,
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

CREATE TABLE IF NOT EXISTS `rent` (
	`rent_uuid`	varchar(100) NOT NULL,
	`mdn`	varchar(100) NOT NULL,
	`rent_stime` timestamp NOT NULL,
	`rent_etime` timestamp NOT NULL,
	`renter_name` varchar(100) NOT NULL,
	`renter_phone` varchar(100) NOT NULL,
	`purpose` varchar(100) NULL,
	`rent_status` enum('RENTING', 'RESERVED', 'RETURNED', 'CANCELED', 'DELETED') NOT NULL,
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

CREATE TABLE IF NOT EXISTS `location` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`drive_start_lon` varchar(100) NOT NULL,
	`drive_start_lat` varchar(100) NOT NULL,
	`drive_end_lon`	varchar(100) NULL,
	`drive_end_lat`	varchar(100) NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `drive` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`rent_uuid` varchar(100) NOT NULL,
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

CREATE TABLE IF NOT EXISTS `gpshistory` (
	`drive_seq` BINARY(16) NOT NULL,
	`drive_id` bigint NOT NULL,
	`o_time` varchar(100) NOT NULL,
	`sec` int NOT NULL,
	`gcd` varchar(10) NOT NULL,
	`lat` varchar(100) NOT NULL,
	`lon` varchar(100) NOT NULL,
	`ang` varchar(100) NOT NULL,
	`spd` varchar(100) NOT NULL,
	`sum` varchar(100) NOT NULL,
	`created_at` timestamp	NOT NULL,
	PRIMARY KEY (`drive_seq`),
	CONSTRAINT `fk_gpshistory_drive`
        FOREIGN KEY (`drive_id`) REFERENCES `drive`(`id`)
);

CREATE TABLE IF NOT EXISTS `car_event` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`mdn` varchar(100) NOT NULL,
	`type` varchar(100) NOT NULL,
	`event_at` timestamp NOT NULL,
	PRIMARY KEY (`id`),
    CONSTRAINT `fk_car_event_car`
        FOREIGN KEY (`mdn`) REFERENCES `car`(`mdn`)
);

CREATE TABLE IF NOT EXISTS `time_distance` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`mdn` varchar(100) NOT NULL,
	`hour` timestamp NOT NULL,
	`distance` varchar(100) NOT NULL,
	PRIMARY KEY (`id`),
    CONSTRAINT `fk_time_distance_car`
        FOREIGN KEY (`mdn`) REFERENCES `car`(`mdn`)
);
