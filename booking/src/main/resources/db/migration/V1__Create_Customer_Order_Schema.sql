-- V1__Create_Customer_Schema.sql
-- Updated schema creation for Customer table only (Order moved to separate service)

CREATE TABLE
    customer (
                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                 name VARCHAR(255) NOT NULL,
                 email VARCHAR(255) NOT NULL UNIQUE,
                 address VARCHAR(255) NOT NULL
);

-- Add index for better query performance
CREATE INDEX idx_customer_email ON customer (email);