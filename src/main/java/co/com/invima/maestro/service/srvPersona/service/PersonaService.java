package co.com.invima.maestro.service.srvPersona.service;


import co.com.invima.maestro.modeloTransversal.dto.correo.CorreoDTO;
import co.com.invima.maestro.modeloTransversal.dto.generic.GenericResponseDTO;
import co.com.invima.maestro.modeloTransversal.dto.persona.PersonaDTO;
import co.com.invima.maestro.modeloTransversal.entities.persona.PersonaDAO;
import co.com.invima.maestro.service.srvPersona.commons.ConsumeEndpoints;
import co.com.invima.maestro.service.srvPersona.commons.RespuestaUsuario;
import co.com.invima.maestro.service.srvPersona.commons.Utils;
import co.com.invima.maestro.service.srvPersona.commons.converter.PersonaConverter;
import co.com.invima.maestro.service.srvPersona.repository.IPersonaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.*;


@Service
@Slf4j
public class PersonaService implements IPersonaService {


    private final IPersonaRepository iPersonaRepository;
    private final ModelMapper modelMapper;

    private final PersonaConverter personaConverter;
    private static JSONParser jsonParser = new JSONParser();
    private ConsumeEndpoints consumeEndpoints;
    private Utils utils;


    @Autowired
    public PersonaService(PersonaConverter personaConverter, ModelMapper modelMapper, IPersonaRepository iPersonaRepository,ConsumeEndpoints consumeEndpoints, Utils utils) {

        this.personaConverter = personaConverter;
        this.modelMapper = modelMapper;
        this.iPersonaRepository = iPersonaRepository;
        this.consumeEndpoints = consumeEndpoints;
        this.utils = utils;
    }

