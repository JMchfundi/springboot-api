package co.ke.tucode.buyer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.ke.tucode.buyer.entities.Africana_User;
import co.ke.tucode.buyer.repositories.Africana_UserRepository;

import jakarta.transaction.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class Africana_UserService implements UserDetailsService {

    @Autowired
    private Africana_UserRepository repository;

    public void save(Africana_User certificate) {
        repository.save(certificate);
    }

    public List<Africana_User> findAll() {
        return repository.findAll();
    }

    public void update(Africana_User certificate) {
        repository.save(certificate);
    }

    public void deleteByEmail(String email) {
        repository.deleteByEmail(email);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<Africana_User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public Long count() {
        return repository.count();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        List<Africana_User> users = repository.findByEmail(username);

        if (users.isEmpty()) {
            throw new UsernameNotFoundException(username + " doesn't exist");
        }

        return new User(users.get(0).getEmail(), users.get(0).getPassword(), users.get(0).getAuthorities());
    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // TODO Auto-generated method stub
                List<Africana_User> users = repository.findByEmail(username);

                if (users.isEmpty()) {
                    throw new UsernameNotFoundException(username + " doesn't exist");
                }

                return new User(users.get(0).getEmail(), users.get(0).getPassword(),  users.get(0).getAuthorities());
            }
        };
    }
}