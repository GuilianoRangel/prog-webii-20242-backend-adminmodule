/*
 * SecurityUserController.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.controller;

import br.ueg.progweb2.arquitetura.adminmodule.controller.enums.ModuleAdminSecurityRole;
import br.ueg.progweb2.arquitetura.adminmodule.dto.filtros.SecurityUserFilterDTO;
import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityUserDTO;
import br.ueg.progweb2.arquitetura.adminmodule.mapper.SecurityUserMapper;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;
import br.ueg.progweb2.arquitetura.adminmodule.service.SecurityUserService;
import br.ueg.progweb2.arquitetura.controllers.enums.ISecurityRole;
import br.ueg.progweb2.arquitetura.exceptions.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

/**
 * Classe de controle referente a entidade {@link SecurityUser}.
 * 
 * @author UEG
 */
@Tag(name = "SecurityUserAPI", description = "Manutenção de usuários do sistema")
@RestController
@RequestMapping("${app.api.base}/user")
public class SecurityUserController extends ModuleAdminAbstractController<
		SecurityUserDTO,
		SecurityUser,
		Long,
		SecurityUserService,
		SecurityUserMapper
		> {
	public static ISecurityRole ROLE_ACTIVATE_INACTIVATE    = ModuleAdminSecurityRole.ACTIVATE_INACTIVATE;
	public static ISecurityRole ROLE_SEARCH                 = ModuleAdminSecurityRole.SEARCH;
	public static ISecurityRole BLOCK_UNBLOCK				= ModuleAdminSecurityRole.BLOCK_UNBLOCK;

	@Autowired
	private SecurityUserMapper securityUserMapper;

	/**
	 * Retorna a instância de {@link SecurityUserDTO} conforme o 'id'
	 * informado.
	 *
	 * @param id
	 * @return
	 */
	@Override
	@GetMapping(path = "/{id}",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@Operation(description = "Obter os dados do Usuário pelo id informado!", responses = {
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
	public ResponseEntity<SecurityUserDTO> getById(
			@Parameter(description = "Id do Usuario")
				@PathVariable final Long id
	) {
		SecurityUser securityUser = service.getByIdFetch(id);
		SecurityUserDTO usuarioTO = new SecurityUserDTO();

		if(securityUser != null)
			usuarioTO = securityUserMapper.toDTO(securityUser);

		return ResponseEntity.ok(usuarioTO);
	}

	/**
	 * Retorna a lista de {@link SecurityUserDTO} de acordo com os filtros
	 * informados.
	 * 
	 * @param filterDTO
	 * @return
	 */
	@PreAuthorize("hasRole(#root.this.getRoleName(#root.this.ROLE_SEARCH))")
	@Operation(description = "Recupera os usuarios pelo Filtro Informado de usuários ativos.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de Grupo pelo filtro",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							array = @ArraySchema(schema = @Schema(implementation = SecurityUserDTO.class)))),
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
	@GetMapping(path = "/filter-actives", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getActiveUserByFilter(
			@Parameter(description = "Filtro de pesquisa", required = true)
				@Valid @ModelAttribute("filtroDTO") SecurityUserFilterDTO filterDTO
	) {
		filterDTO.setActive(true);
		List<SecurityUserDTO> securityUserDTOS = new ArrayList<>();
		List<SecurityUser> securityUsers = service.getUsersByFilter(filterDTO);
		if(securityUsers != null){
			for (SecurityUser securityUser : securityUsers) {
				securityUserDTOS.add(securityUserMapper.toDTO(securityUser));
			}
		}

		return ResponseEntity.ok(securityUserDTOS);
	}

	/**
	 * Retorna a lista de {@link SecurityUserDTO} de acordo com os filtros
	 * informados.
	 * 
	 * @param filtroDTO
	 * @return
	 */
	@PreAuthorize("hasRole(#root.this.getRoleName(#root.this.ROLE_SEARCH))")
	@Operation(description = "Recupera os usuarios pelo Filtro Informado.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de Grupo pelo filtro",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							array = @ArraySchema(schema = @Schema(implementation = SecurityUserDTO.class)))),
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
	public ResponseEntity<?> getUsersByFilter(@Parameter(description = "Filtro de pesquisa", required = true) @Valid @ModelAttribute("filtroDTO") final SecurityUserFilterDTO filtroDTO) {
		List<SecurityUser> securityUsers = service.getUsersByFilter(filtroDTO);
		List<SecurityUserDTO> securityUserDTOS = new ArrayList<>();
		for (SecurityUser securityUser : securityUsers) {
			securityUser.setFones(null);
			securityUserDTOS.add (securityUserMapper.toDTO(securityUser));
		}

		return ResponseEntity.ok(securityUserDTOS);
	}

	/**
	 * Inativa o {@link SecurityUser}.
	 * 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole(#root.this.getRoleName(#root.this.ROLE_ACTIVATE_INACTIVATE))")
	@Operation(description = "Inativa o usuario.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso",
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
	@PutMapping(path = "/{id:[\\d]+}/inactivate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> inactivateUser(
			@Parameter(description = "Código do Usuário", required = true)
				@PathVariable final BigDecimal id
	) {
		service.inactivate(id.longValue());
		return ResponseEntity.ok().build();
	}

	/**
	 * Ativa o {@link SecurityUser}.
	 *
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole(#root.this.getRoleName(#root.this.ROLE_ACTIVATE_INACTIVATE))")
	@Operation(description = "Ativa o usuário.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso",
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
	@PutMapping(path = "/{id:[\\d]+}/activate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> activateUser(
			@Parameter(description = "Código do Usuário", required = true)
			@PathVariable final BigDecimal id
	) {
		service.activate(id.longValue());
		return ResponseEntity.ok().build();
	}

	/**
	 * Verifica se o Login informado é válido e se está em uso.
	 * 
	 * @param login
	 * @return
	 */
	@Operation(description = "Verifica se o Login informado é válido e se está em uso.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso",
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
	@GetMapping(path = "login/validate/{login}")
	public ResponseEntity<?> validateLogin(
			@Parameter(description = "LOGIN") @PathVariable final String login
	) {
		service.validateUserLogin(login);
		return ResponseEntity.ok().build();
	}

	/**
	 * Verifica se o Login informado é válido e se está em uso.
	 * 
	 * @param login
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@Operation(description = "Verifica se o Login informado é válido e se está em uso.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso",
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
	@GetMapping(path = "/{id:[\\d]+}/login/validate/{login}")
	public ResponseEntity<?> validateUserLogin(
			@Parameter(description = "Código do Usuário") @PathVariable final BigDecimal id,
			@Parameter(description = "LOGIN") @PathVariable final String login) {
		service.validateUserLogin(login, id.longValue());
		return ResponseEntity.ok().build();
	}

	//@PreAuthorize("isAuthenticated()")
	@Operation(description = "Retorna Relatório de Usuários.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso",
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
	@GetMapping(path = "/users-report/{idGrupo}",produces = { MediaType.APPLICATION_PDF_VALUE })
	public ResponseEntity<?> getGroupReport(
			@Parameter(description = "Código do Grupo") @PathVariable final Long idGrupo
	) throws IOException, JRException {
		return this.toPDF(service.generateReport(idGrupo),"Relatorio-usuarios.pdf");
	}

	//@PreAuthorize("isAuthenticated()")
	@Operation(description = "Retorna Relatório de Grupos.", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso",
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
	public ResponseEntity<?> getGroupReport2() throws IOException, JRException {
		return this.toPDF(service.generateReport(null),"Relatorio-grupo2.pdf");
	}

	@PreAuthorize("isAnonymous()")
	@Operation(description = "Carregar dados iniciais - sistema admin Module", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso",
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
	@GetMapping(path = "/initialize-admin-user/{password}")
	public ResponseEntity<?> initializeAdminDataUser(
			@Parameter(description = "senha") @PathVariable final String password) {
		service.initializeSecurity(password);
		return ResponseEntity.ok().build();
	}
}
