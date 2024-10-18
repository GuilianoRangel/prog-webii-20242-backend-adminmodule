/*
 * SecurityModuleService.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.service;

import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityModuleDTO;
import br.ueg.progweb2.arquitetura.adminmodule.exception.AdminModuleMessageCode;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityModule;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityModuleRepository;
import br.ueg.progweb2.arquitetura.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Classe de négocio referente a entidade {@link SecurityModule}.
 *
 * @author UEG
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SecurityModuleService {

	@Autowired
	private SecurityModuleRepository repository;

	/**
	 * Retorna uma lista de {@link SecurityModuleDTO} ativos.
	 *
	 * @return
	 */
	public List<SecurityModule> getActives() {
		List<SecurityModule> modulos = repository.getActives();

		if (CollectionUtils.isEmpty(modulos)) {
			throw new BusinessException(AdminModuleMessageCode.ERROR_NO_RECORD_FOUND);
		}
		return modulos;
	}

	/**
	 * Retorna uma lista de {@link SecurityModuleDTO} cadastrados.
	 *
	 * @return
	 */
	public List<SecurityModule> getTodos() {
		List<SecurityModule> modules = repository.findAll();

		if (CollectionUtils.isEmpty(modules)) {
			throw new BusinessException(AdminModuleMessageCode.ERROR_NO_RECORD_FOUND);
		}
		return modules;
	}
}
