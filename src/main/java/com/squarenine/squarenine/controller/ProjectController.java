package com.squarenine.squarenine.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.squarenine.squarenine.entity.ProjectEntity;
import com.squarenine.squarenine.entity.SubProjectEntity;
import com.squarenine.squarenine.service.ProjectService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/project/get/{id}")
    private Optional<ProjectEntity> getProject(@PathVariable Long id){
        return projectService.getProject(id);
    }

    @GetMapping("/project/get/all")
    private List<ProjectEntity> getAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping("/project/sub/get/{id}")
    private Optional<SubProjectEntity> getSubProject(@PathVariable Long id){
        return projectService.getSubProject(id);
    }

    @GetMapping("/project/sub/get/pid/{id}")
    private List<SubProjectEntity> getSubProjectByPid(@PathVariable Long id){
        return projectService.getAllSubProjectsByPid(id);
    }
}
