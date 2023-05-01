USE `customers-db`;

create table if not exists customers (

    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    customer_id VARCHAR(50),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(50),
    phone_number VARCHAR(50) UNIQUE ,
    street VARCHAR(50),
    city VARCHAR(50),
    province VARCHAR(50),
    country VARCHAR(50),
    postal_code VARCHAR(9)

);