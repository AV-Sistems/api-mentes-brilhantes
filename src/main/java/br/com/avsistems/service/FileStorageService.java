package br.com.avsistems.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class FileStorageService {

    private static final String PUBLIC_UPLOADS_PATH = "uploads";

    @ConfigProperty(name = "app.upload.root-dir", defaultValue = "uploads")
    String uploadRootDir;

    public String uploadPartners(byte[] imageBuffer, String fileName) throws IOException {
        return uploadImage("partners", imageBuffer, fileName);
    }

    public String uploadTask(byte[] imageBuffer, String fileName) throws IOException {
        return uploadImage("tasks", imageBuffer, fileName);
    }

    public String uploadUserImage(byte[] imageBuffer, String fileName) throws IOException {
        return uploadImage("users", imageBuffer, fileName);
    }

    public String uploadGifts(byte[] imageBuffer, String fileName) throws IOException {
        return uploadImage("gifts", imageBuffer, fileName);
    }

    public String uploadReceivedAward(byte[] imageBuffer, String fileName) throws IOException {
        return uploadImage("received-awards", imageBuffer, fileName);
    }

    private String uploadImage(String type, byte[] imageBuffer, String fileName) throws IOException {
        Path directory = Path.of(uploadRootDir, type);
        Files.createDirectories(directory);

        Path filePath = directory.resolve(fileName);
        Files.write(filePath, imageBuffer);

        return PUBLIC_UPLOADS_PATH + "/" + type + "/" + fileName;
    }
}
