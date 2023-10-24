document.addEventListener("DOMContentLoaded", function () {
  var mensajeExito = document.querySelector(".mensajeExito");

  if (mensajeExito) {
    mensajeExito.classList.add(
      "animate__animated",
      "animate__fadeInDown",
      "animate__faster"
    );

    setTimeout(function () {
      mensajeExito.classList.add("animate__fadeOutUp");
      setTimeout(function () {
        mensajeExito.style.display = "none";
      }, 500);
    }, 2000);
  }
});
