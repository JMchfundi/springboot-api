package co.ke.tucode.systemuser.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

//import co.ke.tucode.africana_api.services.FilePreview;
import co.ke.tucode.configjwt.TokenProviderTuCode;
import co.ke.tucode.systemuser.entities.Africana_User;
import co.ke.tucode.systemuser.entities.Role;
import co.ke.tucode.systemuser.payloads.LoginRequest;
import co.ke.tucode.systemuser.repositories.CitizenCategoryRepository;
import co.ke.tucode.systemuser.repositories.Employment_DetailsRepository;
import co.ke.tucode.systemuser.repositories.Family_ResidenceRepository;
import co.ke.tucode.systemuser.repositories.Next_Of_KinRepository;
import co.ke.tucode.systemuser.repositories.Ownership_PrefferenceRepository;
import co.ke.tucode.systemuser.repositories.Personal_InfoRepository;
import co.ke.tucode.systemuser.services.Africana_UserService;
import jakarta.validation.Valid;
import java.util.List;

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
    // @CrossOrigin(origins = {"https://www.boreshamaisha.tucode.co.ke", "http://localhost:3000"}, allowedHeaders = "X-Requested-With, Content-Type, Authorization,Origin, Accept, Access-Control-Request-Method", methods = RequestMethod.GET)
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
    // @CrossOrigin(origins = {"https://www.boreshamaisha.tucode.co.ke", "http://localhost:3000"})
    @RequestMapping(value = "/login_request", method = RequestMethod.POST)
    public ResponseEntity<?> login_request(@Valid @RequestBody LoginRequest request) {

                final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);        
                return new ResponseEntity(new String(tokenProviderTuCode.generateRefinedToken(authentication)), HttpStatus.OK);

                // return new ResponseEntity(user.get(0), HttpStatus.OK);
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
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            service.save(user);

            // return new ResponseEntity(tokenProviderTuCode.generateToken(
            // service.loadUserByUsername(user.get(0).getEmail())), HttpStatus.OK);

            return new ResponseEntity(user, HttpStatus.CREATED);
        }
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


@GetMapping("/current_user")
public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    String email = userDetails.getUsername(); // Extract email from token

    List<Africana_User> users = service.findByEmail(email);

    if (users == null || users.isEmpty()) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("User not found with email: " + email);
    }

    // Safe to call .get(0) now
    return ResponseEntity.ok(users.get(0));
}

}
