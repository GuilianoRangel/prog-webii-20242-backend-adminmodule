/*
 * StatusAtivoInativoConverter.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.model.enums.converter;


import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Classe de conversão JPA referente ao enum {@link StatusActiveInactive}.
 * 
 * @author UEG
 */
@Converter(autoApply = true)
public class StatusActiveInactiveConverter implements AttributeConverter<StatusActiveInactive, String> {

	@Override
	public String convertToDatabaseColumn(StatusActiveInactive status) {
		return status != null ? status.getId() : null;
	}

	@Override
	public StatusActiveInactive convertToEntityAttribute(String id) {
		return StatusActiveInactive.getById(id);
	}

}