    /**
     * @author Gustavo Ortiz
     */
    @Override
    public ResponseEntity<GenericResponseDTO> crearPersona(String persona) {
                try{
                    JSONObject personaJson = (JSONObject) jsonParser.parse(persona);

                    String documentoAverificar = personaJson.get("numeroDocumento").toString();

                    String idTipoDocumento = personaJson.get("idTipoDocumento").toString();

                    ResponseEntity<GenericResponseDTO> respuestaConsulta = consultarSiExistePersona(documentoAverificar, Integer.parseInt(idTipoDocumento));

                    String idEmpresa =personaJson.get("idEmpresa").toString(); //sacar el id de la empresa

                    String nombreEmpresa = iPersonaRepository.buscarNombreEmpresa(Integer.parseInt(idEmpresa)); //consultar el nombre de la empresa

                    String correoUsuario = personaJson.get("correoElectronico").toString();
                    if(respuestaConsulta.getBody().getObjectResponse() == null){ //la persona no existe
//-------------------------------------GUARDAR PERSONA--------------------------------------------------------

                        ResponseEntity<GenericResponseDTO> solicitudCreacion = consumeEndpoints.crearPersonaYUsuario(persona);

                        if (solicitudCreacion.getBody().getObjectResponse().equals(false)){ //si el usuario ya esta registrado

                            return new ResponseEntity<>(GenericResponseDTO.builder()
                                    .message("este correo ya esta asignado a un usuario")
                                    .objectResponse(null)
                                    .statusCode(HttpStatus.BAD_REQUEST.value())
                                    .build(), HttpStatus.BAD_REQUEST);

                        }else{
                            //se obtiene el mensaje luego de guardar la persona con su usuario
                            Object mensaje = solicitudCreacion.getBody().getObjectResponse();
                            RespuestaUsuario respuestaUsuario = new RespuestaUsuario();
                            modelMapper.map(mensaje, respuestaUsuario);



                            //se arma el json para asignar permisos a la persona
                            JSONObject permisosPersonaEmpresa = new JSONObject();
                            permisosPersonaEmpresa.put("idEmpresa", personaJson.get("idEmpresa"));
                            permisosPersonaEmpresa.put("idPersona", Integer.parseInt(respuestaUsuario.getPersona()));
                            permisosPersonaEmpresa.put("idRolPersona", personaJson.get("idRolPersona"));
                            permisosPersonaEmpresa.put("registarInformacion", personaJson.get("registarInformacion"));
                            permisosPersonaEmpresa.put("firmar", personaJson.get("firmar"));
                            permisosPersonaEmpresa.put("pagar", personaJson.get("pagar"));
                            permisosPersonaEmpresa.put("consultar", personaJson.get("consultar"));

                            // se guardan los permisos de la persona
                            String respuestaPermisos = iPersonaRepository.guardarPersonaPermisos(permisosPersonaEmpresa.toString());

                            JSONObject jsonRespuestaPermisos = (JSONObject) jsonParser.parse(respuestaPermisos);
                            String codigoPermisos = jsonRespuestaPermisos.get("respuesta").toString();
                            JSONObject jsonCodigoPermisos = (JSONObject) jsonParser.parse(codigoPermisos);
                            if (jsonCodigoPermisos.get("codigo").equals("00")){

                                enviarCorreoRoles(correoUsuario, personaJson, nombreEmpresa);
                                return new ResponseEntity<>(GenericResponseDTO.builder()
                                        .message("guardado exitoso")
                                        .objectResponse("ok")
                                        .statusCode(HttpStatus.OK.value())
                                        .build(), HttpStatus.OK);
                            }else{
                                return new ResponseEntity<>(GenericResponseDTO.builder()
                                        .message("error al guardar los permisos de la persona")
                                        .objectResponse(jsonCodigoPermisos.get("mensaje"))
                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                        .build(), HttpStatus.BAD_REQUEST);
                            }
                        }

                    }else{
                        //si la persona ya existe-------------------------------------------
                        Object personaEncontrada = respuestaConsulta.getBody().getObjectResponse();
                        PersonaDTO personaDTO = new PersonaDTO();
                        modelMapper.map(respuestaConsulta.getBody().getObjectResponse(), personaDTO);

                        //mapear el json para asignar permisos a la persona--------------------------------
                        JSONObject permisosPersonaEmpresa = new JSONObject();
                        permisosPersonaEmpresa.put("idEmpresa", personaJson.get("idEmpresa"));
                        permisosPersonaEmpresa.put("idPersona", personaDTO.getId());
                        permisosPersonaEmpresa.put("idRolPersona", personaJson.get("idRolPersona"));
                        permisosPersonaEmpresa.put("descripcionRolPersona", personaJson.get("descripcionRolPersona"));
                        permisosPersonaEmpresa.put("registrarInformacion", personaJson.get("registrarInformacion"));
                        permisosPersonaEmpresa.put("firmar", personaJson.get("firmar"));
                        permisosPersonaEmpresa.put("pagar", personaJson.get("pagar"));
                        permisosPersonaEmpresa.put("consultar", personaJson.get("consultar"));

                        //se guardan los permisos
                        String respuestaPermisos = iPersonaRepository.guardarPersonaPermisos(permisosPersonaEmpresa.toString());

                        JSONObject jsonRespuestaPermisos = (JSONObject) jsonParser.parse(respuestaPermisos);
                        String codigoPermisos = jsonRespuestaPermisos.get("respuesta").toString();
                        JSONObject jsonCodigoPermisos = (JSONObject) jsonParser.parse(codigoPermisos);

                        if (jsonCodigoPermisos.get("codigo").equals("00")){ //si fue exitoso el guardado de permisos

                            Integer confimacionCorreo = enviarCorreoRoles(correoUsuario, personaJson, nombreEmpresa);
                            if (confimacionCorreo.equals(0)){
                                return new ResponseEntity<>(GenericResponseDTO.builder()
                                        .message("guardado de permisos y envio de correo exitoso")
                                        .objectResponse("ok")
                                        .statusCode(HttpStatus.OK.value())
                                        .build(), HttpStatus.OK);
                            }else{
                                return new ResponseEntity<>(GenericResponseDTO.builder()
                                        .message("guardado de permisos exitoso pero ocurrio un error al enviar correo")
                                        .objectResponse(null)
                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                        .build(), HttpStatus.BAD_REQUEST);
                            }

                        }else{
                            String cadenaCompleta = jsonCodigoPermisos.get("mensaje").toString();

                            if (cadenaCompleta.matches(".*ERROR_NUMBER 515.*")){
                                return new ResponseEntity<>(GenericResponseDTO.builder()
                                        .message("error asignandole permisos a " + personaDTO.getPrimerNombre() + " " + personaDTO.getPrimerApellido() + ", la persona no esta registrada.")
                                        .objectResponse(jsonCodigoPermisos.get("mensaje"))
                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                        .build(), HttpStatus.BAD_REQUEST);
                            }else if (cadenaCompleta.matches(".*ERROR_NUMBER 2627.*")){
                                return new ResponseEntity<>(GenericResponseDTO.builder()
                                        .message("la persona "+ personaDTO.getPrimerNombre()+" "+ personaDTO.getPrimerApellido() +" ya esta registrada con esta empresa y este rol")
                                        .objectResponse(jsonCodigoPermisos.get("mensaje"))
                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                        .build(), HttpStatus.BAD_REQUEST);
                            }else{
                                return new ResponseEntity<>(GenericResponseDTO.builder()
                                        .message("error guardando la persona con los permisos")
                                        .objectResponse(jsonCodigoPermisos.get("mensaje"))
                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                        .build(), HttpStatus.BAD_REQUEST);
                            }


                        }
                    }

                }catch (Exception e){
                    log.error(e.getMessage());
                    e.printStackTrace();
                    return new ResponseEntity<>(GenericResponseDTO.builder()
                            .message("Error guardando la persona:  " + "error al guardar, recuerde enviar todos los campos" +
                                    " especificados y que el correo electronico debe de ser unico")
                            .objectResponse(e.getMessage())
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build(), HttpStatus.BAD_REQUEST);
                }
    }


