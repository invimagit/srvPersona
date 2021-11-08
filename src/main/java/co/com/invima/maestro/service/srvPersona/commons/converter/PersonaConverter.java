package co.com.invima.maestro.service.srvPersona.commons.converter;

import co.com.invima.maestro.modeloTransversal.dto.grupoEmpresarial.GrupoEmpresarialDTO;
import co.com.invima.maestro.modeloTransversal.entities.grupoEmpresarial.GrupoEmpresarialDAO;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class PersonaConverter {

    private final ModelMapper modelMapper;

    @Autowired
    public PersonaConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * @param grupoEmpresarialDAO
     * @return GrupoEmpresarialDTO
     * @author JBermeo
     * method to convert GrupoEmpresarialDAO to GrupoEmpresarialDTO
     */
    public GrupoEmpresarialDTO grupoEmpresarialDAOtoDTO(GrupoEmpresarialDAO grupoEmpresarialDAO) {
        GrupoEmpresarialDTO grupoEmpresarialDTO = new GrupoEmpresarialDTO();
        modelMapper.map(grupoEmpresarialDAO, grupoEmpresarialDTO);
        return grupoEmpresarialDTO;

    }


    /**
     * @param grupoEmpresarialDTO
     * @return GrupoEmpresarialDAO
     * @author JBermeo
     * method to convert GrupoEmpresarialDTO to GrupoEmpresarialDAO
     */
    public GrupoEmpresarialDAO grupoEmpresarialDTOtoDAO(GrupoEmpresarialDTO grupoEmpresarialDTO) throws NotFoundException {
        GrupoEmpresarialDAO grupoEmpresarialDAO = new GrupoEmpresarialDAO();
        modelMapper.map(grupoEmpresarialDTO, grupoEmpresarialDAO);

        return grupoEmpresarialDAO;
    }

    public GrupoEmpresarialDAO requestToDAO(Object request){
        GrupoEmpresarialDAO grupoEmpresarialDAO = new GrupoEmpresarialDAO();
        modelMapper.map(request, grupoEmpresarialDAO);

        return grupoEmpresarialDAO;
    }

}
