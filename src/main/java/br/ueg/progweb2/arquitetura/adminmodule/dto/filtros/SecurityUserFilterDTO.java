package br.ueg.progweb2.arquitetura.adminmodule.dto.filtros;

import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe de transferência referente aos dados filtro para Usuário.
 *
 * @author UEG
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Entidade de transferência de dados de filtro de Usuário")
public @Data class SecurityUserFilterDTO implements Serializable {

    private static final long serialVersionUID = 3180319002111253549L;

    @Size(max = 39)
    @Schema(description = "Login do Usuário")
    private String login;

    @Size(max = 100)
    @Schema(description = "Nome do Usuário")
    private String name;

    @Schema(description = "Usuário Ativo")
    private Boolean active;

    /**
     * @return the id
     */
    @JsonIgnore
    public StatusActiveInactive getStatusEnum() {
    	StatusActiveInactive status = null;

        if (Objects.nonNull(this.active)) {
            status = StatusActiveInactive.getById(this.active);
        }
        return status;
    }

}
