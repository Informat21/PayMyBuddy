-- =====================
-- = TABLE : user
-- =====================
CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    balance DECIMAL(38, 2) DEFAULT 0.00
);

-- =====================
-- = TABLE : transaction
-- =====================
CREATE TABLE IF NOT EXISTS transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    description VARCHAR(255),
    amount DECIMAL(38, 2) NOT NULL,
    timestamp DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT fk_sender_id FOREIGN KEY (sender_id) REFERENCES user(id),
    CONSTRAINT fk_receiver_id FOREIGN KEY (receiver_id) REFERENCES user(id)
);

-- =====================
-- = TABLE : connection
-- =====================
CREATE TABLE IF NOT EXISTS connection (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    buddy_id INT NOT NULL,

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT fk_buddy FOREIGN KEY (buddy_id) REFERENCES user(id),

    CONSTRAINT unique_connection UNIQUE (user_id, buddy_id)
);
