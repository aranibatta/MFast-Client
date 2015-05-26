USE mfast_test;


# Patient selection
SET @SelectedPatient = 'AG47K';
SET @SelectedTask = 'TEST_FUNCTIONAL_WS_LEFT';
SET @SelectedVisitNumber = 1;
SET @SelectedRepetition = 1;
SET @SelectedWeight = 0;
SET @SelectedTrialNumber = 1;

# Get distinct visit dates into a new table called 'Visits'
SET @rn = 0;
DROP TABLE IF EXISTS Visits;
CREATE TEMPORARY TABLE IF NOT EXISTS Visits AS (
SELECT @rn:=@rn+1 AS count, date FROM 
(SELECT DISTINCT(DATE(measurementsession.timestamp)) AS date
FROM measurementsession, patient 
WHERE patient.customPatientId = @SelectedPatient
AND MeasurementSession.sessionPatientId = patient.patientId) AS VisitsTemp ORDER BY date ASC );

# add visit count column to the table
SELECT * FROM Visits;

# Select measurement frames by Visit Number
SELECT date FROM Visits WHERE
Visits.count = @SelectedVisitNumber 
INTO @SelectedDate;

SELECT @SelectedDate;


# Get list of session IDs that match particular task on the same day
SET @rn = 0;
DROP TABLE IF EXISTS Trials;
CREATE TEMPORARY TABLE IF NOT EXISTS Trials AS (
SELECT @rn:=@rn+1 AS count, measurementSessionId AS sessionId FROM
(SELECT MeasurementSession.measurementSessionId FROM MeasurementSession, Patient, Evaluations
WHERE Patient.customPatientId = @SelectedPatient
AND Evaluations.evaluationName = @SelectedTask
AND MeasurementSession.timestamp BETWEEN CONCAT(@SelectedDate,' 00:00:00') AND CONCAT(@SelectedDate, ' 23:59:00')
AND MeasurementSession.sessionPatientId = Patient.patientId
AND MeasurementSession.evaluationID = Evaluations.evaluationID
AND MeasurementSession.weight = @SelectedWeight) AS TempTrails);

# Trials now contains a list of measurementSessionId's
SELECT * FROM Trials;

# Select trial based on SelectedTrialNumber variable (1,2,3..) and return sessionID
SELECT sessionId FROM Trials WHERE
Trials.count = @SelectedTrialNumber
INTO @currentSessionId;

SELECT @currentSessionId;

# Now that we know sessionID, we can do a simple query to retrieve the MeasuremetFrame data
SELECT 	MeasurementSession.timestamp, 
		MeasurementFrame.frameNumber, 
		MeasurementFrame.frameTime,
		MeasurementFrame.realTimeStampSeconds,
		MeasurementFrame.realTimeStampMicros,
		MeasurementFrame.skeletonCount
FROM MeasurementFrame, MeasurementSession, Trials
WHERE MeasurementFrame.measurementSessionId = Trials.sessionId;

# Now grab the skeleton data
SELECT 	MeasurementFrame.frameNumber,
		MeasurementFrame.frameTime,
		MeasurementFrame.realTimeStampSeconds,
		MeasurementFrame.realTimeStampMicros,
