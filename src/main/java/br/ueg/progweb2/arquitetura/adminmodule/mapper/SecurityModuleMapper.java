package br.ueg.progweb2.arquitetura.adminmodule.mapper;


import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityModuleDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityModule;
import br.ueg.progweb2.arquitetura.mapper.SimpleGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = { StatusYesNoMapper.class, SecurityFeatureMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, //checa se o valor é nulo antes de setar
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE //se o valor não for passado não faz nada.
)
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
