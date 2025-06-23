package com.squarenine.squarenine.service;

import java.util.List;
import java.util.Optional;

import com.squarenine.squarenine.entity.ProjectEntity;
import com.squarenine.squarenine.entity.SubProjectEntity;

public interface ProjectService {
  Optional<ProjectEntity> getProject(Long id);

  List<ProjectEntity> getAllProjects();

  Optional<SubProjectEntity> getSubProject(Long id);

  List<SubProjectEntity> getAllSubProjectsByPid(Long id);
}
