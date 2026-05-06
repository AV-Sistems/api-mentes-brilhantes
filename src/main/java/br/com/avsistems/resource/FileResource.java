package br.com.avsistems.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.io.File;
import java.nio.file.Files;

@Path("/uploads")
public class FileResource {

    private static final Logger LOG = Logger.getLogger(FileResource.class);

    @GET
    @Path("/{type}/{filename}")
    public Response getFile(@PathParam("type") String type, @PathParam("filename") String filename) {
        try {
            String filepath = "uploads/" + type + "/" + filename;
            File file = new File(filepath);

            if (!file.exists()) {
                LOG.warnf("Arquivo não encontrado: %s", filepath);
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            byte[] fileContent = Files.readAllBytes(file.toPath());
            String mediaType = getMediaType(filename);

            return Response.ok(fileContent)
                    .header("Content-Type", mediaType)
                    .header("Content-Disposition", "inline; filename=\"" + filename + "\"")
                    .build();
        } catch (Exception e) {
            LOG.errorf("Erro ao servir arquivo: %s", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getMediaType(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex <= 0) {
            return "application/octet-stream";
        }
        String ext = filename.substring(dotIndex).toLowerCase();
        return switch (ext) {
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".png" -> "image/png";
            case ".gif" -> "image/gif";
            case ".webp" -> "image/webp";
            case ".svg" -> "image/svg+xml";
            default -> "application/octet-stream";
        };
    }
}

