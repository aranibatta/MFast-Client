SELECT 
MeasurementFrame.measurementFrameId, 
MeasurementFrame.frameNumber, 
MeasurementFrame.frameTime,
MeasurementFrame.realTimeStampSeconds,
MeasurementFrame.realTimeStampMicros,
MeasurementFrame.skeletonCount, 
MeasurementFrame.measurementSessionId, 
MeasurementFrame.timestamp 
FROM Patient, Evaluations, MeasurementSession, MeasurementFrame
WHERE Patient.lastName = 'Skace'
AND Patient.patientId = MeasurementSession.sessionPatientId
AND Evaluations.evaluationName = 'TEST_REACHABLE_WS_RIGHT'
AND Evaluations.evaluationId = MeasurementSession.evaluationId
AND measurementsession.timestamp  BETWEEN '2014-09-03 00:00:00' AND '2014-09-03 23:59:59'
AND MeasurementFrame.measurementSessionId = MeasurementSession.measurementSessionId
ORDER BY measurementFrameId DESC;

