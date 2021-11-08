package co.com.invima.maestro.service.srvPersona.service;

import co.com.invima.maestro.modeloTransversal.dto.generic.GenericResponseDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;

public interface IPersonaService {

    ResponseEntity<GenericResponseDTO> crearPersona(String persona);

    ResponseEntity<GenericResponseDTO> consultarSiExistePersona(String numeroDocumento, Integer idTipoDocumento);

    ResponseEntity<GenericResponseDTO> consultarTodosEmpresaRolPersona(String correoPersona, Integer idEmpresa);

    ResponseEntity<GenericResponseDTO> actualizarDireccion(String genericRequestDTO);

    Integer enviarCorreoRoles (String correoUsuario, JSONObject personaJson, String nombreEmpresa);

    ResponseEntity<GenericResponseDTO> guardarPersonasPermisos(String personas) throws ParseException;
}
