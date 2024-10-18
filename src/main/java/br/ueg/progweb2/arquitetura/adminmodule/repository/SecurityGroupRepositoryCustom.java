/*
 * SecurityGroupRepositoryCustom.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.repository;


import br.ueg.progweb2.arquitetura.adminmodule.dto.filtros.SecurityGroupFilterDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;

import java.util.List;

public interface SecurityGroupRepositoryCustom {

	List<SecurityGroup> findAllByFilter(SecurityGroupFilterDTO securityGroupFilterDTO);

}
