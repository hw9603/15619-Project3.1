-- using mysql 5.7.11+
-- before logging into your database ...
-- mysql --username=username --password=user_password --local-infile=1
-- or if remotely to something like if you are using RDS
-- mysql --username=username -h db_hostname --port db_port --password=user_password --local-infile=1

-- Step 1 create database
drop database if exists sample_db;
create database sample_db;
use sample_db;

-- Step 2 create course table
drop table if exists `course`;
create table `course` (
	`courseId` varchar(22) not null,
	`name` varchar(140) not null,
	primary key (courseId)
);

-- Step 3 create project table
drop table if exists `project`;
create table `project` (
	`projectId` varchar(22) not null,
	`name` varchar(140) not null,
	`courseId` varchar(22) not null,
	primary key (projectId),
	foreign key (courseId) references course(courseId)
);
