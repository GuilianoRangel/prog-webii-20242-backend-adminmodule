package br.ueg.progweb2.arquitetura.adminmodule.model;


import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.converter.StatusActiveInactiveConverter;
import br.ueg.progweb2.arquitetura.model.GenericModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "TBL_GRUPO_USUARIO")
@EqualsAndHashCode
@SequenceGenerator(name = "TBL_S_GRUPO_USUARIO", sequenceName = "TBL_S_GRUPO_USUARIO", allocationSize = 1)
public @Data
class SecurityGroup implements GenericModel<Long>, Serializable {
    private static final long serialVersionUID = -8899975090870463525L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_S_GRUPO_USUARIO")
    @Column(name = "id_grupo", nullable = false)
    @Max(99999999L)
    private Long id;

    @Column(name = "nome", length = 50, nullable = false)
    private String name;

    @Column(name = "descricao", length = 200, nullable = false)
    private String description;

    @Convert(converter = StatusActiveInactiveConverter.class)
    @Column(name = "status", nullable = false, length = 1)
    private StatusActiveInactive status;

    @EqualsAndHashCode.Exclude
    @ToStringExclude
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SecurityGroupFeature> groupFeatures;

    @EqualsAndHashCode.Exclude
    @ToStringExclude
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<SecurityUserGroup> userGroups;
}
