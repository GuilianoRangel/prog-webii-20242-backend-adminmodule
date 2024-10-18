package br.ueg.progweb2.arquitetura.adminmodule.repository;

import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Classe de persistência referente a entidade {@link SecurityGroup}.
 *
 * @author UEG
 */
@Repository
public interface SecurityGroupRepository extends JpaRepository<SecurityGroup, Long>, SecurityGroupRepositoryCustom {

    @Query("select ug from SecurityGroup ug" +
            " where " +
            " ug.name=:name " +
            " and ug.status=:status")
    List<SecurityGroup> findByFilter(String name, StatusActiveInactive status);



    /**
     * Retorna os roles do sistema conforme o 'idUsuario' e o 'mnemonico'
     * informados.
     *
     * @param userId -
     * @return -
     */
    @Query("SELECT CONCAT('ROLE_', module.mnemonic, '_', feature.mnemonic) FROM SecurityUserGroup securityUserGroup "
            + " INNER JOIN securityUserGroup.group group INNER JOIN securityUserGroup.user usuario"
            + " INNER JOIN group.groupFeatures groupFeatures"
            + " INNER JOIN groupFeatures.feature feature "
            + " INNER JOIN feature.module module"
            + " WHERE group.status='A' AND feature.status='A' AND module.status='A' "
            + " AND usuario.id = :userId"
            + " ORDER BY module.mnemonic, feature.mnemonic")
    List<String> findRolesByUser(@Param("userId") final Long userId);

    /**
     * Retorna o número de {@link SecurityGroup} pelo 'name' e 'Sistema', desconsiderando o
     * 'Grupo' com o 'id' informado.
     *
     * @param name
     * @param groupId
     * @return
     */
    @Query("SELECT COUNT(group) FROM SecurityGroup group " +
            " WHERE lower(group.name) LIKE lower(:name) " +
            " AND (:groupId IS NULL OR group.id != :groupId)")
    Long countByNameAndGroupAndNotId(@Param("name") final String name,
                                            @Param("groupId") final Long groupId);

    /**
     * Retorna o {@link SecurityGroup} pelo 'id' informado.
     *
     * @param id
     * @return
     */
    @Query("SELECT group FROM SecurityGroup group "
            + "	LEFT JOIN FETCH group.groupFeatures groupFeatures"
            + "	LEFT JOIN FETCH groupFeatures.feature feature"
            + "	LEFT JOIN FETCH feature.module WHERE group.id = :id")
    SecurityGroup findByIdFetch(@Param("id") final Long id);

    /**
	 * Retorna uma lista de {@link SecurityGroup} ativos .
	 *
	 * @return
	 */
	@Query(" SELECT DISTINCT group FROM SecurityGroup group "
			+ " WHERE group.status = 'A' "
			+ " ORDER BY group.name ASC ")
	List<SecurityGroup> getActives();
}
