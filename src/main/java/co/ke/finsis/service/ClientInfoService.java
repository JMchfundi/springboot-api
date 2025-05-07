package co.ke.finsis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


    // Create or Update (Save) ClientInfo
    public ClientInfo saveClientInfo(ClientInfo clientInfo, MultipartFile idDocument, MultipartFile passportPhoto) throws IOException {
        // Handle file uploads
        handleFileUploads(clientInfo, idDocument, passportPhoto);

        return clientInfoRepository.save(clientInfo);
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
    private void handleFileUploads(ClientInfo clientInfo, MultipartFile idDocument, MultipartFile passportPhoto) throws IOException {

        // Handle ID Document file upload
        if (idDocument != null && !idDocument.isEmpty()) {
            String idDocumentPath = saveFileToServer(idDocument, "id_documents");
            clientInfo.setIdDocumentPath(idDocumentPath);
        }

        // Handle Passport Photo file upload
        if (passportPhoto != null && !passportPhoto.isEmpty()) {
            String passportPhotoPath = saveFileToServer(passportPhoto, "passport_photos");
            clientInfo.setPassportPhotoPath(passportPhotoPath);
        }
    }

    // Method to save file to server directory and return the file path
    private String saveFileToServer(MultipartFile file, String subDir) throws IOException {
        // Create directories if they do not exist
        Path directoryPath = Paths.get(uploadDir, subDir);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Generate the file path
        Path filePath = Paths.get(directoryPath.toString(), file.getOriginalFilename());

        // Save the file to the server
        file.transferTo(filePath.toFile());

        return filePath.toString();  // Return the path to store in DB
    }
}
