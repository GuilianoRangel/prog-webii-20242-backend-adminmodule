package br.ueg.progweb2.arquitetura.adminmodule.mapper;


import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityModuleDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityModule;
import br.ueg.progweb2.arquitetura.mapper.SimpleGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { StatusYesNoMapper.class, SecurityFeatureMapper.class})
public interface SecurityModuleMapper
        extends SimpleGenericMapper<
        SecurityModuleDTO,
        SecurityModule,
        Long
        > {

    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "status.description", target = "statusDescription")
    SecurityModuleDTO toDTO(SecurityModule securityModule);

    @Mapping(target = "status", expression = "java( StatusActiveInactive.getById( securityModuleDTO.getStatusId() ) )")
    SecurityModule toModel(SecurityModuleDTO securityModuleDTO);
}
