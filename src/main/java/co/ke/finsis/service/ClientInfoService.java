package co.ke.finsis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.ke.finsis.entity.ClientInfo;
import co.ke.finsis.entity.Group;
import co.ke.finsis.entity.LoanType;
import co.ke.finsis.payload.ClientDto;
import co.ke.finsis.repository.ClientInfoRepository;
import co.ke.finsis.repository.GroupRepository;
import co.ke.tucode.accounting.entities.Account;
import co.ke.tucode.accounting.entities.AccountType;
import co.ke.tucode.accounting.repositories.AccountRepository;
import co.ke.tucode.accounting.services.AccountService;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientInfoService {

    @Autowired
    private ClientInfoRepository clientInfoRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GroupRepository groupRepository;

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

    public List<ClientDto> getAllClients() {
        return clientInfoRepository.findAll().stream()
                .map(client -> new ClientDto(
                        client.getId(),
                        client.getFullName(),
                        client.getEmail(),
                        client.getPhoneNumber(),
                        client.getIdNumber(),
                        client.getClientGroup() != null ? client.getClientGroup().getGroupName() : null))
                .toList();
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

    // ✅ 1. Fetch and set the group from the transient group ID
    if (clientInfo.getGroup() != null) {
        Group group = groupRepository.findById(clientInfo.getGroup())
                .orElseThrow(
                        () -> new IllegalArgumentException("Group not found with ID: " + clientInfo.getGroup()));
        clientInfo.setClientGroup(group);
    } else {
        throw new IllegalArgumentException("Group ID is required");
    }

    if (idDocument != null && !idDocument.isEmpty()) {
        String idDocumentPath = saveFileToServer(idDocument, "id_documents");
        clientInfo.setIdDocumentPath("api/clients/files/id_documents/" + idDocumentPath);
    }

    if (passportPhoto != null && !passportPhoto.isEmpty()) {
        String passportPhotoPath = saveFileToServer(passportPhoto, "passport_photos");
        clientInfo.setPassportPhotoPath("api/clients/files/passport_photos/" + passportPhotoPath);
    }

    // Initialize accounts list if null
    if (clientInfo.getAccounts() == null) {
        clientInfo.setAccounts(new ArrayList<>());
    }

    // Create savings account
    Account account = new Account();
    account.setName(clientInfo.getFullName() + " - Savings Account");
    account.setType(AccountType.LIABILITY); // Because the organization owes savings to client

    Account savedAccount = accountService.createAccount(account);

    // Add account to client's accounts
    clientInfo.getAccounts().add(savedAccount);
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

public Long getOrCreateClientCurrentAccount(ClientInfo client) {
    String accountCode = "CURRENT-" + client.getIdNumber() + "-" + client.getId();

    return accountRepository.findByCode(accountCode)
            .map(account -> {
                if (!client.getAccounts().contains(account)) {
                    client.getAccounts().add(account);
                    clientInfoRepository.save(client);
                }
                return account.getId();
            })
            .orElseGet(() -> {
                Account account = Account.builder()
                        .name(client.getFullName() + " - Current Account")
                        .code(accountCode)
                        .type(AccountType.ASSET) 
                        .balance(BigDecimal.ZERO)
                        .build();
                Account savedAccount = accountRepository.save(account);
                client.getAccounts().add(savedAccount);
                clientInfoRepository.save(client);
                return savedAccount.getId();
            });
}
}
