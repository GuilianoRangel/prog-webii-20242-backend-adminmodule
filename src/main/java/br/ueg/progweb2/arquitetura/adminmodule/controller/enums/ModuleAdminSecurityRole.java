package br.ueg.progweb2.arquitetura.adminmodule.controller.enums;

import br.ueg.progweb2.arquitetura.controllers.enums.ISecurityRole;
import lombok.Getter;

@Getter
public enum ModuleAdminSecurityRole implements ISecurityRole {
    SEARCH              ("SEARCH"               , "Pesquisar"),
    ACTIVATE_INACTIVATE ("ACTIVATE_INACTIVATE"  , "Ativar/Desativar"),
    ;
    private final String name;

    private final String label;

    ModuleAdminSecurityRole(final String name, final String label) {
        this.name = name;
        this.label = label;
    }

    @Override
    public String toString() {
        return name;
    }
}

