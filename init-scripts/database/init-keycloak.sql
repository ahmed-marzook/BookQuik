-- Create keycloak database
CREATE DATABASE IF NOT EXISTS keycloak;

-- Grant all privileges to the root user for keycloak database
GRANT ALL PRIVILEGES ON keycloak.* TO 'root'@'%';

-- Optionally, grant privileges to the bookquik_user if needed
GRANT ALL PRIVILEGES ON keycloak.* TO 'bookquik_user'@'%';

-- Create service-specific databases
CREATE DATABASE IF NOT EXISTS booking_service;
CREATE DATABASE IF NOT EXISTS inventory_service;
CREATE DATABASE IF NOT EXISTS user_service;
CREATE DATABASE IF NOT EXISTS order_service;
CREATE DATABASE IF NOT EXISTS payment_service;
CREATE DATABASE IF NOT EXISTS notification_service;

-- Grant all privileges to bookquik_user for all service databases
GRANT ALL PRIVILEGES ON booking_service.* TO 'bookquik_user'@'%';
GRANT ALL PRIVILEGES ON inventory_service.* TO 'bookquik_user'@'%';
GRANT ALL PRIVILEGES ON user_service.* TO 'bookquik_user'@'%';
GRANT ALL PRIVILEGES ON order_service.* TO 'bookquik_user'@'%';
GRANT ALL PRIVILEGES ON payment_service.* TO 'bookquik_user'@'%';
GRANT ALL PRIVILEGES ON notification_service.* TO 'bookquik_user'@'%';

-- Apply changes
FLUSH PRIVILEGES;