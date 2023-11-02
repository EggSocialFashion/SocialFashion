document.addEventListener("DOMContentLoaded", function () {
  var likeButtons = document.querySelectorAll(".like-button");
  likeButtons.forEach(function (button, index) {
    var publicacionId = button.getAttribute("data-publicacion-id");
    var usuarioId = button.getAttribute("data-usuario-id");
    var corazonIcon = button.querySelector("#corazon-icon");
    // Hacer una solicitud AJAX para verificar si el usuario ha dado like
    fetch(
      "/likes/checkLike?publicacionId=" +
        publicacionId +
        "&usuarioId=" +
        usuarioId
    )
      .then((response) => response.json())
      .then((liked) => {
        if (liked) {
          localStorage.setItem("indexpublicacion", index);
          corazonIcon.innerHTML =
            '<svg xmlns="http://www.w3.org/2000/svg" width="48" fill=red height="48" viewBox="0 0 24 24"><path class="heart-icon-liked" d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" class="heart-icon" /></svg>';
        } else {
          corazonIcon.innerHTML =
            '<svg xmlns="http://www.w3.org/2000/svg" width="48" fill=white height="48" viewBox="0 0 24 24"><path class="heart-icon" d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" class="heart-icon" /></svg>';
        }
      });
    button.addEventListener("click", function (event) {
      // Realizar la acción de dar/quitar like
      fetch(
        "/likes/GenerandoLikes/toggleLike?publicacionId=" +
          publicacionId +
          "&usuarioId=" +
          usuarioId
      )
        .then((response) => response.json())
        .then((liked) => {
          if (liked) {
            localStorage.setItem("indexpublicacion", index);
            corazonIcon.innerHTML =
              '<svg xmlns="http://www.w3.org/2000/svg" width="48" height="48"   fill="none" viewBox="0 0 24 24"><path class="heart-icon-liked" d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" class="heart-icon" /></svg>';
          } else {
            corazonIcon.innerHTML =
              '<svg xmlns="http://www.w3.org/2000/svg" width="48" height="48"   fill="none" viewBox="0 0 24 24"><path class="heart-icon" d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" class="heart-icon" /></svg>';
          }
        });
    });
  });
});