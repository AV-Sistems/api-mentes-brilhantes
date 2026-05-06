package br.com.avsistems.dto.request;

import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class ReceivedAwardsMultipartForm {

    @RestForm("data")
    @PartType(MediaType.TEXT_PLAIN)
    public String data;

    @RestForm("file")
    public FileUpload file;

    @RestForm("image")
    public FileUpload image;

    public FileUpload uploadedFile() {
        if (image != null) {
            return image;
        }
        return file;
    }
}
