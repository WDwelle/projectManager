package com.wdwelle.projectManager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wdwelle.projectManager.models.Project;
import com.wdwelle.projectManager.repositories.ProjectRepository;

@Service
public class MainService {
	@Autowired
	private ProjectRepository projectRepo;

	// find all projects
	public List<Project> allProjects(){
		return projectRepo.findAll();
	}
	
	// create a project
	public Project addProject(Project project) {
		return projectRepo.save(project);
	}
	
	// edit a project
	public Project editProject(Project project) {
		return projectRepo.save(project);
	}	
	
	// show one project
	public Project findOneProject(Long id) {
		Optional<Project> optionalProject = projectRepo.findById(id);
		if(optionalProject.isPresent()) {
			return optionalProject.get();
		}else {
			return null;
		}
	}
	
	// delete a project by id
	public void deleteProject(Long id) {
		projectRepo.deleteById(id);
	}
}
