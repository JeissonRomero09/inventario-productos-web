CREATE DATABASE IF NOT EXISTS Inventario
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE Inventario;

CREATE TABLE IF NOT EXISTS products (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    sku VARCHAR(50) NOT NULL,
    name VARCHAR(120) NOT NULL,
    category VARCHAR(80) NOT NULL DEFAULT 'General',
    description VARCHAR(1000) NOT NULL DEFAULT '',
    unit_price DECIMAL(14, 2) UNSIGNED NOT NULL DEFAULT 0.00,
    stock INT UNSIGNED NOT NULL DEFAULT 0,
    low_stock_threshold INT UNSIGNED NOT NULL DEFAULT 5,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_products_sku (sku),
    KEY idx_products_name (name),
    KEY idx_products_stock (stock, low_stock_threshold)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS inventory_movements (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    product_id BIGINT UNSIGNED NOT NULL,
    movement_type ENUM('ENTRY', 'EXIT') NOT NULL,
    quantity INT UNSIGNED NOT NULL,
    unit_price_snapshot DECIMAL(14, 2) UNSIGNED NOT NULL,
    note VARCHAR(255) NOT NULL DEFAULT '',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_movements_created_at (created_at),
    KEY idx_movements_product_created (product_id, created_at),
    CONSTRAINT fk_movements_product
        FOREIGN KEY (product_id) REFERENCES products (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;
