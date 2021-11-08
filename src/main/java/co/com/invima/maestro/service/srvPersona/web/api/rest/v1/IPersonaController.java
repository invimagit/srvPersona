package co.com.invima.maestro.service.srvPersona.web.api.rest.v1;

import co.com.invima.maestro.modeloTransversal.dto.generic.GenericResponseDTO;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IPersonaController {

    ResponseEntity<GenericResponseDTO> crearPersona(String persona);

    ResponseEntity<GenericResponseDTO> consultarSiExistePersona(String numeroDocumento, Integer idTipoDocumento);

    ResponseEntity<GenericResponseDTO> consultarTodosEmpresaRolPersona(String correoPersona, Integer idEmpresa);

    ResponseEntity<GenericResponseDTO> actualizarDireccion(@RequestBody String genericRequestDTO);

    ResponseEntity<GenericResponseDTO> guardarPersonasPermisos(@RequestBody String personas) throws ParseException;

}
