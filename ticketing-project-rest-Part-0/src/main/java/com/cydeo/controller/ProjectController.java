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

import javax.annotation.security.RolesAllowed;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    ProjectService projectService;

    @GetMapping
    public ResponseEntity<ResponseWrapper> getProjects() {
        List<ProjectDTO> projectDTOList = projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("Projects are successfully retrieved", projectDTOList, HttpStatus.OK));
    }

    @GetMapping("/{code}")
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable("code") String code) {
        ProjectDTO projectDTO = projectService.getByProjectCode(code);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully retrieved", projectDTO, HttpStatus.OK));
    }

    @PostMapping
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO project) {
        projectService.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Project is successfully created", HttpStatus.CREATED));
    }

    @PutMapping
    @RolesAllowed({"Manager"})
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO project) {
        projectService.update(project);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully updated", project, HttpStatus.OK));
    }

    @RolesAllowed({"Manager"})
    @DeleteMapping("/{projectCode}")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectCode") String code) {
        projectService.delete(code);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully deleted", HttpStatus.OK));
    }

    @RolesAllowed({"Manager"})
    @GetMapping("/manager/project-status")
    public ResponseEntity<ResponseWrapper> getProjectByManager() {
        List<ProjectDTO> projects = projectService.listAllProjectDetails();
        return ResponseEntity.ok(new ResponseWrapper("Projects are successfully retrieved", projects, HttpStatus.OK));
    }

    @RolesAllowed({"Manager"})
    @PutMapping("/manager/complete/{projectCode}")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode") String code) {
        projectService.complete(code);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully completed", HttpStatus.OK));
    }

}
