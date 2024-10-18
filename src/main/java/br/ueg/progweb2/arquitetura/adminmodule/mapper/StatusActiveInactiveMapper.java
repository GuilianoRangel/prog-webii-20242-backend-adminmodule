package br.ueg.progweb2.arquitetura.adminmodule.mapper;


import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class StatusActiveInactiveMapper {
    public StatusActiveInactive asStatusActiveInactive(boolean valor){
        return StatusActiveInactive.getById(valor);
    }

    public boolean asBoolean(String valor){
        if(valor == null){
            return false;
        }
        return valor.equalsIgnoreCase(StatusActiveInactive.ACTIVE.toString());
    }
}
