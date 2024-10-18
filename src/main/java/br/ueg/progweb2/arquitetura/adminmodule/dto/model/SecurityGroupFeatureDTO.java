/*
 * SecurityGroupFeatureDTO.java
 * Copyright UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.dto.model;

import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityGroupFeature;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.io.Serializable;

/**
 * Classe de transferência referente a entidade {@link SecurityGroupFeature}.
 *
 * @author UEG
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Entidade de transferência de Grupo funcionalidades")
public @Data class SecurityGroupFeatureDTO implements Serializable {

	private static final long serialVersionUID = -3858126875464908214L;

	@Schema(description =  "Código do Grupo Funcionalidade")
	private String id;

	@Schema(description =  "Código do Grupo")
	private String groupId;

	@Schema(description =  "Funcionalidade do Grupo")
	private SecurityFeatureDTO feature;



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
