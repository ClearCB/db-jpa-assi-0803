
-- Create DATABASE

DROP DATABASE IF EXISTS epicgames;
CREATE DATABASE epicgames;

-- Use Database
USE epicgames;

-- Creating TABLES
CREATE TABLE Games (
	id INT,
	name VARCHAR(60),
	description VARCHAR(60),
	release_date DATE,
	platform VARCHAR(60) CHECK (platform in ('NES','Nintendo 64','Xbox 360','PlayStation 3','PC','Xbox One','PlayStation 4','Nintendo Switch')),
	image MEDIUMBLOB,
	PRIMARY KEY (id) 
);

CREATE TABLE Users (
	id INT,
	username VARCHAR(60) UNIQUE,
	password VARCHAR(60),
	email VARCHAR(60),
	registration_date DATE,
	PRIMARY KEY (id) 
);

CREATE TABLE Scores (
	id INT ,
	user_id INT,
	game_id INT,
	score INT CHECK (score >= 0 AND score <= 100),
	date DATE,
	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (game_id) REFERENCES Games(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Purchases (
	id INT,
	user_id INT,
	game_id INT,
	purchase_date DATE,
	price DECIMAL(10, 2),
	PRIMARY KEY (id), 
	FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE SET NULL ON UPDATE SET NULL,
	FOREIGN KEY (game_id) REFERENCES Games(id) ON DELETE SET NULL ON UPDATE SET NULL
);

CREATE TABLE Studios (
	id INT ,
	name VARCHAR(60),
	description VARCHAR(200),
	foundation_date DATE,
	location VARCHAR(60),
	PRIMARY KEY (id) 
);


CREATE TABLE Developers (
	id INT,
	name VARCHAR(60),
	surname VARCHAR(60),
	studio_id INT,
	age INT,
	PRIMARY KEY (id) ,
	FOREIGN KEY (studio_id) REFERENCES Studios(id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE GamesDevelopers (
	game_id INT,
	developer_id INT,
	PRIMARY KEY (game_id, developer_id),
	FOREIGN KEY (game_id) REFERENCES Games(id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (developer_id) REFERENCES Developers(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE GamesStudios (
	game_id INT,
	studio_id INT,
	PRIMARY KEY (game_id, studio_id),
	FOREIGN KEY (game_id) REFERENCES Games(id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (studio_id) REFERENCES Studios(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Sales (
	id INT ,
	game_id INT,
	sale_date DATE,
	sale_hour Time,
	revenue DECIMAL(10, 2),
	PRIMARY KEY (id),
	FOREIGN KEY (game_id) REFERENCES Games(id) ON DELETE SET NULL ON UPDATE SET NULL
);

-- INDEX

CREATE INDEX scoreUser__index on Scores(score,user_id);
CREATE INDEX userGameDate__index on Purchases(user_id,game_id,purchase_date);

-- ROLES

DROP ROLE IF EXISTS 'writer' ;
CREATE ROLE 'writer';
GRANT INSERT ON epicgames.* TO 'writer';


DROP ROLE IF EXISTS 'reader' ;
CREATE ROLE 'reader';
GRANT SELECT ON epicgames.* TO 'reader';


-- CREATE USERS 

DROP USER IF EXISTS 'admin_user'@'%' ;
CREATE USER 'admin_user'@'%' IDENTIFIED BY 'mystrongpass1';

DROP USER IF EXISTS 'writer_user'@'%' ;
CREATE USER 'writer_user'@'%' IDENTIFIED BY 'mystrongpass2';

DROP USER IF EXISTS 'reader_user'@'%' ;
CREATE USER 'reader_user'@'%' IDENTIFIED BY 'mystrongpass3';


-- ASIGN ROLE TO USER

GRANT ALL PRIVILEGES ON *.* TO 'admin_user'@'%' WITH GRANT OPTION;

REVOKE ALL PRIVILEGES ON *.* FROM 'writer_user'@'%';
GRANT INSERT ON epicgames.* TO 'writer_user'@'%';

REVOKE ALL PRIVILEGES ON *.* FROM 'reader_user'@'%';
GRANT SELECT ON epicgames.*  TO 'reader_user'@'%';

-- TO GRANT AN EXTERNAL USER AND ALL PRIVILEGES

-- CREATE USER [USERNAME]@[IPDIRECTION] IDENTIFIED BY '[PASSWORD]'; --> WITH '%' ALL IPS INCLUDED
