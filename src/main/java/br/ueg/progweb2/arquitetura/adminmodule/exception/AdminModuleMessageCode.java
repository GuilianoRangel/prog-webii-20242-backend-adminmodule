package br.ueg.progweb2.arquitetura.adminmodule.exception;

import br.ueg.progweb2.arquitetura.exceptions.MessageCode;

public enum AdminModuleMessageCode implements MessageCode {
    ERROR_NO_FILTER_PROVIDED("MODULEADMIN-MSG-001",400),
    ERROR_NO_RECORD_FOUND("MODULEADMIN-MSG-002", 404),
    ERROR_DUPLICATED_GROUP("MODULEADMIN-MSG-003", 400 ),
    ERROR_DUPLICATED_LOGIN("MODULEADMIN-MSG-004", 400 ),
    ERROR_WRONG_PASSWORD("MODULEADMIN-MSG-005", 400 ),
    ERROR_WRONG_LAST_PASSWORD("MODULEADMIN-MSG-006", 400),
    ERROR_USER_NOT_FOUND("MODULEADMIN-MSG-007", 400 ),
    ERROR_INVALID_LOGIN("MODULEADMIN-MSG-008", 400 ),
    ERROR_ALREADY_USED_LOGIN("MODULEADMIN-MSG-009", 400),
    ERROR_USER_BLCOKED("MODULEADMIN-MSG-010", 400);

    private final String code;

    private final Integer status;

    /**
     * Construtor da classe.
     *
     * @param code   -
     * @param status -
     */
    AdminModuleMessageCode(final String code, final Integer status) {
        this.code = code;
        this.status = status;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @see Enum#toString()
     */
    @Override
    public String toString() {
        return code;
    }
}
