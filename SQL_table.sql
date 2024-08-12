-- Drop existing users if they exist
DROP USER IF EXISTS 'cwt_user'@'localhost';
DROP USER IF EXISTS 'concur_user'@'localhost';
DROP USER IF EXISTS 'customer_user'@'localhost';

-- Create new users
CREATE USER 'cwt_user'@'localhost' IDENTIFIED BY 'password';
CREATE USER 'concur_user'@'localhost' IDENTIFIED BY 'password';
CREATE USER 'customer_user'@'localhost' IDENTIFIED BY 'password';

-- Grant permissions
GRANT SELECT, INSERT, UPDATE, DELETE ON db.token TO 'cwt_user'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON db.user_details TO 'concur_user'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON db.user_profile TO 'customer_user'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;
