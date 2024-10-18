/*
 * StatusYesNoConverter.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.model.enums.converter;

import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusYesNo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Classe de conversão JPA referente ao enum {@link StatusYesNo}.
 * 
 * @author UEG
 */
@Converter(autoApply = true)
public class StatusYesNoConverter implements AttributeConverter<StatusYesNo, String> {

	/**
	 * @see AttributeConverter#convertToDatabaseColumn(Object)
	 */
	@Override
	public String convertToDatabaseColumn(final StatusYesNo status) {
		return status != null ? status.getId() : null;
	}

	/**
	 * @see AttributeConverter#convertToEntityAttribute(Object)
	 */
	@Override
	public StatusYesNo convertToEntityAttribute(String id) {
		return StatusYesNo.getById(id);
	}

}
