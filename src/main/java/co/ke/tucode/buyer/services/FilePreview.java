package co.ke.tucode.buyer.services;
// package co.ke.tucode.africana_api.services;

// import org.springframework.core.io.Resource;
// import org.springframework.core.io.UrlResource;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// import co.ke.tucode.africana_api.entities.Africana_User;

// import java.net.MalformedURLException;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// public class FilePreview {

//     public static class TempFile{
//         public static String tempFilePath (Africana_User certificate){
//             return System.getProperty("java.io.tmpdir")+"//"+certificate.getChild_name()+"fathersid.pdf";
//         }
//     }

//     public ResponseEntity preview(Africana_UserService service, Long id){
//         Africana_User certificate = service.findById(id);

//         if (certificate == null)
//             return new ResponseEntity("no data found for"+certificate.getChild_name(), HttpStatus.NOT_FOUND);

//         else {
//             String filename = TempFile.tempFilePath(certificate);

//             Resource resource = null;

//             try {
//                 resource = new UrlResource(Paths.get(filename).toUri());
//             } catch (MalformedURLException e) {
//                 e.printStackTrace();
//             }

//             if (resource.exists()){
//                 return ResponseEntity.ok()
//                         .header(HttpHeaders.CONTENT_DISPOSITION, "found")
//                         .body(resource);

//             }
//         }

//         return new ResponseEntity("done"+certificate.getChild_name(), HttpStatus.NOT_FOUND);
//     }
// }
