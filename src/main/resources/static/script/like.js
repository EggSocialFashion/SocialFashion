

//document.addEventListener("DOMContentLoaded", function () {
//    var publicacionesElements = document.querySelectorAll(".swiper-slide");
//
//    publicacionesElements.forEach(function (publicacionElement) {
//        var publicacionData = JSON.parse(publicacionElement.getAttribute("data-publicacion"));
//        var likesData = JSON.parse(publicacionElement.getAttribute("data-likes"));
//        cont = 0;
//        var likeButtonContainer = document.createElement("div");
//        likeButtonContainer.className = "like-buttons";
//        for (var i = 0; i < publicacionData.length; i++) {
//            cont = 0;
//            for (var j = 0; j < likesData.length; j++) {
//                if (publicacionData[i].idPublicacion === likesData[j].publicacion.idPublicacion) {
//                    if (likesData[j].estado === true) {
//                        var likeButtonHtml = `
//                        <button type="submit" class="like-button">
//                           <svg id="corazon" xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24">
//                  <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z " class="heart-icon" />
//              </svg>
//                        </button>
//                    `;
//                        var contenidoGeneradoElement = document.getElementById(`botonLike`);
//                        contenidoGeneradoElement.innerHTML += likeButtonHtml;
//                        cont++;
//                        break;
//                    } else {
//                        var likeButtonHtml = `
//                        <button type="submit" class="like-button">
//                            <svg id="corazon" xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24">
//                         <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z " class="heart-icon" />
//                        </svg>
//                        </button>
//                        var contenidoGeneradoElement = document.getElementById(`botonLike`);
//                        contenidoGeneradoElement.innerHTML += likeButtonHtml;
//                        cont++;
//                        break;
//                    }
//                }
//            }
//            if (cont === 0) {
//                var likeButtonHtml = `
//                        <button type="submit" class="like-button">
//                            <svg id="corazon" xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24">
//                  <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z " class="heart-icon" />
//              </svg>
//                        </button>
//                    `;
//                var contenidoGeneradoElement = document.getElementById(`botonLike`);
//                contenidoGeneradoElement.innerHTML += likeButtonHtml;
//
//
//            }
//
//        }
//
//    });
//});
