package br.ueg.progweb2.arquitetura.adminmodule.repository;

import br.ueg.progweb2.arquitetura.adminmodule.dto.GroupStatisticsDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUserGroup;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityUserGroupRepository extends JpaRepository<SecurityUserGroup, Long> {

    SecurityUserGroup findByUser(SecurityUser user);

    @Query("select distinct g from SecurityUserGroup ug " +
            "LEFT JOIN ug.user u " +
            "LEFT JOIN ug.group g " +
            "LEFT JOIN FETCH g.groupFeatures gf " +
            "LEFT JOIN FETCH gf.feature f " +
            "where ug.user.id=:userId")
    List<SecurityGroup> findByUserId(Long userId);

    @Query("select distinct ug.group from SecurityUserGroup ug " +
            "LEFT JOIN ug.user u " +
            "LEFT JOIN ug.group g " +
            "where ug.group.status=:status " +
            "and ug.group.name=:name")
    List<SecurityGroup> findByFilter(StatusActiveInactive status, String name);


    @Query("select new br.ueg.progweb2.arquitetura.adminmodule.dto.GroupStatisticsDTO(" +
            " g.id, g.name, g.description,count(ug)" +
            " ) " +
            " from SecurityGroup g " +
            " left join g.userGroups ug " +
            " group by g.id, g.name, g.description " +
            " order by g.id")
    List<GroupStatisticsDTO> getGroupStatistics();
}
