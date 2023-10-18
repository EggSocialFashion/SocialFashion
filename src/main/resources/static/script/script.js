document.addEventListener("DOMContentLoaded", function () {
  var swiper = new Swiper(".mySwiper", {
    effect: "coverflow",
    grabCursor: true,
    centeredSlides: true,
    slidesPerView: "auto",
    coverflowEffect: {
      rotate: 15,
      stretch: 0,
      depth: 300,
      modifier: 1,
      slideShadows: true
    },
    loop: true
  });

  swiper.on("slideChange", function () {
    // Obtenemos el ID de la publicación asociada a la diapositiva actual
    var currentSlide = document.querySelector(".swiper-slide-active");
    var publicacionId = currentSlide.getAttribute("data-id");

    // Haz algo con el ID de la publicación (por ejemplo, muestra el ID en la consola)
    console.log("ID de la publicación actual:", publicacionId);
  });
});
