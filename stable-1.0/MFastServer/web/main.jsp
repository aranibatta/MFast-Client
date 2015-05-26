<%-- 
    Document   : index
    Created on : Feb 17, 2014, 4:06:33 PM
    Author     : Saqib
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Kinect Motion and Function Assessment</title>
        <link href="css/redmond/jquery-ui-1.9.2.custom.css" rel="stylesheet">
        <link type="text/css" href="development-bundle/themes/base/jquery.ui.selectmenu.css" rel="stylesheet" />

        <script src="js/jquery-1.8.3.js"></script>
        <script src="js/jquery-ui-1.9.2.custom.js"></script>
        <script type="text/javascript" src="development-bundle/ui/jquery.ui.selectmenu.js"></script>
        <script type="text/javascript" src="mfast.js"></script>

        <script type="text/javascript" src="jqplot/jquery.jqplot.min.js"></script>
        <script type="text/javascript" src="jqplot/plugins/jqplot.barRenderer.min.js"></script>
        <script type="text/javascript" src="jqplot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
        <script type="text/javascript" src="jqplot/plugins/jqplot.pointLabels.min.js"></script>
        <script type="text/javascript" src="jqplot/plugins/jqplot.dateAxisRenderer.min.js"></script>
        <script type="text/javascript" src="jqplot/plugins/jqplot.highlighter.min.js"></script>
        <script type="text/javascript" src="jqplot/plugins/jqplot.cursor.min.js"></script>

        <script>

            var sid = <%= request.getParameter("sid")%>;
            var tid = <%= request.getParameter("tid")%>;
            var tname = "<%= request.getParameter("tname")%>";
            var lid = <%= request.getParameter("lid")%>;
            var lname = "<%= request.getParameter("lname")%>";
            var editingOrAdding = "none";
            
            
            var sessionCommentsMaxChar = 150;
            var patientCommentsMaxChar = 150;

            var sample_graph_data = [
                {
                    date: "01/01/2014",
                    weight: 10
                },
                {
                    date: "01/02/2014",
                    weight: 20
                },
                {
                    date: "01/03/2014",
                    weight: 30
                },
                {
                    date: "01/04/2014",
                    weight: 40
                },
                {
                    date: "01/05/2014",
                    weight: 50
                },
                {
                    date: "01/06/2014",
                    weight: 60
                },
                {
                    date: "01/07/2014",
                    weight: 10
                },
                {
                    date: "01/08/2014",
                    weight: 40
                },
                {
                    date: "01/09/2014",
                    weight: 30
                },
                {
                    date: "01/10/2014",
                    weight: 50
                },
                {
                    date: "01/01/2014",
                    weight: 15
                },
                {
                    date: "01/02/2014",
                    weight: 25
                },
                {
                    date: "01/03/2014",
                    weight: 35
                },
                {
                    date: "01/04/2014",
                    weight: 45
                },
                {
                    date: "01/05/2014",
                    weight: 55
                },
                {
                    date: "01/06/2014",
                    weight: 65
                },
                {
                    date: "01/07/2014",
                    weight: 15
                },
                {
                    date: "01/08/2014",
                    weight: 45
                },
                {
                    date: "01/09/2014",
                    weight: 35
                },
                {
                    date: "01/10/2014",
                    weight: 55
                }
            ];


            var fake = 1;
            var simulationSessionID = -1;
            var simulationPatientID = -1;
            
            var selectedTestName = "";
            var currentPatientId = -1;
            
            // Get IP of the client (TODO: should fix this within server)
            var dcAppServerIp = "localhost";
            $.get("http://ipinfo.io", function (response) {
                dcAppServerIp = response.ip;
            }, "jsonp");
         

            function getSelectedGroupID() {
                return $("#groups_list").val();
            }

            function getSelectedGroupName() {
                return $("#groups_list").find(":selected").text();
            }

            function getSelectedPatientID() {
                currentPatientId = $("#users_list").val();
                return $("#users_list").val();
            }

            function getSelectedPatientName() {
                return $("#users_list").find(":selected").text();
            }
            
            function getPatientPosition() {
                var value = $("#position_radioset :radio:checked").attr('id');
                if (value === "position_sitting") {
                    return 0;
                }
                return 1;
            }
            
            function getSelectedLevel() {
                var value = $("#level_radioset :radio:checked").attr('id');
                if (value === "level_low") {
                    return 0;
                }
                return 1;
            }
            
            function getSelectedLevelString() {
                var value = $("#level_radioset :radio:checked").attr('id');
                var level = "";
                if (value === "level_low") {
                    level = "Low";
                }
                else {
                    level = "High";
                }
                
                return level;
            }

            function getPositionString() {
                var value = $("#position_radioset :radio:checked").attr('id');
//                alert("Event = " + value);
                if (value === "position_sitting") {
                    return "man_sitting";
                }
                return "man_standing";
            }

            function getWeight() {
                return $("#weight_slider").slider('value');
            }

            function getMode() {
                if ($("#testmode_radioset :radio:checked").attr('id') === "test_mode_on") {
                    return "Test";
                }
                return "Record";
            }

            function isTestMode() {
                return getMode() === "Test";
            }
            
            function isShowResultsOn() {
                if ($("#results_display_button").is(':checked'))
                    return 1;
                else
                    return 0;
            }

            function dateAfter(date, amount) {
                var tmpDate = new Date(date);
                tmpDate.setDate(tmpDate.getDate() + amount)
                return tmpDate;
            }
            ;

            function getFormattedDate(date) {
                var year = date.getFullYear();
                var month = (1 + date.getMonth()).toString();
                month = month.length > 1 ? month : '0' + month;
                var day = date.getDate().toString();
                day = day.length > 1 ? day : '0' + day;
                return month + "/" + day + "/" + year;
            }

            function checkProgressAndClose() {

            }
            function getEnteredPatientInfo() {
                var gender = "female";
                if ($("#patient_gender :radio:checked").attr('id') === "gender_male") {
                    gender = "male";
                }
                var domi = "left";
                if ($("#patient_hand :radio:checked").attr('id') === "hand_right") {
                    domi = "right";
                }
                var reqType = 1; // adding
                if (editingOrAdding === "editing") {
                    reqType = 4;
                }
                var patientInfo = {
                    loginSessionId: sid,
                    requestType: reqType,
                    therapistId: tid,
                    locationId: lid,
                    patient: {
                        patientId: getSelectedPatientID(),
                        firstName: $("#patient_first_name").val(),
                        lastName: $("#patient_last_name").val(),
                        DOB: $("#patient_dob").val(),
                        customPatientId: $("#patient_custom_id").val(),
                        gender: gender,
                        handDominance: domi,
                        comments: $("#patient_comments").val(),
                        patientGroupId: parseFloat($("#patient_group").val()),
                        rightStern2acJointLength: parseFloat($("#patient_right_snjoint").val()),
                        leftStern2acJointLength: parseFloat($("#patient_left_snjoint").val()),
                        rightUpperArmLength: parseFloat($("#patient_right_upper_arm").val()),
                        leftUpperArmLength: parseFloat($("#patient_left_upper_arm").val()),
                        height: parseFloat($("#patient_height").val()),
                        weight: parseFloat($("#patient_weight").val()),
                        rightForearmLength: parseFloat($("#patient_right_forearm").val()),
                        leftForearmLength: parseFloat($("#patient_left_forearm").val()),
                        rightArmLength: parseFloat($("#patient_right_arm").val()),
                        leftArmLength: parseFloat($("#patient_left_arm").val()),
                    }
                };
                return patientInfo;
            }

            function setPatientInfo(pinfo) {
                //alert(JSON.stringify(pinfo));
                if ("male" === pinfo.gender) {
                    $("#gender_male").attr("checked", "checked");
                } else {
                    $("#gender_female").attr("checked", "checked");
                }
                $("#patient_gender").buttonset('refresh');

                if ("left" === pinfo.handDominance) {
                    $("#hand_left").attr("checked", "checked");
                } else {
                    $("#hand_right").attr("checked", "checked");
                }
                $("#patient_hand").buttonset('refresh');

                $("#patient_first_name").val(pinfo.firstName);
                $("#patient_last_name").val(pinfo.lastName);
                $("#patient_dob").val(pinfo.DOB);
                $("#patient_custom_id").val(pinfo.customPatientId);
                $("#patient_comments").val(pinfo.comments);
                $("#patient_group").selectmenu("value", pinfo.patientGroupId);
                $("#patient_right_snjoint").val(pinfo.rightStern2acJointLength);
                $("#patient_left_snjoint").val(pinfo.leftStern2acJointLength);
                $("#patient_right_upper_arm").val(pinfo.rightUpperArmLength);
                $("#patient_left_upper_arm").val(pinfo.leftUpperArmLength);
                $("#patient_height").val(pinfo.height);
                $("#patient_weight").val(pinfo.weight);
                $("#patient_right_forearm").val(pinfo.rightForearmLength);
                $("#patient_left_forearm").val(pinfo.leftForearmLength);
                $("#patient_right_arm").val(pinfo.rightArmLength);
                $("#patient_left_arm").val(pinfo.leftArmLength);
                
                // updates to patient comments character counting:
                $('#patient_comments_remaining').text(patientCommentsMaxChar - $('#patient_comments').val().length);
                $('#patient_comments').keyup(function() {
                    var len = this.value.length;
                    if (len >= patientCommentsMaxChar) {
                    this.value = this.value.substring(0, patientCommentsMaxChar);
                    }
                    $('#patient_comments_remaining').text(patientCommentsMaxChar - len);
                });
                $('#patient_comments_max').html(patientCommentsMaxChar);
            }
            
            function refreshButtonLabels() {
                $("#left_button1").html(getMode() + "<br>Left");
                $("#right_button1").html(getMode() + "<br>Right");
                $("#left_button2").html(getMode() + "<br>Left");
                $("#right_button2").html(getMode() + "<br>Right");
                $("#left_button3").html(getMode() + "<br>Left");
                $("#right_button3").html(getMode() + "<br>Right");
                $("#left_button4").html(getMode() + "<br>Left");
                $("#right_button4").html(getMode() + "<br>Right");
            }

            function refreshManImages() {
                var str = getPositionString();
//                alert("Position = " + str);
                $("#image1").attr("src", str + "1.png");
                $("#image2").attr("src", str + "2.png");
                $("#image3").attr("src", str + "3.png");
                $("#image4").attr("src", str + "4.png");//ANKI
            }

            function refreshGroupsList() {
                //alert("Refresh groups");
                showProgressDialog("Loading ...", "Please wait. Loading Groups data.");

                var postData = {
                    "loginSessionId": sid,
                    "therapistId": tid,
                    "locationId" : lid,
                    "requestType": 2
                };

                $.ajax({
                    url: getGroupsListURL,
                    type: 'POST',
                    data: JSON.stringify(postData),
                    contentType: 'text/plain',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        //alert("Refresh groups - " + JSON.stringify(msg));
                        hideProgressDialog();
                        // alert("success " + JSON.stringify(msg));
                        var b = msg['requestStatus'];
                        if (typeof b === "undefined") {
                            b = true;
                        }
                        //alert("b = " + b);

                        if (b) {
                            // alert("success-true");

                            //alert(JSON.stringify(msg));
                            // alert("FixME: refresh groups list");
                            refreshGroupsListSelectMenu(msg);
                        } else {
                            //alert("success-false");
                            refreshGroupsListSelectMenu(error_groups_data);
                            showErrorDialog("Groups", "Error occured.<br>" + msg['msg']);
                        }
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        hideProgressDialog();
                        // alert("error");
                        refreshGroupsListSelectMenu(error_groups_data);
                        showErrorDialog("Groups", "Error occured.<br>" + thrownError);
                    }

                });
            }

            function refreshGroupsListSelectMenu(groups_data) {
                var lp = $("#groups_list_parent");
                lp.empty();
                var s = $("<select id=\"groups_list\" name=\"groups_list\" class=\"ui-button\" style=\"width:150px; height:50px;\"/>");
                var ps = groups_data.groupList;

                for (var i = 0; i < ps.length; i++) {
                    $("<option />", {value: ps[i].patientGroupId, text: ps[i].groupName}).appendTo(s);
                }
                s.appendTo(lp);

                s.selectmenu({
                    change: function() {
                        refreshUsersList();
                    }

                });
                refreshGroupsListSelectMenuForEditDialog(groups_data);
                refreshUsersList();
            }

            function refreshGroupsListSelectMenuForEditDialog(groups_data) {
                var lp = $("#patient_group_parent");
                lp.empty();
                var s = $("<select id=\"patient_group\" name=\"patient_group\" class=\"ui-button\" style=\"width:150px; height:50px;\"/>");
                var ps = groups_data.groupList;

                for (var i = 0; i < ps.length; i++) {
                    $("<option />", {value: ps[i].patientGroupId, text: ps[i].groupName}).appendTo(s);
                }
                s.appendTo(lp);
                s.selectmenu();
            }


            function refreshUsersList() {
                var gid = getSelectedGroupID();
                var gname = getSelectedGroupName();
                showProgressDialog("Loading ...", "Please wait. Loading patients data for group " + gname + ". gid=" + gid);
                var postData = {
                    "loginSessionId": sid,
                    "locationId" : lid,
                    "requestType": 2,
                    "patientGroupId": gid
                };

                $.ajax({
                    url: patientManagerURL,
                    type: 'POST',
                    data: JSON.stringify(postData),
                    contentType: 'text/plain',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        hideProgressDialog();
                        // alert("success " + JSON.stringify(msg));
                        var b = msg['requestStatus'];
                        if (typeof b === "undefined") {
                            b = true;
                        }
                        // alert("b = " + b);

                        if (b) {
                            // alert("success-true");

                            // alert(JSON.stringify(msg));
                            refreshUsersListSelectMenu(msg);
                        } else {
                            // alert("success-false");
                            refreshUsersListSelectMenu(error_users_data);
                            showErrorDialog("Patients", "Error occured.<br>" + msg['msg']);
                        }
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        hideProgressDialog();
                        // alert("error");
                        refreshUsersListSelectMenu(error_users_data);
                        showErrorDialog("Patients", "Error occured.<br>" + thrownError);
                    }

                });



            }
            function refreshUsersListSelectMenu(users_data) {
                var lp = $("#users_list_parent");
                lp.empty();
                var s = $("<select id=\"users_list\" name=\"users_list\" class=\"ui-button\" style=\"width:150px; height:50px;\"/>");
                var ps = users_data.patients;
                var exists = 0;
                // alert("Refreshing userslist....." + $("#users_list"));
                for (var i = 0; i < ps.length; i++) {
                    if (ps[i].firstName && ps[i].lastName)
                    {
                        if (ps[i].customPatientId)
                            $("<option />", {value: ps[i].patientId, text: ps[i].customPatientId + " - " + ps[i].lastName + ", " + ps[i].firstName}).appendTo(s);
                        else
                            $("<option />", {value: ps[i].patientId, text: ps[i].lastName + ", " + ps[i].firstName}).appendTo(s);
                    }
                    else
                    {
                        $("<option />", {value: ps[i].patientId, text: ps[i].customPatientId}).appendTo(s);
                    }
                     // TODO: Maybe use iether first/last name or user id
                    // $("<option />", {value: ps[i].patientId, text: ps[i].firstName + " " + ps[i].lastName}).appendTo(s);
                    
                    //alert("ps[i].patientId " + ps[i].patientId + " currentID:" + currentPatientId);
                    // check if currentPatientId exists on the list, otherwise reset it:
                    if (parseInt(ps[i].patientId) == currentPatientId) exists = 1;
                }
                s.appendTo(lp);
                
               //alert("exists: " + exists + " currentID:" + currentPatientId);
                // select patient by ID or select default
                if (exists > 0 && currentPatientId > 0)
                {
                    //alert("Exists: " + currentPatientId);
                    s.selectmenu();
                    $("#users_list").selectmenu("value", currentPatientId);
                }
                else
                {
                    // reset ID:
                    currentPatientId = -1;
                    s.selectmenu();
                }

            }

            function setSliderTicks(el) {
                var $slider = $(el);
                var max = $slider.slider("option", "max");
                var min = $slider.slider("option", "min");
                var spacing = 100 / (max - min);

                $slider.find('.ui-slider-tick-mark1').remove();
                $slider.find('.ui-slider-tick-mark2').remove();
                for (var i = 0; i <= max - min; i++) {
                    $('<span class="ui-slider-tick-mark1">' + ' ' + '</span>').css('left', (spacing * i) + '%').appendTo($slider);
                    $('<span class="ui-slider-tick-mark2">' + i + '</span>').css('left', (spacing * i) + '%').appendTo($slider);
                }
            }

            function savePatientInfo(pinfo) {
                // alert(JSON.stringify(pinfo));

                $.ajax({
                    url: patientManagerURL,
                    type: 'POST',
                    data: JSON.stringify(pinfo),
                    contentType: 'text/plain',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        // alert(JSON.stringify(msg));
                        if (msg['requestStatus'] === true) {
                            // if patient was added, server returned the new patient id!
                            if (editingOrAdding === "adding") {
                                currentPatientId = msg['patientId'];
                            }
                            refreshUsersList();
                        } else {
                            showErrorDialog("Save Patient Info", "Error occured.<br>" + msg['msg']);
                        }
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        showErrorDialog("Save Patient Info", "Error occured.<br>" + thrownError);
                    }

                });
            }

            function addNewPatient() {
                editingOrAdding = "adding";
                setPatientInfo(empty_patientInfoJSON);
                $("#patient_first_name").val("");
                $("#patient_last_name").val("");
                $("#patient_dob").val("01/01/1976");
                
                var selectedValue = $('#groups_list').val();
                $('#patient_group').val(selectedValue);
                $('#patient_group').selectmenu();
              
                $("#patient_dialog").dialog("option", "title", "Add new Patient");
                $("#patient_dialog").dialog("open");
            }

            function editPatientInfo() {
                var pid = getSelectedPatientID();
                var postData = {
                    "loginSessionId": sid,
                    "requestType": 3, // get patient info for patient 
                    "patientId": pid
                };


                $.ajax({
                    url: patientManagerURL,
                    type: 'POST',
                    data: JSON.stringify(postData),
                    contentType: 'text/plain',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        // alert(JSON.stringify(msg));
                        var b = msg['requestStatus'];
                        if (typeof b === "undefined") {
                            b = true;
                        }

                        if (b) {
                            setPatientInfo(msg);
                            editingOrAdding = "editing";
                            $("#patient_dialog").dialog("option", "title", "Edit Patient Info");
                            $("#patient_dialog").dialog("open");
                        } else {
                            showErrorDialog("Edit Patient Info", "Error occured.<br>" + msg['msg']);
                        }
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        showErrorDialog("Edit Patient Info", "Error occured.<br>" + thrownError);
                    }

                });

            }

            function showProgressDialog(title, text) {
                hideProgressDialog();
                $("#progress_dialog").dialog("option", "title", title);
                $("#progress_dialog_text").html(text);
                $("#progress_dialog").dialog("open");
            }

            function hideProgressDialog() {
                $("#progress_dialog").dialog("close");
            }

            function hideErrorDialog() {
                $("#error_dialog").dialog("close");
            }
            function showErrorDialog(title, text) {
                $("#error_dialog").dialog("option", "title", title);
                $("#error_dialog_text").html(text);
                $("#error_dialog").dialog("open");
            }
            function hideMessageDialog() {
                $("#message_dialog").dialog("close");
            }

            function showMessageDialog(title, text) {
                $("#message_dialog").dialog("option", "title", title);
                $("#message_dialog_text").html(text);
                $("#message_dialog").dialog("open");
            }


            function updateBarGraph(data) {

// alert($("#graph_from").val() + " - " + $("#graph_from").val());
//                var s1 = [1.7, 4.6, 2.0, 7.3];
                // Can specify a custom tick Array.
                // Ticks should match up one for each y value (category) in the series.
//                var ticks = ['01/10/2014', '02/02/2014', '03/03/2014', '04/04/2014'];

                var y_weights = [];
                var x_dates = [];
                var dddd = [];
                for (var i = 0; i < data.length; i++) {
                    // alert(JSON.stringify(data[i]));
                    y_weights.push(data[i].weight);
                    x_dates.push(data[i].date)
                    dddd.push([data[i].date, data[i].weight]);
                }
                $("#patient_chart").empty();
                
/////////////##### default bar graph                
//                var plot1 = $.jqplot('patient_chart', [y_weights], {
//                    // The "seriesDefaults" option is an options object that will
//                    // be applied to all series in the chart.
//                    seriesDefaults: {
//                        renderer: $.jqplot.BarRenderer,
//                        rendererOptions: {fillToZero: true}
//                    },
//                    // Custom labels for the series are specified with the "label"
//                    // option on the series option.  Here a series option object
//                    // is specified for each series.
//                    series: [
//                        {label: ' '}
//                    ],
//                    // Show the legend and put it outside the grid, but inside the
//                    // plot container, shrinking the grid to accomodate the legend.
//                    // A value of "outside" would not shrink the grid and allow
//                    // the legend to overflow the container.
//                    legend: {
//                        show: true,
//                        placement: 'outsideGrid'
//                    },
//                    axes: {
//                        // Use a category axis on the x axis and use our custom ticks.
//                        xaxis: {
//                            renderer: $.jqplot.DateAxisRenderer
//                            //,
//                            //ticks: x_dates
//                        },
//                        // Pad the y axis just a little so bars can get close to, but
//                        // not touch, the grid boundaries.  1.2 is the default padding.
//                        yaxis: {
//                            pad: 1.0,
//                            tickOptions: {formatString: '%dkg&nbsp;&nbsp;&nbsp;&nbsp;'}
//                        }
//                    }
//                });

////////////// ##### With mouse over tool tip        
                var plot1 = $.jqplot('patient_chart', [dddd], {
                    title: '',
                    axes: {
                        xaxis: {
                            renderer: $.jqplot.DateAxisRenderer,
                            tickOptions: {
                                formatString: '%b&nbsp;%#d'
                            }
                        },
                        yaxis: {
                            pad: 1.5,
                            tickOptions: {
                                formatString: '%dkg&nbsp;&nbsp;&nbsp;&nbsp;'
                            }
                        }
                    },
                    highlighter: {
                        show: true,
                        sizeAdjust: 7.5
                    },
                    cursor: {
                        show: false
                    }
                });

            }
            function reloadPatientChart() {

                var postData = {
                    loginSessionId: sid,
                    patientId: getSelectedPatientID(),
                    startDate: $("#graph_from").val(),
                    endDate: $("#graph_to").val(),
                    dataType: 0
                }
                // REMOVE this after testing.
                //alert(JSON.stringify(postData))
                $.ajax({
                    url: pastResultsURL,
                    type: 'POST',
                    data: JSON.stringify(postData),
                    contentType: 'text/plain',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        // alert(JSON.stringify(msg));
                        var b = msg['requestStatus'];
                        if (typeof b === "undefined") {
                            b = true;
                        }

                        if (b) {
                            updateBarGraph(msg['data']);
                        } else {
                            showErrorDialog("Past Results", "Error occured.<br>" + msg['msg']);
                        }
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        showErrorDialog("Past Results", "Error occured.<br>" + thrownError);
                    }

                });

            }

            function showGraphDialog(title, text) {
                // fetch last data.
                var current = new Date();
                $("#graph_from").val(getFormattedDate(dateAfter(current, -(10))));
                $("#graph_to").val(getFormattedDate(current));

                $("#graph_dialog").dialog("option", "title", title);
                $("#graph_dialog").dialog("open");
//                updateBarGraph(sample_graph_data);
                reloadPatientChart();
            }


            function hideMessageDialog() {
                $("#message_dialog").dialog("close");
            }

            function logout() {
                // alert("FixME: Logout");
                // $.cookie(msg['therapistId'] + "-cookie", 0);
                var postData = {
                    "loginSessionId": sid,
                    "locationId" : lid,
                    "therapistId": tid
                };
                $.ajax({
                    url: therapistLogoutURL,
                    type: 'POST',
                    data: JSON.stringify(postData),
                    contentType: 'text/plain',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        window.location = "index.html";
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        window.location = "index.html";
                    }
                });
            }

            function showStartTestDialog(testId) {
                
                if (!isTestMode()) {
                    var testCounter = getPastTestCount(testId) + 1; // must add 1 for current value
                    $("#simstart_dialog").data("testId", testId);
                    $("#simstart_dialog").data("testCounter", testCounter);
                    var level = getSelectedLevelString();
                    //alert(level);
                    var text = "Selected patient: <B>" + getSelectedPatientName() +
                                "</B><BR>Selected test: <B>" + selectedTestName + 
                                "</B><BR>Selected level: <B>" + level +                            
                                "</B><BR>Selected weight: <B>" + getWeight() + " kg</B>" + 
                                "<BR><BR> <h3>Repetition: <B>" + testCounter + "</B></h3>";
                    //alert(text);
                    $("#simstart_dialog_text").html(text);
                    $("#simstart_dialog").dialog("open");
                }
                else {
                    var text = "<B>Test Mode selected. No data will be recorded!</B>";
                    $("#teststart_dialog_text").html(text);
                    $("#teststart_dialog").dialog("open");
                }
            }

            function getPastTestCount(testID) {
                    var postData = {
                    "loginSessionId": sid,
                    "requestType": 2, // indicates we are only requesting number of tests of this type today
                    "patientId": getSelectedPatientID(),
                    "patientName": getSelectedPatientName(),
                    "testId": testID,
                    "level": getSelectedLevel(), // level of the video (hi/low)
                    "position": getPatientPosition(), // patient sitting or standing (hi/low)
                    "weight": getWeight(), // weight the patient is holding
                    "clientVersion": 1.0,
                    "therapistId": tid
                };
                
                var testCountToday = -1;
                //alert("getPastTestCount(): Recording/testing for id= " + $("#users_list").val() + "  Name= " + $("#users_list").find(":selected").text());
                //alert("getPastTestCount(): POST " + JSON.stringify(postData));
                
               // send post request to server
                $.ajax({
                    url: evaluationRequestURL,
                    type: 'POST',
                    data: JSON.stringify(postData),
                    contentType: 'text/plain',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        // alert(JSON.stringify(msg));
                        if (msg['requestStatus'] === true) {
                            testCountToday = msg['testCountToday'];
                            selectedTestName = msg['testName'];
                        } else {
                            showErrorDialog("Data Acquisition", "Error occured.<br>" + msg['msg']);
                        }
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        showErrorDialog("Data Acquisition", "Error occured.<br>" + thrownError);
                    }

                });

                return testCountToday;
            }
            
            function startSimulation(testID) {
                
                var postData = {
                    "loginSessionId": sid,
                    "requestType": isTestMode() ? 0 : 1, // indicates whether to record or only test
                    "patientId": getSelectedPatientID(),
                    "patientName": getSelectedPatientName(),
                    "testId": testID,
                    "level": getSelectedLevel(), // patient sitting or standing (hi/low)
                    "position": getPatientPosition(), // patient sitting or standing (hi/low)
                    "weight": getWeight(), // weight the patient is holding
                    "clientVersion": 1.0,
                    "therapistId": tid,
                    "locationId" : lid,
                    "showResults": isShowResultsOn(),
                    "dcAppServerIp": dcAppServerIp
                };

                //alert("Recording/testing for id= " + $("#users_list").val() + "  Name= " + $("#users_list").find(":selected").text());
                //alert("POST " + JSON.stringify(postData));

                $.ajax({
                    url: evaluationRequestURL,
                    type: 'POST',
                    data: JSON.stringify(postData),
                    contentType: 'text/plain',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        // alert(JSON.stringify(msg));
                        if (msg['requestStatus'] === true) {
                            simulationSessionID = msg['sessionId'];
                            simulationPatientID = msg['patientId'];
                            showProgressDialog("Recording", "Please do not use Browser back buttons, or, Refresh until data acquisition is completed.");
                            setTimeout(checkForEndOfSimulation, 3000);
                        } else {
                            showErrorDialog("Data Acquisition", "Error occured.<br>" + msg['msg']);
                        }
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        showErrorDialog("Data Acquisition", "Error occured.<br>" + thrownError);
                    }

                });

            }


            function checkForEndOfSimulation() {
                //alert("FixME: Check for end of simulation " + fake);


                var postData = {
                    "loginSessionId": sid,
                    "patientId": simulationPatientID,
                    "sessionId": simulationSessionID
                };

                //alert("Recording/testing for id= " + $("#users_list").val() + "  Name= " + $("#users_list").find(":selected").text());
                //alert("POST " + JSON.stringify(postData));

                $.ajax({
                    url: checkSessionStatusURL,
                    type: 'POST',
                    data: JSON.stringify(postData),
                    contentType: 'text/plain',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        // alert(JSON.stringify(msg));
                        var b = msg['requestStatus'];
                        if (typeof b === "undefined") {
                            b = true;
                        }

                        if (b) {
                            if (msg['status'] === true) {
                                simulationEnded();
                            } else {
                                setTimeout(checkForEndOfSimulation, 3000);
                            }
                        } else {
                            showErrorDialog("Data Acquisition Status", "Error occured.<br>" + msg['msg']);
                            setTimeout(checkForEndOfSimulation, 3000);
                        }
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        showErrorDialog("Data Acquisition Status", "Error occured.<br>" + thrownError);
                    }

                });
            }

            function simulationEnded() {
                hideProgressDialog();
                
                if (!isTestMode()) {
                    $("#session_comments").html("");
                    $('#session_comments_remaining').text(sessionCommentsMaxChar);
                    $("#simquestion_dialog").dialog("open");
                }
                else
                {
                    rejectSimulationData();
                }
                
            }

            function rejectSimulationData() {
                var postData = {
                    "loginSessionId": sid,
                    "sessionId": simulationSessionID
                };

                //alert("Recording/testing for id= " + $("#users_list").val() + "  Name= " + $("#users_list").find(":selected").text());
                //alert("POST " + JSON.stringify(postData));
                $("#simquestion_dialog").dialog("close");

                $.ajax({
                    url: deleteEvaluationSessionURL,
                    type: 'POST',
                    data: JSON.stringify(postData),
                    contentType: 'text/plain',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        // alert(JSON.stringify(msg));
                        var b = msg['requestStatus'];
                        if (typeof b === "undefined") {
                            b = true;
                        }

                        if (b) {
                            //
                        } else {
                            showErrorDialog("Data Acquisition Delete", "Error occured.<br>" + msg['msg']);

                        }
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        showErrorDialog("Data Acquisition Status", "Error occured.<br>" + thrownError);
                    }

                });
            }
            
            function updateNotes() {
                
                var postData = {
                    "loginSessionId": sid,
                    "sessionId": simulationSessionID,
                    "sessionNotes": $("#session_comments").val()
                };

                //alert("Recording/testing for id= " + $("#users_list").val() + "  Name= " + $("#users_list").find(":selected").text());
                //alert("POST " + JSON.stringify(postData));

                $.ajax({
                    url: updateEvaluationSessionURL,
                    type: 'POST',
                    data: JSON.stringify(postData),
                    contentType: 'text/plain',
                    dataType: 'json',
                    async: false,
                    success: function(msg) {
                        // alert(JSON.stringify(msg));
                        var b = msg['requestStatus'];
                        if (typeof b === "undefined") {
                            b = true;
                        }

                        if (b) {
                            //
                        } else {
                            showErrorDialog("Data Acquisition Status", "Error occured.<br>" + msg['msg']);

                        }
                    },
                    error: function(xhr, ajaxOptions, thrownError) {
                        showErrorDialog("Data Acquisition Status", "Error occured.<br>" + thrownError);
                    }

                });

            }            

            function saveSimulationData() {

                updateNotes();
                $("#simquestion_dialog").dialog("close");
            }
            
            
            function startVideoPlayback() {
                window.open("video.html", "window name", "window settings");
                return false;
            }
            

            $(function() {
                $(document).ready(function() {
                    // if($.cookie(msg['therapistId'] + "-cookie") == 0) {
                    //    
                    // }
                    refreshGroupsList();
                });

                $("#error_message").hide();
                $("#edit_button").button();
                $("#last_report_button").button();
                $("#add_user_button").button();
                $("#logout_button").button();
                $("#groups_list").selectmenu();
                $("#users_list").selectmenu();

                $("#position_radioset").buttonset();
                $("#level_radioset").buttonset();
                $("#testmode_radioset").buttonset();

                $("#weight").html('Weight: <span style="border:0; color:#f6931f; font-weight:bold;">0.0 kg</span>');

                $("#weight_slider").slider({
                    range: "min",
                    value: 0.0,
                    min: 0.0,
                    max: 5.0,
                    step: 0.1,
                    create: function(event, ui) {
                        setSliderTicks(event.target);
                    },
                    slide: function(event, ui) {
                        setSliderTicks(event.target);
                        $("#weight").html('Weight: <span style="border:0; color:#f6931f; font-weight:bold;">' + ui.value.toFixed(1) + ' kg</span>');
                    }
                });

                $("#left_button1").button();
                $("#right_button1").button();
                $("#left_button2").button();
                $("#right_button2").button();
                $("#left_button3").button();
                $("#right_button3").button();
                $("#left_button4").button();
                $("#right_button4").button();
                
                $("#play_video_button").button();

                $("#accordion1").accordion({collapsible: false});
                $("#accordion2").accordion({collapsible: false});
                $("#accordion3").accordion({collapsible: false});
                $("#accordion4").accordion({collapsible: false});
                $("#accordion5").accordion({collapsible: false});

                $("#patient_gender").buttonset();
                $("#patient_left_upper_arm").spinner({
                    min: 1,
                    max: 30,
                    value: 8
                });
                $("#patient_right_upper_arm").spinner({
                    min: 1,
                    max: 30,
                    value: 8
                });
                
                $("#patient_left_arm").spinner({
                    min: 1,
                    max: 30,
                    value: 8
                });
                $("#patient_right_arm").spinner({
                    min: 1,
                    max: 30,
                    value: 8
                });                

                $("#patient_height").spinner({
                    min: 24,
                    max: 100,
                    value: 32
                });
                $("#patient_weight").spinner({
                    min: 10,
                    max: 500,
                    value: 30
                });
                $("#patient_dob").datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showOn: "button",
                    buttonImage: "calendar.png",
                    buttonImageOnly: true
                });
                $("#patient_hand").buttonset();

                $("#patient_left_forearm").spinner({
                    min: 1,
                    max: 30,
                    value: 8
                });
                $("#patient_right_forearm").spinner({
                    min: 1,
                    max: 30,
                    value: 8
                });
                $("#patient_left_snjoint").spinner({
                    min: 1,
                    max: 30,
                    value: 8
                });
                $("#patient_right_snjoint").spinner({
                    min: 1,
                    max: 30,
                    value: 8
                });
                $("#patient_group").selectmenu();

                $("#progress_dialog").dialog({
                    autoOpen: false,
                    width: 400,
                    modal: true,
                    closeOnEscape: false,
                    draggable: false,
                    dialogClass: "no-close",
                    buttons: [
                        {
                            text: "Please Wait",
                            click: function() {
                                checkProgressAndClose();
                            }
                        },
                        {
                            text: "Cancel",
                            click: function() {
                                $(this).dialog("close");
                                //checkProgressAndClose();
                            }
                        }                        
                    ]
                });
                $("#error_dialog").dialog({
                    autoOpen: false,
                    width: 400,
                    modal: true,
                    closeOnEscape: false,
                    draggable: false,
                    buttons: [
                        {
                            text: " OK ",
                            click: function() {
                                $(this).dialog("close");
                            }
                        }
                    ]
                }).parent().addClass("ui-state-error");

                $("#message_dialog").dialog({
                    autoOpen: false,
                    width: 400,
                    modal: true,
                    closeOnEscape: false,
                    draggable: false,
                    buttons: [
                        {
                            text: " OK ",
                            click: function() {
                                $(this).dialog("close");
                            }
                        }
                    ]
                });

                $("#simstart_dialog").dialog({
                    autoOpen: false,
                    width: 400,
                    modal: true,
                    closeOnEscape: false,
                    draggable: false,
                    buttons: [
                        {
                            text: " Start ",
                            click: function() {
                                 var testId = $(this).data("testId");
                                 var testCounter = $(this).data("testCounter");
                                 //alert("testId = " + testId + ", testCounter = " + testCounter + " (Remove later)");
                                startSimulation(testId);
                                $(this).dialog("close");
                            }
                        },
                        {
                            text: "Cancel",
                            click: function() {
                                //rejectSimulationData();
                                $(this).dialog("close");
                            }
                        }
                    ]
                });
                
                $("#teststart_dialog").dialog({
                    autoOpen: false,
                    width: 400,
                    modal: true,
                    closeOnEscape: false,
                    draggable: false,
                    buttons: [
                        {
                            text: "Start Test",
                            click: function() {
                                 var testId = $(this).data("testId");
                                 //alert("testId = " + testId + ", testCounter = " + testCounter + " (Remove later)");
                                startSimulation(testId);
                                $(this).dialog("close");
                            }
                        },
                        {
                            text: "Cancel",
                            click: function() {
                                //rejectSimulationData();
                                $(this).dialog("close");
                            }
                        }
                    ]
                });

                $("#simquestion_dialog").dialog({
                    autoOpen: false,
                    width: 400,
                    modal: true,
                    closeOnEscape: false,
                    draggable: false,
                    buttons: [
                        {
                            text: " Save ",
                            click: function() {
                                saveSimulationData();
                                $(this).dialog("close");
                            }
                        },
                        {
                            text: "Reject",
                            click: function() {
                                rejectSimulationData();
                                $(this).dialog("close");
                            }
                        }
                    ]
                });

                // session notes:
                $('#session_comments_remaining').text(sessionCommentsMaxChar);
                $('#session_comments').keyup(function() {
                    var len = this.value.length;
                    if (len >= sessionCommentsMaxChar) {
                        this.value = this.value.substring(0, sessionCommentsMaxChar);
                    }
                    $('#session_comments_remaining').text(sessionCommentsMaxChar - len);
                });
                $('#session_comments_max').html(sessionCommentsMaxChar);

                $("#patient_dialog").dialog({
                    autoOpen: false,
                    width: 600,
                    modal: true,
                    closeOnEscape: false,
                    draggable: false,
                    buttons: [
                        {
                            text: "Cancel",
                            click: function() {
                                $(this).dialog("close");
                            }
                        },
                        {
                            text: " Save ",
                            click: function() {
                                var pinfo = getEnteredPatientInfo();
                                $(this).dialog("close");
                                savePatientInfo(pinfo);
                            }
                        }
                    ]
                });

                $("#graph_from").datepicker({
                    defaultDate: "+1w",
                    changeMonth: true,
                    numberOfMonths: 3,
                    showOn: "button",
                    buttonImage: "calendar.png",
                    buttonImageOnly: true,
                    onClose: function(selectedDate) {
                        $("#graph_to").datepicker("option", "minDate", selectedDate);
                    }
                });
                $("#graph_to").datepicker({
                    defaultDate: "+1w",
                    changeMonth: true,
                    numberOfMonths: 3,
                    showOn: "button",
                    buttonImage: "calendar.png",
                    buttonImageOnly: true,
                    onClose: function(selectedDate) {
                        $("#graph_from").datepicker("option", "maxDate", selectedDate);
                    }
                });
                $("#graph_refresh_button").button();
                $("#graph_refresh_button").click(function() {
                    reloadPatientChart();
                });

                $("#graph_dialog").dialog({
                    autoOpen: false,
                    width: 800,
                    height: 600,
                    modal: true,
                    closeOnEscape: true,
                    draggable: false,
                    buttons: [
                        {
                            text: "Close",
                            click: function() {
                                $(this).dialog("close");
                            }
                        }
                    ]
                });


                $("#logout_button").click(function(event) {
                    logout();
                });
                $("#last_report_button").click(function(event) {
                    showGraphDialog(getSelectedPatientName(), "");
                });

                $("#edit_button").click(function(event) {
                    // $("#patient_dialog").dialog("open");
                    editPatientInfo();
                });
                $("#add_user_button").click(function(event) {
                    // $("#patient_dialog").dialog("open");
                    addNewPatient();
                });
                /////// Events
                $("#testmode_radioset").change(function() {
                    refreshButtonLabels();
                });
                
                $("#level_radioset").change(function() {
                    //refreshManImages();
                });                

                $("#position_radioset").change(function() {
                    refreshManImages();
                });

                $("#left_button1").click(function(event) {
                    showStartTestDialog(1);
                    //startSimulation(1);
                });
                $("#right_button1").click(function(event) {
                    showStartTestDialog(0);
                    //startSimulation(0);
                });

                $("#left_button2").click(function(event) {
                    showStartTestDialog(3);
                    //startSimulation(3);
                });
                $("#right_button2").click(function(event) {
                    showStartTestDialog(2);
                    //startSimulation(2);
                });

                $("#left_button3").click(function(event) {
                    showStartTestDialog(5);
                    //startSimulation(5);
                });
                $("#right_button3").click(function(event) {
                    showStartTestDialog(4);
                    //startSimulation(4);
                });

                $("#left_button4").click(function(event) {
                    showStartTestDialog(6);
                    //startSimulation(6);
                });
                $("#right_button4").click(function(event) {
                    showStartTestDialog(7);
                    //startSimulation(7);
                });
                
                $("#play_video_button").click(function(event) {
                    startVideoPlayback();
                });
                
                // setTimeout(refreshUsersList, 3000);
                // refreshUsersList();
                refreshButtonLabels();
            });
        </script>

        <style>

            #accordion1 .ui-icon { display: none; }
            #accordion2 .ui-icon { display: none; }
            #accordion3 .ui-icon { display: none; }
            #accordion4 .ui-icon { display: none; }

            .ui-datepicker-trigger { position:relative;top:4px;left:-2px; }

            .ui-slider-tick-mark1{
                display:inline-block;
                width:2px;
                background:gray;
                height:2px;
                position:absolute;
                top:21px;
            }

            .no-close .ui-dialog-titlebar-close {
                display: none;
            }

            .ui-slider-tick-mark2{
                display:inline-block;
                width:2px;
                background:red;
                height:0px;
                position:absolute;
                top:23px;
                margin-left:-2px;
            }

            fieldset { border:0; }
            label,select,.ui-select-menu { float: left; margin-right: 10px; }
            select { width: 200px; }
            .wrap ul.ui-selectmenu-menu-popup li a { font-weight: bold; }

            body{
                font: 62.5% "Trebuchet MS", sans-serif;
                margin: 10px;
            }
            table {
                border-width: 1px 1px 1px 1px;
                border-style: none;
                border-spacing: 0px;
            }
            .demoHeaders {
                margin-top: 2em;
            }
            #dialog-link {
                padding: .4em 1em .4em 20px;
                text-decoration: none;
                position: relative;
            }
            #dialog-link span.ui-icon {
                margin: 0 5px 0 0;
                position: absolute;
                left: .2em;
                top: 50%;
                margin-top: -8px;
            }
            #icons {
                margin: 0;
                padding: 0;
            }
            #icons li {
                margin: 2px;
                position: relative;
                padding: 4px 0;
                cursor: pointer;
                float: left;
                list-style: none;
            }
            #icons span.ui-icon {
                float: left;
                margin: 0 4px;
            }
            .fakewindowcontain .ui-widget-overlay {
                position: absolute;
            }
            .user_button  { width: 100px; }
            .five_select {width: 125px; }

            #tbl_wrap { height: 100%; width: 100%; padding: 0; margin: 0; }
            #td_wrap { vertical-align: middle; text-align: center; }

            .ui-slider .ui-slider-handle {
                cursor:default;
                height:2em;
                position:absolute;
                width:1em;
                z-index:2;
            }

        </style>
        <style type="text/css">
            <!--
            html, body, #tbl_wrap {  width: 100%; padding: 0; margin: 0; }
            #td_wrap { vertical-align: middle; text-align: center; }
            -->
        </style>
    </head>
    <body style="background:#3D99C8;">     
        <table id="tbl_wrap">
            <tbody>
                <tr>
                    <td id="td_wrap">
                        <!-- START: Anything between these wrapper comment lines will be centered -->
                        <div style="background:#3D99C8;border: 0px solid black; display: inline-block;">
                            <table style="width:1000px;" >
                                <tbody>
                                    <tr>
                                        <td style="background:#3D99C8; padding: 1px 1px 1px 1px;" colspan=2>
                                            <table width="100%"><tr>
                                                    <td width="20%"> <img width="150px" height="120px" src="logo.png"> </td>
                                                    <td width="60%" style="vertical-align: bottom; color:#FFFFFF"><h1>MFAsT - Kinect Motion and Function Assessment</h1></td>
                                                    <td width="20%" align="right" valign="bottom"> 
                                                        <table>   
                                                            <tr><td style="text-align: right;"><h2 class="demoHeaders" style="color: #FBFCFC; margin-bottom: 1px;"><%=request.getParameter("tname")%> (<%=request.getParameter("lname")%>)</h2></td></tr>
                                                            <tr><td style="text-align: right;"><button style="margin-bottom: 12px;" id="logout_button">Logout / Change</button></td></tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="background:#3D99C8; padding: 10px 10px 10px 10px;" valign="top" >
                                            <table style="width: 100%;" >
                                                <tbody>
                                                    <tr>
                                                        <td style="text-align:left; padding-bottom: 0px;">
                                                            <h3 class="demoHeaders" style="color:#FBFCFC; text-align: left; margin-left: 4px; margin-top: 1px; margin-bottom: 3px">Groups:</h3>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td style="text-align:right; padding-bottom: 15px;">
                                                            <div id="groups_list_parent" name="groups_list_parent">
                                                                <select id="groups_list" class="ui-button" style="width:180px; height:50px;">
                                                                    <option value="--error--">Loading...</option>
                                                                </select>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td style="text-align:left; padding-bottom: 0px;">
                                                            <h3 class="demoHeaders" style="color:#FBFCFC; text-align: left; margin-left: 4px; margin-top: 1px; margin-bottom: 3px">Patients:</h3>
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td style="text-align:right">
                                                            <div id="users_list_parent" name="users_list_parent">
                                                                <select id="users_list" class="ui-button, five_select" style="width:180px; height:50px;">
                                                                    <option value="--error-">Loading ...</option>
                                                                </select>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">
                                                            <button class="user_button" id="edit_button">Edit Patient</button>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">
                                                            <button class="user_button" id="last_report_button">Last
                                                                Report</button>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">
                                                            <button class="user_button" id="add_user_button">Add
                                                                Patient</button>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </td>


                                        <td colspan=2 style="background:#fff4de; padding: 10px 10px 10px 10px;">

                                            <table  style="width:100%">
                                                <tr> <!-- options -->
                                                    <td  colspan=2 >
                                                        <table style="width:100%">
                                                            <tr style="padding: 10px 10px 10px 10px;">
                                                                <td style="width:13%; vertical-align:middle; padding: 10px 10px 10px 10px; ">
                                                                    <h3 class="demoHeaders" style="margin-top: 10px;text-align:left;">Level:</h3>
                                                                </td>                                                                
                                                                <td   style="width:20%; vertical-align:middle;padding: 10px 10px 10px 10px;">
                                                                    <div id="level_radioset">
                                                                        <input type="radio" id="level_low" name="level_low_high" checked="checked"><label for="level_low">Low</label>
                                                                        <input type="radio" id="level_high" name="level_low_high" checked="checked"><label for="level_high">High</label>
                                                                    </div>
                                                                </td>
                                                                <td style="width:13%; vertical-align:middle; padding: 10px 10px 10px 10px; ">
                                                                    <h3 class="demoHeaders" style="margin-top: 10px;text-align:left;">Position:</h3>
                                                                </td>
                                                                <td   style="width:20%; vertical-align:middle;padding: 10px 10px 10px 10px;">
                                                                    <div id="position_radioset">
                                                                        <input type="radio" id="position_sitting" name="position_sit_stand" checked="checked"><label for="position_sitting">Sitting</label>
                                                                        <input type="radio" id="position_standing" name="position_sit_stand" ><label for="position_standing">Standing</label>
                                                                    </div>
                                                                </td>
                                                                <td style="width:13%; vertical-align:middle;padding: 10px 10px 10px 10px;">
                                                                    <h3 class="demoHeaders" style="margin-top: 10px;">Test Mode:</h3>
                                                                </td>
                                                                <td style="width:20%; vertical-align:middle;padding: 10px 10px 10px 10px;">
                                                                    <div id="testmode_radioset">
                                                                        <input type="radio" id="test_mode_on" name="test_mode"><label for="test_mode_on">On</label>
                                                                        <input type="radio" id="test_mode_off" name="test_mode" checked="checked"><label for="test_mode_off">Off</label>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td style="vertical-align:middle; margin-top:100px;padding: 10px 10px 10px 10px;">
                                                                    <h3 class="demoHeaders" id="weight" style="margin-top: 1px;text-align:left;">Weight: <span style="border:0; color:#f6931f; font-weight:bold;">1.0 kg</span></h3>
                                                                </td>
                                                                <td colspan=3 style="padding: 10px 10px 10px 10px;">	
                                                                    <div id="weight_slider" style="margin-bottom:15px;padding-bottom:10px;"></div>
                                                                </td>
                                                                 <td style="width:10%; vertical-align:middle;padding: 10px 10px 10px 10px;">
                                                                </td>
                                                                <td style="width:40%; vertical-align:top;padding: 10px 10px 10px 10px;">
                                                                    <button class="user_button" id="play_video_button" style="width:160px;">Instructional Video</button>
                                                                </td>                                                                    
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr> <!-- options -->
                                                <tr><td>
                                                        <table style="width:100%;">
                                                            <tr>
                                                                <td>
                                                                    <div id="accordion1">
                                                                        <h3>Reachable WS</h3>
                                                                        <div>
                                                                            <table width="100%">	
                                                                                <tr>
                                                                                    <td valign="bottom" width="30%"><button class="user_button" id="right_button1">Record<br>Right</button></td>
                                                                                    <td valign="bottom"><img id="image1" src="man_sitting1.png" width="120px" height="120px"></img></td>
                                                                                    <td valign="bottom" width="30%"><button class="user_button" id="left_button1">Record<br>Left</button></td>
                                                                                </tr>
                                                                            </table>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                                <td>
                                                                    <div id="accordion2">
                                                                        <h3>Functional WS</h3>
                                                                        <div>
                                                                            <table width="100%">	
                                                                                <tr>
                                                                                    <td valign="bottom"><button class="user_button" id="right_button2">Record<br>Right</button></td>
                                                                                    <td valign="bottom"><img id="image2" src="man_sitting2.png" width="120px" height="120px"></img></td>
                                                                                    <td valign="bottom"><button class="user_button" id="left_button2">Record<br>Left</button></td>
                                                                                </tr>
                                                                            </table>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>

                                                            <tr>
                                                                <td>
                                                                    <div id="accordion3">
                                                                        <h3>Shoulder Rotation</h3>
                                                                        <div>
                                                                            <table width="100%">	
                                                                                <tr>
                                                                                    <td valign="bottom"><button class="user_button" id="right_button3">Record<br>Right</button></td>
                                                                                    <td valign="bottom"><img id="image3" src="man_sitting3.png" width="120px" height="120px"></img></td>
                                                                                    <td valign="bottom"><button class="user_button" id="left_button3">Record<br>Left</button></td>
                                                                                </tr>
                                                                            </table>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                                <td>
                                                                    <div id="accordion4">
                                                                        <h3>Free ROM</h3>
                                                                        <div>
                                                                            <table width="100%">	
                                                                                <tr>
                                                                                    <td valign="bottom"><button class="user_button" id="right_button4">Record<br>Right</button></td>
                                                                                    <td valign="bottom"><img id="image4" src="man_sitting4.png" width="120px" height="120px"></img></td>
                                                                                    <td valign="bottom"><button class="user_button" id="left_button4">Record<br>Left</button></td>
                                                                                </tr>
                                                                            </table>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td></tr>
                                                    <tr>
                                                        <td align="right" style="vertical-align:top;">
                                                            <table style="width:100%">
                                                                <tbody>
                                                                    <tr>
                                                                        <td style="width:80%"> </td>
                                                                        <td style="text-align:right; vertical-align:middle; float: right;">
                                                                            <h3>
                                                                                <label for="results" style="font-size: 10px; position: relative; top: 2px;">Show results:</label>
                                                                                <div style="width:100px; padding: 0px 0px 0px 0px;">
                                                                                    <input type="checkbox" id="results_display_button" name="results" style="vertical-align:top">
                                                                                </div>
                                                                            </h3>
                                                                        </td>
                                                                    </tr>
                                                                </tbody>
                                                            </table>
                                                        </td>
                                                    </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </tbody>
                                </tbody>
                            </table>
                        </div>
                                                                            
                        <!-- END: Anything between these wrapper comment lines will be centered --> 
                    </td>
                </tr>
            </tbody>
        </table>


        <!-- EDIT DIALOG -->                                                
        <div id="patient_dialog" title="Patient Info">
            <table style="background:#FBFCFC; padding: 1px 1px 1px 1px;" width="100%">
                <tr>
                    <td>
                        <table style="background:#FBFCFC; padding: 1px 1px 1px 1px;"  width="100%">
                            <tr >
                                <td style="padding: 5px 5px 5px 5px; font-weight:bold;" width="25%">Patient ID:</td>
                                <td colspan="3" style="padding: 5px 25px 5px 5px;" width="75%"><input style="width:100%; height:100%" id="patient_custom_id" type="text" /></td>
                            </tr>

                            <tr >
                                <td style="padding: 5px 5px 5px 5px;" width="25%">First Name:</td>
                                <td style="padding: 5px 25px 5px 5px;" width="25%"><input id="patient_first_name" type="text" /></td>
                                <td style="padding: 5px 5px 5px 25px;" width="25%">Last Name:</td>
                                <td style="padding: 5px 5px 5px 5px;" width="25%"><input id="patient_last_name" type="text" /></td>
                            </tr>

                            <tr style="background:#FBFCFC; padding: 1px 1px 1px 1px;">
                                <td style="padding: 5px 5px 5px 5px;">Height (cm):</td>
                                <td style="padding: 5px 25px 5px 5px;"><input id="patient_height" value="160" /></td>
                                <td style="padding: 5px 5px 5px 25px;">Weight (kg):</td>
                                <td style="padding: 5px 5px 5px 5px;"><input id="patient_weight" value="60"/></td>
                            </tr>
                            <tr style="background:#FBFCFC; padding: 1px 1px 1px 1px;">
                                <td style="padding: 5px 5px 5px 5px;">Gender:</td>
                                <td style="padding: 5px 25px 5px 5px;">
                                    <div id="patient_gender">
                                        <input type="radio" id="gender_male" name="gender" checked="checked"><label for="gender_male">Male</label>
                                        <input type="radio" id="gender_female" name="gender" ><label for="gender_female">Female</label>
                                    </div>
                                </td>
                                <td style="padding: 5px 5px 5px 25px;">Hand Dominance:</h3></td>
                                <td style="padding: 5px 5px 5px 5px;">
                                    <div id="patient_hand">
                                        <input type="radio" id="hand_right" name="hand_dom" ><label for="hand_right">Right</label>
                                        <input type="radio" id="hand_left" name="hand_dom" checked="checked"><label for="hand_left">Left</label>
                                    </div>
                                </td>
                            </tr>
                            <tr style="background:#FBFCFC; padding: 1px 1px 1px 1px;">
                                <td style="padding: 5px 5px 5px 5px;font-weight:bold;">Date of Birth:</td>
                                <td style="padding: 5px 5px 5px 5px;"><div><input id="patient_dob" value="08/14/1976"/></div></td>
                                <td style="padding: 5px 5px 5px 25px;">Group:</td>
                                <td style="padding: 5px 5px 5px 5px;">
                                    <div id="patient_group_parent" name="patient_group_parent">
                                        <select id="patient_group" class="ui-button" style="width:150px; height:50px;">
                                            <option value="--error--">Loading ...</option>
                                        </select>
                                    </div>

                                </td>
                            </tr>
                            <tr >
                                <td style="padding: 5px 5px 5px 5px; vertical-align:top " width="25%">Comments/Notes:</td>
                                <td colspan="3" style="padding: 5px 25px 5px 5px;" width="75%"><textarea style="width:100%; height:100%;"id="patient_comments" name="patient_comments" rows="3"></textarea></td>
                            </tr>
                            <tr>
                                <td style="padding: 5px 5px 5px 5px; vertical-align:top;" colspan=4>
                                    <div style="text-align:right;">(<span id="patient_comments_remaining"> </span> of <span id ="patient_comments_max"> </span>)
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>


                <tr>
                    <td style="padding: 25px 1px 1px 1px;">
                        <div id="accordion5">
                            <h3>Measurements</h3>
                            <div>
                                <table width="100%" style="padding: 1px 1px 1px 1px;">	
                                    <tr style="padding: 5px 5px 5px 5px;">
                                        <td></td>
                                        <td>Left</td>
                                        <td>Right</td>
                                    </tr>
                                    <tr style="padding: 5px 5px 5px 5px;">
                                        <td style="padding: 5px 5px 5px 5px;">Sternal Notch to AC Joint (cm):</td>
                                        <td style="padding: 5px 5px 5px 5px;"><input id="patient_left_snjoint" /></td>
                                        <td style="padding: 5px 5px 5px 5px;"><input id="patient_right_snjoint" /></td>
                                    </tr>                                    
                                    <tr style="padding: 5px 5px 5px 5px;">
                                        <td style="padding: 5px 5px 5px 5px;">Upper Arm (cm):</td>
                                        <td style="padding: 5px 5px 5px 5px;"><input id="patient_left_upper_arm" /></td>
                                        <td style="padding: 5px 5px 5px 5px;"><input id="patient_right_upper_arm" /></td>
                                    </tr>
                                    <tr style="padding: 5px 5px 5px 5px;">
                                        <td style="padding: 5px 5px 5px 5px;">Forearm (cm):</td>
                                        <td style="padding: 5px 5px 5px 5px;"><input id="patient_left_forearm" /></td>
                                        <td style="padding: 5px 5px 5px 5px;"><input id="patient_right_forearm" /></td>
                                    </tr>
                                    <tr><td colspan="3"><hr></td></tr>                                    
                                    <tr style="padding: 5px 5px 5px 5px;">
                                        <td style="padding: 5px 5px 5px 5px;">Arm Length (cm):</td>
                                        <td style="padding: 5px 5px 5px 5px;"><input id="patient_left_arm" /></td>
                                        <td style="padding: 5px 5px 5px 5px;"><input id="patient_right_arm" /></td>
                                    </tr>                                    
                                </table>
                            </div>
                        </div>
                    </td>

                </tr>
            </table>
        </div>


        <!-- PROGRESS DIALOG -->
        <div id="progress_dialog" title="Data acquisition in progress">
            <div style="text-align:center;"><p id="progress_dialog_text">TODO</p></div>
            <div style="text-align:center;"><img src="ajax-loader.gif"></div>
        </div>        

        <!-- MESSAGE DIALOG -->
        <div id="message_dialog" title="Message">
            <div style="text-align:center;"><p id="message_dialog_text">TODO</p></div>
        </div>                                              

        <!-- ERROR DIALOG -->
        <div id="error_dialog" title="Error">
            <div style="text-align:center;"><p id="error_dialog_text">TODO</p></div>
        </div>                                              

        <!-- SIMQuestion DIALOG -->
        <div id="simquestion_dialog" title="Data Acquisition Done">
            <div style="text-align:center;"><p id="simquestion_dialog_text">Data acquisition completed successfully. 
                    <br>Would you like to <b>Save</b> or <b>Reject</b> this session data?</p></div>
            <div style="text-align:left;"><p>Session notes: </p></div>
            <div style="text-align:center;">    
            <textarea style="width:100%; height:100%;"id="session_comments" name="session_comments" rows="3"></textarea>
            <p style="text-align:right;">(<span id="session_comments_remaining"> </span> of <span id ="session_comments_max"> </span>)</p>
            </div>
           
        </div>                                              

        <!-- SIMStart DIALOG -->
        <div id="simstart_dialog" title="Data Acquisition Start">
            <div style="text-align:center;"><p id="simstart_dialog_text">This is what you are starting.... <br> Ready to start?</p></div>
        </div>   
        
        <!-- Test Start DIALOG -->
        <div id="teststart_dialog" title="Acquisition Test Start">
            <div style="text-align:center;"><p id="teststart_dialog_text">This is what you are starting.... <br> Ready to start?</p></div>
        </div>          
        
        <!-- GRAPH DIALOG -->
        <div id="graph_dialog" title="Message">
            <table style="background:#FBFCFC; padding: 1px 1px 1px 1px;"  width="100%" height="100%">
                <tr>
                    <td style="width:24%;height:100%;background:#3D99C8; padding: 10px 20px 10px 10px;" valign="top" >
                        <table style="width:100%;height:100%;">
                            <tr >
                                <td style="vertical-align: top;">
                                    <table style="width:100%;">
                                        <tr >
                                            <td><h3 class="demoHeaders" style="margin-top: 10px; color:#FBFCFC;">Select Period</h3></td>
                                        </tr>

                                        <tr >
                                            <td >From</td>
                                        </tr>
                                        <tr>
                                            <td ><input id="graph_from" name="graph_from"></td>
                                        </tr>
                                        <tr><td>&nbsp;</td></tr>
                                        <tr>
                                            <td >To</td>
                                        </tr>
                                        <tr>
                                            <td ><input id="graph_to" name="graph_to"></td>

                                        </tr>
                                        <tr><td>&nbsp;</td></tr>
                                        <tr>
                                            <td style="text-align: right"><button id="graph_refresh_button">Reload</button></td>
                                        </tr>
                                    </table>

                                </td>
                            </tr>
                        </table>
                    </td>
                    <td style="width:100%;"> 
                        <div style="margin-left: 20px;padding-right: 10px;" id="patient_chart"></div>
                    </td>
                </tr>
            </table>
        </div>   
        

    </body>
</html>