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