package com.squarenine.squarenine.service.impl;

import com.squarenine.squarenine.entity.ProjectEntity;
import com.squarenine.squarenine.entity.SubProjectEntity;
import com.squarenine.squarenine.repository.ProjectRepository;
import com.squarenine.squarenine.repository.SubProjectRepository;
import com.squarenine.squarenine.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

  private ProjectRepository projectRepository;
  private SubProjectRepository subProjectRepository;


  public ProjectServiceImpl(ProjectRepository projectRepository, SubProjectRepository subProjectRepository) {
    this.projectRepository = projectRepository;
    this.subProjectRepository = subProjectRepository;
  }

  @Override
  public Optional<ProjectEntity> getProject(Long id) {
    return projectRepository.findById(id);
  }

  @Override
  public List<ProjectEntity> getAllProjects() {
    return projectRepository.findAll();
  }

  @Override
  public Optional<SubProjectEntity> getSubProject(Long id) {
    return subProjectRepository.findById(id);
  }

  @Override
  public List<SubProjectEntity> getAllSubProjectsByPid(Long id) {
    return subProjectRepository.findByPId(id);
  }
}
