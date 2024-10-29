package br.ueg.progweb2.arquitetura.adminmodule.validations;

import br.ueg.progweb2.arquitetura.adminmodule.exception.AdminModuleMessageCode;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityUserRepository;
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
public class SecurityUserCreateValidation implements IValidations<SecurityUser> {
    @Autowired
    protected SecurityUserRepository repository;
    @Override
    public void validate(SecurityUser securityUser, ValidationAction action) {
        if(Objects.equals(ValidationAction.CREATE, action) ||
                Objects.equals(ValidationAction.UPDATE, action)
        ) {
            validateDuplicateLoginOnCreate(securityUser);
        }
    }

    private void validateDuplicateLoginOnCreate(SecurityUser securityUser) {
        Long count = repository.countByLogin(securityUser.getLogin().trim());

        if ( (count > BigDecimal.ONE.longValue() && securityUser.getId()!=null) ||
                (count > BigDecimal.ZERO.longValue() && securityUser.getId()==null)
        ) {
            throw new BusinessException(AdminModuleMessageCode.ERROR_DUPLICATED_LOGIN);
        }
    }
}
