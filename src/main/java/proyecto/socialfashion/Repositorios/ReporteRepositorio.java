package proyecto.socialfashion.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import proyecto.socialfashion.Entidades.Reporte;

@Repository
public interface ReporteRepositorio extends JpaRepository<Reporte, String> {

}
