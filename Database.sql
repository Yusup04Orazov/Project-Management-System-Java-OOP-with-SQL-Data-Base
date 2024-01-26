use yorazov;

DROP TABLE IF EXISTS Invoices;
DROP TABLE IF EXISTS TreatmentDetails;
DROP TABLE IF EXISTS Appointments;
DROP TABLE IF EXISTS Services;
DROP TABLE IF EXISTS Dentists;
DROP TABLE IF EXISTS Patients;

CREATE TABLE Patients (
  PatientID INT PRIMARY KEY,
  FirstName VARCHAR(50),
  LastName VARCHAR(50),
  DateOfBirth DATE,
  PhoneNumber VARCHAR(20),
  Email VARCHAR(50),
  Address VARCHAR(100)
);

CREATE TABLE Dentists (
  DentistID INT PRIMARY KEY,
  FirstName VARCHAR(50),
  LastName VARCHAR(50),
  Specialty VARCHAR(50)
);

CREATE TABLE Services (
  ServiceID INT PRIMARY KEY,
  ServiceName VARCHAR(100),
  ServiceDescription TEXT
);

CREATE TABLE Appointments (
  AppointmentID INT PRIMARY KEY,
  PatientID INT,
  AppointmentDateTime DATETIME,
  ServiceID INT,
  DentistID INT,
  FOREIGN KEY (PatientID) REFERENCES Patients(PatientID),
  FOREIGN KEY (ServiceID) REFERENCES Services(ServiceID),
  FOREIGN KEY (DentistID) REFERENCES Dentists(DentistID)
);

CREATE TABLE TreatmentDetails (
  TreatmentDetailsID INT PRIMARY KEY,
  AppointmentID INT,
  ToothNumber INT,
  TreatmentNotes TEXT,  
  FOREIGN KEY (AppointmentID) REFERENCES Appointments(AppointmentID)
);

CREATE TABLE Invoices (
  InvoiceID INT PRIMARY KEY,
  PatientID INT,
  AppointmentID INT,
  AmountDue DECIMAL(10,2),
  FOREIGN KEY (PatientID) REFERENCES Patients(PatientID),
  FOREIGN KEY (AppointmentID) REFERENCES Appointments(AppointmentID)
);

INSERT INTO Patients (PatientID, FirstName, LastName, DateOfBirth, PhoneNumber, Email, Address)
VALUES
  (1, 'John', 'Doe', '1980-03-15', '555-1234', 'jdoe@email.com', '123 Main St, City, State 12345'),
  (2, 'Jane', 'Doe', '1985-11-02', '555-5678', 'jane.doe@email.com', '456 Park Ave, City, State 12345'),
  (3, 'Bob', 'Smith', '1990-05-20', '555-9101', 'bob.smith@email.com', '789 Elm St, City, State 12345');

INSERT INTO Dentists (DentistID, FirstName, LastName, Specialty)
VALUES
  (1, 'Sarah', 'Lee', 'General'),
  (2, 'Mark', 'Jones', 'Orthodontics'),
  (3, 'John', 'Kim', 'Oral Surgery');
  
INSERT INTO Services (ServiceID, ServiceName, ServiceDescription)  
VALUES
  (1, 'Exam', 'A complete dental examination'),
  (2, 'Cleaning', 'Professional teeth cleaning'),
  (3, 'Crown', 'Repair decayed or damaged teeth');
  
INSERT INTO Appointments (AppointmentID, PatientID, AppointmentDateTime, ServiceID, DentistID)
VALUES
  (1, 1, '2023-03-01 09:00:00', 1, 1),
  (2, 1, '2023-06-01 11:00:00', 2, 3),
  (3, 2, '2023-02-15 10:30:00', 3, 2);

INSERT INTO TreatmentDetails (TreatmentDetailsID, AppointmentID, ToothNumber, TreatmentNotes)  
VALUES
  (1, 1, 3, 'Tooth 3 has minor decay'),
  (2, 2, 12, 'Tooth 12 cracked'),
  (3, 3, 5, 'Fit crown on tooth 5');
  
INSERT INTO Invoices (InvoiceID, PatientID, AppointmentID, AmountDue)
VALUES
  (1, 1, 1, 100.00),
  (2, 1, 2, 150.00),
  (3, 2, 3, 250.00);