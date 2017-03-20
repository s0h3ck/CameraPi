enable_mv_buttons();

var ROOT = "cameraPi_server";

function enable_mv_buttons(){
    var locked = false;
    var mv_buttons = document.querySelectorAll(".mv-buttons button");
    
    function mv_btn_click(e){
        var el = e.target;
        locked = true;
        
        var action = el.getAttribute("data-action");
        
        $.get("camera?action="+action, function(data){
            console.log(data);
        });
    }
    
    for(var i = 0; i < mv_buttons.length; i++){
        mv_buttons[i].addEventListener("click", mv_btn_click);
    }
}
