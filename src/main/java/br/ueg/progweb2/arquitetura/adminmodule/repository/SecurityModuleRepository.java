/*
 * SecurityModuleRepository.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.repository;

import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityModuleRepository extends JpaRepository<SecurityModule, Long> {


	@Query(" SELECT DISTINCT module FROM SecurityModule module "
			+ " INNER JOIN FETCH module.features features"
			+ " WHERE module.status = 'A'"
			+ " ORDER BY module.name ASC, features.name ASC ")
	List<SecurityModule> getActives();

}
