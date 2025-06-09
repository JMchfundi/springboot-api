package co.ke.finsis.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import co.ke.finsis.entity.OfficerRegistration;
import co.ke.finsis.payload.OfficerDto;
import co.ke.finsis.payload.OfficerRegistrationRequest;
import co.ke.finsis.service.OfficerRegistrationService;
import co.ke.mail.services.MailService;

@RestController
@RequestMapping("/api/officer")
@CrossOrigin
public class OfficerRegistrationController {

    private final OfficerRegistrationService service;
    private final MailService mailService;

    public OfficerRegistrationController(OfficerRegistrationService service, MailService mailService) {
        this.service = service;
        this.mailService = mailService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createOfficer(
            @RequestPart("data") OfficerRegistrationRequest request,
            @RequestPart("idDocument") MultipartFile idDocument,
            @RequestPart("passportPhoto") MultipartFile passportPhoto) {
        try {
            request.setIdDocument(idDocument);
            request.setPassportPhoto(passportPhoto);
            return ResponseEntity.ok(service.create(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<OfficerDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOfficer(
            @PathVariable Long id,
            @RequestPart("data") OfficerRegistrationRequest request,
            @RequestPart(value = "idDocument", required = false) MultipartFile idDocument,
            @RequestPart(value = "passportPhoto", required = false) MultipartFile passportPhoto) {
        try {
            request.setIdDocument(idDocument);
            request.setPassportPhoto(passportPhoto);
            return ResponseEntity.ok(service.update(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOfficer(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Deleted officer with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/send-credentials/{officerId}")
    public ResponseEntity<?> sendCredentials(@PathVariable Long officerId) {
        try {
            mailService.sendCredentials(officerId);
            return ResponseEntity.ok("Credentials sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }

}
