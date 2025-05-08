package co.ke.finsis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import co.ke.finsis.entity.ClientInfo;
import co.ke.finsis.repository.ClientInfoRepository;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ClientInfoService {

    @Autowired
    private ClientInfoRepository clientInfoRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void ensureUploadDirExists() {
        Path rootPath = Paths.get(uploadDir);
        if (!Files.exists(rootPath)) {
            try {
                Files.createDirectories(rootPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create upload directory: " + uploadDir, e);
            }
        }
    }

    // Get All Clients
    public List<ClientInfo> getAllClients() {
        return clientInfoRepository.findAll();
    }

    // Get a Client by ID
    public Optional<ClientInfo> getClientById(Long id) {
        return clientInfoRepository.findById(id);
    }

    // Update existing ClientInfo
    public ClientInfo updateClientInfo(Long id, ClientInfo clientInfo) throws IOException {
        if (clientInfoRepository.existsById(id)) {
            clientInfo.setId(id);
            return saveClientInfo(clientInfo, null, null);
        }
        throw new RuntimeException("Client not found");
    }

    // Delete Client by ID
    public void deleteClientInfo(Long id) {
        if (clientInfoRepository.existsById(id)) {
            clientInfoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Client not found");
        }
    }

    // File Handling: Handle file uploads to server directory
    public ClientInfo saveClientInfo(ClientInfo clientInfo, MultipartFile idDocument, MultipartFile passportPhoto)
            throws IOException {

        // Handle ID Document file upload
        if (idDocument != null && !idDocument.isEmpty()) {
            String idDocumentPath = saveFileToServer(idDocument, "id_documents");
            clientInfo.setIdDocumentPath("api/clients/files/id_documents/"+idDocumentPath);
        }

        // Handle Passport Photo file upload
        if (passportPhoto != null && !passportPhoto.isEmpty()) {
            String passportPhotoPath = saveFileToServer(passportPhoto, "passport_photos");
            clientInfo.setPassportPhotoPath("api/clients/files/passport_photos/"+passportPhotoPath);
        }

        return clientInfoRepository.save(clientInfo);
    }

    // Method to save file to server directory and return the file path
    private String saveFileToServer(MultipartFile file, String subDir) throws IOException {
        // Resolve the absolute path to the base upload directory
        Path basePath = Paths.get(uploadDir).toAbsolutePath().normalize();

        // Append sub-directory
        Path directoryPath = basePath.resolve(subDir);

        // Create directory if it doesn't exist
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Generate unique file name
        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = directoryPath.resolve(uniqueFileName);

        // Save file
        try {
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + uniqueFileName, e);
        }

        return uniqueFileName.toString(); // You can also store a relative path if needed
    }
}
