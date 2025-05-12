package co.ke.finsis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import co.ke.finsis.entity.ClientInfo;
import co.ke.finsis.service.ClientInfoService;
import co.ke.tucode.systemuser.entities.ProfileUpload;

import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientInfoController {

    @Autowired
    private ClientInfoService clientInfoService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Create or Update Client (Submit Form)
    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> submitForm(@Valid @RequestPart("clientInfo") ClientInfo clientInfo,
            @RequestPart("idDocument") MultipartFile idDocument,
            @RequestPart("passportPhoto") MultipartFile passportPhoto) {
        try {
            // Process and save the client information

            clientInfoService.saveClientInfo(clientInfo, idDocument, passportPhoto);
            // Save the files (idDocument and passportPhoto) if needed

            // Process the uploaded files (e.g., save them to a storage location)
            // Example: clientInfoService.saveAttachments(idDocument, passportPhoto);

            return ResponseEntity.ok("Client and attachments submitted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while processing the form and attachments.");
        }
    }

    // Get All Clients
    @GetMapping("/all")
    public List<ClientInfo> getAllClients() {
        return clientInfoService.getAllClients();
    }

    // Get a Client by ID
    @GetMapping("/{id}")
    public ResponseEntity<ClientInfo> getClientById(@PathVariable Long id) {
        Optional<ClientInfo> client = clientInfoService.getClientById(id);
        if (client.isPresent()) {
            return ResponseEntity.ok(client.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update Client Info
    @PutMapping("/{id}")
    public ResponseEntity<String> updateClient(@PathVariable Long id, @Valid @RequestBody ClientInfo clientInfo,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error occurred.");
        }

        try {
            clientInfoService.updateClientInfo(id, clientInfo);
            return ResponseEntity.ok("Client updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating client.");
        }
    }

    // Delete Client
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        try {
            clientInfoService.deleteClientInfo(id);
            return ResponseEntity.ok("Client deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting client.");
        }
    }

    // File Upload (Separate endpoint for file uploads if needed independently)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("idDocument") MultipartFile idDocument,
            @RequestParam("passportPhoto") MultipartFile passportPhoto) {
        try {
            // You can process the files here if needed
            return ResponseEntity.ok("Files uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading files.");
        }
    }

    @GetMapping("/files/{folder}/{fileName:.+}")
    public ResponseEntity<?> getFile(@PathVariable String folder, @PathVariable String fileName) {
        try {
            // Sanitize inputs (avoid path traversal)
            if (!folder.matches("[a-zA-Z0-9_-]+") || !fileName.matches("[\\w\\-. ]+")) {
                return ResponseEntity.badRequest().body("Invalid path.");
            }
            // Construct the file path
            Path filePath = Paths.get(System.getProperty("user.dir"), uploadDir, folder).resolve(fileName).normalize();
            // Path filePath = Paths.get("uploads", folder).resolve(fileName).normalize();
            File file = filePath.toFile();
    
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
    
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            String contentType = Files.probeContentType(filePath);
    
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .body(resource);
    
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Could not serve file: " + e.getMessage());
        }
    }
    
}
