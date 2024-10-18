/*
 * SecurityUserDTO.java
 * Copyright UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.dto.model;

import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe de transferência referente a entidade {@link SecurityUser}.
 *
 * @author UEG
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Entidade de transferência de Usuario")
public @Data class SecurityUserDTO implements Serializable {

	private static final long serialVersionUID = -3145730384721847808L;

	@Schema(description =  "Código do Usuário")
	private String id;

	@JsonFormat(shape = Shape.STRING)
	@Schema(description =  "Data da última atualização do cadastro do Usuário")
	private LocalDate lastUpdateDate;

	@JsonFormat(shape = Shape.STRING)
	@Schema(description =  "Data do cadastro do Usuário")
	private LocalDate createdDate;

	@Size(max = 75)
	@Schema(description =  "Email do usuário")
	private String email;

	@Size(max = 20)
	@Schema(description =  "Login do Usuário")
	private String login;

	@Size(max = 65)
	@Schema(description =  "Nome do Usuário")
	private String name;

	@Schema(description =  "Código do Status do Usuário")
	private boolean status;

	@Schema(description =  "Acesso do Usuário Bloqueado")
	private boolean blockedAccess;

	@Schema(description =  "Grupos do Usuário")
	private List<SecurityUserGroupDTO> groups;

	@Schema(description =  "Telefones do Usuário")
	private List<SecurityUserFoneDTO> fones;

	public SecurityUserDTO(String id, String login) {
		this.id = id;
		this.login = login;
	}

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
