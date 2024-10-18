package br.ueg.progweb2.arquitetura.adminmodule.service.impl;

import br.ueg.progweb2.arquitetura.adminmodule.dto.GroupStatisticsDTO;
import br.ueg.progweb2.arquitetura.adminmodule.dto.filtros.SecurityGroupFilterDTO;
import br.ueg.progweb2.arquitetura.adminmodule.exception.AdminModuleMessageCode;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityGroupRepository;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityUserGroupRepository;
import br.ueg.progweb2.arquitetura.adminmodule.service.SecurityGroupService;
import br.ueg.progweb2.arquitetura.exceptions.ApiMessageCode;
import br.ueg.progweb2.arquitetura.exceptions.BusinessException;
import br.ueg.progweb2.arquitetura.service.impl.GenericCrudWithValidationsService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SecurityGroupServiceImpl
        extends GenericCrudWithValidationsService<SecurityGroup, Long, SecurityGroupRepository>
        implements SecurityGroupService {

    @Autowired
    private SecurityUserGroupRepository securityUserGroupRepository;

    @Autowired
    private DataSource dataSource;

    /**
     * Retorna uma lista de {@link SecurityGroup} conforme o filtro de pesquisa informado.
     *
     * @param filterDTO
     * @return
     */
    public List<SecurityGroup> getGroupByFilter(final SecurityGroupFilterDTO filterDTO) {
        validateFilterMandatoryFields(filterDTO);

        List<SecurityGroup> securityGroups = repository.findAllByFilter(filterDTO);

        if (CollectionUtils.isEmpty(securityGroups)) {
            throw new BusinessException(ApiMessageCode.ERROR_RECORD_NOT_FOUND);
        }

        return securityGroups;
    }

    /**
     * Verifica se pelo menos um campo de pesquisa foi informado, e se informado o
     * nome do Grupo, verifica se tem pelo meno 4 caracteres.
     *
     * @param filterDTO -
     */
    private void validateFilterMandatoryFields(final SecurityGroupFilterDTO filterDTO) {
        boolean vazio = Boolean.TRUE;

        if (Strings.isNotEmpty(filterDTO.getName())) {
            vazio = Boolean.FALSE;
        }

        if (Objects.nonNull(filterDTO.getActive())) {
            vazio = Boolean.FALSE;
        }
        if(filterDTO.getModuleId()!=null && filterDTO.getModuleId()>0){
            vazio = Boolean.FALSE;

        }

        if (vazio) {
            throw new BusinessException(AdminModuleMessageCode.ERROR_NO_FILTER_PROVIDED);
        }
    }

    /**
	 * Retorna uma lista de {@link SecurityGroup} ativos .
	 *
	 * @return
	 */
	public List<SecurityGroup> getActives() {
		List<SecurityGroup> securityGroups = repository.getActives();

		if (CollectionUtils.isEmpty(securityGroups)) {
			throw new BusinessException(AdminModuleMessageCode.ERROR_NO_RECORD_FOUND);
		}
		return securityGroups;
	}

    /**
     * Retorna os roles do sistema conforme o 'idUsuario' e o 'mnemonico'
     * informados.
     *
     * @param idUsuario
     * @return
     */
    public List<String> getRolesByUser(final Long idUsuario) {
        return repository.findRolesByUser(idUsuario);
    }

    /**
     * Inativa o {@link SecurityGroup} pelo 'id'.
     *
     * @param id
     * @return
     */
    public SecurityGroup inactivate(final Long id) {
        SecurityGroup securityGroup = getGroupByIdFetch(id);
        securityGroup.setStatus(StatusActiveInactive.INACTIVE);
        return repository.save(securityGroup);
    }

    /**
     * Ativa o {@link SecurityGroup} pelo 'id'.
     *
     * @param id
     * @return
     */
    public SecurityGroup activate(final Long id) {
        SecurityGroup securityGroup = getGroupByIdFetch(id);
        securityGroup.setStatus(StatusActiveInactive.ACTIVE);
        return repository.save(securityGroup);
    }

    /**
     * Retorna a instância de {@link SecurityGroup} conforme o 'id' informado.
     *
     * @param id
     * @return
     */
    public SecurityGroup getGroupByIdFetch(final Long id) {
        return repository.findByIdFetch(id);
    }

    /**
     * Relatório de estatisticas de usuários por grupo
     * @return
     */
    public List<GroupStatisticsDTO> getGroupStatistics(){
        return securityUserGroupRepository.getGroupStatistics();
    }

    public JasperPrint generateReport(){
        try {
         Connection connection = dataSource.getConnection();
            Map<String, Object> params = new HashMap<>();
            JasperDesign jd =
                    JRXmlLoader.load(
                            this.getClass()
                                    .getResourceAsStream("/module-admin/relatorios/total_usuarios_por_grupo.jrxml"));
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


    @Override
    protected void prepareToCreate(SecurityGroup dado) {

    }
}
