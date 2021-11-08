package co.com.invima.maestro.service.srvPersona.web.api.rest.v1;


import co.com.invima.maestro.modeloTransversal.dto.generic.GenericResponseDTO;
import co.com.invima.maestro.service.srvPersona.service.IPersonaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/Persona")
@CrossOrigin({"*"})
public class PersonaController implements IPersonaController {

    private final IPersonaService service;

    @Autowired
    public PersonaController(IPersonaService service) {
        this.service = service;
    }


    @Override
    @PostMapping("/")
    @ApiOperation(value = "guardar persona con permisos de empresa y rol", notes = "metodo para guardar una persona con sus roles, empresa asociada y permisos otorgados")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> crearPersona(@ApiParam(value = "El json para guardar la persona con sus permisos debe tener la siguiente estructura: \n " +
            "{\n " +
            "\"idTipoDocumento\": 55,\n"+
            "\"numeroDocumento\": 124546,\n"+
            "\"primerNombre\": \"prueba\",\n"+
            "\"segundoNombre\": \"2\",\n"+
            "\"primerApellido\": \" ksdjf\",\n"+
            "\"segundoApellido\": \"iiox\",\n"+
            "\"correoElectronico\": \"prueba23@soaint.com\",\n"+
            "\"telefono\": 123456,\n"+
            "\"celular\": 132156,\n"+
            "\"idPais\": 1,\n"+
            "\"idDepartamento\": 29,\n"+
            "\"idMunicipio\": 1,\n"+
            "\"direccion\": \"mz 4ta\",\n"+
            "\"idEmpresa\": 1,\n"+
            "\"idRolPersona\": 4,\n"+
            "\"descripcionRolPersona\": \"Representante Legal\", \n"+
            "\"registarInformacion\": true,\n"+
            "\"firmar\": true,\n"+
            "\"pagar\":true,\n"+
            "\"consultar\": false\n"+
            "}\n" +
            "(cuidado con las comillas dobles al copiar y pegar el ejemplo)") @RequestBody String persona) {
        return service.crearPersona(persona);
    }

    @Override
    @GetMapping("/buscarPorDocumento/{numeroDocumento}/{idTipoDocumento}")
    @ApiOperation(value = "guardar persona con permisos de empresa y rol", notes = "metodo para guardar una persona con sus roles, empresa asociada y permisos otorgados")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> consultarSiExistePersona(@PathVariable String numeroDocumento, @PathVariable Integer idTipoDocumento) {
        return service.consultarSiExistePersona(numeroDocumento, idTipoDocumento);
    }

