/*
 * FoneType.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.model.enums;

import java.util.Arrays;

/**
 * Enum com as possíveis representações de Tipos de Telefone.
 *
 * @author UEG
 */
public enum FoneType {

	CELULAR(1L, "Celular"), RESIDENCIAL(2L, "Residencial"), COMERCIAL(3L, "Comercial");

	private final Long id;

	private final String description;

	/**
	 * Construtor da classe.
	 * 
	 * @param id
	 * @param description
	 */
	FoneType(final Long id, final String description) {
		this.id = id;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the descricao
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Retorna a instância de {@link FoneType} conforme o 'id' informado.
	 * 
	 * @param id
	 * @return
	 */
	public static FoneType getById(final Long id) {
		return Arrays.stream(values()).filter(value -> value.getId().equals(id)).findFirst().orElse(null);
	}
}
