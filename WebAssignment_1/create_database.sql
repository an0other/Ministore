/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  an0other
 * Created: Jun 10, 2025
 */

use master;

CREATE DATABASE WEB_MINISTORE;

use WEB_MINISTORE

-- USERS
CREATE TABLE users (
    username NVARCHAR(50) PRIMARY KEY,                  -- Tự nhập, là khóa chính
    password NVARCHAR(255) NOT NULL,
	email NVARCHAR(100) NOT NULL UNIQUE,
    full_name NVARCHAR(100),
status BIT default 1
    role NVARCHAR(10) DEFAULT 'USER'                   -- USER | ADMIN
);

-- CATEGORIES
CREATE TABLE categories (
    category_id NVARCHAR(20) PRIMARY KEY,               -- VD: CAT01, CAT02
    name NVARCHAR(100) NOT NULL
);

-- PRODUCTS
CREATE TABLE products (
    product_id NVARCHAR(20) PRIMARY KEY,                -- VD: PRD001, PRD002
    name NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(10, 2) NOT NULL,
    image_url NVARCHAR(255),
    category_id NVARCHAR(20),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- ORDERS
CREATE TABLE orders (
    order_id NVARCHAR(20) PRIMARY KEY,                  -- VD: ORD0001
    username NVARCHAR(50) NOT NULL,
    total_price DECIMAL(10,2),
    status NVARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (username) REFERENCES users(username)
);

-- ORDER_ITEMS
CREATE TABLE order_items (
    order_item_id NVARCHAR(20) PRIMARY KEY,             -- VD: ITEM0001
    order_id NVARCHAR(20),
    product_id NVARCHAR(20),
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,                       -- Giá lúc mua
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);