    @Override
    @GetMapping("/buscarTodosEmpresaRolPersona/{correoPersona}/{idEmpresa}")
    @ApiOperation(value = "buscar personas con permisos de empresa y rol", notes = "metodo para buscar las personas con sus roles, empresa asociada y permisos otorgados")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})

    public ResponseEntity<GenericResponseDTO> consultarTodosEmpresaRolPersona(@PathVariable String correoPersona,@PathVariable Integer idEmpresa) {
        return service.consultarTodosEmpresaRolPersona(correoPersona, idEmpresa);
    }


    @Override
    @PutMapping("/actualizarDireccion")
    @ApiOperation(value = "Actualizar direcciòn", notes = "Actualizar direcciòn")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> actualizarDireccion(@ApiParam(type = "Json", value =
            "el parametro direcciòn  debe ser un json con la siguiente estructura:" +
                    "<br>"
                    + " { <br>"
                    + "     \"idSede\": 66,\n"
                    + "     \"estructuraDireccionSede\": "
                    + "{\n"
                    + "     \"descripcionTipoDireccion\": \"Urbana\",\n"
                    + "     \"descripcionTipoVia\": \"Avenida\",\n"
                    + "     \"nomenclaturaDireccion\": \"N\",\n"
                    + "     \"valorNumeroNomenclatura\": 9,\n"
                    + "     \"numeroInicialDireccion\": 5,\n"
                    + "     \"numeroFinalDireccion\": 70,\n"
                    + "     \"barrio\": \"Los almendros\",\n"
                    + "     \"descripcionTipoInmueble\": \"Edificio\",\n"
                    + "     \"otroValorDescTipoInmueble\": \"\",\n"
                    + "     \"descripcionComplemento\": \"Lote\",\n"
                    + "     \"otroValorDescComplemento\": \"\",\n"
                    + "     \"detalles\": \"\"\n"
                    + "},\n"
                    + "     \"estructuraDireccionJudicial\": "
                    + "{\n"
                    + "     \"descripcionTipoDireccion\": \"Urbana\",\n"
                    + "     \"descripcionTipoVia\": \"Calle\",\n"
                    + "     \"nomenclaturaDireccion\": \"B\",\n"
                    + "     \"valorNumeroNomenclatura\": 3,\n"
                    + "     \"numeroInicialDireccion\": 5,\n"
                    + "     \"numeroFinalDireccion\": 70,\n"
                    + "     \"barrio\": \"Los almendros\",\n"
                    + "     \"descripcionTipoInmueble\": \"Parque empresarial\",\n"
                    + "     \"otroValorDescTipoInmueble\": \"\",\n"
                    + "     \"descripcionComplemento\": \"Lote\",\n"
                    + "     \"otroValorDescComplemento\": \"\",\n"
                    + "     \"detalles\": \"\"\n"
                    + "},\n"
                    + "     \"estructuraDireccionNotificacion\": "
                    + "{\n"
                    + "     \"descripcionTipoDireccion\": \"Urbana\",\n"
                    + "     \"descripcionTipoVia\": \"Carrera\",\n"
                    + "     \"nomenclaturaDireccion\": \"E\",\n"
                    + "     \"valorNumeroNomenclatura\": 4,\n"
                    + "     \"numeroInicialDireccion\": 6,\n"
                    + "     \"numeroFinalDireccion\": 10,\n"
                    + "     \"barrio\": \"Los almendros\",\n"
                    + "     \"descripcionTipoInmueble\": \"Complejo industrial\",\n"
                    + "     \"otroValorDescTipoInmueble\": \"\",\n"
                    + "     \"descripcionComplemento\": \"Manzana\",\n"
                    + "     \"otroValorDescComplemento\": \"\",\n"
                    + "     \"detalles\": \"\"\n"
                    + "},\n"
                    + "     \"estructuraDireccionComercial\": "
                    + "{\n"
                    + "     \"descripcionTipoDireccion\": \"Urbana\",\n"
                    + "     \"descripcionTipoVia\": \"Transversal\",\n"
                    + "     \"nomenclaturaDireccion\": \"E\",\n"
                    + "     \"valorNumeroNomenclatura\": 4,\n"
                    + "     \"numeroInicialDireccion\": 6,\n"
                    + "     \"numeroFinalDireccion\": 10,\n"
                    + "     \"barrio\": \"Los almendros\",\n"
                    + "     \"descripcionTipoInmueble\": \"Bodega\",\n"
                    + "     \"otroValorDescTipoInmueble\": \"\",\n"
                    + "     \"descripcionComplemento\": \"Bloque\",\n"
                    + "     \"otroValorDescComplemento\": \"\",\n"
                    + "     \"detalles\": \"\"\n"
                    + "}\n"
                    + "      }<br>", required = true) String genericRequestDTO) {
        return service.actualizarDireccion(genericRequestDTO);
    }

    @Override
    @PostMapping("/guardarPersonas")
    @ApiOperation(value = "guardar personas con permisos de empresa y rol", notes = "metodo para guardar mas de una persona con sus roles, empresa asociada y permisos otorgados")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. El recurso se obtiene correctamente", response = GenericResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request.Esta vez cambiamos el tipo de dato de la respuesta (String)", response = String.class),
            @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    public ResponseEntity<GenericResponseDTO> guardarPersonasPermisos(@ApiParam(value = "El json para guardar la persona con sus permisos debe tener la siguiente estructura: \n " +
            "[ \n" +
            "{\n " +
            "\"idTipoDocumento\": 55,\n"+
            "\"numeroDocumento\": 124546,\n"+
            "\"primerNombre\": \"prueba\",\n"+
            "\"segundoNombre\": \"2\",\n"+
            "\"primerApellido\": \" ksdjf\",\n"+
            "\"segundoApellido\": \"iiox\",\n"+
            "\"correoElectronico\": \"prueba23@soaint.com\",\n"+
            "\"telefono\": 123456,\n"+
            "\"celular\": 132156,\n"+
            "\"idPais\": 1,\n"+
            "\"idDepartamento\": 29,\n"+
            "\"idMunicipio\": 1,\n"+
            "\"direccion\": \"mz 4ta\",\n"+
            "\"idEmpresa\": 1,\n"+
            "\"idRolPersona\": 4,\n"+
            "\"descripcionRolPersona\": \"Representante Legal\", \n"+
            "\"registarInformacion\": true,\n"+
            "\"firmar\": true,\n"+
            "\"pagar\":true,\n"+
            "\"consultar\": false\n"+
            "}, " +
            " \"{\n" +
            "            \"idTipoDocumento\": 55,\n" +
            "            \"numeroDocumento\": 124546,\n" +
            "            \"primerNombre\": \"prueba\",\n" +
            "            \"segundoNombre\": \"2\",\n" +
            "            \"primerApellido\": \" ksdjf\",\n" +
            "            \"segundoApellido\": \"iiox\",\n" +
            "            \"correoElectronico\": \"prueba23@soaint.com\",\n" +
            "            \"telefono\": 123456,\n" +
            "            \"celular\": 132156,\n" +
            "            \"idPais\": 1,\n" +
            "            \"idDepartamento\": 29,\n" +
            "            \"idMunicipio\": 1,\n" +
            "            \"direccion\": \"mz 4ta\",\n" +
            "            \"idEmpresa\": 1,\n" +
            "            \"idRolPersona\": 4,\n" +
            "            \"descripcionRolPersona\": \"Representante Legal\", \n" +
            "            \"registarInformacion\": true,\n" +
            "            \"firmar\": true,\n" +
            "            \"pagar\":true,\n" +
            "            \"consultar\": false\n" +
            "            \"}\" +\n" +
            "(cuidado con las comillas dobles al copiar y pegar el ejemplo)") @RequestBody String personas) throws ParseException {
        return service.guardarPersonasPermisos(personas);
    }

}