`skeleton`.`skeletonSequenceNumber`,
    `skeleton`.`headX`, `skeleton`.`headY`, `skeleton`.`headZ`,
    `skeleton`.`hipCenterX`, `skeleton`.`hipCenterY`, `skeleton`.`hipCenterZ`,
    `skeleton`.`leftAnkleX`, `skeleton`.`leftAnkleY`, `skeleton`.`leftAnkleZ`,
    `skeleton`.`rightAnkleX`, `skeleton`.`rightAnkleY`, `skeleton`.`rightAnkleZ`,
    `skeleton`.`leftElbowX`, `skeleton`.`leftElbowY`, `skeleton`.`leftElbowZ`,
    `skeleton`.`rightElbowX`, `skeleton`.`rightElbowY`, `skeleton`.`rightElbowZ`,
    `skeleton`.`leftFootX`, `skeleton`.`leftFootY`, `skeleton`.`leftFootZ`,
    `skeleton`.`rightFootX`, `skeleton`.`rightFootY`, `skeleton`.`rightFootZ`,
    `skeleton`.`leftHandX`, `skeleton`.`leftHandY`, `skeleton`.`leftHandZ`,
    `skeleton`.`rightHandX`, `skeleton`.`rightHandY`, `skeleton`.`rightHandZ`,
    `skeleton`.`leftHipX`, `skeleton`.`leftHipY`, `skeleton`.`leftHipZ`,
    `skeleton`.`rightHipX`, `skeleton`.`rightHipY`, `skeleton`.`rightHipZ`,
    `skeleton`.`leftKneeX`, `skeleton`.`leftKneeY`, `skeleton`.`leftKneeZ`,
    `skeleton`.`rightKneeX`, `skeleton`.`rightKneeY`, `skeleton`.`rightKneeZ`,
    `skeleton`.`leftShoulderX`, `skeleton`.`leftShoulderY`, `skeleton`.`leftShoulderZ`,
    `skeleton`.`rightShoulderX`, `skeleton`.`rightShoulderY`, `skeleton`.`rightShoulderZ`,
    `skeleton`.`leftWristX`, `skeleton`.`leftWristY`, `skeleton`.`leftWristZ`,
    `skeleton`.`rightWristX`, `skeleton`.`rightWristY`, `skeleton`.`rightWristZ`,
    `skeleton`.`shoulderCenterX`, `skeleton`.`shoulderCenterY`, `skeleton`.`shoulderCenterZ`,
    `skeleton`.`spineX`, `skeleton`.`spineY`, `skeleton`.`spineZ`
FROM Skeleton, MeasurementFrame, MeasurementSession, Trials
WHERE MeasurementFrame.measurementSessionId = Trials.sessionId
AND Skeleton.measurementFrameId = MeasurementFrame.measurementFrameId;





#--------------------------------------------------------#
/*
SELECT 	MeasurementSession.timestamp, 
		MeasurementFrame.frameNumber, 
		MeasurementFrame.frameTime,
		MeasurementFrame.realTimeStampSeconds,
		MeasurementFrame.realTimeStampMicros,
		MeasurementFrame.skeletonCount
FROM MeasurementFrame, MeasurementSession, Patient, Evaluations, Visits
WHERE Patient.customPatientId = @SelectedPatient
AND Evaluations.evaluationName = @SelectedTask
AND MeasurementSession.timestamp BETWEEN CONCAT(@SelectedDate,' 00:00:00') AND CONCAT(@SelectedDate, ' 23:59:00')
AND MeasurementSession.sessionPatientId = Patient.patientId
AND MeasurementSession.evaluationID = Evaluations.evaluationID
AND MeasurementFrame.measurementSessionId = MeasurementSession.measurementSessionId;




# Select measurement frames by Date
SET @SelectedDate = '2013-10-22';
SELECT 	MeasurementSession.timestamp, 
		MeasurementFrame.frameNumber, 
		MeasurementFrame.frameTime,
		MeasurementFrame.realTimeStampSeconds,
		MeasurementFrame.realTimeStampMicros,
		MeasurementFrame.skeletonCount
FROM MeasurementFrame, MeasurementSession, Patient, Evaluations 
WHERE Patient.customPatientId = @SelectedPatient
AND Evaluations.evaluationName = @SelectedTask
AND MeasurementSession.timestamp BETWEEN CONCAT(@SelectedDate,' 00:00:00') AND CONCAT(@SelectedDate, ' 23:59:00')
AND MeasurementSession.sessionPatientId = Patient.patientId
AND MeasurementSession.evaluationID = Evaluations.evaluationID
AND MeasurementFrame.measurementSessionId = MeasurementSession.measurementSessionId;

*/