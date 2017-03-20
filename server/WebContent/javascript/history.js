// Verify if we are in "history" page
// In order to start the history-related
// logic.

$(document).ready(function(){
	if($(".page-history").length == 1){
	    init_history();
	}
});
var faces_data_test = {
    count: 3,
    faces: [
        {x:0,y:0,w:40,h:40},
        {x:240,y:60,w:70,h:70},
        {x:200,y:190,w:60,h:60},
    ]
};
function init_history(){
	var picture = $(".current-picture img");
	var can = $(".camerapi-canvas")[0];
	var ctx = can.getContext("2d");
	var current = 0;
	var date_data = {};
	
	camerapioverlay.setCanvas(can);
	camerapioverlay.setContext(ctx);
	
	function init_controls(){
		$(".qs-minus-1f").click(previous.bind(this, 1));
		$(".qs-plus-1f").click(next.bind(this, 1));
		
		$(".qs-minus-5f").click(previous.bind(this, 5));
		$(".qs-plus-5f").click(next.bind(this, 5));
		
		$(".qs-minus-20f").click(previous.bind(this, 20));
		$(".qs-plus-20f").click(next.bind(this, 20));
	}
	
	function update_faces(image_data){
		camerapioverlay.update_faces(image_data);
	}
	
	function update_movement(image_data){
		camerapioverlay.update_movement(image_data);
	}
	
	function switch_to_photo(id){
		var id = parseInt(id);
		current = id;
		picture[0].src = generate_url(id);
		
		// Go to top
		window.scrollTo(0,160);
		
		$(".img-title").html(generate_time_str(id));
		if( date_data[current] == undefined ||
			date_data[current].jsonData == undefined ){
			return;
		}

		var facesData = null;
		
		imageData = date_data[current].jsonData;
		
		camerapioverlay.clearCanvas();
		update_faces(imageData);
		update_movement(imageData);
		
	}
	
	function switch_to_current(fromQuicksel){
		if(typeof fromQuicksel == "undefined"){
			var fromQuicksel = false;			
		}
		// Limit bounds
		if(current < 0){
			current = date_data.length - 1;
		} else if (current >= date_data.length){
			current = 0;
		}
		
		if(typeof date_data[current] != "undefined"){
			var hour = new Date(date_data[current].date).getHours();
			// When hour is changed through these buttons
			if(fromQuicksel){
				// Update hour to new value
				if($(".history-hour").val() != hour){
					$(".history-hour").val(hour);
					update_view();
				}
			}
		}
		
		switch_to_photo(current);
		update_view();
	}
	
	function next(qty){
		current += qty;
		switch_to_current(true);
	}
	
	function previous(qty){
		current -= qty;
		switch_to_current(true);
	}
	
	function generate_url(id){
		if(typeof date_data[id] == "undefined"){
			return "";
		}
		var url = "photos/" + date_data[id].filename;
		return url;
	}
	
	function update_view(){
		// Clear thumbs
		$(".current-thumbs").html("");
		var hour = $(".history-hour").val();
		
		var thumbs = generate_thumbs_for_hour(hour);
		
		$(".current-thumbs").append(thumbs);
	}
	
	function generate_thumbs_for_hour(hour){
		var this_hour = [];
		var div = $("<div class='thumbs'>");
		
		// Build this_hour array
		for(var i = 0; i < date_data.length; i++){
			var date = new Date(date_data[i].date);
			
			if(date.getHours() == hour){
				this_hour.push(i);
			}
		}
		
		// Create 10 thumbnails
		for(var i = 0; i < 10; i++){
			var id = this_hour[Math.floor(i/10 * this_hour.length)];
			
			if(typeof date_data[id] == "undefined"){
				continue;
			}
			
			var thumb = generate_thumb(id);
			
			div.append(thumb);
		}
		
		return div;
	}
	
	function generate_thumb(id){
		var url = generate_url(id);
		
		var dateStr = generate_time_str(id);
		
		// Create one thumb
		var thumb = $(
			"<div class='thumb' data-id='"+id+"'>" +
			"<span class='thumb-title'>" + dateStr + "</span>" +
			"<img src='"+url+"'>" +
			"</div>");
		
		// Activate click
		thumb.click(function(e){
			// Do not go to page
			e.preventDefault();
			// Copy link
			switch_to_photo($(this).attr("data-id"));
		});
		
		return thumb;
	}
	
	// Add a zero if num < 10
	function pad_one_zero(n){
		if(n < 10){
			return "0" + n;
		}
		return n;
	}
	
	function generate_time_str(id){
		if(typeof date_data[id] == "undefined"){
			return "No data for selected time";
		}
		
		var date = new Date(date_data[id].date);
		var dateStr = 
			pad_one_zero(date.getHours()) + "h" +  
			pad_one_zero(date.getMinutes()) + "m" + 
			pad_one_zero(date.getSeconds()) + "s";
		
		return dateStr;
	}
	
	function init_form(){
		// Many browsers still do not support <input type='date'>
		var picker = new Pikaday({
	        field: $(".history-datepicker")[0]
	    });
		
		picker.setDate(new Date());
		
		// Initial setup
		switch_to_date(picker.getDate());
		
		$(".history-datepicker").change(function() {
			switch_to_date(picker.getDate());
		});
		
		$(".history-hour").change(function(){
			switch_to_current();
			update_view();
		});
	}
	
	function create_hours_thumbnails(){
		var container = $(".hours-thumb-container");
		// Clear old ones
		container.html("");
		
		// Create for all hours
		for(var i = 0; i < 24; i++){
			container.append("<h3>"+(i < 10? "0" + i: i )+"h00</h3>");
			var thumbs = generate_thumbs_for_hour(i);
			container.append(thumbs);
		}
	}
	
	function switch_to_date(date){
		var date_param = date.getTime();
		$.get("camera?action=list-date&date=" + date_param, function(data){
			var data = data == ""? "{}": data;
			date_data = JSON.parse(data);
			if(data.length != 0){			
				switch_to_current();
				update_view();
				create_hours_thumbnails();
			}
		});
	}
	
	init_controls();
	init_form();
}
