document.addEventListener("DOMContentLoaded", function () {
  var swiper = new Swiper(".mySwiper", {
    effect: "coverflow",
    grabCursor: true,
    timeline: true,
    centeredSlides: true,
    slidesPerView: "auto",
    coverflowEffect: {
      rotate: 20,
      stretch: 20,
      depth: 300,
      modifier: 1,
      slideShadows: true
    },
    loop: true
  });
  const index = localStorage.getItem("indexpublicacion");
  localStorage.setItem("indexswiper", swiper.activeIndex);
  if (index) {
    swiper.slideTo(index, 0);
  }
});
