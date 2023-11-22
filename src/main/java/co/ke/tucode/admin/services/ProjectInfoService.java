package co.ke.tucode.admin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.ke.tucode.admin.entities.ProjectInfo;
import co.ke.tucode.admin.repositories.ProjectInfoRepo;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProjectInfoService {

    @Autowired
    private ProjectInfoRepo repository;

    public void save(ProjectInfo projectInfo){
        repository.save(projectInfo);
    }

    public List<ProjectInfo> findAll(){
        return repository.findAll();
    }

    public void update(ProjectInfo projectInfo){
        repository.save(projectInfo);
    }

    public void deleteByName(String Name) {
        repository.deleteByProjectname(Name);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public List<ProjectInfo> findByName(String Name){
        return repository.findByProjectname(Name);
    }

    public boolean existsByName(String Name) {
        return repository.existsByProjectname(Name);
    }

    public Long count() {
        return repository.count();
    }
}
