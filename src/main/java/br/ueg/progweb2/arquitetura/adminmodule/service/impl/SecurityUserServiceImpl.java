package br.ueg.progweb2.arquitetura.adminmodule.service.impl;

import br.ueg.progweb2.arquitetura.adminmodule.dto.filtros.SecurityUserFilterDTO;
import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityUserDTO;
import br.ueg.progweb2.arquitetura.adminmodule.exception.AdminModuleMessageCode;
import br.ueg.progweb2.arquitetura.adminmodule.mapper.SecurityUserMapper;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUserFone;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUserGroup;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityUserRepository;
import br.ueg.progweb2.arquitetura.adminmodule.service.EmailEngineService;
import br.ueg.progweb2.arquitetura.adminmodule.service.SecurityInitializeService;
import br.ueg.progweb2.arquitetura.adminmodule.service.SecurityUserService;
import br.ueg.progweb2.arquitetura.exceptions.ApiMessageCode;
import br.ueg.progweb2.arquitetura.exceptions.BusinessException;
import br.ueg.progweb2.arquitetura.model.dtos.AuthDTO;
import br.ueg.progweb2.arquitetura.model.dtos.AuthUserDTO;
import br.ueg.progweb2.arquitetura.service.UserPasswordService;
import br.ueg.progweb2.arquitetura.service.impl.GenericCrudWithValidationsService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Service
public class SecurityUserServiceImpl
        extends GenericCrudWithValidationsService<SecurityUser, Long, SecurityUserRepository>
        implements SecurityUserService {

    @Autowired
    private EmailEngineService emailService;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private SecurityUserMapper securityUserMapper;

    @Autowired
    private SecurityInitializeService initializeService;

    @Override
    protected void prepareToCreate(SecurityUser securityUser) {
        securityUser.setStatus(StatusActiveInactive.ACTIVE);
        LocalDate dataCadastro = LocalDate.now();
        securityUser.setLastUpdateDate(dataCadastro);
        securityUser.setCreatedDate(dataCadastro);
        securityUser.setPassword(new BCryptPasswordEncoder().encode("123456"));
    }
    /**
     * Retorna a instância do {@link SecurityUser} conforme o 'login' informado.
     *
     * @param login
     * @return
     */
    public SecurityUser getByLogin(String login) {
        SecurityUser byLogin = repository.findByLogin(login);
        if(Objects.isNull(byLogin)){
            return null;
        }
        Optional<SecurityUser> byIdFetch = repository.findByIdFetch(byLogin.getId());

        return byIdFetch.get();
    }

    @Override
    public SecurityUser update(SecurityUser dataToUpdate) {
        SecurityUser securityUserDB = this.validateIdModelExists(dataToUpdate.getId());
        dataToUpdate.setPassword(securityUserDB.getPassword());
        return super.update(dataToUpdate);
    }

    /**
     * Retorna a Lista de {@link SecurityUserDTO} conforme o filtro pesquisado.
     *
     * @param filtroDTO
     * @return
     */
    public List<SecurityUser> getUsersByFilter(SecurityUserFilterDTO filtroDTO) {
        validarCamposObrigatoriosFiltro(filtroDTO);

        List<SecurityUser> securityUsers = repository.findAllByFilter(filtroDTO);

        if (CollectionUtils.isEmpty(securityUsers)) {
            throw new BusinessException(AdminModuleMessageCode.ERROR_NO_RECORD_FOUND);
        }

        return securityUsers;
    }

    /**
     * Verifica se pelo menos um campo de pesquisa foi informado, e se informado o
     * nome do Grupo, verifica se tem pelo meno 4 caracteres.
     *
     * @param filtroDTO
     */
    private void validarCamposObrigatoriosFiltro(final SecurityUserFilterDTO filtroDTO) {
        boolean vazio = Boolean.TRUE;

        if (Strings.isNotEmpty(filtroDTO.getName())) {
            vazio = Boolean.FALSE;
        }

        if (Strings.isNotEmpty(filtroDTO.getLogin())) {
            vazio = Boolean.FALSE;
        }

        if (Objects.nonNull(filtroDTO.getActive())) {
            vazio = Boolean.FALSE;
        }

        if (vazio) {
            throw new BusinessException(AdminModuleMessageCode.ERROR_NO_FILTER_PROVIDED);
        }
    }

    /**
     * Retorna a instância de {@link SecurityUser} conforme o 'id' informado.
     *
     * @param id -
     * @return -
     */
    public SecurityUser getById(final Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Retorna a instância de {@link SecurityUser} conforme o 'id' informado.
     *
     * @param id
     * @return
     */
    public SecurityUser getByIdFetch(final Long id) {
        return repository.findByIdFetch(id).orElseThrow(() ->new BusinessException(ApiMessageCode.ERROR_RECORD_NOT_FOUND));
    }

    /**
     * Valida se as senhas foram preenchidas e se são iguais
     *
     * @param redefinirSenhaDTO -
     */
    private void validarConformidadeSenha(final AuthUserDTO redefinirSenhaDTO) {
        if (Strings.isEmpty(redefinirSenhaDTO.getNewPassword()) || Strings.isEmpty(redefinirSenhaDTO.getConfirmPassword())) {
            throw new BusinessException(ApiMessageCode.ERROR_MANDATORY_FIELDS);
        }

        if (!redefinirSenhaDTO.getNewPassword().equals(redefinirSenhaDTO.getConfirmPassword())) {
            throw new BusinessException(AdminModuleMessageCode.ERROR_WRONG_PASSWORD);
        }
    }

    /**
     * Valida se a senha corrente foi preenchida e corresponde a senha armazenada no
     * keycloak.
     *
     * @param securityUser -
     * @param senhaAntiga -
     */
    private void validarSenhaCorrente(SecurityUser securityUser, String senhaAntiga) {
        if (Strings.isEmpty(senhaAntiga)) {
            throw new BusinessException(ApiMessageCode.ERROR_MANDATORY_FIELDS);
        }

        AuthDTO authDTO = new AuthDTO();
        authDTO.setLogin(securityUser.getLogin());
        authDTO.setPassword(senhaAntiga);
        if (!UserPasswordService.loginByPassword(this.securityUserMapper.toCredentialDTO(securityUser), authDTO)) {
            throw new BusinessException(AdminModuleMessageCode.ERROR_WRONG_LAST_PASSWORD);
        }
    }

    /**
     * Realiza a inclusão ou alteração de senha do {@link SecurityUser}.
     *
     * @param usuarioSenhaDTO -
     */
    public SecurityUser redefinirSenha(final AuthUserDTO usuarioSenhaDTO) {
        SecurityUser securityUser = getById(usuarioSenhaDTO.getUserId());

        validarConformidadeSenha(usuarioSenhaDTO);

        if (!usuarioSenhaDTO.isActivate() && !usuarioSenhaDTO.isRecover()) {
            validarSenhaCorrente(securityUser, usuarioSenhaDTO.getOldPassword());
        } else {
            securityUser.setStatus(StatusActiveInactive.ACTIVE);
        }
        securityUser.setPassword(usuarioSenhaDTO.getNewPassword());
        return repository.save(securityUser);
    }

    /**
     * Retorna a instância de {@link SecurityUser} conforme o 'login' informado.
     *
     * @param login
     * @return
     */
    public SecurityUser findByLoginUsuario(final String login) {
        return repository.findByLogin(login);
    }

    /**
     * Retorna a instância do {@link SecurityUser} conforme o 'login' informado
     * e que não tenha o 'id' informado.
     *
     * @param login
     * @param id
     * @return
     */
    public SecurityUser findByLoginUsuarioAndNotId(final String login, final Long id) {
        return repository.findByLoginAndNotId(login, id);
    }

    /**
     * Solicita a recuperação de senha do {@link SecurityUser}.
     *
     * @param login -
     * @return -
     */
    public SecurityUser recuperarSenha(final String login) {
        SecurityUser securityUser = findByLoginUsuario(login);

        if (securityUser == null) {
            throw new BusinessException(AdminModuleMessageCode.ERROR_USER_NOT_FOUND);
        }

        emailService.enviarEmailEsqueciSenha(securityUser);
        return securityUser;
    }

    /**
     * Inativa o {@link SecurityUser}.
     *
     * @param id
     * @return
     */
    public SecurityUser inactivate(final Long id) {
        SecurityUser securityUser = getById(id);
        securityUser.setStatus(StatusActiveInactive.INACTIVE);
        return repository.save(securityUser);
    }

    /**
     * Ativa o {@link SecurityUser}.
     *
     * @param id
     * @return
     */
    public SecurityUser activate(final Long id) {
        SecurityUser securityUser = getById(id);
        securityUser.setStatus(StatusActiveInactive.ACTIVE);
        return repository.save(securityUser);
    }

    /**
     * Verifica se o Login informado é tem um valor válido.
     *
     * @param login
     * @return
     */
    private boolean isLoginValido(final String login) {
        boolean valido = false;

        //Colocar outras vaidações se necessário
        if (Strings.isNotEmpty(login)) {
            valido = true;
        }

        if(Strings.isNotEmpty(login) && login.length()>3){
            valido = true;
        }
        return valido;
    }

    /**
     * Verifica se o login informado é válido e se está em uso.
     *
     * @param login
     */
    public void validateUserLogin(final String login) {
        validateUserLogin(login, null);
    }

    /**
     * Verifica se o Login informado é válido e se está em uso.
     *
     * @param login
     * @param id
     */
    public void validateUserLogin(final String login, final Long id) {
        if (!isLoginValido(login)) {
            throw new BusinessException(AdminModuleMessageCode.ERROR_INVALID_LOGIN);
        }

        SecurityUser securityUser;

        if (id == null) {
            securityUser = findByLoginUsuario(login);
        } else {
            securityUser = findByLoginUsuarioAndNotId(login, id);
        }

        if (securityUser != null) {
            throw new BusinessException(AdminModuleMessageCode.ERROR_ALREADY_USED_LOGIN);
        }
    }

    /**
     * Configura o {@link SecurityUser} dentro de {@link SecurityUserGroup} e {@link SecurityUserFone} para salvar.
     *
     * @param securityUser
     */
    public void configurarUsuarioGruposAndTelefones(SecurityUser securityUser) {
        for (SecurityUserGroup securityUserGroup : securityUser.getGroups()) {
            securityUserGroup.setUser(securityUser);
        }

        if(Objects.nonNull(securityUser.getFones())){
            for (SecurityUserFone securityUserFone : securityUser.getFones()) {
                securityUserFone.setSecurityUser(securityUser);
            }
        }
    }

    public JasperPrint generateReport(Long idGrupo){
        try {
            Connection connection = dataSource.getConnection();
            Map<String, Object> params = new HashMap<>();
            params.put("id_grupo",idGrupo);
            JasperDesign jd =
                    JRXmlLoader.load(
                            this.getClass()
                                    .getResourceAsStream("/module-admin/relatorios/usuarios_por_grupo.jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(jd);
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, params, connection);
            return jasperPrint;
            //TODO Não foi feito o tratamento correto ainda
        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void initializeSecurity(String password) {
        if(Strings.isNotEmpty(password) && password.equals("inicializar")){
            initializeService.initialize();
        }else{
            throw new BusinessException(AdminModuleMessageCode.ERROR_INVALID_LOGIN);
        }
    }
}
