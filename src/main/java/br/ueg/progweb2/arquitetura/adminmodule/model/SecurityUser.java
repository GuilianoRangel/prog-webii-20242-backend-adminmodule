/*
 * SecurityUser.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.model;


import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusYesNo;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.converter.StatusActiveInactiveConverter;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.converter.StatusYesNoConverter;
import br.ueg.progweb2.arquitetura.model.GenericModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * Classe de representação de 'Usuário'.
 *
 * @author UEG
 */
@Entity
@Table(name = "TBL_USUARIO")
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name = "TBL_S_USUARIO", sequenceName = "TBL_S_USUARIO", allocationSize = 1)
public @Data class SecurityUser implements GenericModel<Long>, Serializable{

	@Serial
	private static final long serialVersionUID = -8899975090870463525L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_S_USUARIO")
	@Column(name = "id_usuario", nullable = false)
	private Long id;

	@CreationTimestamp
	@Column(name = "data_atualizacao", nullable = false)
	private LocalDate lastUpdateDate;

	@UpdateTimestamp
	@Column(name = "data_cadastro", nullable = false)
	private LocalDate createdDate;

	@Column(name = "email", length = 75, nullable = false)
	private String email;

	@Column(name = "login", length = 20, nullable = false)
	private String login;

	@Column(name = "senha", length = 255, nullable = false)
	private String password;

	@Column(name = "nome", length = 65, nullable = false)
	private String name;

	@Convert(converter = StatusActiveInactiveConverter.class)
	@Column(name = "status", nullable = false, length = 1)
	private StatusActiveInactive status;

	@Convert(converter = StatusYesNoConverter.class)
	@Column(name = "bloqueado", length = 1)
	private StatusYesNo blockedAccess;

	@EqualsAndHashCode.Exclude
	@ToStringExclude
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<SecurityUserGroup> groups;

	@EqualsAndHashCode.Exclude
	@ToStringExclude
	@OneToMany(mappedBy = "securityUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<SecurityUserFone> fones;

	/**
	 * Verifica se o Status do Usuário é igual a 'Ativo'.
	 *
	 * @return -
	 */
	@Transient
	public boolean isActiveState() {
		return Objects.nonNull(status)
				&& StatusActiveInactive.ACTIVE.getId().equals(status.getId())
				&& (Objects.isNull(blockedAccess)
				|| StatusYesNo.NO.getId().equals(blockedAccess.getId()))
		;
	}

}
