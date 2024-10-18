package br.ueg.progweb2.arquitetura.adminmodule.service;

import br.ueg.progweb2.arquitetura.adminmodule.dto.filtros.SecurityUserFilterDTO;
import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityUserDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUserFone;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUserGroup;
import br.ueg.progweb2.arquitetura.model.dtos.AuthUserDTO;
import br.ueg.progweb2.arquitetura.service.CrudService;
import net.sf.jasperreports.engine.JasperPrint;

import java.util.List;

public interface SecurityUserService extends CrudService<SecurityUser, Long> {
    /**
     * Retorna a instância do {@link SecurityUser} conforme o 'login' informado.
     *
     * @param login
     * @return
     */
    SecurityUser getByLogin(String login);

    /**
     * Retorna a Lista de {@link SecurityUserDTO} conforme o filtro pesquisado.
     *
     * @param filtroDTO
     * @return
     */
    List<SecurityUser> getUsersByFilter(SecurityUserFilterDTO filtroDTO);

    /**
     * Retorna a instância de {@link SecurityUser} conforme o 'id' informado.
     *
     * @param id -
     * @return -
     */
    SecurityUser getById(final Long id);

    /**
     * Retorna a instância de {@link SecurityUser} conforme o 'id' informado.
     *
     * @param id
     * @return
     */
    SecurityUser getByIdFetch(final Long id);

    /**
     * Realiza a inclusão ou alteração de senha do {@link SecurityUser}.
     *
     * @param usuarioSenhaDTO -
     */
    SecurityUser redefinirSenha(final AuthUserDTO usuarioSenhaDTO);

    /**
     * Retorna a instância de {@link SecurityUser} conforme o 'login' informado.
     *
     * @param login
     * @return
     */
    SecurityUser findByLoginUsuario(final String login) ;

    /**
     * Retorna a instância do {@link SecurityUser} conforme o 'login' informado
     * e que não tenha o 'id' informado.
     *
     * @param login
     * @param id
     * @return
     */
    SecurityUser findByLoginUsuarioAndNotId(final String login, final Long id) ;

    /**
     * Solicita a recuperação de senha do {@link SecurityUser}.
     *
     * @param login -
     * @return -
     */
     SecurityUser recuperarSenha(final String login);

    /**
     * Inativa o {@link SecurityUser}.
     *
     * @param id
     * @return
     */
    SecurityUser inactivate(final Long id);

    /**
     * Ativa o {@link SecurityUser}.
     *
     * @param id
     * @return
     */
    SecurityUser activate(final Long id) ;

    /**
     * Verifica se o login informado é válido e se está em uso.
     *
     * @param login
     */
    void validateUserLogin(final String login);

    /**
     * Verifica se o Login informado é válido e se está em uso.
     *
     * @param login
     * @param id
     */
    void validateUserLogin(final String login, final Long id);

    /**
     * Configura o {@link SecurityUser} dentro de {@link SecurityUserGroup} e {@link SecurityUserFone} para salvar.
     *
     * @param securityUser
     */
    void configurarUsuarioGruposAndTelefones(SecurityUser securityUser) ;

    JasperPrint generateReport(Long idGrupo);

    void initializeSecurity(String senha) ;
}
