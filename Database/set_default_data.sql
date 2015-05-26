use mfast;

/*
 Insert default values for the Evaluations table:
 This maps the names
*/
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE mfast.evaluations;
SET FOREIGN_KEY_CHECKS=1;

INSERT INTO evaluations (evaluationId, evaluationName, evaluationDescription)
VALUES
('0', 'TEST_REACHABLE_WS_RIGHT', 'Reachable Workspace (Right Side)'),
('1', 'TEST_REACHABLE_WS_LEFT', 'Reachable Workspace (Left Side)'),
('2', 'TEST_FUNCTIONAL_WS_RIGHT', 'Functional Workspace (Right Side)'),
('3', 'TEST_FUNCTIONAL_WS_LEFT', 'Functional Workspace (Left Side)'),
('4', 'TEST_SHOULDER_ROTATION_RIGHT', 'Shoulder Rotation (Right Side)'),
('5', 'TEST_SHOULDER_ROTATION_LEFT', 'Shoulder Rotation (Left Side)'),
('6', 'TEST_FREE_ROM_RIGHT', 'TEST_FREE_ROM_RIGHT'),
('7', 'TEST_FREE_ROM_LEFT', 'TEST_FREE_ROM_LEFT');

/*
 This is the location of the site
 */
 
 INSERT INTO location (locationId, locationName, locationDescription)
 VALUES
 ('1', 'UCB', ''),
 ('101', 'UCDMC', 'Jay Han Lab');


/*
 Edit this section to add groups
 */
INSERT INTO patientgroup (patientGroupId, groupName, groupDescription)
VALUES
(1, 'Test', 'Test/demo subjects'),
(2, 'Controls', 'Controls for various studies'),
(3, 'DMD', 'Duchene Muscular Dystrophy'),
(4, 'FSHD', 'Facioscapulohumeral muscular dystrophy'),
(5, 'U01', 'U01 Study'),
(6, 'BMD', 'Becker Muscular Dystrophy'),
(7, 'ALS', 'ALS Study'),
(8, 'Upper Arm Injury', 'Pilot upper arm injury');
(9, 'Kinect Pilot', 'Kinect Pilot');
(10, 'PT Pilot', 'PT Pilot');

 


/* 
 Edit this script to insert user names
 */
INSERT INTO therapist (therapistId, locationId, firstName, lastName, userName, password, isAdmin)
VALUES
('1', '1', 'Gregorij', 'K', 'gregorij', 'admin', '1'),
('2', '101', 'Alina', 'N', 'alina', 'ucdmc', '1'),
('3', '101', 'Jay', 'H', 'jay', 'ucdmc', '0');


INSERT INTO patient (customPatientId, firstName, lastName, DOB, gender, therapistId, locationId, handDominance, patientGroupId)
VALUES
('K0001', 'John', 'Doe', '1976-08-14 00:00:00', 'male', '1', '1', '1', '1'),
('K0002', 'Jane', 'Doe', '1976-01-01 00:00:00', 'female', '1', '1', '1', '1'),
('K0001', 'John', 'Doe', '1976-08-14 00:00:00', 'male', '1', '101', '1', '1'),
('K0002', 'Jane', 'Doe', '1976-01-01 00:00:00', 'female', '1', '101', '1', '1');



