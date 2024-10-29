/*
 * SecurityUserMapper.java
 * Copyright UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.mapper;

import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityUserDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;
import br.ueg.progweb2.arquitetura.mapper.SimpleGenericMapper;
import br.ueg.progweb2.arquitetura.model.dtos.CredencialDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
		uses = {
				StatusActiveInactiveMapper.class,
				StatusYesNoMapper.class,
				SecurityUserGroupMapper.class,
				SecurityUserFoneMapper.class
		},
		nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, //checa se o valor é nulo antes de setar
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE //se o valor não for passado não faz nada.
)
public interface SecurityUserMapper
		extends SimpleGenericMapper<
		SecurityUserDTO,
		SecurityUser,
		Long
		> {

	public CredencialDTO toCredentialDTO(SecurityUser securityUser);

	public SecurityUser toUsuarioFromCredentialDTO(CredencialDTO credencialDTO);
}
