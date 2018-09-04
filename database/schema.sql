use logreader;

CREATE TABLE IF NOT EXISTS `logs` (
	`id` int(10) NOT NULL auto_increment,
	`ip_address` varchar(255),
	`content` text,
	`date` datetime,
	PRIMARY KEY( `id` )
) COMMENT='logs read from access log file';


CREATE TABLE IF NOT EXISTS `banned_ips` (
	`id` int(10) NOT NULL auto_increment,
	`ip_address` varchar(255),
	`date` datetime,
	`reason` text,
	PRIMARY KEY( `id` )
) COMMENT='banned ips populated by log reader tool';

