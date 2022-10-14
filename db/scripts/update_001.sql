CREATE TABLE if not exists post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   city_id INT,
   visible BOOLEAN,
   created timestamp
);

CREATE TABLE if not exists candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   city_id INT,
   visible BOOLEAN,
   created timestamp,
   photo bytea
);

CREATE TABLE if not exists users (
  id SERIAL PRIMARY KEY,
  email varchar(100) unique,
  password TEXT
);

