package br.ueg.progweb2.arquitetura.adminmodule.repository;

import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroupFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface SecurityGroupFeatureRepository extends JpaRepository<SecurityGroupFeature, Long> {
    Set<SecurityGroupFeature> findByGroup(SecurityGroup securityGroup);

    /**
     * Retorna o {@link SecurityGroupFeature} pelo 'id' informado.
     *
     * @param id
     * @return
     */
    @Query("SELECT securityGroupFeature FROM SecurityGroupFeature securityGroupFeature "
            + " INNER JOIN FETCH securityGroupFeature.group group"
            + "	INNER JOIN FETCH securityGroupFeature.feature"
            + "	WHERE securityGroupFeature.id = :id")
    SecurityGroupFeature findByIdFetch(@Param("id") final Long id);

}
