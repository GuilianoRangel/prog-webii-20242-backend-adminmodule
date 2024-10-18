package br.ueg.progweb2.arquitetura.adminmodule.service;

import br.ueg.progweb2.arquitetura.adminmodule.dto.GroupStatisticsDTO;
import br.ueg.progweb2.arquitetura.adminmodule.dto.filtros.SecurityGroupFilterDTO;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;
import br.ueg.progweb2.arquitetura.service.CrudService;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public interface SecurityGroupService  extends CrudService<SecurityGroup, Long> {

    /**
     * Retorna uma lista de {@link SecurityGroup} conforme o filtro de pesquisa informado.
     *
     * @param filterDTO
     * @return
     */
    List<SecurityGroup> getGroupByFilter(final SecurityGroupFilterDTO filterDTO);

    /**
	 * Retorna uma lista de {@link SecurityGroup} ativos .
	 *
	 * @return
	 */
	List<SecurityGroup> getActives() ;

    /**
     * Retorna os roles do sistema conforme o 'idUsuario' e o 'mnemonico'
     * informados.
     *
     * @param idUsuario
     * @return
     */
    List<String> getRolesByUser(final Long idUsuario) ;

    /**
     * Inativa o {@link SecurityGroup} pelo 'id'.
     *
     * @param id
     * @return
     */
    SecurityGroup inactivate(final Long id);

    /**
     * Ativa o {@link SecurityGroup} pelo 'id'.
     *
     * @param id
     * @return
     */
    SecurityGroup activate(final Long id);

    /**
     * Retorna a instância de {@link SecurityGroup} conforme o 'id' informado.
     *
     * @param id
     * @return
     */
    SecurityGroup getGroupByIdFetch(final Long id) ;

    /**
     * Relatório de estatisticas de usuários por grupo
     * @return
     */
    List<GroupStatisticsDTO> getGroupStatistics();

    JasperPrint generateReport();

}
