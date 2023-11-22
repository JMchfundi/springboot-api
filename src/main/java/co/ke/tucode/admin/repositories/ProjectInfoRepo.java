package co.ke.tucode.admin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.tucode.admin.entities.ProjectInfo;

public interface ProjectInfoRepo extends JpaRepository<ProjectInfo, Integer> {
    public List<ProjectInfo> findByProjectname(String projectname);
    public boolean existsByProjectname(String name); 
    public void deleteByProjectname(String name);
}
