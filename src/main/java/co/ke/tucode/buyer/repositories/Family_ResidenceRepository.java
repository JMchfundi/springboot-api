package co.ke.tucode.buyer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.tucode.buyer.entities.Africana_User;
import co.ke.tucode.buyer.entities.CitizenCategory;
import co.ke.tucode.buyer.entities.Family_Residence;
import co.ke.tucode.buyer.entities.Personal_Info;

public interface Family_ResidenceRepository extends JpaRepository<Family_Residence, Integer> {
//    public List<Africana_User> findByEmail(String email);
 //   public boolean existsByEmail(String email); 
//    public void deleteByEmail(String email);    
}
