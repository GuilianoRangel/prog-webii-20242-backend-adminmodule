package br.ueg.progweb2.arquitetura.adminmodule.mapper;


import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityGroupFeatureDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroupFeature;
import br.ueg.progweb2.arquitetura.mapper.SimpleGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Classe adapter referente a entidade {@link SecurityGroupFeature}.
 *
 * @author UEG
 */
@Mapper(componentModel = "spring", uses = { StatusYesNoMapper.class, SecurityFeatureMapper.class, SecurityGroupMapper.class})
public interface SecurityGroupFeatureMapper
        extends SimpleGenericMapper<
        SecurityGroupFeatureDTO,
        SecurityGroupFeature,
        Long
        > {

    @Mapping(source = "group.id", target = "groupId")
    SecurityGroupFeatureDTO toDTO(SecurityGroupFeature securityGroupFeature);

    /**
     * Converte o DTO {@link SecurityGroupFeatureDTO} para entidade {@link SecurityGroupFeature}
     *
     * @param securityGroupFeatureDTO
     * @return
     */
    @Mapping(source = "groupId", target = "group.id")
    SecurityGroupFeature toModel(SecurityGroupFeatureDTO securityGroupFeatureDTO);
}
