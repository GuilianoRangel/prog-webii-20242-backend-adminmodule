/*
 * SecurityUserGroupMapper.java
 * Copyright UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.mapper;

import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityUserGroupDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
		uses = {
				StatusYesNoMapper.class,
				SecurityUserMapper.class,
				SecurityGroupMapper.class
		} ,
		nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, //checa se o valor é nulo antes de setar
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE //se o valor não for passado não faz nada.
)
public interface SecurityUserGroupMapper {

	@Mapping(source = "user.id", target = "userId")
	@Mapping(source = "group.id", target = "groupId")
	@Mapping(source = "group.name", target = "groupName")
	SecurityUserGroupDTO toDTO(SecurityUserGroup securityUserGroup);

	@Mapping(source = "userId", target = "user.id")
	@Mapping(source = "groupId", target = "group.id")
	SecurityUserGroup toModel(SecurityUserGroupDTO securityUserGroupDTO);
}
