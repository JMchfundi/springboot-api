package co.ke.tucode.admin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.tucode.admin.entities.ProjectUpload;


public interface ProjectUploadRepo extends JpaRepository<ProjectUpload, Integer> {
    //public List<ProjectUpload> findByName(String string);
    // public boolean existsByEmail(String email); 
    // public void deleteByEmail(String email);    
}
