package co.ke.tucode.admin.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import co.ke.tucode.admin.entities.ProjectInfo;
import co.ke.tucode.admin.entities.ProjectLocation;
import co.ke.tucode.admin.entities.ProjectUpload;
import co.ke.tucode.admin.payloads.ProjectDataPayload;
import co.ke.tucode.admin.payloads.ProjectLocationPayload;
import co.ke.tucode.admin.payloads.ProjectUploadPayload;
import co.ke.tucode.admin.repositories.ProjectInfoRepo;
import co.ke.tucode.admin.repositories.ProjectLocationRepo;
import co.ke.tucode.admin.repositories.ProjectUploadRepo;
import co.ke.tucode.admin.services.ProjectInfoService;
import co.ke.tucode.buyer.entities.Africana_User;
import co.ke.tucode.buyer.entities.DocUpload;

@RestController
@RequestMapping("/project_api/v1")
public class ProjectController {

    @Autowired
    private ProjectInfoRepo service;
    @Autowired
    private ProjectLocationRepo locationRepoService;
    @Autowired
    private ProjectUploadRepo uploadRepoService;
    @Autowired
    private ProjectInfoRepo projectInfoRepo;

    /*
     * .......................obr_put_file upload db
     * data.............................
     */
    @RequestMapping(value = "/post_proj_data", method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> put_file(@RequestPart("string") ProjectDataPayload projectDataPayload,
            @RequestPart List<MultipartFile> files) {
        ProjectUpload upload = new ProjectUpload();
        ProjectInfo projectInfo = new ProjectInfo();
        if (!files.isEmpty()) {
            try {
                projectInfo.setConstname(projectDataPayload.getConstname());
                projectInfo.setHousetype(projectDataPayload.getHousetype());
                projectInfo.setMap_keyword(projectDataPayload.getMap_keyword());
                projectInfo.setMap_location(projectDataPayload.getMap_location());
                projectInfo.setNumofunits(projectDataPayload.getNumofunits());
                projectInfo.setPrice(projectDataPayload.getPrice());
                projectInfo.setProjaddress(projectDataPayload.getProjaddress());
                projectInfo.setProjdescription(projectDataPayload.getProjdescription());
                projectInfo.setProjectname(projectDataPayload.getProjectname());
                projectInfo.setSizeinsqkm(projectDataPayload.getSizeinsqkm());
                projectInfo.setUser_signature(projectDataPayload.getUser_signature());

                if (!service.existsByProjectname(projectInfo.getProjectname())) {
                    service.save(projectInfo);
                    for (MultipartFile file : files) {
                        upload.setImage(file.getBytes());
                        upload.setName(projectInfo.getProjectname() + file.getOriginalFilename());
                        upload.setUrl(ServletUriComponentsBuilder
                                .fromCurrentContextPath()
                                .path("/profile_api/v1/get/")
                                .path(upload.getName())
                                .toUriString());
                        upload.setInfo(projectInfo);
                        uploadRepoService.save(upload);
                        return new ResponseEntity(HttpStatus.OK);
                    }

                } else {
                    for (MultipartFile file : files) {
                        upload.setImage(file.getBytes());
                        upload.setName(projectInfo.getProjectname() + file.getOriginalFilename());
                        upload.setUrl(ServletUriComponentsBuilder
                                .fromCurrentContextPath()
                                .path("/profile_api/v1/get/")
                                .path(upload.getName())
                                .toUriString());
                        upload.setInfo(service.findByProjectname(projectInfo.getProjectname()).get(0));
                        uploadRepoService.save(upload);
                        return new ResponseEntity(HttpStatus.OK);
                    }
                }
                return new ResponseEntity("Please Select Valid File", HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (IOException e) {
                return new ResponseEntity(e, HttpStatus.EXPECTATION_FAILED);
            }
        } else
            return new ResponseEntity("kindly choose a file",
                    HttpStatus.EXPECTATION_FAILED);
    }

    /*
     * .......................obr_get_service retrieve all db
     * data.............................
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<?> get_service() {
        List<ProjectInfo> projectInfos = projectInfoRepo.findAll();

        if (projectInfos.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        return new ResponseEntity(projectInfos, HttpStatus.OK);
    }

        /*
     * .......................get_by_name 
     * for data display to the interface
     */
    @RequestMapping(value = "/get_by_name", method = RequestMethod.GET)
    public ResponseEntity<?> getByName(@RequestParam String projectname) {
        List<ProjectInfo> projectInfos = service.findByProjectname(projectname);

        if (projectInfos.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        return new ResponseEntity(projectInfos.get(0), HttpStatus.OK);
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
}
