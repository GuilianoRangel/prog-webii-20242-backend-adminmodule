/*
 * SecurityUserGroup.java
 * Copyright UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.model;

import br.ueg.progweb2.arquitetura.model.GenericModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "TBL_USUARIO_GRUPO")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "TBL_S_USUARIO_GRUPO", sequenceName = "TBL_S_USUARIO_GRUPO", allocationSize = 1)
public @Data class SecurityUserGroup implements GenericModel<Long>, Serializable {

	private static final long serialVersionUID = -8899975090870463525L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_S_USUARIO_GRUPO")
	@Column(name = "id_usuario_grupo", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false,
	foreignKey = @ForeignKey(name = "FK_USUARIO_GRUPO_USUARIO"))
	private SecurityUser user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo", nullable = false,
	foreignKey = @ForeignKey(name = "FK_USUARIO_GRUPO_GRUPO"))
	private SecurityGroup group;
}
