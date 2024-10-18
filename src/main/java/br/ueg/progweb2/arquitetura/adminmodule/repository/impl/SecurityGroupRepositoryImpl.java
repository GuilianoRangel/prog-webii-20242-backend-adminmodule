package br.ueg.progweb2.arquitetura.adminmodule.repository.impl;


import br.ueg.progweb2.arquitetura.adminmodule.dto.filtros.SecurityGroupFilterDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityGroupRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SecurityGroupRepositoryImpl implements SecurityGroupRepositoryCustom {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<SecurityGroup> findAllByFilter(SecurityGroupFilterDTO securityGroupFilterDTO) {
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT group FROM SecurityGroup group");
        jpql.append(" LEFT JOIN FETCH group.groupFeatures groupFeatures");
        jpql.append(" LEFT JOIN FETCH groupFeatures.feature feature ");
        jpql.append(" LEFT JOIN FETCH feature.module module ");

        jpql.append(" WHERE 1=1 ");

        if (Strings.isNotEmpty(securityGroupFilterDTO.getName())) {
            jpql.append(" AND UPPER(group.name) LIKE UPPER('%' || :name || '%')  ");
            parameters.put("name", securityGroupFilterDTO.getName());
        }

        if (securityGroupFilterDTO.getIdStatusEnum()!=null) {
            jpql.append(" AND group.status = :status ");
            parameters.put("status", securityGroupFilterDTO.getIdStatusEnum());
        }


        if (securityGroupFilterDTO.getModuleId() != null) {
            jpql.append(" AND module.id = :moduleId ");
            parameters.put("moduleId", securityGroupFilterDTO.getModuleId());
        }

        TypedQuery<SecurityGroup> query = entityManager.createQuery(jpql.toString(), SecurityGroup.class);
        parameters.entrySet().forEach(parametro -> query.setParameter(parametro.getKey(), parametro.getValue()));
        return query.getResultList();
    }

}
