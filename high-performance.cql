CREATE KEYSPACE highPerformance
WITH replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 1
};

CREATE TABLE profiles (
	userID uuid,
	profileID uuid,
	name text,
	age int,
	PRIMARY KEY (userID, profileID)
)
WITH caching = { 
	'keys' : 'ALL',
	'rows_per_partition' : 'NONE'
};
