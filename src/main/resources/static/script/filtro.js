document.addEventListener("DOMContentLoaded", function () {
    const categoriaCheckboxes = document.querySelectorAll(".categoria");
    const diseinadoresSelect = document.getElementById("usuarios");
    const diseinadores = document.querySelectorAll(".swiper-slide");

    // Función para mostrar u ocultar elementos en función de las selecciones
    function filtrarLista() {
      const categoriaSeleccionada = Array.from(categoriaCheckboxes)
        .filter((checkbox) => checkbox.checked)
        .map((checkbox) => checkbox.value);

      const diseinadoresSeleccionados = Array.from(diseinadoresSelect.options)
        .filter((option) => option.selected)
        .map((option) => option.value);

      diseinadores.forEach((diseinador) => {
        const categoria = diseinador.getAttribute("data-tipo");
        const usuario = diseinador.getAttribute("data-usuario");

        const categoriaCoincide = categoriaSeleccionada.length === 0 || categoriaSeleccionada.includes(categoria);
        const usuarioCoincide = diseinadoresSeleccionados.length === 0 || diseinadoresSeleccionados.includes(usuario);

        if (categoriaCoincide && usuarioCoincide) {
          diseinador.style.display = "block";
        } else {
          diseinador.style.display = "none";
        }
      });
    }

    // Agregar controladores de eventos a los elementos de filtro
    categoriaCheckboxes.forEach((checkbox) => {
      checkbox.addEventListener("change", filtrarLista);
    });

    diseinadoresSelect.addEventListener("change", filtrarLista);
  });