/*
 * EmailEngineService.java
 * Copyright (c) UEG.
 */
package br.ueg.progweb2.arquitetura.adminmodule.service;

import br.ueg.progweb2.arquitetura.adminmodule.configuration.ModuleAdminConstant;
import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityUser;
import br.ueg.progweb2.arquitetura.adminmodule.email.Email;
import br.ueg.progweb2.arquitetura.adminmodule.email.EmailException;
import br.ueg.progweb2.arquitetura.config.Constante;
import br.ueg.progweb2.arquitetura.model.dtos.AuthUserDTO;//UsuarioSenhaDTO;
import br.ueg.progweb2.arquitetura.exceptions.BusinessException;
import br.ueg.progweb2.arquitetura.security.KeyToken;
import br.ueg.progweb2.arquitetura.security.TokenBuilder;
import br.ueg.progweb2.arquitetura.security.TokenBuilder.JWTToken;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe de serviço de envio de e-mails.
 *
 * @author UEG
 */
@Service
public class EmailEngineService {

	@Value("${app.api.security.auth.url-reset-password}")
	private String urlRedefinirSenha;

	@Autowired
	private Configuration freemarkerConfig;

	@Autowired
	private KeyToken keyToken;

	/**
	 * Envia o e-mail de ativação do {@link SecurityUser}
	 * 
	 * @param securityUser -
	 */
	public void enviarEmailAtivacaoUsuario(final SecurityUser securityUser) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put(Constante.PARAM_NAME, securityUser.getName());

			String url = getURLValidacao(securityUser, AuthUserDTO.PasswordResetType.activate);
			params.put(Constante.PARAM_LINK, url);
			Email mail = new Email();
			mail.setSubject(ModuleAdminConstant.MAIL_SUBJECT_CREATE_USER);

			Template template = freemarkerConfig.getTemplate(ModuleAdminConstant.TEMPLATE_CREATE_USER);
			String body = FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
			mail.setBody(body);

			mail.addAddressTO(securityUser.getEmail()).send();
		} catch (EmailException | IOException | TemplateException e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * Envia o e-mail de esqueci a senha do {@link SecurityUser}
	 * 
	 * @param securityUser -
	 */
	public void enviarEmailEsqueciSenha(final SecurityUser securityUser) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put(Constante.PARAM_NAME, securityUser.getName());

			String url = getURLValidacao(securityUser, AuthUserDTO.PasswordResetType.recover);
			params.put(Constante.PARAM_LINK, url);

			Email mail = new Email();
			mail.setSubject(ModuleAdminConstant.MAIL_SUBJECT_PASSWORD_RECOVER);

			Template template = freemarkerConfig.getTemplate(ModuleAdminConstant.TEMPLATE_LOST_PASSWORD);
			String body = FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
			mail.setBody(body);

			mail.addAddressTO(securityUser.getEmail()).send();
		} catch (EmailException | IOException | TemplateException e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * Retorna a URL de ativação do {@link SecurityUser}
	 * 
	 * @param securityUser -
	 * @param tipo -
	 * @return -
	 */
	private String getURLValidacao(final SecurityUser securityUser, final AuthUserDTO.PasswordResetType tipo) {
		TokenBuilder builder = new TokenBuilder(keyToken);
		builder.addParam(Constante.PARAM_USER_ID, securityUser.getId());
		builder.addParam(Constante.PARAM_RESET_PASSWORD_TYPE, tipo.toString());

		JWTToken token = builder.buildValidation(Constante.PARAM_TIME_TOKEN_VALIDATION);
		return urlRedefinirSenha + token.getToken();
	}
}
