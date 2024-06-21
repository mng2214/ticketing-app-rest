package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    ProjectService projectService;

    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllProjects() {
        List<ProjectDTO> projectDTO = projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("List of all projects", projectDTO, HttpStatus.OK));
    }

    @GetMapping("/{projectCode}")
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable String projectCode) {
        ProjectDTO byProjectCode = projectService.getByProjectCode(projectCode);
        return ResponseEntity.ok(new ResponseWrapper(byProjectCode.toString(), byProjectCode, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO project) {
        projectService.save(project);
        return ResponseEntity.ok(new ResponseWrapper("Project created", project, HttpStatus.CREATED));
    }

    @PutMapping()
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO project) {
        projectService.save(project);
        return ResponseEntity.ok(new ResponseWrapper("Project updated", project, HttpStatus.OK));
    }

    @DeleteMapping("/{projectCode}")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectCode") String projectCode) {
        projectService.delete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project deleted", HttpStatus.NO_CONTENT));
    }

    @GetMapping("/manager/project-status")
    public ResponseEntity<ResponseWrapper> getProjectByManager() {
        List<ProjectDTO> projects = projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("Projects assigned to the manager", projects, HttpStatus.OK));
    }

    @GetMapping("/manager/complete/{projectCode}")
    public ResponseEntity<ResponseWrapper> completeProject(@PathVariable("projectCode") String projectCode) {
        projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project completed", projectService.getByProjectCode(projectCode), HttpStatus.OK));
    }


}
