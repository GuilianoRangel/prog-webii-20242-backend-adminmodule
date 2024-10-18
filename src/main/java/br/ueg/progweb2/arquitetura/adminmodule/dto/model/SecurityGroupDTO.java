/*
 * SecurityGroupDTO.java
 * Copyright UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.dto.model;


import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * Classe de transferência referente a entidade {@link SecurityGroup}.
 *
 * @author UEG
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Entidade de transferência de Grupo")
public @Data class SecurityGroupDTO implements Serializable {

	private static final long serialVersionUID = -4907983765144204384L;

	@Schema(description = "Código do Grupo de Usuários")
	private Long id;

	@Size(max = 50)
	@Schema(description = "Nome do Grupo de usuários")
	private String name;

	@Size(max = 200)
	@Schema(description = "Descricao do Grupo de Usuários")
	private String description;

	@Schema(description = "Código do Status do Grupo de Usuários")
	private boolean status;

	@Schema(description = "indica se o Grupo de Usuários é de administradores")
	private boolean administrator;

	@Schema(description = "Lista de Grupo Funcionalidades que o Grupo de usuário possui")
	private Set<SecurityGroupFeatureDTO> groupFeatures;
}
