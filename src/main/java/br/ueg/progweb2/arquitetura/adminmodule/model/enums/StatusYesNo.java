package br.ueg.progweb2.arquitetura.adminmodule.model.enums;

import java.util.Arrays;

/**
 * Enum com as possíveis representações de Status Sim/Não.
 *
 * @author UEG
 */
public enum StatusYesNo {

	YES("S", "Sim"), NO("N", "Não");

	private final String id;

	private final String description;

	/**
	 * Construtor da classe.
	 *
	 * @param id -
	 * @param description -
	 */
	StatusYesNo(final String id, final String description) {
		this.id = id;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the descricao
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Retorna a instância de {@link StatusYesNo} conforme o 'id' informado.
	 *
	 * @param id -
	 * @return -
	 */
	public static StatusYesNo getById(final String id) {
		return Arrays.stream(values()).filter(value -> value.getId().equals(id)).findFirst().orElse(null);
	}


	/**
	 * Retorna a instância de true ou false conforme o 'id' informado.
	 *
	 * @param idb -
	 * @return -
	 */
	public static StatusYesNo getByIdStatusYesNo(final boolean idb) {
		String id = idb ? "S" : "N";
		return Arrays.stream(values()).filter(value -> value.getId().equals(id)).findFirst().orElse(null);
	}

}
