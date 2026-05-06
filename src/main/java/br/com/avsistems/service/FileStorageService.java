package br.com.avsistems.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ApplicationScoped
public class FileStorageService {

    // Defina onde as imagens serão salvas (pode vir de um @ConfigProperty)
    private final String uploadDir = "uploads";

    public String uploadPartners(byte[] imageBuffer, String fileName) throws IOException {
        Path path = Paths.get(uploadDir+"/partners");

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        Path filePath = path.resolve(fileName);
        Files.write(filePath, imageBuffer);

        return filePath.toString(); // Retorna o caminho para salvar no banco
    }

    public String uploadTask(byte[] imageBuffer, String fileName) throws IOException {
        Path path = Paths.get(uploadDir + "/tasks");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path filePath = path.resolve(fileName);
        Files.write(filePath, imageBuffer);
        return filePath.toString();
    }

    public String uploadUserImage(byte[] imageBuffer, String fileName) throws IOException {
        Path path = Paths.get(uploadDir+"/users");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path filePath = path.resolve(fileName);
        Files.write(filePath, imageBuffer);
        return filePath.toString();
    }

    public String uploadGifts(byte[] imageBuffer, String fileName) throws IOException {
        Path path = Paths.get(uploadDir + "/gifts");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path filePath = path.resolve(fileName);
        Files.write(filePath, imageBuffer);
        return filePath.toString();
    }

    public String uploadReceivedAward(byte[] imageBuffer, String fileName) throws IOException {
        Path path = Paths.get(uploadDir + "/received-awards");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path filePath = path.resolve(fileName);
        Files.write(filePath, imageBuffer);
        return filePath.toString();
    }
}
