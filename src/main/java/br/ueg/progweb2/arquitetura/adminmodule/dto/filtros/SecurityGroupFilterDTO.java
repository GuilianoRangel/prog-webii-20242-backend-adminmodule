package br.ueg.progweb2.arquitetura.adminmodule.dto.filtros;

import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados do filtro de pesquisa de Grupos")
public @Data class SecurityGroupFilterDTO implements Serializable {
    private static final long serialVersionUID = 7616722014159043532L;

    @Schema(description =  "Nome do Grupo")
    private String name;

    @Schema(description =  "Grupo ativo")
    private Boolean active;

    @Schema(description =  "Identificação do Módulo")
    private Long moduleId;

    /**
     * @return the id
     */
    @JsonIgnore
    public StatusActiveInactive getIdStatusEnum() {
        StatusActiveInactive status = null;

        if (Objects.nonNull(this.getActive())) {
            status = StatusActiveInactive.getById(this.getActive());
        }
        return status;
    }



}
