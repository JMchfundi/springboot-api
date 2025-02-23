package co.ke.tucode.buyer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import co.ke.tucode.buyer.entities.Africana_User;
import co.ke.tucode.buyer.entities.Role;
import co.ke.tucode.buyer.entities.CitizenCategory;
import co.ke.tucode.buyer.entities.DocUpload;
import co.ke.tucode.buyer.entities.Employment_Details;
import co.ke.tucode.buyer.entities.Employment_DetailsPayload;
import co.ke.tucode.buyer.entities.Family_Residence;
import co.ke.tucode.buyer.entities.Next_Of_Kin;
import co.ke.tucode.buyer.entities.Ownership_Prefference;
import co.ke.tucode.buyer.entities.Personal_Info;
import co.ke.tucode.buyer.entities.UserRole;
import co.ke.tucode.buyer.payloads.CitizenCategoryPayload;
import co.ke.tucode.buyer.payloads.Family_ResidencePayload;
import co.ke.tucode.buyer.payloads.LoginRequest;
import co.ke.tucode.buyer.payloads.Next_Of_KinPayload;
import co.ke.tucode.buyer.payloads.Ownership_PrefferencePayload;
import co.ke.tucode.buyer.payloads.Personal_InfoPayload;
import co.ke.tucode.buyer.repositories.CitizenCategoryRepository;
import co.ke.tucode.buyer.repositories.Employment_DetailsRepository;
import co.ke.tucode.buyer.repositories.Family_ResidenceRepository;
import co.ke.tucode.buyer.repositories.Next_Of_KinRepository;
import co.ke.tucode.buyer.repositories.Ownership_PrefferenceRepository;
import co.ke.tucode.buyer.repositories.Personal_InfoRepository;
import co.ke.tucode.buyer.repositories.UserRoleRepository;
import co.ke.tucode.buyer.services.Africana_UserService;
import co.ke.tucode.configjwt.JwtAuthenticationFilter;
//import co.ke.tucode.africana_api.services.FilePreview;
import co.ke.tucode.configjwt.TokenProviderTuCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
// @RequestMapping
public class UserController {

    @Autowired
    private Africana_UserService service;

    @Autowired
    private CitizenCategoryRepository citizenCategoryRepository;

    @Autowired
    private Personal_InfoRepository personal_InfoRepository;

    @Autowired
    private Family_ResidenceRepository family_ResidenceRepository;

    @Autowired
    private Next_Of_KinRepository next_Of_KinRepository;

    @Autowired
    private Employment_DetailsRepository employment_DetailsRepository;

    @Autowired
    private Ownership_PrefferenceRepository ownership_PrefferenceRepository;

