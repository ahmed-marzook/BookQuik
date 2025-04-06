-- V1__Create_Initial_Schema.sql
-- Initial schema creation for Venue and Event tables
-- Create Venue table first since Event references it
CREATE TABLE
    venue (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR(255) NOT NULL,
        total_capacity BIGINT NOT NULL,
        address VARCHAR(255) NOT NULL
    );

-- Create Event table with foreign key reference to Venue
CREATE TABLE
    event (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR(255) NOT NULL,
        total_capacity BIGINT NOT NULL,
        left_capacity BIGINT NOT NULL,
        venue_id BIGINT NOT NULL,
        ticket_price DECIMAL NOT NULL,
        FOREIGN KEY (venue_id) REFERENCES venue (id)
    );

-- Add indexes for better query performance
CREATE INDEX idx_event_venue_id ON event (venue_id);

CREATE INDEX idx_venue_name ON venue (name);

CREATE INDEX idx_event_name ON event (name);