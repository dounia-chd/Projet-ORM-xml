C-- Script d'initialisation de la base de données EMSI

CREATE DATABASE IF NOT EXISTS emsi_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE emsi_db;

-- Table Client
CREATE TABLE IF NOT EXISTS client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    INDEX idx_email (email)
) ENGINE=InnoDB;

-- Table Commande
CREATE TABLE IF NOT EXISTS commande (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_commande DATE NOT NULL,
    id_client BIGINT NOT NULL,
    montant DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_client) REFERENCES client(id) ON DELETE CASCADE,
    INDEX idx_client (id_client)
) ENGINE=InnoDB;

-- Données de test
INSERT INTO client (nom, email) VALUES 
    ('Ahmed Bennani', 'ahmed.bennani@emsi.ma'),
    ('Fatima Zahra', 'fatima.zahra@emsi.ma'),
    ('Youssef Alami', 'youssef.alami@emsi.ma');

INSERT INTO commande (date_commande, id_client, montant) VALUES
    ('2025-01-15', 1, 1250.00),
    ('2025-01-20', 1, 890.50),
    ('2025-01-22', 2, 3400.00);
