-- V1__Create_Customer_Order_Schema.sql
-- Initial schema creation for Customer and Order tables for booking service
-- Create Customer table first since Order references it
CREATE TABLE
    customer (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL UNIQUE,
        address VARCHAR(255) NOT NULL
    );

-- Create Order table with foreign key reference to Customer
CREATE TABLE
    `order` (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        total DECIMAL(10, 2) NOT NULL,
        quantity BIGINT NOT NULL,
        placed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        customer_id BIGINT NOT NULL,
        event_id BIGINT NOT NULL,
        FOREIGN KEY (customer_id) REFERENCES customer (id)
        -- Note: event_id is a foreign key to an event in another service,
        -- so we don't create an actual constraint here
    );

-- Add indexes for better query performance
CREATE INDEX idx_customer_email ON customer (email);

CREATE INDEX idx_order_customer_id ON `order` (customer_id);

CREATE INDEX idx_order_event_id ON `order` (event_id);

CREATE INDEX idx_order_placed_at ON `order` (placed_at);