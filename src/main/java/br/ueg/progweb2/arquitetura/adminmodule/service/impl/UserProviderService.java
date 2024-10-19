package br.ueg.progweb2.arquitetura.adminmodule.service.impl;

import br.ueg.progweb2.arquitetura.adminmodule.exception.AdminModuleMessageCode;
import br.ueg.progweb2.arquitetura.adminmodule.mapper.SecurityUserMapper;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;
import br.ueg.progweb2.arquitetura.adminmodule.service.SecurityGroupService;
import br.ueg.progweb2.arquitetura.adminmodule.service.SecurityUserService;
import br.ueg.progweb2.arquitetura.exceptions.BusinessException;
import br.ueg.progweb2.arquitetura.model.dtos.CredencialDTO;
import br.ueg.progweb2.arquitetura.model.dtos.AuthUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
public class UserProviderService implements br.ueg.progweb2.arquitetura.service.IUserProviderService {
    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private SecurityGroupService securityGroupService;

    @Autowired
    private SecurityUserMapper securityUserMapper;
    @Override
    public CredencialDTO getCredentialByLogin(String username) {
        SecurityUser byLogin = this.securityUserService.getByLogin(username);
        if(Objects.isNull(byLogin)){
            throw new BusinessException(AdminModuleMessageCode.ERROR_USER_NOT_FOUND);
        }
        CredencialDTO credencialDTO = this.securityUserMapper.toCredentialDTO(byLogin);
        credencialDTO.setRoles(securityGroupService.getRolesByUser(byLogin.getId()));
        return credencialDTO;
    }

    private static CredencialDTO getCredencialDTO() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String senhaCodificada = bCryptPasswordEncoder.encode("admin");
        return CredencialDTO.builder()
                .login("admin")
                .id(1L)
                .name("Admin")
                .email("admin@admin.com.br")
                .roles(Arrays.asList("ROLE_ADMIN", "ROLE_TIPO_INCLUIR"))
                .activeState(true)
                .password(senhaCodificada)
                .build();
    }

    @Override
    public CredencialDTO resetPassword(AuthUserDTO usuarioSenhaDTO) {
        return securityUserMapper.toCredentialDTO(this.securityUserService.redefinirSenha(usuarioSenhaDTO));
    }

    @Override
    public CredencialDTO getCredentialByEmail(String email) {
        return this.securityUserMapper.toCredentialDTO(this.securityUserService.findByLoginUsuario(email));
    }
}
