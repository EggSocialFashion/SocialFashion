document.addEventListener("DOMContentLoaded", function () {
  const categoriaCheckboxes = document.querySelectorAll(".categoria");
  const diseinadoresSelect = document.getElementById("usuarios");
  const swiperContainer = document.querySelector(".mySwiper .swiper-wrapper"); // Contenedor del carrusel
  // Copia de seguridad de todos los elementos originales en el carrusel
  const originalDiseinadores = Array.from(swiperContainer.children);
  // Función para mostrar u ocultar elementos en función de las selecciones
  function filtrarLista() {
    const categoriaSeleccionada = Array.from(categoriaCheckboxes)
      .filter((checkbox) => checkbox.checked)
      .map((checkbox) => checkbox.value);

    const diseinadoresSeleccionados = Array.from(diseinadoresSelect.options)
      .filter((option) => option.selected)
      .map((option) => option.value);

    // Eliminar todos los elementos del carrusel
    while (swiperContainer.firstChild) {
      swiperContainer.removeChild(swiperContainer.firstChild);
    }

    // Filtrar y agregar elementos de nuevo en función de las selecciones
    originalDiseinadores.forEach((diseinador) => {
      const categoria = diseinador.getAttribute("data-tipo");
      const usuario = diseinador.getAttribute("data-usuario");

      const categoriaCoincide =
        categoriaSeleccionada.length === 0 ||
        categoriaSeleccionada.includes(categoria);
      const usuarioCoincide =
        diseinadoresSeleccionados.length === 0 ||
        diseinadoresSeleccionados.includes(usuario);

      if (categoriaCoincide && usuarioCoincide) {
        swiperContainer.appendChild(diseinador.cloneNode(true));
      }
    });

    // Después de filtrar los elementos, re-inicializa el carrusel Swiper
    if (swiper) {
      swiper.destroy();
    }
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
        slideShadows: true,
      },
      loop: true,
    });
  }

  // Agregar controladores de eventos a los elementos de filtro
  categoriaCheckboxes.forEach((checkbox) => {
    checkbox.addEventListener("change", filtrarLista);
  });

  diseinadoresSelect.addEventListener("change", filtrarLista);

  // Inicializar el carrusel al cargar la página
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
      slideShadows: true,
    },
    loop: true,
  });
});