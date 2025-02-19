package co.ke.tucode.admin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.ke.tucode.admin.entities.ProjectInfo;
import co.ke.tucode.admin.entities.ProjectLocation;
import co.ke.tucode.admin.repositories.ProjectInfoRepo;
import co.ke.tucode.admin.repositories.ProjectLocationRepo;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectLocationService {

    @Autowired
    private ProjectLocationRepo repository;

    public void save(ProjectLocation projectLocation){
        repository.save(projectLocation);
    }

    public List<ProjectLocation> findAll(){
        return repository.findAll();
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public List<ProjectLocation> findById(Integer integer){
        return repository.findById(integer).stream().collect(Collectors.toList());
    }

    public boolean existsById(Integer integer) {
        return repository.existsById(integer);
    }

    public Long count() {
        return repository.count();
    }
}
