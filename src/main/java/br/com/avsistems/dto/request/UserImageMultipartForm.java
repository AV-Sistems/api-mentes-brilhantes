package br.com.avsistems.dto.request;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class UserImageMultipartForm {

    @RestForm("image")
    public FileUpload file;
}

