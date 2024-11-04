/*
 * SecurityModuleController.java
 * Copyright UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.controller;

import br.ueg.progweb2.arquitetura.adminmodule.dto.model.SecurityModuleDTO;
import br.ueg.progweb2.arquitetura.adminmodule.mapper.SecurityModuleMapper;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityModule;
import br.ueg.progweb2.arquitetura.adminmodule.service.SecurityModuleService;
import br.ueg.progweb2.arquitetura.exceptions.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de controle referente a entidade {@link SecurityModule}.
 * 
 * @author UEG
 */
@RestController
@Tag(name = "SecurityModuleAPI", description = "Informações dos Modulos do Sistema")
@RequestMapping("${app.api.base}/modules")
public class SecurityModuleController  {

	@Autowired
	private SecurityModuleService securityModuleService;

	@Autowired
	private SecurityModuleMapper securityModuleMapper;

	/**
	 * Retorna uma lista de {@link SecurityModuleDTO} ativos conforme o 'id' do Sistema informado.
	 *
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@Operation(description = "Retorna uma lista de Módulos ativos.", responses = {
			@ApiResponse(responseCode = "200", description = "Listagem de Modulos do Sistema",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							array = @ArraySchema(schema = @Schema(implementation = SecurityModuleDTO.class)))),
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
	@GetMapping(path = "/active-modules", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getActiveModules() {
		List<SecurityModule> modulos = securityModuleService.getActives();
		List<SecurityModuleDTO> modulosDTO = new ArrayList<>();
		for(SecurityModule modulo: modulos){
			modulosDTO.add(securityModuleMapper.toDTO(modulo));
		}
		return ResponseEntity.ok(modulosDTO);
	}
}
