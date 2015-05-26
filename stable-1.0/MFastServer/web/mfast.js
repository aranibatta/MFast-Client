
var therapistLoginURL = "webresources/therapistlogin";
var therapistLogoutURL = "webresources/TherapistLogout";
var patientManagerURL = "webresources/patientmanager";
var evaluationRequestURL = "webresources/EvaluationRequest";
var getGroupsListURL = "webresources/patientGroupManager";
var checkSessionStatusURL="webresources/CheckSessionStatus";
var deleteEvaluationSessionURL="webresources/DeleteEvaluationSession";
var updateEvaluationSessionURL="webresources/UpdateEvaluationSession";
var pastResultsURL="webresources/pastresults";
//var playvideoURL="webresources/playvideo";

var mock_mode = false;

var mock_therapistLoginRequestJSON = {
    "loginName": "saqib",
    "password": "pwd12345"
};

var mock_therapistLoginResponseSuccessJSON = {
    "msg": "Success",
    "status": true,
    "therapistId": 2034 // to be used with every evaluation request going forward 
};

var mock_therapistLoginResponseFailedJSON = {
    "msg": "Incorrect user name or password",
    "status": false,
    "errorId": 1000
};

var mock_patientsListRequestJSON = {
    "requestType": 2, // get patient list for this therapist 
    "therapistId": 1234
};

var mock_patientsListResponseJSON = {
    "patientList": [
        {
            "patientId": 1234,
            "firstName": "Saqib",
            "lastName": "Ahmad"
        },
        {
            "patientId": 1235,
            "firstName": "Anki",
            "lastName": "Nilaturu"
        }
    ]
};

var mock_addPatientRequestJSON = {
    "requestType": 1, // add patient
    "therapistId": 1234,
    "locationId": 1,
    "patient": {
        "firstName": "Saqib",
        "lastName": "Ahmad",
        "DOB": "08/14/1974",
        "gender": "male",
        "leftArmLength": 32,
        "rightArmLength": 32,
        "handDominance": "right",
        "therapistId": "1234"
    }
};

var mock_addPatientResponseSuccessJSON = {
    "msg": "Patient Added Successfully",
    "status": true,
    "patientId": 1234
};

var mock_addPatientResponseFailedJSON = {
    "msg": "Patient Added Successfully",
    "status": true,
    "patientId": 1234
};

var mock_patientInfoRequestJSON = {
    "requestType": 3, // get patient info for patient 
    "patientId": 1234
};

var mock_patientInfoResponseSuccessJSON = {
    "patient": {
        "patientId": 1234,
        "firstName": "Saqib",
        "lastName": "Ahmad",
        "DOB": "08/14/1974",
        "gender": "male",
        "leftArmLength": 32,
        "rightArmLength": 32,
        "handDominance": "right",
        "therapistId": "1234"
    }
};

var mock_evaluationRequestJSON = {
    "requestType": 1, // indicates whether to record or only test
    "patientId": 23,
    "testId": 2,
    "level": 0, // level for the video
    "position" : 0, // patient sitting or standing
    "weight": 10.5, // weight the patient is holding
    "clientVersion": 1.0,
    "therapistId": 2
};

var mock_evaluationResponseSuccessJSON = {
    "msg": "Running requested Evaluation",
    "status": true,
    "sessionId": 2034 // to be used to ping the server for request status 
};

var mock_evaluationResponseFailedJSON = {
    "msg": "Error message",
    "status": false,
    "errorId": 1000
};

var empty_patientInfoJSON = {
    "patientId": 0,
    "firstName": "",
    "lastName": "",
    "DOB": "01/01/1976",
    "gender": "male",
    "handDominance": "right",
    "therapistId": "1234"
};


var error_groups_data = {
        "patientGroupList" : [
        {
            "patientGroupId" : "--error--",
            "groupName" : "-- Error --"
        }
    ]
};

var error_users_data = {
        "patients": [
        {
            "patientId": "0",
            "firstName": "-- Error --",
            "lastName": "",
            "customPatientId": ""
        }
        ]
};

var users_data = {
    "patientList": [
        {
            "patientId": 1,
            "firstName": "Saqib",
            "lastName": "Ahmad",
            "customPatientId": ""
        },
        {
            "patientId": 1235,
            "firstName": "Anki",
            "lastName": "Nilaturu"
        },
        {
            "patientId": 1236,
            "firstName": "Anki1",
            "lastName": "Nilaturu"
        },
        {
            "patientId": 1237,
            "firstName": "Anki2",
            "lastName": "Nilaturu"
        },
        {
            "patientId": 1238,
            "firstName": "Anki3",
            "lastName": "Nilaturu"
        },
        {
            "patientId": 1239,
            "firstName": "Anki4",
            "lastName": "Nilaturu"
        }
    ]
};
