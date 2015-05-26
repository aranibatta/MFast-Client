
/*
 Insert default values for the Evaluations table:
 This maps the names
*/
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE mfast.evaluations;
SET FOREIGN_KEY_CHECKS=1;

INSERT INTO mfast.evaluations (evaluationId, evaluationName, evaluationDescription)
VALUES
('0', 'TEST_REACHABLE_WS_RIGHT', 'Reachable Workspace (Right Side)'),
('1', 'TEST_REACHABLE_WS_LEFT', 'Reachable Workspace (Left Side)'),
('2', 'TEST_FUNCTIONAL_WS_RIGHT', 'Functional Workspace (Right Side)'),
('3', 'TEST_FUNCTIONAL_WS_LEFT', 'Functional Workspace (Left Side)'),
('4', 'TEST_SHOULDER_ROTATION_RIGHT', 'Shoulder Rotation (Right Side)'),
('5', 'TEST_SHOULDER_ROTATION_LEFT', 'Shoulder Rotation (Left Side)'),
('6', 'TEST_FREE_ROM_RIGHT', 'TEST_FREE_ROM_RIGHT'),
('7', 'TEST_FREE_ROM_LEFT', 'TEST_FREE_ROM_LEFT');