    @Autowired
    private TokenProviderTuCode tokenProviderTuCode;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /*
     * @RequestMapping("/")
     * public String root(Model model){
     * 
     * Certificate certificate = new Certificate();
     * model.addAttribute("certificate", certificate);
     * 
     * return "index";
     * }
     */
    /*
     * .......................obr_get_service retrieve all db
     * data.............................
     */
    @CrossOrigin(origins = {"https://www.boreshamaisha.tucode.co.ke", "http://localhost:3000"}, allowedHeaders = "X-Requested-With, Content-Type, Authorization,Origin, Accept, Access-Control-Request-Method", methods = RequestMethod.GET)
    @RequestMapping(value = "/get_service", method = RequestMethod.GET)
    public ResponseEntity<?> get_service() {
        List<Africana_User> certificates = service.findAll();

        if (certificates.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        return new ResponseEntity(certificates, HttpStatus.OK);
    }

    /*
     * .......................obr_get_service retrieve all db
     * data.............................
     */
    @GetMapping("/get_user_data/{user}")
    public ResponseEntity<?> get_email(@PathVariable String email) {
        List<Africana_User> users = service.findByEmail(email);
        if (users.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity(users.get(0), HttpStatus.OK);

    }

    @RequestMapping(value = "/get_user", method = RequestMethod.GET)
    public ResponseEntity<?> get_user(@RequestParam(name = "email") String email) {
        List<Africana_User> users = service.findByEmail(email);

        if (users.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        return new ResponseEntity(users.get(0), HttpStatus.OK);
    }

    /*
     * .......................obr_get_service_id retrieve specific db
     * data.............................
     */
    @RequestMapping(value = "/login_request", method = RequestMethod.POST)
    public ResponseEntity<?> login_request(@Valid @RequestBody LoginRequest request) {

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        List<Africana_User> user = service.findByEmail(request.getEmail());

        if (user.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        else {
            if (passwordEncoder.matches(request.getPassword(), user.get(0).getPassword())) {
                return new ResponseEntity(tokenProviderTuCode.generateToken(
                        service.loadUserByUsername(user.get(0).getEmail())), HttpStatus.OK);

                // return new ResponseEntity(user.get(0), HttpStatus.OK);

            }

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /*
     * .......................obr_post_service insert db
     * data.............................
     */
    @RequestMapping(value = "/post_service", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> post_service(@ModelAttribute Africana_User user,
            UriComponentsBuilder builder) {
        if (service.existsByEmail(user.getEmail()))
            return new ResponseEntity(null, HttpStatus.CONFLICT);

        else {
            // user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setAccess("Buyer");
            user.setCitizenCategory(new CitizenCategory(null, null, null, null, user.getUser_signature()));
            user.setPersonal_Info(new Personal_Info(null, null, null, null, null, null, null, null, null, null,
                    user.getUser_signature()));
            user.setResidence(new Family_Residence(null, null, null, null, null, user.getUser_signature()));
            user.setNext_Of_Kin(new Next_Of_Kin(null, null, null, null, user.getUser_signature(), null, null, null));
            user.setEmployment_Details(
                    new Employment_Details(null, null, null, null, null, null, null, null, user.getUser_signature()));
            user.setOwnership_Prefference(new Ownership_Prefference(null, null, null, null, user.getUser_signature()));
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            service.save(user);

            // return new ResponseEntity(tokenProviderTuCode.generateToken(
            // service.loadUserByUsername(user.get(0).getEmail())), HttpStatus.OK);

            return new ResponseEntity(user, HttpStatus.CREATED);
        }
    }

    /*
     * .......................obr_post_service insert db
     * data.............................
     */
    @RequestMapping(value = "/post_citizen", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> post_citizen(@ModelAttribute CitizenCategoryPayload categoryPayload,
            UriComponentsBuilder builder) {
        Africana_User user = null;
        CitizenCategory citizenCategory = null;
        if (service.existsByEmail(categoryPayload.getUser_signature())) {
            citizenCategory = citizenCategoryRepository
                    .findById(service.findByEmail(categoryPayload.getUser_signature()).get(0).getCategoryId())
                    .stream().collect(Collectors.toList()).get(0);
            citizenCategory.setCivil_servant(categoryPayload.getSelectval01());
            citizenCategory.setDefence_force(categoryPayload.getSelectval02());
            citizenCategory.setSacco_member(categoryPayload.getSelectval03());
            user = service.findByEmail(categoryPayload.getUser_signature()).get(0);
            user.setCitizenCategory(citizenCategory);
            service.save(user);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    /*
     * .......................obr_post_service insert db
     * data.............................
     */
    @RequestMapping(value = "/post_person_info", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> post_person_info(@ModelAttribute Personal_InfoPayload personal_InfoPayload,
            UriComponentsBuilder builder) {
        Africana_User user = null;
        Personal_Info personal_Info = null;
        if (service.existsByEmail(personal_InfoPayload.getUser_signature())) {
            personal_Info = personal_InfoRepository
                    .findById(service.findByEmail(personal_InfoPayload.getUser_signature()).get(0).getPersonal_InfoID())
                    .stream().collect(Collectors.toList()).get(0);
            personal_Info.setDisability(personal_InfoPayload.getAbility());
            personal_Info.setDob(personal_InfoPayload.getDate());
            personal_Info.setEmail(personal_InfoPayload.getEmail());
            personal_Info.setId_number(personal_InfoPayload.getNumber());
            personal_Info.setKra_pin(personal_InfoPayload.getAlphanum());
            personal_Info.setMarital_status(personal_InfoPayload.getStatus());
            personal_Info.setPhone_number(personal_InfoPayload.getDigit());
            personal_Info.setResidence(personal_InfoPayload.getName());
            personal_Info.setState_city(personal_InfoPayload.getState());
            personal_Info.setUser_signature(personal_InfoPayload.getUser_signature());
            user = service.findByEmail(personal_InfoPayload.getUser_signature()).get(0);
            user.setPersonal_Info(personal_Info);
            service.save(user);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    /*
     * .......................obr_post_service insert db
     * data.............................
     */
    @RequestMapping(value = "/post_next_kin", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> post_next_kin(@ModelAttribute Next_Of_KinPayload next_Of_KinPayload,
            UriComponentsBuilder builder) {
        Africana_User user = null;
        Next_Of_Kin next_Of_Kin = null;
        if (service.existsByEmail(next_Of_KinPayload.getUser_signature())) {
            next_Of_Kin = next_Of_KinRepository
                    .findById(service.findByEmail(next_Of_KinPayload.getUser_signature()).get(0).getNext_Of_KinID())
                    .stream().collect(Collectors.toList()).get(0);
            next_Of_Kin.setFull_name(next_Of_KinPayload.getFname());
            next_Of_Kin.setFull_name2(next_Of_KinPayload.getLname());
            next_Of_Kin.setId_number(next_Of_KinPayload.getIdnumber());
            next_Of_Kin.setId_number2(next_Of_KinPayload.getId_number());
            next_Of_Kin.setPhone_number(next_Of_KinPayload.getPhonenumber());
            next_Of_Kin.setPhone_number2(next_Of_KinPayload.getPhone_number());
            user = service.findByEmail(next_Of_KinPayload.getUser_signature()).get(0);
            user.setNext_Of_Kin(next_Of_Kin);
            service.save(user);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    /*
     * .......................obr_post_service insert db
     * data.............................
     */
    @RequestMapping(value = "/post_residence", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> post_residence(@ModelAttribute Family_ResidencePayload family_ResidencePayload,
            UriComponentsBuilder builder) {
        Africana_User user = null;
        Family_Residence family_Residence = null;
        if (service.existsByEmail(family_ResidencePayload.getUser_signature())) {
            family_Residence = family_ResidenceRepository
                    .findById(service.findByEmail(family_ResidencePayload.getUser_signature()).get(0).getResidenceID())
                    .stream().collect(Collectors.toList()).get(0);
            family_Residence.setChildren(family_ResidencePayload.getResdigit());
            family_Residence.setCurrent_residence(family_ResidencePayload.getSelectval02());
            family_Residence.setLive_with_family(family_ResidencePayload.getSelectval03());
            family_Residence.setRental(family_ResidencePayload.getResnumber());
            user = service.findByEmail(family_ResidencePayload.getUser_signature()).get(0);
            user.setResidence(family_Residence);
            service.save(user);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    /*
     * .......................obr_post_service insert db
     * data.............................
     */
    @RequestMapping(value = "/post_emp_details", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> post_emp_details(@ModelAttribute Employment_DetailsPayload payload,
            UriComponentsBuilder builder) {
        Africana_User user = null;
        Employment_Details employment_Details = null;
        if (service.existsByEmail(payload.getUser_signature())) {
            employment_Details = employment_DetailsRepository
                    .findById(service.findByEmail(payload.getUser_signature()).get(0).getEmployment_DetailsID())
                    .stream().collect(Collectors.toList()).get(0);
            employment_Details.setCurrent_employer_category(payload.getSelect6());
            employment_Details.setDescribe_income(payload.getSelect4());
            employment_Details.setEmployement_period(payload.getSelect5());
            employment_Details.setEmployement_status(payload.getSelect3());
            employment_Details.setEmployment_number(payload.getEmpnum());
            employment_Details.setIncome_range(payload.getSelect1());
            employment_Details.setOccupation_qualification(payload.getSelect2());
            user = service.findByEmail(payload.getUser_signature()).get(0);
            user.setEmployment_Details(employment_Details);
            service.save(user);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    /*
     * .......................obr_post_service insert db
     * data.............................
     */
    @RequestMapping(value = "/post_home_ownership", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> post_home_ownership(@ModelAttribute Ownership_PrefferencePayload payload,
            UriComponentsBuilder builder) {
        Africana_User user = null;
        Ownership_Prefference ownership_Prefference = null;
        if (service.existsByEmail(payload.getUser_signature())) {
            ownership_Prefference = ownership_PrefferenceRepository
                    .findById(service.findByEmail(payload.getUser_signature()).get(0).getOwnership_PrefferenceID())
                    .stream().collect(Collectors.toList()).get(0);

            ownership_Prefference.setHome_type(payload.getTypology());
            ownership_Prefference.setOwning_reason(payload.getVal01());
            ownership_Prefference.setPreffered_country(payload.getVal02());
            user = service.findByEmail(payload.getUser_signature()).get(0);
            user.setOwnership_Prefference(ownership_Prefference);
            service.save(user);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    // /* .......................obr_put_file upload db
    // data.............................*/
    // @RequestMapping(value = "/put_file/{id}", method = RequestMethod.PUT
    // , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // public ResponseEntity<?> put_file(@PathVariable("id") long id
    // , @RequestParam("file") MultipartFile file) {
    // Africana_User certificate = service.findById(id);

    // if (certificate == null)
    // return new ResponseEntity("Invalid Id No.", HttpStatus.NOT_FOUND);

    // else {
    // if (!file.isEmpty()) {
    // try {
    // Byte[] bytes = new Byte[file.getBytes().length];

    // int i = 0;
    // for (Byte b : file.getBytes()) {
    // bytes[i++] = b;
    // }

    // certificate.setFathers_id(bytes);
    // } catch (IOException e) {
    // return new ResponseEntity(e, HttpStatus.EXPECTATION_FAILED);
    // }

    // service.update(certificate);
    // }

    // else
    // return new ResponseEntity("kindly choose a file",
    // HttpStatus.EXPECTATION_FAILED);
    // }

    // return new ResponseEntity(certificate, HttpStatus.OK);
    // }

    /*
     * .......................obr_delete_service update db
     * data.............................
     */
    @RequestMapping(value = "/delete_service/{email}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
        List<Africana_User> certificate = service.findByEmail(email);

        if (certificate.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        service.deleteByEmail(email);

        return new ResponseEntity(certificate.get(0), HttpStatus.NO_CONTENT);
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
