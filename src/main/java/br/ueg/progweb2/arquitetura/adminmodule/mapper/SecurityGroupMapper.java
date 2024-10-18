package br.ueg.progweb2.arquitetura.adminmodule.mapper;


import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityGroupDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;
import br.ueg.progweb2.arquitetura.mapper.SimpleGenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { StatusActiveInactiveMapper.class, StatusYesNoMapper.class, SecurityFeatureMapper.class})
public interface SecurityGroupMapper
        extends SimpleGenericMapper<
        SecurityGroupDTO,
        SecurityGroup,
        Long
        > {
}
