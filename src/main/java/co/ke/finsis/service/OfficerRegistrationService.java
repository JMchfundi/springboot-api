package co.ke.finsis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.ke.finsis.entity.OfficerRegistration;
import co.ke.finsis.payload.OfficerRegistrationRequest;
import co.ke.finsis.repository.OfficerRegistrationRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class OfficerRegistrationService {

    private final OfficerRegistrationRepository repository;

    @Value("${file.upload-dir}")
    private String uploadDir;


    public OfficerRegistrationService(OfficerRegistrationRepository repository) {
        this.repository = repository;
    }

    public OfficerRegistration create(OfficerRegistrationRequest request) throws IOException {
        OfficerRegistration officer = mapRequestToEntity(request);
        return repository.save(officer);
    }

    public List<OfficerRegistration> getAll() {
        return repository.findAll();
    }

    public OfficerRegistration getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Officer not found with ID: " + id));
    }

    public OfficerRegistration update(Long id, OfficerRegistrationRequest request) throws IOException {
        OfficerRegistration existing = getById(id);

        OfficerRegistration updated = mapRequestToEntity(request);
        updated.setId(existing.getId()); // preserve ID
        return repository.save(updated);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // Mapping logic
    private OfficerRegistration mapRequestToEntity(OfficerRegistrationRequest request) throws IOException {
        String idDocPath = saveFile(request.getIdDocument(), "id_documents");
        String passportPhotoPath = saveFile(request.getPassportPhoto(), "passport_photos");

        OfficerRegistration officer = new OfficerRegistration();
        officer.setFullName(request.getFullName());
        officer.setEmail(request.getEmail());
        officer.setPhoneNumber(request.getPhoneNumber());
        officer.setIdNumber(request.getIdNumber());
        officer.setDob(request.getDob());
        officer.setGender(request.getGender());
        officer.setBranchLocation(request.getBranchLocation());
        officer.setNokName(request.getNokName());
        officer.setNokPhone(request.getNokPhone());
        officer.setNokRelationship(request.getNokRelationship());
        officer.setBankDetails(request.getBankDetails());

        officer.setIdDocumentPath("api/clients/files/id_documents/"+idDocPath);
        officer.setPassportPhotoPath("api/clients/files/passport_photos/"+passportPhotoPath);
        return officer;
    }

    private String saveFile(MultipartFile file, String subDir) throws IOException {
            // Resolve the absolute path to the base upload directory
             // Use a base writable directory — like temp or /home/site/uploads for Azure
        Path basePath = Paths.get(System.getProperty("user.dir"), uploadDir); // Dynamically under current working dir
    
            // Path basePath = Paths.get(uploadDir).toAbsolutePath().normalize();
    
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
