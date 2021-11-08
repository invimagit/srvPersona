package co.com.invima.maestro.service.srvPersona.service;

import co.com.invima.maestro.modeloTransversal.dto.generic.GenericResponseDTO;
import co.com.invima.maestro.service.srvPersona.commons.ConfiguradorSpring;
import co.com.invima.maestro.service.srvPersona.commons.converter.PersonaConverter;
import co.com.invima.maestro.service.srvPersona.web.api.rest.v1.PersonaController;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfiguradorSpring.class})
public class PersonaServiceTest {

    @Autowired
    PersonaController personaController;

    @Autowired
    PersonaConverter personaConverter;


    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void cosultarSiExiste(){
        Integer idTipoDocumento = 44;
        String numeroDocumento ="108187";

        ResponseEntity<GenericResponseDTO> respuesta = personaController.consultarSiExistePersona(numeroDocumento, idTipoDocumento);
        System.out.println(respuesta);
    }

    @Test
    public void consultarTodosEmpresaRolPersona(){
        ResponseEntity<GenericResponseDTO> respuesta = personaController.consultarTodosEmpresaRolPersona("prueba6@yopmail.com", 30780);
        System.out.println(respuesta);
    }

    @Test
    public void guardarVariasPersonas() throws ParseException {
        String personas = "[{\"hola\": \"mundo\"}, {\"hola\": \"mundo2\"}]";
        ResponseEntity<GenericResponseDTO> respuesta = personaController.guardarPersonasPermisos(personas);
    }
}