    /**
     * @author Gustavo Ortiz
     */
    @Override
    public ResponseEntity<GenericResponseDTO> consultarSiExistePersona(String numeroDocumento, Integer idTipoDocumento) {
        try{
            PersonaDAO persona = iPersonaRepository.consultarSiExistePersona(numeroDocumento, idTipoDocumento);
            if (persona == null){
                return new ResponseEntity<>(GenericResponseDTO.builder()
                        .message("no se encontraron personas")
                        .objectResponse(null)
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build(), HttpStatus.NOT_FOUND);
            }else{
                PersonaDTO personaDTO = modelMapper.map(persona, PersonaDTO.class);
                return new ResponseEntity<>(GenericResponseDTO.builder()
                        .message("persona encontrada")
                        .objectResponse(personaDTO)
                        .statusCode(HttpStatus.OK.value())
                        .build(), HttpStatus.OK);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(GenericResponseDTO.builder()
                    .message("Error consultando la persona:  " + e.getMessage())
                    .objectResponse(null)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> consultarTodosEmpresaRolPersona(String correoPersona, Integer idEmpresa) {
        Connection conn;
        String procedimiento = "dbo.USP_ConsultarTodosEmpresaRolPersona_S(?,?,?)";
        try{
            JSONParser parser = new JSONParser();


            utils.cargarDriver();
            conn = utils.realizarConexion();
            CallableStatement cStmt = conn.prepareCall("{call " + procedimiento + "}");
            String datoEntrada1 = "correoPersona_IN";
            String datoEntrada2 = "idEmpresa_IN";
            cStmt.setString(datoEntrada1, correoPersona);
            cStmt.setInt(datoEntrada2, idEmpresa);
            cStmt.registerOutParameter("json_OUT", Types.LONGVARCHAR);


            cStmt.execute();
            String personas = cStmt.getString("json_OUT");
            JSONObject json = (JSONObject) parser.parse(personas);
            //String respuesta = json.get("respuesta").toString();
            //JSONObject respJson = (JSONObject) parser.parse(respuesta);
            String mensaje = json.get("mensaje").toString();
            if ( mensaje.length() > 2){
                return new ResponseEntity<>(GenericResponseDTO.builder()
                        .message("se consultan todos los registros")
                        .objectResponse(json.get("mensaje"))
                        .statusCode(HttpStatus.OK.value())
                        .build(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(GenericResponseDTO.builder()
                        .message("no se encontraron registros")
                        .objectResponse(json.get("mensaje"))
                        .statusCode(HttpStatus.OK.value())
                        .build(), HttpStatus.OK);
            }

        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(GenericResponseDTO.builder()
                    .message("Error consultando la persona:  " + e.getMessage())
                    .objectResponse(null)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> actualizarDireccion(String genericRequestDTO) {
        try {

            String respuestaTramite = iPersonaRepository.actualizarDireccion(genericRequestDTO);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(respuestaTramite);

            JSONObject respuestGenerica = (JSONObject) json.get("respuesta");
            String codigo = (String) respuestGenerica.get("codigo");

            if (codigo.equals("00")) {
                JSONObject mensaje = (JSONObject) json.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message("Se actualiza la direccion").objectResponse(mensaje)
                        .statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
            } else {
                String descripcion = (String) respuestGenerica.get("mensaje");
                return new ResponseEntity<>(GenericResponseDTO.builder().message(descripcion)
                        .objectResponse(new JSONObject()).statusCode(HttpStatus.CONFLICT.value()).build(),
                        HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder().message("Error al actualizar la direccion:  " + e.getMessage())
                    .objectResponse(false).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Integer enviarCorreoRoles(String correoUsuario, JSONObject personaJson, String nombreEmpresa) {
        CorreoDTO correoDTO = new CorreoDTO();
        List<String> destinatarios = new ArrayList<>();

        destinatarios.add(correoUsuario);
        correoDTO.setDestinatarios(destinatarios);
        correoDTO.setAsunto("Notificacion Asignacion De Rol");
        correoDTO.setPlantilla("infoRol.html");
        Map<String, Object> etiqueta = new HashMap();
        String nombreCompleto = personaJson.get("primerNombre").toString()+" "+personaJson.get("primerApellido").toString();
        etiqueta.put(Utils.ciudadano,nombreCompleto);
        etiqueta.put(Utils.rol, personaJson.get("descripcionRolPersona"));
        etiqueta.put(Utils.empresa, nombreEmpresa);
        String valoresEtiquetas = "";
        Object objeto= new Object();

        try {
            valoresEtiquetas = new ObjectMapper().writeValueAsString(etiqueta);
            modelMapper.map(etiqueta,objeto);
            correoDTO.setEtiquetas(valoresEtiquetas);
            log.info(valoresEtiquetas);
            Gson gson = new GsonBuilder().create();
            JSONParser parser2 = new JSONParser();
            String valores = gson.toJson(correoDTO);
            JSONObject jasonTem = (JSONObject) parser2.parse(valores);
            JSONObject jasonTem2 = (JSONObject) parser2.parse(valoresEtiquetas);
            jasonTem.replace("etiquetas",jasonTem2);
            consumeEndpoints.enviarCorreo(jasonTem.toString());
            log.info("se envia el correo exitosamente");
            return 0;
        } catch (JsonProcessingException | ParseException e) {
            log.error("fallo conviertiendo  "+e.getMessage());
            e.printStackTrace();
            return 1;
        }

    }

    @Override
    public ResponseEntity<GenericResponseDTO> guardarPersonasPermisos(String personas) throws ParseException {
        JSONArray jsonArray = (JSONArray) jsonParser.parse(personas);
        //String json = jsonArray.get(1).toString();
        Integer longitud = jsonArray.size();
        Integer contador = 0;
        List<String> errores= new ArrayList<>();
        for (Object json: jsonArray) {
            String jsonTemporal = json.toString();
            ResponseEntity<GenericResponseDTO> respuesta = crearPersona(jsonTemporal);

            if (respuesta.getBody().getStatusCode() != 200){
                errores.add(respuesta.getBody().getMessage());
                /*if (errores.length() > 1) {

                    errores = errores+", "+respuesta.getBody().getMessage();
                }else {
                    errores = respuesta.getBody().getMessage();
                }*/
            }
        }

        if (!errores.isEmpty()){
            return new ResponseEntity<>(GenericResponseDTO.builder()
                    .message("Error guardar las personas:  ")
                    .objectResponse(errores)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build(), HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(GenericResponseDTO.builder()
                    .message("se guardaron las personas y sus permisos" )
                    .objectResponse(true)
                    .statusCode(HttpStatus.OK.value())
                    .build(), HttpStatus.OK);
        }
    }

}
