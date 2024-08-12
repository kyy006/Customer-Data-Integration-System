CREATE TABLE db.user_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255),
    street VARCHAR(255),
    suite VARCHAR(255),
    city VARCHAR(255),
    zipcode VARCHAR(255),
    lat VARCHAR(255),
    lng VARCHAR(255),
    phone VARCHAR(255),
    website VARCHAR(255),
    company_name VARCHAR(255),
    company_catch_phrase VARCHAR(255),
    company_bs VARCHAR(255)
);

CREATE TABLE db.token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    token VARCHAR(255) NOT NULL,
    expiration_timestamp DATETIME NOT NULL,
    UNIQUE (email)
);

CREATE TABLE db.user_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);
