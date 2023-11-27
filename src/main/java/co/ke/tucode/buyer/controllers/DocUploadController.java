package co.ke.tucode.buyer.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import co.ke.tucode.admin.entities.ProjectInfo;
import co.ke.tucode.admin.entities.ProjectLocation;
import co.ke.tucode.admin.entities.ProjectUpload;
import co.ke.tucode.admin.payloads.ProjectLocationPayload;
import co.ke.tucode.admin.payloads.ProjectUploadPayload;
import co.ke.tucode.admin.repositories.ProjectLocationRepo;
import co.ke.tucode.admin.repositories.ProjectUploadRepo;
import co.ke.tucode.admin.services.ProjectInfoService;
import co.ke.tucode.buyer.entities.DocUpload;
import co.ke.tucode.buyer.repositories.DocUploadRepository;

import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/doc_api/v1")
public class DocUploadController {

    @Autowired
    private ProjectInfoService service;
    @Autowired
    private ProjectLocationRepo locationRepoService;
    @Autowired
    private ProjectUploadRepo uploadRepoService;
    @Autowired
    private DocUploadRepository docUploadRepository;

    /*
     * .......................obr_put_file upload db
     * data.............................
     */
    @RequestMapping(value = "/post_file", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> put_file(@RequestParam("filename") String filename,
            @RequestParam("user") String user,
            @RequestParam("file") MultipartFile file) {
        DocUpload upload = new DocUpload();
        // ProjectUpload projectUpload = null;
        // ProjectInfo projectInfo = null;
        // if (service.existsByName(projectname)) {
        // projectUpload = uploadRepoService
        // .findById(service.findByName(projectname).get(0).getProjectUploadID())
        // .stream().collect(Collectors.toList()).get(0);
        if (!file.isEmpty()) {
            try {
                // Files.copy(file.getInputStream(),
                // Paths.get("uploads").resolve(filename+file.getOriginalFilename()));
                if (FilenameUtils.getExtension(file.getOriginalFilename()).equalsIgnoreCase("pdf")) {
                    if (docUploadRepository
                            .existsByName(filename + "." + FilenameUtils.getExtension(file.getOriginalFilename()))) {
                        upload = docUploadRepository
                                .findByName(filename + "." + FilenameUtils.getExtension(file.getOriginalFilename()))
                                .get(0);
                        upload.setFile(file.getBytes());
                        upload.setUser(user);
                        docUploadRepository.save(upload);
                    } else {
                        upload.setFile(file.getBytes());
                        upload.setName(filename + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
                        upload.setUrl(ServletUriComponentsBuilder
                                .fromCurrentContextPath()
                                .path("/doc_api/v1/get/")
                                .path(upload.getName())
                                .toUriString());
                        upload.setUser(user);
                        docUploadRepository.save(upload);
                    }
                } else {
                    return new ResponseEntity("Please Select PDF File", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (IOException e) {
                return new ResponseEntity(e, HttpStatus.EXPECTATION_FAILED);
            }
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity("kindly choose a file",
                    HttpStatus.EXPECTATION_FAILED);
        // }

        // else
        // return new ResponseEntity(projectname, HttpStatus.BAD_REQUEST);

    }

    /*
     * .......................obr_put_file upload db
     * data.............................
     */
    @RequestMapping(value = "/post_url", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> put_url(@ModelAttribute ProjectUploadPayload uploadPayload) {
        ProjectUpload projectUpload = null;
        ProjectInfo projectInfo = null;
        if (service.existsByName(uploadPayload.getProjectname())) {
            projectUpload = uploadRepoService
                    .findById(service.findByName(uploadPayload.getProjectname()).get(0).getProjectUploadID())
                    .stream().collect(Collectors.toList()).get(0);

            projectUpload.setSrcurl(uploadPayload.getSrcurl());
            projectInfo = service.findByName(uploadPayload.getProjectname()).get(0);
            projectInfo.setProjectUpload(projectUpload);
            service.save(projectInfo);
            return new ResponseEntity(HttpStatus.OK);
        }

        else
            return new ResponseEntity(uploadPayload.getProjectname(), HttpStatus.BAD_REQUEST);

    }

    /*
     * .......................obr_get_service retrieve all db
     * data.............................
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<?> get_service() {
        List<DocUpload> docUploads = docUploadRepository.findAll();
        if (docUploads.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity(docUploads, HttpStatus.OK);
    }

    @GetMapping("/get_docs/{user}")
    public ResponseEntity<?> get_email(@PathVariable String user) {
        List<DocUpload> docUploads = docUploadRepository.findByUser(user);
        if (docUploads.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity(docUploads, HttpStatus.OK);

    }

    /*
     * .......................obr_delete_all_service update db
     * data.............................
     */
    @RequestMapping(value = "/delete_all_service", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllUsers() {
        service.deleteAll();

        return new ResponseEntity("db data erased", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/get/{name}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getFile(@PathVariable String name) {
        List<DocUpload> docUpload = docUploadRepository.findByName(name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData(docUpload.get(0).getName(), docUpload.get(0).getName());
        // headers.setContentType(MediaType.APPLICATION_PDF_VALUE);
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok()
                .headers(headers)
                .body(docUpload.get(0).getFile());
    }

    // @GetMapping("/get/{name}")
    // public ResponseEntity<byte[]> getFile(@PathVariable String name) {
    // List<DocUpload> docUpload = docUploadRepository.findByName(name);

    // return ResponseEntity.ok()
    // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
    // docUpload.get(0).getName() + "\"")
    // .body(docUpload.get(0).getFile());
    // }
}
