package br.ueg.progweb2.exampleuseadminmodule;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@PropertySources({
        @PropertySource(value = "classpath:application-admin-module.properties")
})
@SpringBootApplication(
        scanBasePackages = {
                //Para funcionamento da Arquitetura
                "br.ueg.progweb2.arquitetura.*",
                "br.ueg.progweb2.exampleuseadminmodule.*"
        }
)
@EntityScan(basePackageClasses = { Jsr310JpaConverters.class },
        basePackages = {
                //Para funcionamento da Arquitetura
                "br.ueg.progweb2.arquitetura.*",
                "br.ueg.progweb2.exampleuseadminmodule.*"}
)
@EnableJpaRepositories(basePackages = {
        //Para funcionamento da Arquitetura
        "br.ueg.progweb2.arquitetura.*",
        "br.ueg.progweb2.exampleuseadminmodule.*"
})
@OpenAPIDefinition(servers = {@Server(url = "${servidor.url}", description = "Default Server URL")})
public class ProgWebi20242BackendAdminmoduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgWebi20242BackendAdminmoduleApplication.class, args);
    }

}
