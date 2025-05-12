package co.ke.tucode.systemuser.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.tucode.systemuser.entities.Africana_User;
import co.ke.tucode.systemuser.entities.CitizenCategory;
import co.ke.tucode.systemuser.entities.Employment_Details;

public interface Employment_DetailsRepository extends JpaRepository<Employment_Details, Integer> {
//    public List<Africana_User> findByEmail(String email);
 //   public boolean existsByEmail(String email); 
//    public void deleteByEmail(String email);    
}
