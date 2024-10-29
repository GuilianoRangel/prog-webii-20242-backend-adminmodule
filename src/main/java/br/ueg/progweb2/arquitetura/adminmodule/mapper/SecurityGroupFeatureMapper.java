package br.ueg.progweb2.arquitetura.adminmodule.mapper;


import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityGroupFeatureDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroupFeature;
import br.ueg.progweb2.arquitetura.mapper.SimpleGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Classe adapter referente a entidade {@link SecurityGroupFeature}.
 *
 * @author UEG
 */
@Mapper(componentModel = "spring",
        uses = {
            StatusYesNoMapper.class,
            SecurityFeatureMapper.class,
            SecurityGroupMapper.class
        },
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, //checa se o valor é nulo antes de setar
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE //se o valor não for passado não faz nada.
)
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
