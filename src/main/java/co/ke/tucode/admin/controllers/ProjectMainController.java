package co.ke.tucode.admin.controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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
import co.ke.tucode.admin.entities.houseentities.ProjectLocationCordinatesData;
import co.ke.tucode.admin.entities.houseentities.ProjectLocationData;
import co.ke.tucode.admin.entities.houseentities.ProjectLocationPricingData;
import co.ke.tucode.admin.entities.houseentities.ProjectLocationPricingData2;
import co.ke.tucode.admin.entities.houseentities.ProjectMainData;
import co.ke.tucode.admin.entities.houseentities.ProjectMainUpload;
import co.ke.tucode.admin.entities.houseentities.ProjectPricingData;
import co.ke.tucode.admin.entities.houseentities.ProjectRatingData;
import co.ke.tucode.admin.payloads.ProjectDataPayload;
import co.ke.tucode.admin.payloads.ProjectLocationPayload;
import co.ke.tucode.admin.payloads.ProjectUploadPayload;
import co.ke.tucode.admin.repositories.ProjectInfoRepo;
import co.ke.tucode.admin.repositories.ProjectLocationRepo;
import co.ke.tucode.admin.repositories.ProjectUploadRepo;
import co.ke.tucode.admin.repositories.houserepos.ProjectLocationCordinatesDataRepo;
import co.ke.tucode.admin.repositories.houserepos.ProjectLocationDataRepo;
import co.ke.tucode.admin.repositories.houserepos.ProjectLocationPricingData2Repo;
import co.ke.tucode.admin.repositories.houserepos.ProjectLocationPricingDataRepo;
import co.ke.tucode.admin.repositories.houserepos.ProjectMainDataRepo;
import co.ke.tucode.admin.repositories.houserepos.ProjectMainUploadRepo;
import co.ke.tucode.admin.repositories.houserepos.ProjectPricingDataRepo;
import co.ke.tucode.admin.repositories.houserepos.ProjectRatingDataRepo;
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
    @Autowired
    private ProjectLocationDataRepo locationDataRepo;
    @Autowired
    private ProjectLocationCordinatesDataRepo locationCordinatesDataRepo;
    @Autowired
    private ProjectPricingDataRepo pricingDataRepo;
    @Autowired
    private ProjectLocationPricingDataRepo locationPricingDataRepo;
    @Autowired
    private ProjectLocationPricingData2Repo locationPricingData2Repo;
    @Autowired
    private ProjectRatingDataRepo ratingDataRepo;
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
        ProjectLocationData locationData = new ProjectLocationData();
        ProjectLocationCordinatesData locationCordinatesData = new ProjectLocationCordinatesData();
        ProjectPricingData pricingData = new ProjectPricingData();
        ProjectLocationPricingData locationPricingData = new ProjectLocationPricingData();
        ProjectLocationPricingData2 locationPricingData2 = new ProjectLocationPricingData2();
        ProjectRatingData ratingData = new ProjectRatingData();

        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    if (!service.existsByProjectname(projectDataPayload.getProjectname())) {
                        mainData.setProjectname(projectDataPayload.getProjectname());
                        mainData.setUser_signature(projectDataPayload.getUser_signature());

                        service.save(mainData);
                        
                        locationData.setMainData(mainData);
                        locationDataRepo.save(locationData);
                        locationCordinatesData.setLocationData(locationData);
                        locationCordinatesDataRepo.save(locationCordinatesData);

                        pricingData.setMainData(mainData);
                        pricingDataRepo.save(pricingData);
                        locationPricingData.setPricingData(pricingData);
                        locationPricingDataRepo.save(locationPricingData);
                        locationPricingData2.setPricingData(pricingData);
                        locationPricingData2Repo.save(locationPricingData2);

                        ratingData.setMainData(mainData);
                        ratingDataRepo.save(ratingData);

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


        @GetMapping("/get/{name}")
    public ResponseEntity<byte[]> getFile(@PathVariable String name) {
    List<ProjectMainUpload> mainUploads = uploadRepoService.findByName(name);

    byte[] imageBytes = Base64.getDecoder().decode(mainUploads.get(0).getImage());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_JPEG);
    return new ResponseEntity<> (imageBytes, headers, HttpStatus.OK);
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
