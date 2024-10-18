package br.ueg.progweb2.arquitetura.adminmodule.controller;

import br.ueg.progweb2.arquitetura.controllers.SimpleGenericCRUDController;
import br.ueg.progweb2.arquitetura.mapper.GenericMapper;
import br.ueg.progweb2.arquitetura.model.GenericModel;
import br.ueg.progweb2.arquitetura.service.CrudService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ModuleAdminAbstractController<
        DTO,
        MODEL extends GenericModel<TYPE_PK>,
        TYPE_PK,
        SERVICE extends CrudService<MODEL, TYPE_PK>,
        MAPPER extends GenericMapper<DTO, DTO, DTO, DTO , MODEL, TYPE_PK>
        >
        extends SimpleGenericCRUDController<
        DTO, MODEL,TYPE_PK,SERVICE,MAPPER> {


    /**
     * Exporta o {@link JasperPrint} no formato PDF conforme os parâmetros
     * informados.
     *
     * @param print -
     * @param name -
     * @return -
     * @throws JRException -
     * @throws IOException -
     */
    protected ResponseEntity<InputStreamResource> toPDF(final JasperPrint print, final String name)
            throws JRException, IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(print, out);

            try (ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray())) {
                return ResponseEntity.ok().header("Content-Disposition", "inline; filename=" + name.trim())
                        .contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(input));
            }
        }
    }
}
