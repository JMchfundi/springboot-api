package co.ke.tucode.buyer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.ke.tucode.buyer.entities.Africana_User;
import co.ke.tucode.buyer.repositories.Africana_UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class Africana_UserService {

    @Autowired
    private Africana_UserRepository repository;

    public void save(Africana_User certificate){
        repository.save(certificate);
    }

    public List<Africana_User> findAll(){
        return repository.findAll();
    }

    public void update(Africana_User certificate){
        repository.save(certificate);
    }

    public void deleteByEmail(String email) {
        repository.deleteByEmail(email);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public List<Africana_User> findByEmail(String email){
        return repository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public Long count() {
        return repository.count();
    }
}
