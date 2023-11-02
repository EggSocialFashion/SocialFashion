package proyecto.socialfashion.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import proyecto.socialfashion.Entidades.Reporte;

@Repository
public interface ReporteRepositorio extends JpaRepository<Reporte, String> {
    @Query("SELECT r FROM Reporte r WHERE r.idObjeto = :idObjeto")
    public List<Reporte> buscarPorIdObjeto(@Param("idObjeto")String idObjeto);
}
