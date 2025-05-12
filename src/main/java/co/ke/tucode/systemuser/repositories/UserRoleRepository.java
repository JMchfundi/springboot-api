package co.ke.tucode.systemuser.repositories;

// import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.tucode.systemuser.entities.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    // UserRole findUserRole
 //   public boolean existsByEmail(String email); 
//    public void deleteByEmail(String email);    
}
