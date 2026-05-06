package br.com.avsistems.dto.request;

import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class PartnerMultipartForm {

    @RestForm("data")
    @PartType(MediaType.TEXT_PLAIN)
    public String data; // JSON string: {"name":"...","url":"...",...}

    @RestForm("file")
    public FileUpload file; // Compatibilidade com clientes antigos

    @RestForm("image")
    public FileUpload image; // Nome preferencial no frontend

    public FileUpload uploadedFile() {
        if (image != null) {
            return image;
        }
        return file;
    }
}