package br.ueg.progweb2.arquitetura.adminmodule.model;

import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.converter.StatusActiveInactiveConverter;
import br.ueg.progweb2.arquitetura.model.GenericModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Entity
@Table(name = "TBL_FUNCIONALIDADE_MODULO")
@EqualsAndHashCode()
@SequenceGenerator(name = "TBL_S_FUNC_MODULO", sequenceName = "TBL_S_FUNC_MODULO", allocationSize = 1)
public @Data
class SecurityFeature implements GenericModel<Long>, Serializable {
    private static final long serialVersionUID = 4381258342853410159L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_S_FUNC_MODULO")
    @Column(name = "id_funcionalidade_modulo", nullable = false)
    private Long id;

    @Column(name = "nome_funcionalidade_modulo", length = 200, nullable = false)
    private String name;

    @Column(name = "mnemonico_funcionalidade", length = 40, nullable = false)
    private String mnemonic;

    @Convert(converter = StatusActiveInactiveConverter.class)
    @Column(name = "status", nullable = false, length = 1)
    private StatusActiveInactive status;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modulo_sistema", referencedColumnName = "id_modulo_sistema", nullable = false,
    foreignKey = @ForeignKey(name = "FK_FUNCIONALIDADE_MODULO"))
    private SecurityModule module;
}
