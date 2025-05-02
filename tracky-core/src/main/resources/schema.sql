CREATE TABLE IF NOT EXISTS `biz` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`biz_uuid` varchar(36) NOT NULL UNIQUE,
	`biz_name` varchar(20) NOT NULL UNIQUE,
	`biz_reg_num` varchar(12) NOT NULL,
	`biz_admin`	varchar(20) NOT NULL,
	`biz_phone_num`	varchar(13)	NOT NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	`deleted_at` timestamp NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `member` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`biz_id` bigint	NOT NULL,
	`member_id`	varchar(20) NOT NULL UNIQUE,
	`pwd` varchar(100) NOT NULL,
	`email`	varchar(254) NOT NULL,
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

CREATE TABLE IF NOT EXISTS `notice` (
    id bigint NOT NULL AUTO_INCREMENT,
    member_id bigint NOT NULL,
    title varchar(100) NOT NULL,
    content text NOT NULL,
    type enum('IMPORTANT','NORMAL') NOT NULL DEFAULT 'NORMAL',
    created_at timestamp NOT NULL,
    updated_at timestamp NULL,
    deleted_at timestamp NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_notice_member
        FOREIGN KEY (member_id) REFERENCES member(id)
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
	`mdn` varchar(11) NOT NULL,
	`biz_id` bigint NOT NULL,
	`device_id`	bigint NOT NULL,
	`car_type` enum('MINI', 'SEDAN', 'VAN', 'SUV', 'TRUCK', 'BUS', 'SPORTS', 'ETC') NOT NULL,
	`car_name` varchar(20) NOT NULL,
	`car_plate` varchar(20) NOT NULL,
	`car_year` varchar(20) NOT NULL,
	`purpose` varchar(20) NOT NULL,
	`status` enum('RUNNING', 'WAITING', 'FIXING', 'DELETED', 'CLOSED') NOT NULL,
	`sum` double NOT NULL,
    `last_drive` timestamp NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	`deleted_at` timestamp NULL,
	PRIMARY KEY (`mdn`),
    CONSTRAINT `fk_car_biz`
        FOREIGN KEY (`biz_id`) REFERENCES `biz`(`id`),
    CONSTRAINT `fk_car_device`
        FOREIGN KEY (`device_id`) REFERENCES `device`(`id`)
);

CREATE TABLE IF NOT EXISTS `car_event` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`mdn` varchar(11) NOT NULL,
	`type` varchar(20) NOT NULL,
	`event_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
    CONSTRAINT `fk_car_event_car`
        FOREIGN KEY (`mdn`) REFERENCES `car`(`mdn`)
);

CREATE TABLE IF NOT EXISTS `rent` (
	`rent_uuid`	varchar(10) NOT NULL,
	`mdn`	varchar(11) NOT NULL,
	`rent_stime` timestamp NOT NULL,
	`rent_etime` timestamp NOT NULL,
	`renter_name` varchar(20) NOT NULL,
	`renter_phone` varchar(13) NOT NULL,
	`purpose` varchar(20) NULL,
	`rent_status` enum('RESERVED', 'RENTING', 'RETURNED', 'CANCELED', 'DELETED') NOT NULL,
	`rent_loc` varchar(100) NULL,
	`rent_lat` int NULL,
	`rent_lon` int NULL,
	`return_loc` varchar(100) NULL,
	`return_lat` int NULL,
	`return_lon` int NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	`deleted_at` timestamp NULL,
	PRIMARY KEY (`rent_uuid`),
    CONSTRAINT `fk_rent_car`
        FOREIGN KEY (`mdn`) REFERENCES `car`(`mdn`)
);

CREATE TABLE IF NOT EXISTS `location` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`drive_start_lon` int NOT NULL,
	`drive_start_lat` int NOT NULL,
	`drive_end_lon`	int NULL,
	`drive_end_lat`	int NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `drive` (
	`id` bigint	NOT NULL AUTO_INCREMENT,
	`rent_uuid` varchar(10) NOT NULL,
	`mdn` varchar(11) NOT NULL,
	`drive_loc_id` bigint NOT NULL,
	`drive_distance` double NULL,
	`drive_on_time` timestamp NULL,
	`drive_off_time` timestamp NULL,
	`created_at` timestamp NOT NULL,
	`updated_at` timestamp NULL,
	`memo` text NULL,
    `skip_count` int NULL,
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
	`o_time` timestamp NOT NULL,
	`gcd` varchar(10) NOT NULL,
	`lat` int NOT NULL,
	`lon` int NOT NULL,
	`ang` int NOT NULL,
	`spd` int NOT NULL,
	`sum` double NOT NULL DEFAULT 0,
	`created_at` timestamp	NOT NULL,
	PRIMARY KEY (`drive_seq`),
	CONSTRAINT `fk_gpshistory_drive`
        FOREIGN KEY (`drive_id`) REFERENCES `drive`(`id`)
);

CREATE TABLE IF NOT EXISTS `time_distance` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `mdn` varchar(11) NOT NULL,
    `biz_id` bigint NOT NULL,
    `date` timestamp NOT NULL,
    `hour` int NOT NULL,
    `distance` double NOT NULL,
    `created_at` timestamp NOT NULL,
    `updated_at` timestamp NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS daily_statistic (
    id                      BIGINT AUTO_INCREMENT NOT NULL,
    biz_id                  BIGINT        NOT NULL,
    date                    DATE          NOT NULL,
    total_car_count         INT           NOT NULL DEFAULT 0,
    daily_drive_car_count   INT           NOT NULL DEFAULT 0,
    avg_operation_rate      DOUBLE        NOT NULL DEFAULT 0,
    daily_drive_sec         BIGINT        NOT NULL DEFAULT 0,
    daily_drive_count       INT           NOT NULL DEFAULT 0,
    daily_drive_distance    DOUBLE        NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS monthly_statistic (
    id                         BIGINT AUTO_INCREMENT NOT NULL,
    biz_id                     BIGINT        NOT NULL,
    date                       DATE          NOT NULL,
    total_car_count            INT           NOT NULL DEFAULT 0,
    non_operating_car_count    INT           NOT NULL DEFAULT 0,
    avg_operation_rate         DOUBLE        NOT NULL DEFAULT 0,
    total_drive_sec            BIGINT        NOT NULL DEFAULT 0,
    total_drive_count          INT           NOT NULL DEFAULT 0,
    total_drive_distance       DOUBLE        NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);
