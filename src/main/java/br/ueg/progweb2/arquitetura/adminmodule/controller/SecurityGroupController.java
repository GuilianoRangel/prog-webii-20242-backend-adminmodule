package br.ueg.progweb2.arquitetura.adminmodule.controller;

import br.ueg.progweb2.arquitetura.adminmodule.controller.enums.ModuleAdminSecurityRole;
import br.ueg.progweb2.arquitetura.adminmodule.dto.GroupStatisticsDTO;
import br.ueg.progweb2.arquitetura.adminmodule.dto.filtros.SecurityGroupFilterDTO;
import br.ueg.progweb2.arquitetura.adminmodule.dto.filtros.SecurityUserFilterDTO;
import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityGroupDTO;
import br.ueg.progweb2.arquitetura.adminmodule.mapper.SecurityGroupMapper;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;
import br.ueg.progweb2.arquitetura.adminmodule.service.SecurityGroupService;
import br.ueg.progweb2.arquitetura.adminmodule.service.SecurityUserGroupService;
import br.ueg.progweb2.arquitetura.controllers.enums.ISecurityRole;
import br.ueg.progweb2.arquitetura.model.dtos.CredencialDTO;
import br.ueg.progweb2.arquitetura.exceptions.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "SecurityGroupAPI", description = "Manutenção de Grupos de usuários")
@RestController
@RequestMapping(path = "${app.api.base}/groups")
public class SecurityGroupController extends ModuleAdminAbstractController <
        SecurityGroupDTO,
        SecurityGroup,
        Long,
        SecurityGroupService,
        SecurityGroupMapper
        >{
    public static ISecurityRole ROLE_ACTIVATE_INACTIVATE    = ModuleAdminSecurityRole.ACTIVATE_INACTIVATE;
    public static ISecurityRole ROLE_SEARCH                 = ModuleAdminSecurityRole.SEARCH;

    @Autowired
    private SecurityUserGroupService securityUserGroupService;

    /***
     * Recupera grupos vinculados ao usuário
     * @return -
     */
    @PreAuthorize("isAuthenticated()")
    @Operation(description = "Recupera os grupos pelo usuário logado.", operationId = "getGruposByUsuarioLogado", method = "getGruposByUsuarioLogadoM",
            responses = {
            @ApiResponse(responseCode = "200", description = "Listagem de grupos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SecurityGroupDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de Negócio",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping(path = "/user")
    public ResponseEntity<?> getGroupsByLoggedUser() {
        CredencialDTO credential = getCredential();
        Long id = credential.getId();
        List<SecurityGroupDTO> securityGroupDTOS = new ArrayList<>();
        List<SecurityGroup> securityGroups = securityUserGroupService.getUserGroups(id);
        for (SecurityGroup securityGroup : securityGroups) {
            SecurityGroupDTO securityGroupDTO = mapper.toDTO(securityGroup);
            securityGroupDTO.setGroupFeatures(null);
            securityGroupDTOS.add(securityGroupDTO);
        }
        return ResponseEntity.ok(securityGroupDTOS);
    }

    /**
     * Retorna a instância de {@link SecurityGroupDTO} pelo id informado.
     *
     * @param id
     * s@return
     */
    @Override
    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(description = "Obter os dados do Grupo pelo id informado!", responses = {
            @ApiResponse(responseCode = "200", description = "Entidade encontrada",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de Negócio",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<SecurityGroupDTO> getById(
            @Parameter(description = "Código do Grupo", required = true)
            @PathVariable final Long id) {
        SecurityGroup securityGroup = service.getGroupByIdFetch(id);
        SecurityGroupDTO securityGroupDTO = mapper.toDTO(securityGroup);

        return ResponseEntity.ok(securityGroupDTO);
    }

    /**
     * Retorna a buscar de {@link SecurityGroup} por {@link SecurityUserFilterDTO}
     *
     * @param securityGroupFilterDTO -
     * @return -
     */
    @PreAuthorize("hasRole(#root.this.getRoleName(#root.this.ROLE_SEARCH))")
    @Operation(description = "Recupera as informações de Grupo conforme dados informados no filtro de busca", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de Grupo pelo filtro",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SecurityGroupDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de Negócio",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping(path = "/filter", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getAllGroupByFilter(
            @Parameter(description = "Filtro de pesquisa", required = true) @ModelAttribute final SecurityGroupFilterDTO securityGroupFilterDTO) {
        List<SecurityGroupDTO> securityGroupDTOS = new ArrayList<>();
        List<SecurityGroup> securityGroups = service.getGroupByFilter(securityGroupFilterDTO);
        if(!securityGroups.isEmpty()){
            for (SecurityGroup g:
                    securityGroups) {
                SecurityGroupDTO securityGroupDTO = mapper.toDTO(g);
                securityGroupDTO.setGroupFeatures(null);
                securityGroupDTOS.add(securityGroupDTO);
            }
        }

        return ResponseEntity.ok(securityGroupDTOS);
    }

	/**
	 * Retorna uma lista de {@link SecurityGroupDTO} ativos conforme o 'id' do Sistema informado.
	 *
	 * @return -
	 */
	@PreAuthorize("isAuthenticated()")
    @Operation(description = "Retorna uma lista de Grupos ativos conforme o 'id' do Sistema informado.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de Grupo",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SecurityGroupDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de Negócio",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class)))
    })
	@GetMapping(path = "/active-groups", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getActiveGroups() {
		List<SecurityGroup> securityGroups = service.getActives();
		List<SecurityGroupDTO> securityGroupDTOS = new ArrayList<>();
		for (SecurityGroup securityGroup : securityGroups) {
			securityGroup.setGroupFeatures(null);
			SecurityGroupDTO securityGroupDTO = mapper.toDTO(securityGroup);
			securityGroupDTO.setGroupFeatures(null);
			securityGroupDTOS.add(securityGroupDTO);
		}
		return ResponseEntity.ok(securityGroupDTOS);
	}

    /**
     * Retorna uma lista de {@link SecurityGroupDTO} cadastrados.
     *
     * @return -
     */
    @Override
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(description = "lista todos modelos", responses = {
            @ApiResponse(responseCode = "200", description = "Listagem geral",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de Negócio",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<List<SecurityGroupDTO>> listAll() {
        List<SecurityGroup> securityGroups = service.listAll();
        List<SecurityGroupDTO> gruposDTO = new ArrayList<>();
        for (SecurityGroup securityGroup : securityGroups) {
            securityGroup.setGroupFeatures(null);
            SecurityGroupDTO securityGroupDTO = mapper.toDTO(securityGroup);
            securityGroupDTO.setGroupFeatures(null);
            gruposDTO.add(securityGroupDTO);
        }
        return ResponseEntity.ok(gruposDTO);
    }

    /**
     * Inativa o {@link SecurityGroup} pelo 'id' informado.
     *
     * @param id -
     * @return -
     */
    @PreAuthorize("hasRole(#root.this.getRoleName(#root.this.ROLE_ACTIVATE_INACTIVATE))")
    @Operation(description = "Inativa o Grupo pelo id informado.", responses = {
            @ApiResponse(responseCode = "200", description = "Grupo Inativado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de Negócio",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping(path = "/{id:[\\d]+}/inactivate", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> inactivateGroup(@Parameter(description = "Id do Grupo", required = true) @PathVariable final BigDecimal id) {
        service.inactivate(id.longValue());
        return ResponseEntity.ok().build();
    }

    /**
     * Ativa o {@link SecurityGroup} pelo 'id' informado.
     *
     * @param id -
     * @return -
     */
    @PreAuthorize("hasRole('ROLE_GRUPO_ACTIVATE_INACTIVATE')")
    @Operation(description = "Ativa o Grupo pelo id informado.", responses = {
            @ApiResponse(responseCode = "200", description = "Grupo Inativado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de Negócio",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping(path = "/{id:[\\d]+}/activate", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> activateGroup(@Parameter(description = "Id do Grupo", required = true) @PathVariable final BigDecimal id) {
        service.activate(id.longValue());
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("isAuthenticated()")
    @Operation(description = "Retorna Estatisticas de Usuários pro grupo.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de Grupo",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GroupStatisticsDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de Negócio",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping(path = "/statistics",produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getGroupStatistics() {
        List<GroupStatisticsDTO> grupoEstatisticas = service.getGroupStatistics();
        return ResponseEntity.ok(grupoEstatisticas);
    }

    //@PreAuthorize("isAuthenticated()")

    @Operation(description = "Retorna Relatório de Grupos.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de Grupo",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE)),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de Negócio",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping(path = "/users-report",produces = { MediaType.APPLICATION_PDF_VALUE })
    public ResponseEntity<?> getGroupStatisticsReport () throws IOException, JRException {
        return this.toPDF(service.generateReport(),"Relatorio-grupo.pdf");
    }
}
