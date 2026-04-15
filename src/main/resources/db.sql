-- 创建预订信息表
CREATE TABLE IF NOT EXISTS reservation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(11) NOT NULL,
    guest_count INT NOT NULL,
    room_count INT NOT NULL,
    room_type_single BOOLEAN DEFAULT FALSE,
    room_type_standard BOOLEAN DEFAULT FALSE,
    room_type_suite BOOLEAN DEFAULT FALSE,
    pickup_location VARCHAR(100),
    arrival_date DATE NOT NULL,
    arrival_time TIME NOT NULL,
    hotel VARCHAR(100),
    room_number VARCHAR(20),
    table_number VARCHAR(20),
    remark TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建索引以优化查询性能
CREATE INDEX idx_phone ON reservation(phone);
CREATE INDEX idx_name ON reservation(name);
CREATE INDEX idx_arrival_date ON reservation(arrival_date);
