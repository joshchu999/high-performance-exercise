CREATE KEYSPACE highPerformance
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 1
};

CREATE TABLE users (
	userID uuid,
	x int,
	y int,
	PRIMARY KEY (userID)
)
WITH caching = { 
	'keys' : 'ALL',
	'rows_per_partition' : 'NONE'
};

CREATE TABLE monsters (
	monsterID uuid,
	categoryID int,
	x int,
	y int,
	PRIMARY KEY (monsterID)
)
WITH caching = { 
	'keys' : 'ALL',
	'rows_per_partition' : 'NONE'
};

CREATE TABLE pets (
	userID uuid,
	monsterID uuid,
	categoryID int,
	PRIMARY KEY (userID, monsterID)
)
WITH caching = { 
	'keys' : 'ALL',
	'rows_per_partition' : 'NONE'
};

