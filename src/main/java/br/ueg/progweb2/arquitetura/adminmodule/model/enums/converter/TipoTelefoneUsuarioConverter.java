/*
 * TipoTelefoneUsuarioConverter.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.model.enums.converter;

import br.ueg.progweb2.arquitetura.adminmodule.model.enums.FoneType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Classe de conversão JPA referente ao enum {@link FoneType}.
 * 
 * @author UEG
 */
@Converter(autoApply = true)
public class TipoTelefoneUsuarioConverter implements AttributeConverter<FoneType, Long> {

	/**
	 * @see AttributeConverter#convertToDatabaseColumn(Object)
	 */
	@Override
	public Long convertToDatabaseColumn(final FoneType tipo) {
		return tipo != null ? tipo.getId() : null;
	}

	/**
	 * @see AttributeConverter#convertToEntityAttribute(Object)
	 */
	@Override
	public FoneType convertToEntityAttribute(Long id) {
		return FoneType.getById(id);
	}

}
