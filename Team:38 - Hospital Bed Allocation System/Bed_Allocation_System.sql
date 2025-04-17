-- Create the database
CREATE DATABASE IF NOT EXISTS hospital_system;
USE hospital_system;

-- Table: Hospitals
CREATE TABLE IF NOT EXISTS hospitals (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100),
    latitude DOUBLE,
    longitude DOUBLE,
    total_beds INT,
    available_beds INT
);

-- Table: Specialties
CREATE TABLE IF NOT EXISTS specialties (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hospital_id VARCHAR(10),
    specialty_name VARCHAR(100),
    FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE CASCADE
);

-- Table: Patients
CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    severity VARCHAR(20),
    latitude DOUBLE,
    longitude DOUBLE,
    treatment_type VARCHAR(50),
    assigned_hospital VARCHAR(10),
    admission_time TIMESTAMP,
    discharge_time TIMESTAMP NULL,
    FOREIGN KEY (assigned_hospital) REFERENCES hospitals(id)
);

-- Insert into hospitals table
INSERT INTO hospitals (id, name, latitude, longitude, total_beds, available_beds) VALUES
('H1', 'CityCare Hospital', 19.0760, 72.8777, 100, 100),
('H2', 'GreenLife Medical Center', 28.6139, 77.2090, 80, 80),
('H3', 'Sunshine Health Hub', 13.0827, 80.2707, 120, 120),
('H4', 'Metro Heart Institute', 22.5726, 88.3639, 90, 90),
('H5', 'Nova Children Hospital', 17.3850, 78.4867, 70, 70),
('H6', 'Apex Neurology Center', 23.0225, 72.5714, 60, 60),
('H7', 'Hope Eye Hospital', 26.9124, 75.7873, 50, 50),
('H8', 'Shanti Orthopedic Hospital', 21.1458, 79.0882, 75, 75),
('H9', 'HealWell Women\'s Clinic', 18.5204, 73.8567, 40, 40),
('H10', 'Global Multispecialty', 15.3173, 75.7139, 110, 110);

SELECT * FROM hospitals;

-- Insert specialties for each hospital
INSERT INTO specialties (hospital_id, specialty_name) VALUES
('H1', 'cardiology'),
('H1', 'general medicine'),
('H2', 'dermatology'),
('H2', 'ENT'),
('H3', 'pediatrics'),
('H3', 'orthopedic'),
('H4', 'cardiology'),
('H5', 'pediatrics'),
('H6', 'neurology'),
('H7', 'eye care'),
('H8', 'orthopedic'),
('H9', 'gynecology'),
('H9', 'psychiatry'),
('H10', 'urology'),
('H10', 'general surgery'),
('H10', 'oncology');

SELECT * FROM specialties;
SELECT * FROM patients;
