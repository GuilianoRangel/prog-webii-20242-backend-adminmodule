/*
 * SecurityUserFone.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.model;

import br.ueg.progweb2.arquitetura.adminmodule.model.enums.FoneType;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.converter.TipoTelefoneUsuarioConverter;
import br.ueg.progweb2.arquitetura.model.GenericModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Classe de representação de 'Telefone'.
 *
 * @author UEG
 */
@Entity
@Table(name = "TBL_TELEFONE_USUARIO")
@EqualsAndHashCode(of = { "ddd", "numero" })
@SequenceGenerator(name = "TBL_S_TELEFONE_USUARIO", sequenceName = "TBL_S_TELEFONE_USUARIO", allocationSize = 1)
public @Data class SecurityUserFone implements GenericModel<Long>, Serializable{

	private static final long serialVersionUID = -3928643077340896948L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_S_TELEFONE_USUARIO")
	@Column(name = "id_telefone_usuario", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false,
	foreignKey = @ForeignKey(name = "FK_TELEFONE_USUARIO_USUARIO"))
	private SecurityUser securityUser;

	@Column(name = "numero_telefone", length = 11, nullable = false)
	private String number;

	@Convert(converter = TipoTelefoneUsuarioConverter.class)
	@Column(name = "tipo_telefone", nullable = false, length = 1)
	private FoneType type;

	@Column(name = "ddd_telefone", length = 5)
	private Long ddd;
}
