USE `meats-db`;

create table if not exists meats (

    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    meat_id VARCHAR(50),
    animal VARCHAR(50),
    status VARCHAR(50),
    environment VARCHAR(50),
    texture VARCHAR(50),
    expiration_date VARCHAR(75),
    price DOUBLE(40,2)

    );