package br.ueg.progweb2.arquitetura.adminmodule.mapper;


import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusYesNo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class StatusYesNoMapper {
    public StatusYesNo asStatusSimNao(boolean valor){
        return StatusYesNo.getByIdStatusYesNo(valor);
    }

    public boolean asBoolean(StatusYesNo valor){
        if(valor == null){
            return false;
        }
        return valor.toString().equalsIgnoreCase(StatusYesNo.YES.toString());
    }
}
