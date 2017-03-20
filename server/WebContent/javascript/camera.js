/*
 * CameraPi Team © 2016
 * Functions to serve the visual rendering purpose of the camera.
 */

// Verify if we are in "live" page
// In order to start the camera-related
// logic.
if($(".page-live").length == 1){
    init_camera_interface();
}

function init_camera_interface(){
	var can = $(".camerapi-canvas")[0];
	var ctx = can.getContext("2d");
	camerapioverlay.setCanvas(can);
	camerapioverlay.setContext(ctx);
	
    displayCameraTime();
    
    function displayCameraTime()
    {
        var currentTime,
            timeout;
        
        currentTime = getCurrentTime();
        $('.camera-time')[0].innerHTML = currentTime;
        
        setTimeout(displayCameraTime, 500);
    }
    
    function getCurrentTime()
    {
        var timeToday,
            hour,
            minute,
            second;
        
        timeToday = new Date();
        
        hour = timeToday.getHours();
        minute = timeToday.getMinutes();
        second = timeToday.getSeconds();
        
        minute = checkTime(minute);
        second = checkTime(second);
        
        timeToday = hour + ":" + minute + ":" + second;
        
        return timeToday;
    }
    
    function checkTime(i) {
        if (i < 10) {i = "0" + i};  // add zero in front of numbers < 10
        return i;
    }
    
    var cam = $(".camera-preview img")[0];
    
    // Force camera update each 150 ms
    setInterval(function(){
    	$.get("camera?action=get-last", function(data){
    		var data = JSON.parse(data);
    		// Next url is relative to html path
    		cam.src = "photos/" + data.filename;
    		camerapioverlay.clearCanvas();
    		camerapioverlay.update_faces(data.jsonData);
    		camerapioverlay.update_movement(data.jsonData);
    	});
    },150);
}
