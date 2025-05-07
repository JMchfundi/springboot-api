package co.ke.finsis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import co.ke.finsis.entity.ClientInfo;
import co.ke.finsis.service.ClientInfoService;

import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientInfoController {

    @Autowired
    private ClientInfoService clientInfoService;


    // Create or Update Client (Submit Form)
    @PostMapping("/submit")
    public ResponseEntity<String> submitForm(@Valid @RequestPart("clientInfo") ClientInfo clientInfo,
                                             BindingResult bindingResult,
                                             @RequestPart("idDocument") MultipartFile idDocument,
                                             @RequestPart("passportPhoto") MultipartFile passportPhoto) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error occurred.");
        }

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
    public ResponseEntity<String> updateClient(@PathVariable Long id, @Valid @RequestBody ClientInfo clientInfo, BindingResult bindingResult) {
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
}
