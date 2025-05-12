package co.ke.finsis.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.ke.finsis.entity.OfficerRegistration;
import co.ke.finsis.payload.OfficerRegistrationRequest;
import co.ke.finsis.repository.OfficerRegistrationRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class OfficerRegistrationService {

    private final OfficerRegistrationRepository repository;

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

        officer.setIdDocumentPath(idDocPath);
        officer.setPassportPhotoPath(passportPhotoPath);
        return officer;
    }

    private String saveFile(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) return null;
        File dir = new File("uploads/" + folder);
        if (!dir.exists()) dir.mkdirs();
        String path = "uploads/" + folder + "/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        file.transferTo(new File(path));
        return path;
    }
}
