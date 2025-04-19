-- V1__Create_Order_Schema.sql
-- Schema creation for Order table in the new separate order service

CREATE TABLE
    `order` (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                total DECIMAL(10, 2) NOT NULL,
                quantity BIGINT NOT NULL,
                placed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                customer_id BIGINT NOT NULL,
                event_id BIGINT NOT NULL
    -- Note: Both customer_id and event_id are now foreign keys to entities in other services,
    -- so we don't create actual foreign key constraints here
);

-- Add indexes for better query performance
CREATE INDEX idx_order_customer_id ON `order` (customer_id);
CREATE INDEX idx_order_event_id ON `order` (event_id);
CREATE INDEX idx_order_placed_at ON `order` (placed_at);