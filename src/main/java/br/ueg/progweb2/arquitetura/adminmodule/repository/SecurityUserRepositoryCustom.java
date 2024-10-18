/*
 * SecurityUserRepositoryCustom.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.repository;


import br.ueg.progweb2.arquitetura.adminmodule.dto.filtros.SecurityUserFilterDTO;
import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityUserDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;

import java.util.List;

/**
 * Classe de persistência referente a entidade {@link SecurityUser}.
 * 
 * @author UEG
 */
public interface SecurityUserRepositoryCustom {

	/**
	 * Retorna a Lista de {@link SecurityUserDTO} conforme o login pesquisado.
	 * 
	 * @param login -
	 * @return -
	 */
	public List<SecurityUserDTO> findAllByLoginIgnoreCaseContaining(final String login);

	/**
	 * Retorna a Lista de {@link SecurityUserDTO} conforme o filtro pesquisado.
	 * 
	 * @param filterDTO -
	 * @return -
	 */
	public List<SecurityUser> findAllByFilter(SecurityUserFilterDTO filterDTO);

}
