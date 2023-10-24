document.addEventListener("DOMContentLoaded", function () {
  var mensajeError = document.querySelector(".mensajeError");

  if (mensajeError) {
    mensajeError.classList.add(
      "animate__animated",
      "animate__fadeInDown",
      "animate__faster"
    );
    setTimeout(function () {
      mensajeError.classList.add("animate__fadeOutUp");
      setTimeout(function () {
        mensajeError.style.display = "none";
      }, 500);
    }, 2000);
  }
});
