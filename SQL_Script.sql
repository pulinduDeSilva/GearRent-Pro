CREATE database gear_rent_pro;
USE gear_rent_pro;

CREATE TABLE branch (
    branch_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_code VARCHAR(10) UNIQUE,
    name VARCHAR(100) NOT NULL,
    address TEXT,
    contact VARCHAR(20)
);


CREATE TABLE membership (
    membership_id INT AUTO_INCREMENT PRIMARY KEY,
    level_name VARCHAR(20) UNIQUE,
    discount_percentage DECIMAL(5,2)
);


CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    nic_passport VARCHAR(20) UNIQUE,
    contact_no VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    membership_id INT,
    FOREIGN KEY (membership_id) REFERENCES membership(membership_id)
);

CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    price_factor DECIMAL(5,2),
    weekend_multiplier DECIMAL(5,2),
    late_fee_per_day DECIMAL(10,2),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE equipment (
    equipment_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT,
    branch_id INT,
    brand VARCHAR(50),
    model VARCHAR(50),
    purchase_year INT,
    base_daily_price DECIMAL(10,2),
    security_deposit DECIMAL(10,2),
    status ENUM('Available','Reserved','Rented','Under Maintenance'),
    FOREIGN KEY (category_id) REFERENCES category(category_id),
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id)
);



CREATE TABLE reservation (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    equipment_id INT,
    customer_id INT,
    branch_id INT,
    start_date DATE,
    end_date DATE,
    status ENUM('Active','Cancelled','Converted'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id)
);


CREATE TABLE rental (
    rental_id INT AUTO_INCREMENT PRIMARY KEY,
    equipment_id INT,
    customer_id INT,
    branch_id INT,

    start_date DATE,
    end_date DATE,

    rental_amount DECIMAL(10,2),
    security_deposit DECIMAL(10,2),

    membership_discount DECIMAL(10,2),
    long_rental_discount DECIMAL(10,2),

    final_amount DECIMAL(10,2),

    payment_status ENUM('Paid','Partially Paid','Unpaid'),
    rental_status ENUM('Active','Returned','Overdue','Cancelled'),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id)
);



CREATE TABLE rental (
    rental_id INT AUTO_INCREMENT PRIMARY KEY,
    equipment_id INT,
    customer_id INT,
    branch_id INT,

    start_date DATE,
    end_date DATE,

    rental_amount DECIMAL(10,2),
    security_deposit DECIMAL(10,2),

    membership_discount DECIMAL(10,2),
    long_rental_discount DECIMAL(10,2),

    final_amount DECIMAL(10,2),

    payment_status ENUM('Paid','Partially Paid','Unpaid'),
    rental_status ENUM('Active','Returned','Overdue','Cancelled'),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id)
);



CREATE TABLE system_user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    role ENUM('Admin','BranchManager','Staff'),
    branch_id INT,
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id)
);



CREATE TABLE system_config (
    config_key VARCHAR(50) PRIMARY KEY,
    config_value VARCHAR(100)
);


INSERT INTO membership(level_name,discount_percentage) VALUES
('Regular',0),
('Silver',5),
('Gold',10);


INSERT INTO category(name,description,price_factor,weekend_multiplier,late_fee_per_day) VALUES
('Camera','Professional Cameras',1.2,1.2,2000),
('Lens','Camera Lenses',1.0,1.1,1500),
('Drone','Professional Drones',1.5,1.3,5000),
('Lighting','Studio Lighting Kits',1.1,1.1,1800),
('Audio','Professional Audio Gear',1.0,1.1,1500);


INSERT INTO equipment(category_id,branch_id,brand,model,purchase_year,base_daily_price,security_deposit,status) VALUES
(1,1,'Canon','R5',2023,15000,200000,'Available'),
(1,1,'Sony','A7 IV',2022,14000,180000,'Available'),
(1,2,'Nikon','Z6',2021,13000,170000,'Available'),
(1,3,'Canon','R6',2023,14500,190000,'Available'),

(2,1,'Canon','RF 24-70',2022,6000,80000,'Available'),
(2,1,'Sony','FE 70-200',2022,6500,90000,'Available'),
(2,2,'Sigma','Art 35mm',2021,5000,70000,'Available'),
(2,3,'Tamron','28-75',2022,5500,75000,'Available'),

(3,1,'DJI','Mavic 3',2023,20000,300000,'Available'),
(3,2,'DJI','Air 2S',2022,17000,250000,'Available'),
(3,3,'DJI','Mini 3 Pro',2023,15000,200000,'Available'),

(4,1,'Godox','SL60W',2022,4000,50000,'Available'),
(4,2,'Aputure','300D',2023,7000,90000,'Available'),
(4,3,'Neewer','660 LED',2021,3500,40000,'Available'),

(5,1,'Rode','NTG4+',2022,2500,30000,'Available'),
(5,1,'Zoom','H6 Recorder',2023,3000,35000,'Available'),
(5,2,'Shure','SM7B',2022,2800,32000,'Available'),
(5,3,'Rode','Wireless GO II',2023,3500,40000,'Available'),
(5,2,'Tascam','DR-40',2021,2400,30000,'Available'),
(5,3,'Sennheiser','MKE 600',2022,2600,35000,'Available');


INSERT INTO customer(name,nic_passport,contact_no,email,address,membership_id) VALUES
('Nimal Perera','901234567V','0771111111','nimal@gmail.com','Colombo',1),
('Kamal Silva','921234567V','0772222222','kamal@gmail.com','Panadura',2),
('Sunil Fernando','881234567V','0773333333','sunil@gmail.com','Galle',3),
('Ruwan Jayasinghe','931234567V','0774444444','ruwan@gmail.com','Matara',1),
('Saman Dias','891234567V','0775555555','saman@gmail.com','Kalutara',2),
('Tharindu Perera','941234567V','0776666666','tharindu@gmail.com','Colombo',3),
('Ashan Silva','951234567V','0777777777','ashan@gmail.com','Panadura',1),
('Dilshan Fernando','961234567V','0778888888','dilshan@gmail.com','Galle',2),
('Kasun Perera','971234567V','0779999999','kasun@gmail.com','Colombo',3),
('Supun Silva','981234567V','0770000000','supun@gmail.com','Negombo',1);


INSERT INTO system_user(username,password,role,branch_id) VALUES
('admin','admin123','Admin',NULL),
('manager_panadura','1234','BranchManager',1),
('manager_galle','1234','BranchManager',2),
('staff_panadura','1234','Staff',1),
('staff_galle','1234','Staff',2);


INSERT INTO system_user(username,password,role,branch_id) VALUES
('admin','admin123','Admin',NULL),
('manager_panadura','1234','BranchManager',1),
('manager_galle','1234','BranchManager',2),
('staff_panadura','1234','Staff',1),
('staff_galle','1234','Staff',2);