/*
 * SecurityUserRepository.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.repository;

import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Classe de persistência referente a entidade {@link SecurityUser}.
 *
 * @author UEG
 */
@Repository
public interface SecurityUserRepository extends SecurityUserRepositoryCustom, JpaRepository<SecurityUser, Long> {
	/**
	 * Retorna a instância do {@link SecurityUser} conforme o 'login' informado.
	 * 
	 * @param login
	 * @return
	 */
	public SecurityUser findByLogin(final String login);

	/**
	 * Retorna a instância do {@link SecurityUser} conforme o 'login' informado
	 * e que não tenha o 'id' informado.
	 * 
	 * @param login
	 * @param id
	 * @return
	 */
	@Query(" SELECT usuario FROM SecurityUser usuario "
			+ " WHERE usuario.login = :login AND (:id IS NULL OR usuario.id != :id)")
	public SecurityUser findByLoginAndNotId(@Param("login") final String login, @Param("id") final Long id);

	/**
	 * Retorna uma instância de {@link SecurityUser} conforme o 'id' informado.
	 * 
	 * @param id
	 * @return
	 */
	@Query(" SELECT user FROM SecurityUser user "
			+ " LEFT JOIN FETCH user.groups userGroups "
			+ " LEFT JOIN FETCH userGroups.group group "
			+ " LEFT JOIN FETCH user.fones fones "
			+ " WHERE user.id = :id ")
	public Optional<SecurityUser> findByIdFetch(@Param("id") final Long id);


	/**
	 * Retorna o total de Usuários encontrados na base de dados conforme o Login
	 * informado.
	 *
	 * @param login login do usuário
	 * @return
	 */
	public Long countByLogin(final String login);

}
