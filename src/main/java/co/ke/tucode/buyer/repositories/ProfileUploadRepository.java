package co.ke.tucode.buyer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.tucode.buyer.entities.DocUpload;
import co.ke.tucode.buyer.entities.ProfileUpload;

public interface ProfileUploadRepository extends JpaRepository<ProfileUpload, Long> {
   public List<ProfileUpload> findByName(String name);
   public List<ProfileUpload> findByUser(String user);
// public List<DocUpload> (String doc_name);
   // public boolean existsByDoc_name(String doc_name);
//    public List<Africana_User> findByEmail(String email);
      public boolean existsByName(String name); 
//    public void deleteByEmail(String email);    
}
