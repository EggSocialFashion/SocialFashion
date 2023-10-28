document.addEventListener("DOMContentLoaded", function () {
    var likeButtons =  document.querySelectorAll(".like-button"); 
       likeButtons.forEach(function(button){ 
        var likesState = button.getAttribute("data-likes");
        likesState = (likesState === "true");

        if (likesState) {
            button.classList.add("active");
        } else {
            button.classList.add("desactive");
        }

        button.addEventListener("click", function() {
            likesState = !likesState; // Toggle the likes state

            if (likesState) {
                button.classList.toggle("active");
               
            } else {
                button.classList.toggle("desactive");
    
            }
        });
    });
});
