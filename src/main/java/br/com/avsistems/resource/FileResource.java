package br.com.avsistems.resource;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

@jakarta.ws.rs.Path("/uploads")
public class FileResource {

    private static final Logger LOG = Logger.getLogger(FileResource.class);

    @ConfigProperty(name = "app.upload.root-dir", defaultValue = "uploads")
    String uploadRootDir;

    @GET
    @jakarta.ws.rs.Path("/{type}/{filename}")
    public Response getFile(@PathParam("type") String type, @PathParam("filename") String filename) {
        try {
            Path directoryPath = Path.of(uploadRootDir, type).normalize();
            Path filePath = directoryPath.resolve(filename).normalize();

            if (!filePath.startsWith(directoryPath) || !Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                LOG.warnf("Arquivo não encontrado: %s", filePath);
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            byte[] fileContent = Files.readAllBytes(filePath);
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
