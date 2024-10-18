/*
 * SecurityUserRepositoryImpl.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.repository.impl;



import br.ueg.progweb2.arquitetura.adminmodule.dto.filtros.SecurityUserFilterDTO;
import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityUserDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityUserRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SecurityUserRepositoryImpl implements SecurityUserRepositoryCustom {

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<SecurityUserDTO> findAllByLoginIgnoreCaseContaining(final String login) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT new SecurityUserDTO(securityUser.id , usuario.login)");
		jpql.append(" FROM SecurityUser securityUser");
		jpql.append(" WHERE securityUser.login LIKE ('%' + :login + '%')");

		TypedQuery<SecurityUserDTO> query = entityManager.createQuery(jpql.toString(), SecurityUserDTO.class);
		query.setParameter("login", login);
		return query.getResultList();
	}
	
	@Override
	public List<SecurityUser> findAllByFilter(SecurityUserFilterDTO filterDTO) {
		Map<String, Object> parameters = new HashMap<>();
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT DISTINCT securityUser FROM SecurityUser securityUser " +
				"LEFT JOIN FETCH securityUser.groups ug " +
				"LEFT JOIN FETCH ug.group g ");
		jpql.append(" WHERE 1=1 ");
		
		if (Strings.isNotEmpty(filterDTO.getLogin())) {
			jpql.append(" AND UPPER(securityUser.login) LIKE UPPER('%' || :login || '%') ");
			parameters.put("login", filterDTO.getLogin());
		}
		
		if (Strings.isNotEmpty(filterDTO.getName())) {
			jpql.append(" AND UPPER(securityUser.name) LIKE UPPER('%' || :name || '%') ");
			parameters.put("name", filterDTO.getName());
		}

		if (filterDTO.getStatusEnum() != null) {
			jpql.append(" AND securityUser.status = :status ");
			parameters.put("status", filterDTO.getStatusEnum());
		}

		jpql.append(" ORDER BY securityUser.name ASC ");

		TypedQuery<SecurityUser> query = entityManager.createQuery(jpql.toString(), SecurityUser.class);
		parameters.entrySet().forEach(parametro -> query.setParameter(parametro.getKey(), parametro.getValue()));
		return query.getResultList();
	}
	
}
