# You can run these sample scripts by blocks. Select the script block to run and click the 'lightning' icon on the toolbar.

# Set default database
#USE mfast_ucdmc;

# Set patient ID
SET @PatientCustomId = '501006';

#
# Get list of current (valid) sessions for the patient
#
SELECT 
measurementsession.measurementsessionId,
patient.patientId,
DATE(measurementsession.timestamp) AS date,
Time(measurementsession.timestamp) AS time,
evaluations.evaluationName,
measurementsession.weight,
measurementsession.level,
measurementsession.notes
FROM measurementsession, patient, evaluations
WHERE measurementsession.sessionPatientId = patient.patientId
AND patient.customPatientId = @PatientCustomId
AND measurementsession.frameCount > 0
AND measurementsession.isCompleted = 1
AND measurementsession.evaluationId = evaluations.evaluationId
ORDER BY timestamp;


# ------------------------------------------------------------------------- #

#
# Get skeletal data for particular session
#
SET @PatientCustomId = '501006';
SET @SelectedWeight = 0.0;
SET @SelectedLevel = 1;
SET @SelectedEvaluation = 'TEST_REACHABLE_WS_RIGHT';
SET @SelectedTrial = 1;
SET @row_number := 0;

#
# Selects session and returns skeleton data for that session
#
SELECT 
measurementframe.frameNumber,
measurementframe.frameTime,
measurementframe.realTimeStampSeconds,
measurementframe.realTimeStampMicros,
headX, headY, headZ, 
hipCenterX, hipCenterY, hipCenterZ, 
leftAnkleX, leftAnkleY, leftAnkleZ, 
rightAnkleX, rightAnkleY, rightAnkleZ, 
leftElbowX, leftElbowY, leftElbowZ, 
rightElbowX, rightElbowY, rightElbowZ, 
leftFootX, leftFootY, leftFootZ, 
rightFootX, rightFootY, rightFootZ, 
leftHandX, leftHandY, leftHandZ, 
rightHandX, rightHandY, rightHandZ, 
leftHipX, leftHipY, leftHipZ, 
rightHipX, rightHipY, rightHipZ, 
leftKneeX, leftKneeY, leftKneeZ, 
rightKneeX, rightKneeY, rightKneeZ, 
leftShoulderX, leftShoulderY, leftShoulderZ, 
rightShoulderX, rightShoulderY, rightShoulderZ, 
leftWristX, leftWristY, leftWristZ, 
rightWristX, rightWristY, rightWristZ, 
shoulderCenterX, shoulderCenterY, shoulderCenterZ, 
spineX, spineY, spineZ

FROM
measurementFrame,
skeleton,
(
	SELECT 
	(@row_number:=@row_number+1) AS trialCount,
	measurementsession.measurementSessionId,
	measurementsession.evaluationId,
	evaluations.evaluationName,
	measurementsession.sessionLocationId,
	measurementsession.sessionPatientId,
	measurementsession.timestamp,
	measurementsession.weight,
	measurementsession.level,
	measurementsession.position,
	measurementsession.notes
	FROM measurementsession, patient, evaluations
	WHERE measurementsession.sessionPatientId = patient.patientId
	AND patient.customPatientId = @PatientCustomId
	AND measurementsession.frameCount > 0
	AND measurementsession.isCompleted = 1
	AND measurementsession.evaluationId = evaluations.evaluationId
	AND evaluations.evaluationName = @SelectedEvaluation
	AND measurementsession.weight = @SelectedWeight
	AND measurementsession.level = @SelectedLevel
	ORDER BY timestamp
) AS validsessions
WHERE validsessions.trialCount = @SelectedTrial
AND validsessions.measurementSessionId = measurementFrame.measurementSessionId
AND measurementframe.measurementFrameId = skeleton.measurementFrameId


