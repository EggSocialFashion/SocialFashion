document.addEventListener("DOMContentLoaded", function () {
  var mensajeError = document.querySelector(".mensajeError");
  // Muestra el mensaje de error
  mensajeError.classList.add(
    "animate__animated",
    "animate__fadeInDown",
    "animate__faster"
  );

  // Espera 5 segundos y luego oculta el mensaje de error
  setTimeout(function () {
    mensajeError.classList.add("animate__fadeOutUp");
    setTimeout(function () {
      mensajeError.style.display = "none";
    }, 300);
  }, 3000);
});
