package br.ueg.progweb2.arquitetura.adminmodule.appexample.service.impl;

import br.ueg.progweb2.arquitetura.adminmodule.service.SecurityInitializeService;
import br.ueg.progweb2.arquitetura.service.AbstractAppStartupRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner extends AbstractAppStartupRunner {

    private static final Logger LOG =
            LoggerFactory.getLogger(AppStartupRunner.class);

    @Autowired
    private SecurityInitializeService securityInitializeService;

    public void runInitData(){
        this.securityInitializeService.initialize();

        LOG.info("Fim da execução");
    }

}
