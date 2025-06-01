package co.ke.finsis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.ke.finsis.entity.ClientInfo;
import co.ke.finsis.repository.ClientInfoRepository;
import co.ke.tucode.accounting.entities.Account;
import co.ke.tucode.accounting.entities.AccountType;
import co.ke.tucode.accounting.services.AccountService;
import jakarta.annotation.PostConstruct;

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

    @Autowired
    private AccountService accountService;

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

    public List<ClientInfo> getAllClients() {
        return clientInfoRepository.findAll();
    }

    public Optional<ClientInfo> getClientById(Long id) {
        return clientInfoRepository.findById(id);
    }

    public ClientInfo updateClientInfo(Long id, ClientInfo clientInfo) throws IOException {
        if (clientInfoRepository.existsById(id)) {
            clientInfo.setId(id);
            return saveClientInfo(clientInfo, null, null);
        }
        throw new RuntimeException("Client not found");
    }

    public void deleteClientInfo(Long id) {
        if (clientInfoRepository.existsById(id)) {
            clientInfoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Client not found");
        }
    }

    public ClientInfo saveClientInfo(ClientInfo clientInfo, MultipartFile idDocument, MultipartFile passportPhoto)
            throws IOException {

        if (idDocument != null && !idDocument.isEmpty()) {
            String idDocumentPath = saveFileToServer(idDocument, "id_documents");
            clientInfo.setIdDocumentPath("api/clients/files/id_documents/" + idDocumentPath);
        }

        if (passportPhoto != null && !passportPhoto.isEmpty()) {
            String passportPhotoPath = saveFileToServer(passportPhoto, "passport_photos");
            clientInfo.setPassportPhotoPath("api/clients/files/passport_photos/" + passportPhotoPath);
        }

        // Save client (initially without accountId)
        // ClientInfo savedClient = clientInfoRepository.save(clientInfo);

        // Create savings account
        Account account = new Account();
        account.setName(clientInfo.getFullName() + " - Savings Account");
        account.setType(AccountType.LIABILITY); // Because the organization owes savings to client

        Account savedAccount = accountService.createAccount(account);

        // Set accountId and update client
        clientInfo.setAccountId(savedAccount.getId());
        return clientInfoRepository.save(clientInfo);
    }

    private String saveFileToServer(MultipartFile file, String subDir) throws IOException {
        Path basePath = Paths.get(System.getProperty("user.dir"), uploadDir);
        Path directoryPath = basePath.resolve(subDir);

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = directoryPath.resolve(uniqueFileName);

        try {
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + uniqueFileName, e);
        }

        return uniqueFileName;
    }
}
