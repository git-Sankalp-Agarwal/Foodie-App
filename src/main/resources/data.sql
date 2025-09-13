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