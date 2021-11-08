package co.com.invima.maestro.service.srvPersona.commons;

import co.com.invima.maestro.modeloTransversal.dto.generic.GenericResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ConsumeEndpoints {

    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();
    HttpHeaders authHeader;

    /**
     * @param genericRequestDTO object to send Email
     * @author jBermeo
     * method to create all Action's Activity Advance by a List
     */
    public void enviarCorreo(String genericRequestDTO) {
        try {
            ResponseEntity<GenericResponseDTO> responseActionManagement = restTemplate
                    .postForEntity(Utils.ENVIAR_CORREO_URI, genericRequestDTO, GenericResponseDTO.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }


    public ResponseEntity<GenericResponseDTO> crearPersonaYUsuario(String genericRequestDTO) {
        try {
            ResponseEntity<GenericResponseDTO> responseActionManagement = restTemplate
                    .postForEntity(Utils.URL_USUARIO_SERVICES, genericRequestDTO, GenericResponseDTO.class);
            return responseActionManagement;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(GenericResponseDTO.builder()
                    .message("error guardando")
                    .objectResponse("ok")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build(), HttpStatus.BAD_REQUEST);
        }

    }

}
