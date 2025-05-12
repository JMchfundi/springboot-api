package co.ke.tucode.systemuser.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.tucode.systemuser.entities.DocUpload;

public interface DocUploadRepository extends JpaRepository<DocUpload, String> {
   public List<DocUpload> findByName(String name);
   public List<DocUpload> findByUser(String user);
// public List<DocUpload> (String doc_name);
   // public boolean existsByDoc_name(String doc_name);
//    public List<Africana_User> findByEmail(String email);
 public boolean existsByName(String name); 
//    public void deleteByEmail(String email);    
}
