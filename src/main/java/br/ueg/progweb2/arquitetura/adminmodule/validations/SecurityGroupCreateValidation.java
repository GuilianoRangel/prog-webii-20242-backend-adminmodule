package br.ueg.progweb2.arquitetura.adminmodule.validations;

import br.ueg.progweb2.arquitetura.adminmodule.exception.AdminModuleMessageCode;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityGroupRepository;
import br.ueg.progweb2.arquitetura.exceptions.BusinessException;
import br.ueg.progweb2.arquitetura.validations.IValidations;
import br.ueg.progweb2.arquitetura.validations.ValidationAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Order(2)
@Component
public class SecurityGroupCreateValidation implements IValidations<SecurityGroup> {
    @Autowired
    protected SecurityGroupRepository repository;
    @Override
    public void validate(SecurityGroup securityUser, ValidationAction action) {
        if(Objects.equals(ValidationAction.CREATE, action) ||
        Objects.equals(ValidationAction.UPDATE, action)) {
            validateDuplicateGroupOnCreateOrUpdate(securityUser);
        }
    }
    /**
     * Verifica se o Grupo a ser salvo já existe na base de dados.
     *
     * @param securityGroup
     */
    private void validateDuplicateGroupOnCreateOrUpdate(final SecurityGroup securityGroup) {
        Long count = repository.countByNameAndGroupAndNotId(securityGroup.getName(),
                securityGroup.getId());

        if (count > BigDecimal.ZERO.longValue()) {
            throw new BusinessException(AdminModuleMessageCode.ERROR_DUPLICATED_GROUP);
        }
    }
}
