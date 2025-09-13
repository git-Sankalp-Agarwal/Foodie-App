CREATE DATABASE IF NOT EXISTS foodie_db;
USE foodie_db;

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    order_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_time TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_order_status (status),
    INDEX idx_order_time (order_time),
    INDEX idx_customer_name (customer_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO orders (customer_name, total_amount, status, order_time) VALUES
('John Doe', 25.99, 'PENDING', NOW()),
('Jane Smith', 35.50, 'PROCESSED', NOW() - INTERVAL 1 HOUR),
('Bob Johnson', 42.00, 'DELIVERED', NOW() - INTERVAL 2 HOUR);

INSERT INTO order_items (order_id, item_name, quantity, price) VALUES
(1, 'Burger', 2, 8.99),
(1, 'Fries', 1, 3.99),
(1, 'Coke', 2, 2.50),
(2, 'Pizza', 1, 15.99),
(2, 'Salad', 1, 7.99),
(2, 'Juice', 2, 3.50),
(3, 'Pasta', 2, 12.99),
(3, 'Garlic Bread', 1, 4.99),
(3, 'Ice Cream', 2, 5.50);