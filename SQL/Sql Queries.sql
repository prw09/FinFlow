CREATE DATABASE upi_management;

USE upi_management;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    upi_id VARCHAR(50) UNIQUE NOT NULL,
    balance DOUBLE DEFAULT 0
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_upi_id VARCHAR(50) NOT NULL,
    receiver_upi_id VARCHAR(50) NOT NULL,
    amount DOUBLE NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_upi_id) REFERENCES users(upi_id),
    FOREIGN KEY (receiver_upi_id) REFERENCES users(upi_id)
);


ALTER TABLE users ADD COLUMN password VARCHAR(255) NOT NULL;

ALTER TABLE users ADD COLUMN daily_limit DOUBLE DEFAULT 5000;

ALTER TABLE users ADD COLUMN daily_spent DOUBLE DEFAULT 0;
