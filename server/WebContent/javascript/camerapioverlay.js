window.camerapioverlay = {};

(function(){
	var camerapioverlay = {};
	var can = null;
	var ctx = null;
	var w;
	var h;
	
	function setCanvas(c){
		can = c;
		w = can.width = 170;
		h = can.height = 170;
	}
	
	function setContext(c){
		ctx = c;
	}
	
	camerapioverlay.setCanvas = setCanvas;
	camerapioverlay.setContext = setContext;
	
	function clearCanvas(){
		ctx.clearRect(0,0,w,h);
	}
	
	function update_faces(image_data){
		if(image_data == undefined || image_data.faces == undefined){
			return;
		}
		ctx.strokeStyle = "red";
		
		for(var i = 0; i < image_data.faces.length; i++){
			var face = image_data.faces[i];
			ctx.strokeRect(face.x,face.y,face.w,face.h);
		}	
	}
	
	function update_movement(image_data){
		if(image_data == undefined || image_data.movements == undefined){
			return;
		}
		ctx.strokeStyle = "black";
		
		for(var i = 0; i < image_data.movements.length; i++){
			var movement = image_data.movements[i];
			ctx.strokeRect(movement.x,movement.y,movement.w,movement.h);
		}	
	}
	
	camerapioverlay.update_faces = update_faces;
	camerapioverlay.update_movement = update_movement;
	camerapioverlay.clearCanvas = clearCanvas;
	
	window.camerapioverlay = camerapioverlay;
})();