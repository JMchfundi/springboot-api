package co.ke.tucode.systemuser.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.tucode.systemuser.entities.Africana_User;
import co.ke.tucode.systemuser.entities.CitizenCategory;
import co.ke.tucode.systemuser.entities.Ownership_Prefference;

public interface Ownership_PrefferenceRepository extends JpaRepository<Ownership_Prefference, Integer> {
//    public List<Africana_User> findByEmail(String email);
 //   public boolean existsByEmail(String email); 
//    public void deleteByEmail(String email);    
}
