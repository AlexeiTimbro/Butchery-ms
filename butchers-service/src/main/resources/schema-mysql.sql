USE `butchers-db`;

create table if not exists butchers (

    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    butcher_id VARCHAR(50),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    age INTEGER(3),
    email VARCHAR(50),
    phone_number VARCHAR(50) ,
    salary DECIMAL(19,4),
    commission_rate DECIMAL(3,1),
    street VARCHAR (50),
    city VARCHAR (50),
    province VARCHAR (50),
    country VARCHAR (50),
    postal_code VARCHAR (9)
    );