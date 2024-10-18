/*
 * SecurityUserGroupDTO.java
 * Copyright UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.dto.model;

import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUserGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.io.Serializable;

/**
 * Classe de transferência referente a entidade {@link SecurityUserGroup}.
 *
 * @author UEG
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Entidade de transferência de Usuario Grupos")
public @Data class SecurityUserGroupDTO implements Serializable {

	private static final long serialVersionUID = -813015525141429296L;

	@Schema(description =  "Código do Usuário Grupo")
	private String id;

	@Schema(description =  "Código do Usuário")
	private String userId;

	@Schema(description =  "Código do Grupo")
	private String groupId;

	@Schema(description =  "Nome do Grupo")
	private String groupName;

	/**
	 * @return the id
	 */
	@JsonIgnore
	public Long getIdLong() {
		Long idLong = null;

		if (Strings.isNotEmpty(id)) {
			idLong = Long.parseLong(id);
		}
		return idLong;
	}
}
