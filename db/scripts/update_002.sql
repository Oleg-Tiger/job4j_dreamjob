CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   city_id INT,
   visible BOOLEAN,
   created timestamp,
   photo bytea
);