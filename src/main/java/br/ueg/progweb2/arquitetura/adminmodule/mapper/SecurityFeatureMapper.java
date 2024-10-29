package br.ueg.progweb2.arquitetura.adminmodule.mapper;


import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityFeatureDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityFeature;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Classe adapter referente a entidade {@link SecurityFeature}.
 *
 * @author UEG
 */
@Mapper(componentModel = "spring",
        uses = { StatusYesNoMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, //checa se o valor é nulo antes de setar
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE //se o valor não for passado não faz nada.
)
public interface SecurityFeatureMapper {
    /**
     * Converte a entidade {@link SecurityFeature} em DTO {@link SecurityFeatureDTO}
     *
     * @param securityFeature -
     * @return -
     */

    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "status.description", target = "statusDescription")
    SecurityFeatureDTO toDTO(SecurityFeature securityFeature);

    /**
     * Converte o DTO {@link SecurityFeatureDTO} para entidade {@link SecurityFeature}
     *
     * @param securityFeatureDTO -
     * @return -
     */

    @Mapping(target = "status", expression = "java( StatusActiveInactive.getById( securityFeatureDTO.getStatusId() ) )")
    SecurityFeature toEntity(SecurityFeatureDTO securityFeatureDTO);
}
