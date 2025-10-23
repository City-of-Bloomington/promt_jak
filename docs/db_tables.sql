
;;
;; These tables are needed for the app to work
;; tested on mysql database
;; 
;; 1- agenda_info
;;
CREATE TABLE `agenda_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `infoDate` date NOT NULL,
  `pid` int(11) NOT NULL,
  `sid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=185230 DEFAULT CHARSET=utf8;
;;
;; 2- areas
;;
CREATE TABLE `areas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(70) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
;;
;; 3- budget_direct_expenses
;;
CREATE TABLE `budget_direct_expenses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `budget_id` int(11) DEFAULT NULL,
  `description` varchar(30) DEFAULT NULL,
  `expenses` decimal(8,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `budget_id` (`budget_id`),
  CONSTRAINT `budget_direct_expenses_ibfk_1` FOREIGN KEY (`budget_id`) REFERENCES `eval_budgets` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=957 DEFAULT CHARSET=utf8;
;;
;; 4- categories
;; 
CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(70) DEFAULT NULL,
  `active` char(1) DEFAULT 'y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
;;
;; 5- contacts
;;
CREATE TABLE `contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(70) DEFAULT NULL,
  `address` varchar(250) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone_c` varchar(30) DEFAULT NULL,
  `phone_h` varchar(30) DEFAULT NULL,
  `phone_w` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=871 DEFAULT CHARSET=utf8;
;;
;; 6- eval_budgets 
;;
CREATE TABLE `eval_budgets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_fees` decimal(8,2) DEFAULT NULL,
  `admin_percent` enum('5','10','15') DEFAULT NULL,
  `sponsorship` double DEFAULT NULL,
  `sponsor_revenue` char(1) DEFAULT NULL,
  `donation` double DEFAULT NULL,
  `wby` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `eval_budgets_ibfk_1` FOREIGN KEY (`id`) REFERENCES `programs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11680 DEFAULT CHARSET=utf8;
;;
;; 7- eval_outcomes
;;
CREATE TABLE `eval_outcomes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eval_id` int(11) DEFAULT NULL,
  `obj_id` int(11) DEFAULT NULL,
  `outcome` text,
  PRIMARY KEY (`id`),
  KEY `obj_id` (`obj_id`),
  KEY `eval_id` (`eval_id`),
  CONSTRAINT `eval_outcomes_ibfk_1` FOREIGN KEY (`obj_id`) REFERENCES `plan_objectives` (`id`),
  CONSTRAINT `eval_outcomes_ibfk_2` FOREIGN KEY (`eval_id`) REFERENCES `evaluations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=719 DEFAULT CHARSET=utf8;
;;
;; 8- eval_staffs
;;
CREATE TABLE `eval_staffs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eval_id` int(11) DEFAULT NULL,
  `staff_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `staff_id` (`staff_id`),
  KEY `eval_id` (`eval_id`),
  CONSTRAINT `eval_staffs_ibfk_2` FOREIGN KEY (`staff_id`) REFERENCES `plan_staffs` (`id`),
  CONSTRAINT `eval_staffs_ibfk_3` FOREIGN KEY (`eval_id`) REFERENCES `evaluations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=604 DEFAULT CHARSET=utf8;
;;
;; 9- evaluation
;;
CREATE TABLE `evaluations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `attendance` int(11) DEFAULT NULL,
  `assignment` varchar(1000) DEFAULT NULL,
  `recommend` text,
  `partnership` varchar(500) DEFAULT NULL,
  `sponsorship` varchar(1000) DEFAULT NULL,
  `flier_points` varchar(500) DEFAULT NULL,
  `life_cycle` enum('Intro','Growth','Maturity','Saturation','Decline') DEFAULT NULL,
  `other` varchar(1500) DEFAULT NULL,
  `wby` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `life_cycle_info` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `evaluations_ibfk_1` FOREIGN KEY (`id`) REFERENCES `programs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11680 DEFAULT CHARSET=utf8;
;;
;; 10- facilities
;;
CREATE TABLE `facilities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) DEFAULT NULL,
  `statement` text,
  `schedule` varchar(4000) DEFAULT NULL,
  `closings` varchar(4000) DEFAULT NULL,
  `other` text,
  `lead_id` int(11) DEFAULT NULL,
  `type` enum('Facility','Park','Trail','Other') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lead_id` (`lead_id`),
  CONSTRAINT `facilities_ibfk_1` FOREIGN KEY (`lead_id`) REFERENCES `leads` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 ;
;;
;; 11- fee_items
;;
CREATE TABLE `fee_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `budget_id` int(11) DEFAULT NULL,
  `fee_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `rate` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fee_id` (`fee_id`),
  KEY `budget_id` (`budget_id`),
  CONSTRAINT `fee_items_ibfk_2` FOREIGN KEY (`fee_id`) REFERENCES `fee_types` (`id`),
  CONSTRAINT `fee_items_ibfk_3` FOREIGN KEY (`budget_id`) REFERENCES `eval_budgets` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=499 DEFAULT CHARSET=utf8;
;;
;; 12- fee_types
;;
CREATE TABLE `fee_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 ;
;;
;; 13- general_listings
;;
CREATE TABLE `general_listings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `season` enum('Fall/Winter','Winter/Spring','Summer','Ongoing') DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `lead_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `ltime` varchar(20) DEFAULT NULL,
  `days` varchar(30) DEFAULT NULL,
  `cost` double(8,2) DEFAULT NULL,
  `description` text,
  `codeNeed` char(1) DEFAULT NULL,
  `code` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lead_id` (`lead_id`),
  CONSTRAINT `general_listings_ibfk_1` FOREIGN KEY (`lead_id`) REFERENCES `leads` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=192 DEFAULT CHARSET=utf8;
;;
;; 14- history
;;
CREATE TABLE `history` (
  `id` int(11) DEFAULT NULL,
  `action` enum('Created','Updated','Saved') DEFAULT NULL,
  `type` enum('Program','Facility','General','Market','Plan','Volunteer','Sponsor') DEFAULT NULL,
  `action_by` int(11) DEFAULT NULL,
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
;;
;; 15- leads
;;
CREATE TABLE `leads` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(70) DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8 ;
;;
;; 16- locations
;;
 CREATE TABLE `locations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(70) DEFAULT NULL,
  `location_url` varchar(160) DEFAULT NULL,
  `facility_id` int(11) DEFAULT NULL,
  `active` char(1) DEFAULT 'y',
  PRIMARY KEY (`id`),
  KEY `facility_id` (`facility_id`),
  CONSTRAINT `locations_ibfk_1` FOREIGN KEY (`facility_id`) REFERENCES `facilities` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8 ;
;;
;; 17- market_ad_details
;;
CREATE TABLE `market_ad_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `market_id` int(11) DEFAULT NULL,
  `type_id` int(11) NOT NULL,
  `direct` char(1) DEFAULT NULL,
  `expenses` decimal(8,2) DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `details` text,
  PRIMARY KEY (`id`),
  KEY `type_id` (`type_id`),
  KEY `market_id` (`market_id`),
  CONSTRAINT `market_ad_details_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `market_ads` (`id`),
  CONSTRAINT `market_ad_details_ibfk_2` FOREIGN KEY (`market_id`) REFERENCES `marketing` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=906 DEFAULT CHARSET=utf8 ;
;;
;; 18- market_ads
;;
CREATE TABLE `market_ads` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ;
;;
;;19- market_announce_details
;;
CREATE TABLE `market_announce_details` (
  `market_id` int(11) NOT NULL DEFAULT '0',
  `an_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`market_id`,`an_id`),
  KEY `an_id` (`an_id`),
  CONSTRAINT `market_announce_details_ibfk_2` FOREIGN KEY (`an_id`) REFERENCES `market_announces` (`id`),
  CONSTRAINT `market_announce_details_ibfk_3` FOREIGN KEY (`market_id`) REFERENCES `marketing` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
;;
;; 20- market_announces
;;
 CREATE TABLE `market_announces` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `active` char(1) DEFAULT 'y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ;
;;
;; 21- market_type_details
;;
CREATE TABLE `market_type_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `market_id` int(11) DEFAULT NULL,
  `type_id` int(11) NOT NULL,
  `direct` char(1) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `expenses` decimal(8,2) DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `details` text,
  PRIMARY KEY (`id`),
  KEY `type_id` (`type_id`),
  KEY `market_id` (`market_id`),
  CONSTRAINT `market_type_details_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `marketing_types` (`id`),
  CONSTRAINT `market_type_details_ibfk_2` FOREIGN KEY (`market_id`) REFERENCES `marketing` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4826 DEFAULT CHARSET=utf8;
;;
;; 22- marketing
;;
CREATE TABLE `marketing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `other_ad` varchar(70) DEFAULT NULL,
  `class_list` varchar(500) DEFAULT NULL,
  `other_market` varchar(1000) DEFAULT NULL,
  `spInstructions` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11198 DEFAULT CHARSET=utf8 ;
;;
;; 23- marketing_facilities
;;
CREATE TABLE `marketing_facilities` (
  `market_id` int(11) NOT NULL DEFAULT '0',
  `facility_id` int(11) NOT NULL DEFAULT '0',
  `year` int(11) DEFAULT NULL,
  `season` enum('Fall/Winter','Winter/Spring','Summer','Ongoing') DEFAULT NULL,
  PRIMARY KEY (`market_id`,`facility_id`),
  KEY `facility_id` (`facility_id`),
  CONSTRAINT `marketing_facilities_ibfk_1` FOREIGN KEY (`market_id`) REFERENCES `marketing` (`id`),
  CONSTRAINT `marketing_facilities_ibfk_2` FOREIGN KEY (`facility_id`) REFERENCES `facilities` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
;;
;; 24- marketing_generals
;;
CREATE TABLE `marketing_generals` (
  `market_id` int(11) NOT NULL DEFAULT '0',
  `general_id` int(11) NOT NULL DEFAULT '0',
  `year` int(11) DEFAULT NULL,
  `season` enum('Fall/Winter','Winter/Spring','Summer','Ongoing') DEFAULT NULL,
  PRIMARY KEY (`market_id`,`general_id`),
  KEY `general_id` (`general_id`),
  CONSTRAINT `marketing_generals_ibfk_1` FOREIGN KEY (`market_id`) REFERENCES `marketing` (`id`),
  CONSTRAINT `marketing_generals_ibfk_2` FOREIGN KEY (`general_id`) REFERENCES `general_listings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

;;
;; 25- marketing programs
;;
CREATE TABLE `marketing_programs` (
  `market_id` int(11) NOT NULL DEFAULT '0',
  `prog_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`market_id`,`prog_id`),
  KEY `prog_id` (`prog_id`),
  CONSTRAINT `marketing_programs_ibfk_1` FOREIGN KEY (`market_id`) REFERENCES `marketing` (`id`),
  CONSTRAINT `marketing_programs_ibfk_2` FOREIGN KEY (`prog_id`) REFERENCES `programs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
;;
;; 26- marketing_types
;;
create table  marketing_types (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `active` char(1) DEFAULT 'y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 ;
;;
;; 27- plan_contacts
;;
CREATE TABLE `plan_contacts` (
  `plan_id` int(11) DEFAULT NULL,
  `con_id` int(11) DEFAULT NULL,
  KEY `plan_id` (`plan_id`),
  KEY `con_id` (`con_id`),
  CONSTRAINT `plan_contacts_ibfk_2` FOREIGN KEY (`con_id`) REFERENCES `contacts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
;;
;; 28-plan_objectives
;;
create table  `plan_objectives` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plan_id` int(11) DEFAULT NULL,
  `objective` text,
  PRIMARY KEY (`id`),
  KEY `plan_id` (`plan_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8129 DEFAULT CHARSET=utf8 ;
;;
;; 29- plan_staffs
;;
CREATE TABLE `plan_staffs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plan_id` int(11) DEFAULT NULL,
  `staff_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `plan_id` (`plan_id`),
  KEY `staff_id` (`staff_id`),
  CONSTRAINT `plan_staffs_ibfk_2` FOREIGN KEY (`staff_id`) REFERENCES `staff_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4291 DEFAULT CHARSET=utf8 ;
;;
;; 30- plans
;;
create table `plans` (
  `id` int(11) NOT NULL,
  `program` varchar(100) DEFAULT NULL,
  `ideas` varchar(2000) DEFAULT NULL,
  `profit_obj` varchar(200) DEFAULT NULL,
  `partner` varchar(80) DEFAULT NULL,
  `market` varchar(80) DEFAULT NULL,
  `frequency` varchar(60) DEFAULT NULL,
  `min_max` varchar(30) DEFAULT NULL,
  `event_time` varchar(30) DEFAULT NULL,
  `est_time` varchar(30) DEFAULT NULL,
  `year_season` varchar(30) DEFAULT NULL,
  `history` text,
  `supply` text,
  `timeline` text,
  `lead_id` int(11) DEFAULT NULL,
  `sponsor` varchar(70) DEFAULT NULL,
  `p_duration` varchar(20) DEFAULT NULL,
  `goals` varchar(1000) DEFAULT NULL,
  `attendcount` varchar(30) DEFAULT NULL,
  `life_cycle` enum('Intro','Growth','Maturity','Saturation','Decline') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lead_id` (`lead_id`),
  CONSTRAINT `plans_ibfk_1` FOREIGN KEY (`lead_id`) REFERENCES `leads` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
;;
;; 31- pre_plans
;;
CREATE TABLE `pre_plans` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prev_id` int(11) DEFAULT NULL,
  `determinants` varchar(20) DEFAULT NULL,
  `pre_eval_text` text,
  `market_considers` varchar(20) DEFAULT NULL,
  `fulfilled` char(1) DEFAULT NULL,
  `season` enum('Fall/Winter','Winter/Spring','Summer','Ongoing') DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `explained` text,
  `name` varchar(100) DEFAULT NULL,
  `lead_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4562 DEFAULT CHARSET=utf8;

;;
;; 32- program_bases
;;
 CREATE TABLE `program_bases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(70) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
;;
;; 33- programs
;;
CREATE TABLE `programs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lead_id` int(11) DEFAULT NULL,
  `statement` text,
  `title` varchar(100) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `season` enum('Fall/Winter','Winter/Spring','Summer','Ongoing') DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `nraccount` varchar(15) DEFAULT NULL,
  `fee` varchar(15) DEFAULT NULL,
  `oginfo` varchar(4000) DEFAULT NULL,
  `inCityFee` varchar(20) DEFAULT NULL,
  `nonCityFee` varchar(20) DEFAULT NULL,
  `otherFee` varchar(30) DEFAULT NULL,
  `allAge` char(1) DEFAULT NULL,
  `partGrade` varchar(15) DEFAULT NULL,
  `minMaxEnroll` varchar(20) DEFAULT NULL,
  `regDeadLine` date DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL,
  `instructor` varchar(120) DEFAULT NULL,
  `description` varchar(160) DEFAULT NULL,
  `days` varchar(35) DEFAULT NULL,
  `startTime` varchar(30) DEFAULT NULL,
  `endTime` varchar(30) DEFAULT NULL,
  `classCount` varchar(10) DEFAULT NULL,
  `code` varchar(10) DEFAULT NULL,
  `area_id` int(11) DEFAULT NULL,
  `codeNeed` char(1) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `codeTask` char(1) DEFAULT NULL,
  `marketTask` char(1) DEFAULT NULL,
  `volTask` char(1) DEFAULT NULL,
  `sponTask` char(1) DEFAULT NULL,
  `ageFrom` int(11) DEFAULT NULL,
  `ageTo` int(11) DEFAULT NULL,
  `budgetTask` char(1) DEFAULT NULL,
  `evalTask` char(1) DEFAULT NULL,
  `wParent` char(1) DEFAULT NULL,
  `waitList` int(11) DEFAULT NULL,
  `plan_id` int(11) DEFAULT NULL,
  `oplocation` varchar(80) DEFAULT NULL,
  `category2_id` int(11) DEFAULT NULL,
  `version` char(4) DEFAULT NULL,
  `otherAge` varchar(30) DEFAULT NULL,
  `subcat` varchar(50) DEFAULT NULL,
  `received` date DEFAULT NULL,
  `memberFee` varchar(20) DEFAULT NULL,
  `nonMemberFee` varchar(20) DEFAULT NULL,
  `season2` enum('Fall/Winter','Winter/Spring','Summer','Ongoing') DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `taxonomy_ids` set('1','2','3','4','5','101','102','103','104','105','106','107','108','109','110','111','112','201','202','203','204','205','206','207','301','302','303','304','305','306','307','401','402','403','404','405','406','407','408','113') DEFAULT NULL,
  `location_details` varchar(150) DEFAULT NULL,
  `noPublish` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lead_id` (`lead_id`),
  KEY `category_id` (`category_id`),
  KEY `area_id` (`area_id`),
  KEY `location_id` (`location_id`),
  KEY `plan_id` (`plan_id`),
  KEY `received` (`received`),
  KEY `title` (`title`),
  KEY `season` (`season`,`year`),
  KEY `season_2` (`season`,`year`,`lead_id`),
  CONSTRAINT `programs_ibfk_1` FOREIGN KEY (`lead_id`) REFERENCES `leads` (`id`),
  CONSTRAINT `programs_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `programs_ibfk_3` FOREIGN KEY (`area_id`) REFERENCES `areas` (`id`),
  CONSTRAINT `programs_ibfk_4` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`),
  CONSTRAINT `programs_ibfk_5` FOREIGN KEY (`plan_id`) REFERENCES `plans` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11708 DEFAULT CHARSET=utf8;
;;
;; 34- programs_access
;; 
CREATE TABLE `programs_access` (
  `id` int(11) NOT NULL,
  `userid` varchar(70) DEFAULT NULL,
  `type` enum('owner','editor') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
;;
;; 35- programs_inclusion
;;
CREATE TABLE `programs_inclusion` (
  `id` int(11) NOT NULL,
  `consult_ada` char(1) DEFAULT NULL,
  `training` char(1) DEFAULT NULL,
  `train_aware` char(1) DEFAULT NULL,
  `train_basics` char(1) DEFAULT NULL,
  `train_consider` char(1) DEFAULT NULL,
  `train_behave` char(1) DEFAULT NULL,
  `train_trip` char(1) DEFAULT NULL,
  `train_other` varchar(500) DEFAULT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `market` char(1) DEFAULT NULL,
  `site_visit` char(1) DEFAULT NULL,
  `consult` char(1) DEFAULT NULL,
  `sign` char(1) DEFAULT NULL,
  `prov_sign` char(1) DEFAULT NULL,
  `consult_pro` char(1) DEFAULT NULL,
  UNIQUE KEY `id` (`id`),
  CONSTRAINT `programs_inclusion_ibfk_1` FOREIGN KEY (`id`) REFERENCES `programs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
;;
;; 36- promt_file_seq
;;
CREATE TABLE `promt_file_seq` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
;;
;; 37- promt_files
;;
promt_files | CREATE TABLE `promt_files` (
  `id` int(10) unsigned NOT NULL,
  `related_id` int(10) unsigned NOT NULL,
  `type` enum('Program','Plan','Marketing','Facility','Other') DEFAULT NULL,
  `added_by_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `name` varchar(70) DEFAULT NULL,
  `old_name` varchar(150) DEFAULT NULL,
  `notes` varchar(1024) DEFAULT NULL,
  UNIQUE KEY `id` (`id`),
  KEY `added_by_id` (`added_by_id`),
  CONSTRAINT `promt_files_ibfk_1` FOREIGN KEY (`added_by_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
;;
;; 38- session_opts
;;
CREATE TABLE `session_opts` (
  `sid` int(11) DEFAULT NULL,
  `code_c` char(1) DEFAULT NULL,
  `id` int(11) DEFAULT NULL,
  `days_c` char(1) DEFAULT NULL,
  `startTime_c` char(1) DEFAULT NULL,
  `endTime_c` char(1) DEFAULT NULL,
  `regDeadLine_c` char(1) DEFAULT NULL,
  `inCityFee_c` char(1) DEFAULT NULL,
  `nonCityFee_c` char(1) DEFAULT NULL,
  `otherFee_c` char(1) DEFAULT NULL,
  `location_c` char(1) DEFAULT NULL,
  `allAge_c` char(1) DEFAULT NULL,
  `partGrade_c` char(1) DEFAULT NULL,
  `minMaxEnroll_c` char(1) DEFAULT NULL,
  `classCount_c` char(1) DEFAULT NULL,
  `description_c` char(1) DEFAULT NULL,
  `instructor_c` char(1) DEFAULT NULL,
  `ageFrom_c` char(1) DEFAULT NULL,
  `ageTo_c` char(1) DEFAULT NULL,
  `startDate_c` char(1) DEFAULT NULL,
  `endDate_c` char(1) DEFAULT NULL,
  `wParent_c` char(1) DEFAULT NULL,
  `otherAge_c` char(1) DEFAULT NULL,
  `memberFee_c` char(1) DEFAULT NULL,
  `nonMemberFee_c` char(1) DEFAULT NULL,
  `sessionSort` varchar(30) DEFAULT NULL,
  KEY `id` (`id`),
  CONSTRAINT `session_opts_ibfk_1` FOREIGN KEY (`id`) REFERENCES `programs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
;;
;; 39- session_reorders
;;
CREATE TABLE `session_reorders` (
  `order_id` int(11) NOT NULL,
  `prog_id` int(11) DEFAULT NULL,
  `old_sid` int(11) DEFAULT NULL,
  UNIQUE KEY `order_id` (`order_id`,`prog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
;;
;; 40- sessions
;;
CREATE TABLE `sessions` (
  `sid` int(11) NOT NULL DEFAULT '0',
  `code` varchar(10) DEFAULT NULL,
  `id` int(11) NOT NULL DEFAULT '0',
  `days` varchar(35) DEFAULT NULL,
  `startTime` varchar(30) DEFAULT NULL,
  `endTime` varchar(30) DEFAULT NULL,
  `regDeadLine` date DEFAULT NULL,
  `inCityFee` varchar(20) DEFAULT NULL,
  `nonCityFee` varchar(20) DEFAULT NULL,
  `otherFee` varchar(20) DEFAULT NULL,
  `location` varchar(120) DEFAULT NULL,
  `allAge` char(1) DEFAULT NULL,
  `partGrade` varchar(15) DEFAULT NULL,
  `minMaxEnroll` varchar(10) DEFAULT NULL,
  `classCount` varchar(10) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `instructor` varchar(120) DEFAULT NULL,
  `ageFrom` int(11) DEFAULT NULL,
  `ageTo` int(11) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `wParent` char(1) DEFAULT NULL,
  `otherAge` varchar(30) DEFAULT NULL,
  `memberFee` varchar(20) DEFAULT NULL,
  `nonMemberFee` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`sid`),
  KEY `regDeadLine` (`regDeadLine`),
  KEY `startDate` (`startDate`,`endDate`),
  CONSTRAINT `sessions_ibfk_1` FOREIGN KEY (`id`) REFERENCES `programs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
;;
;; 41- sponsors
;;
CREATE TABLE `sponsors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `attendCount` varchar(20) DEFAULT NULL,
  `market` varchar(80) DEFAULT NULL,
  `monetary` char(1) DEFAULT NULL,
  `tangible` char(1) DEFAULT NULL,
  `services` char(1) DEFAULT NULL,
  `signage` char(1) DEFAULT NULL,
  `exhibitSpace` char(1) DEFAULT NULL,
  `tshirt` char(1) DEFAULT NULL,
  `comments` text,
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  CONSTRAINT `sponsors_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `programs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=336 DEFAULT CHARSET=utf8;
;;
;; 42-  staff_expenses
;;
CREATE TABLE `staff_expenses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `budget_id` int(11) DEFAULT NULL,
  `staff_id` int(11) DEFAULT NULL,
  `hours` decimal(8,2) DEFAULT NULL,
  `rate` decimal(8,2) DEFAULT NULL,
  `type` enum('Direct','Indirect') DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `staff_id` (`staff_id`),
  KEY `budget_id` (`budget_id`),
  CONSTRAINT `staff_expenses_ibfk_2` FOREIGN KEY (`staff_id`) REFERENCES `staff_types` (`id`),
  CONSTRAINT `staff_expenses_ibfk_3` FOREIGN KEY (`budget_id`) REFERENCES `eval_budgets` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1062 DEFAULT CHARSET=utf8 ;
;;
;; 43- staff_types
;;
CREATE TABLE `staff_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
;;
;; 44- supply_items
;;
 CREATE TABLE `supply_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `budget_id` int(11) DEFAULT NULL,
  `supply_id` int(11) DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `other` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `supply_id` (`supply_id`),
  KEY `budget_id` (`budget_id`),
  CONSTRAINT `supply_items_ibfk_2` FOREIGN KEY (`supply_id`) REFERENCES `supply_types` (`id`),
  CONSTRAINT `supply_items_ibfk_3` FOREIGN KEY (`budget_id`) REFERENCES `eval_budgets` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
;;
;; 45- taxonomy_terms
;;
 CREATE TABLE `taxanomy_terms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
;;
;; 46-taxonomy_subs
;;
CREATE TABLE `taxonomy_subs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tax_id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tax_id` (`tax_id`),
  CONSTRAINT `taxonomy_subs_ibfk_1` FOREIGN KEY (`tax_id`) REFERENCES `taxonomies` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=409 DEFAULT CHARSET=utf8;
;;
;; 47- users
;;
 CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(50) DEFAULT NULL,
  `role` enum('Edit','Edit:Delete','Admin:Edit:Delete') DEFAULT NULL,
  `full_name` varchar(50) DEFAULT NULL,
  `active` char(1) DEFAULT 'y',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userid` (`userid`),
  UNIQUE KEY `userid_2` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8 ;
;;
;; 48- vol_shifts
;;
CREATE TABLE `vol_shifts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `volCount` varchar(3) DEFAULT NULL,
  `days` varchar(20) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `startTime` varchar(20) DEFAULT NULL,
  `endTime` varchar(20) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `notes` text,
  `duties` varchar(500) DEFAULT NULL,
  `onsite` char(1) DEFAULT NULL,
  `pre_train` char(1) DEFAULT NULL,
  `lead_id` int(11) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  KEY `lead_id` (`lead_id`),
  CONSTRAINT `vol_shifts_ibfk_2` FOREIGN KEY (`lead_id`) REFERENCES `leads` (`id`),
  CONSTRAINT `vol_shifts_ibfk_3` FOREIGN KEY (`pid`) REFERENCES `programs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1847 DEFAULT CHARSET=utf8 ;
;;
;; 49- vol_trainings
;;
CREATE TABLE `vol_trainings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `shift_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `startTime` varchar(20) DEFAULT NULL,
  `endTime` varchar(20) DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL,
  `other` varchar(50) DEFAULT NULL,
  `tdays` varchar(20) DEFAULT NULL,
  `notes` text,
  PRIMARY KEY (`id`),
  KEY `pid` (`pid`),
  CONSTRAINT `vol_trainings_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `programs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8;
;;
;; 50-  web_publish_programs
;;
CREATE TABLE `web_publish_programs` (
  `publish_id` int(10) unsigned NOT NULL DEFAULT '0',
  `prog_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`publish_id`,`prog_id`),
  KEY `prog_id` (`prog_id`),
  CONSTRAINT `web_publish_programs_ibfk_1` FOREIGN KEY (`publish_id`) REFERENCES `web_publishes` (`id`),
  CONSTRAINT `web_publish_programs_ibfk_2` FOREIGN KEY (`prog_id`) REFERENCES `programs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
;;
;; 51- web_publishes
;;
CREATE TABLE `web_publishes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `web_publishes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8 ;
;;
;;
;;

