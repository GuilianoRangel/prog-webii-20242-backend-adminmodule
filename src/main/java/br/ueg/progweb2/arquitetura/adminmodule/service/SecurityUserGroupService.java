package br.ueg.progweb2.arquitetura.adminmodule.service;

import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityUserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SecurityUserGroupService {
    @Autowired
    private SecurityUserGroupRepository securityUserGroupRepository;

    /***
     * Retorna Grupos vinculados a um usuário específico
     * @param userId
     * @return
     */
    public List<SecurityGroup> getUserGroups(Long userId){
        return securityUserGroupRepository.findByUserId(userId);
    }

}
