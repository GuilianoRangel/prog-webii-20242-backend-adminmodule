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

@Mapper(componentModel = "spring",
		uses = {
				StatusActiveInactiveMapper.class,
				StatusYesNoMapper.class,
				SecurityUserGroupMapper.class,
				SecurityUserFoneMapper.class
		})
public interface SecurityUserMapper
		extends SimpleGenericMapper<
		SecurityUserDTO,
		SecurityUser,
		Long
		> {

	public CredencialDTO toCredentialDTO(SecurityUser securityUser);

	public SecurityUser toUsuarioFromCredentialDTO(CredencialDTO credencialDTO);
}
