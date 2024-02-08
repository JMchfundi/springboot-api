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
import org.springframework.web.bind.annotation.PathVariable;
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
import co.ke.tucode.admin.entities.houseentities.ProjectMainData;
import co.ke.tucode.admin.entities.houseentities.ProjectMainUpload;
import co.ke.tucode.admin.payloads.ProjectDataPayload;
import co.ke.tucode.admin.payloads.ProjectLocationPayload;
import co.ke.tucode.admin.payloads.ProjectUploadPayload;
import co.ke.tucode.admin.repositories.ProjectInfoRepo;
import co.ke.tucode.admin.repositories.ProjectLocationRepo;
import co.ke.tucode.admin.repositories.ProjectUploadRepo;
import co.ke.tucode.admin.repositories.houserepos.ProjectMainDataRepo;
import co.ke.tucode.admin.repositories.houserepos.ProjectMainUploadRepo;
import co.ke.tucode.admin.services.ProjectInfoService;
import co.ke.tucode.buyer.entities.Africana_User;
import co.ke.tucode.buyer.entities.DocUpload;

@RestController
@RequestMapping("/project_api/v1/house")
public class ProjectMainController {

    @Autowired
    private ProjectMainDataRepo service;
    @Autowired
    private ProjectMainUploadRepo uploadRepoService;
    
    /*
     * .......................obr_put_file upload db
     * data.............................
     */
    @RequestMapping(value = "/post_proj_data", method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> put_file(@RequestPart("string") ProjectDataPayload projectDataPayload,
            @RequestPart List<MultipartFile> files) {
        ProjectMainUpload mainUpload = new ProjectMainUpload();
        ProjectMainData mainData = new ProjectMainData();
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                try {    
                    if (!service.existsByProjectname(projectDataPayload.getProjectname())) {
                        mainData.setProjectname(projectDataPayload.getProjectname());
                        mainData.setUser_signature(projectDataPayload.getUser_signature());
    
                        service.save(mainData);
                            mainUpload.setImage(file.getBytes());
                            mainUpload.setName(projectDataPayload.getProjectname() + file.getOriginalFilename());
                            mainUpload.setUrl(ServletUriComponentsBuilder
                                    .fromCurrentContextPath()
                                    .path("/profile_api/v1/get/")
                                    .path(mainUpload.getName())
                                    .toUriString());
                            mainUpload.setMainData(mainData);
                            uploadRepoService.save(mainUpload);
                            return new ResponseEntity(HttpStatus.OK);
                    } else {
                            mainUpload.setImage(file.getBytes());
                            mainUpload.setName(projectDataPayload.getProjectname() + file.getOriginalFilename());
                            mainUpload.setUrl(ServletUriComponentsBuilder
                                    .fromCurrentContextPath()
                                    .path("/profile_api/v1/get/")
                                    .path(mainUpload.getName())
                                    .toUriString());
                            mainUpload.setMainData(service.findByProjectname(projectDataPayload.getProjectname()).get(0));
                            uploadRepoService.save(mainUpload);
                            return new ResponseEntity(HttpStatus.OK);
                    }
                } catch (IOException e) {
                    return new ResponseEntity(e, HttpStatus.EXPECTATION_FAILED);
                }                    
            }
            return new ResponseEntity("Please Select Valid File", HttpStatus.INTERNAL_SERVER_ERROR);
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
        List<ProjectMainData> mainDatas = service.findAll();

        if (mainDatas.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        return new ResponseEntity(mainDatas, HttpStatus.OK);
    }

        /*
     * .......................get_by_name 
     * for data display to the interface
     */
    @RequestMapping(value = "/get_by_name", method = RequestMethod.GET)
    public ResponseEntity<?> get_user(@RequestParam(name = "projectName") String projectName) {
        List<ProjectMainData> mainDatas = service.findByProjectname(projectName);

        if (mainDatas.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        return new ResponseEntity(mainDatas.get(0), HttpStatus.OK);
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
