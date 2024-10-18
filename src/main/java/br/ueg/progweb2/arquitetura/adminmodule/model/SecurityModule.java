package br.ueg.progweb2.arquitetura.adminmodule.model;

import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.converter.StatusActiveInactiveConverter;
import br.ueg.progweb2.arquitetura.model.GenericModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Table(name = "TBL_MODULO_SISTEMA")
@EqualsAndHashCode()
@SequenceGenerator(name = "TBL_S_MODULO_SISTEMA", sequenceName = "TBL_S_MODULO_SISTEMA", allocationSize = 1)
public @Data
class SecurityModule implements GenericModel<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_S_MODULO_SISTEMA")
    @Column(name = "id_modulo_sistema", nullable = false)
    private Long id;

    @Column(name = "nome_modulo", length = 200, nullable = false)
    private String name;

    @Column(name = "mnemonico_modulo", length = 40, nullable = false)
    private String mnemonic;

    @Convert(converter = StatusActiveInactiveConverter.class)
    @Column(name = "status", nullable = false, length = 1)
    private StatusActiveInactive status;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SecurityFeature> features;

}
