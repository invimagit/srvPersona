package co.com.invima.maestro.service.srvPersona.repository;


import co.com.invima.maestro.modeloTransversal.entities.persona.PersonaDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonaRepository extends JpaRepository<PersonaDAO, Integer>, JpaSpecificationExecutor<PersonaDAO> {

    @Procedure("dbo.USP_PersonaPermisoEmpresa_I")
    String guardarPersonaPermisos(String json_IN);

    @Query("SELECT persona FROM PersonaDAO persona " +
            "WHERE persona.numeroDocumento =:numeroDocumento " +
            "AND persona.idTipoDocumento =:idTipoDocumento")
    PersonaDAO consultarSiExistePersona(@Param("numeroDocumento") String numeroDocumento,@Param("idTipoDocumento") Integer idTipoDocumento);

    @Procedure("dbo.USP_ConsultarTodosEmpresaRolPersona_S")
    String consultarTodosEmpresaRolPersona ();

    @Procedure("dbo.USP_Direccion_U")
    String actualizarDireccion(String json);

    @Query("SELECT empresa.razonSocial FROM EmpresaDAO empresa WHERE empresa.id=:idEmpresa")
    String buscarNombreEmpresa(@Param("idEmpresa") Integer idEmpresa);
}
