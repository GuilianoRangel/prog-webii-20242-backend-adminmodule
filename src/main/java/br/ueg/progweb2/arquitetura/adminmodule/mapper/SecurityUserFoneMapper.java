/*
 * SecurityUserFoneMapper.java
 * Copyright UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.mapper;

import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityUserFoneDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUserFone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
		uses = {
			SecurityUserMapper.class
		})
public interface SecurityUserFoneMapper {

	@Mapping(source = "securityUser.id", target = "userId")
	@Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "type.description", target = "typeDescription")
	SecurityUserFoneDTO toDTO(SecurityUserFone securityUserFone);

	@Mapping(source = "userId", target = "securityUser.id")
	@Mapping(target = "type", expression = "java( FoneType.getById( securityUserFoneDTO.getTypeId() ) )")
	SecurityUserFone toModel(SecurityUserFoneDTO securityUserFoneDTO);
}
