/*
 * StatusActiveInactive.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.model.enums;


import lombok.Getter;

import java.util.Arrays;

/**
 * Enum com as possíveis representações de Status Ativo/Inativo.
 * 
 * @author UEG
 */
@Getter
public enum StatusActiveInactive {

	ACTIVE("A", "Ativo"),
	INACTIVE("I", "Inativo");

	private final String id;

	private final String description;

	/**
	 * Construtor da classe.
	 *
	 * @param id -
	 * @param descricao -
	 */
	StatusActiveInactive(final String id, final String descricao) {
		this.id = id;
		this.description = descricao;
	}

	/**
	 * Retorna a instância de {@link StatusActiveInactive} conforme o 'id' informado.
	 * 
	 * @param id -
	 * @return -
	 */
	public static StatusActiveInactive getById(final String id) {
		return Arrays.stream(values()).filter(value -> value.getId().equals(id)).findFirst().orElse(StatusActiveInactive.INACTIVE);
	}

	/**
	 * Retorna a instância de {@link StatusActiveInactive} conforme o 'id' informado.
	 *
	 * @param id -
	 * @return -
	 */
	public static StatusActiveInactive getById(final boolean id) {
		return id? StatusActiveInactive.ACTIVE : StatusActiveInactive.INACTIVE;
	}

}
