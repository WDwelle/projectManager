package com.wdwelle.projectManager.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.wdwelle.projectManager.models.Project;
import com.wdwelle.projectManager.services.MainService;

@Controller
public class ProjectController {

	@Autowired
	private MainService mainService;
	
	
	
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model) {
		if(session.getAttribute("userId")==null) {
			return "redirect:/";
		}
		List<Project> projects = mainService.allProjects();
		model.addAttribute("projects", projects);
		
		return "dashboard.jsp";
	}
	
	//create -- getMapping, postMapping
	@GetMapping("/projects/add")
	public String createProjectForm(@ModelAttribute("project")Project project, HttpSession session) {	
		if(session.getAttribute("userId")==null) {
			return "redirect:/";
		}
		return "addProject.jsp";
	}
	
	@PostMapping("/projects/add")
	public String processCreateProject(@Valid @ModelAttribute("project")Project project, 
			BindingResult result, HttpSession session){
		if(session.getAttribute("userId")==null) {
			return "redirect:/";
		}
		if(result.hasErrors()) {
			return "addProject.jsp";
		}else {
			mainService.addProject(project);
			return "redirect:/dashboard";
		}
	}
	
	//edit -- getMapping, postMapping
	@GetMapping("/projects/{id}/edit")
	public String editProjectForm(@PathVariable("id")Long id, HttpSession session, Model model) {
		Project project = mainService.findOneProject(id);
		// check if the user is the project creator
		Long userId = (Long) session.getAttribute("userId");
		if(!project.getCreator().getId().equals(userId)) {
			return "redirect:/dashboard";
		}
		model.addAttribute("project", project);
		return "editProject.jsp";
	}
	
	//edit form: 1. PUT route includes id 2. add hidden method PUT 3. path should be called id not projectId
	@PutMapping("/projects/{id}/edit")
	public String processEditProject(@Valid @ModelAttribute("project")Project project, 
			BindingResult result, HttpSession session){
		// check if the user is the project creator
		Long userId = (Long) session.getAttribute("userId");
		if(!project.getCreator().getId().equals(userId)) {
			return "redirect:/dashboard";
		}
		if(result.hasErrors()) {
			return "editProject.jsp";
		}else {
			mainService.editProject(project);
			return "redirect:/dashboard";
		}
	}
	
	@DeleteMapping("/projects/{id}/delete")
	public String processDelete(@PathVariable("id")Long id, HttpSession session) {
		if(session.getAttribute("userId")==null) {
			return "redirect:/";
		}
		mainService.deleteProject(id);
		return "redirect:/dashboard";
		
	}
	
	@GetMapping("/projects/{id}")
	public String showOneProject(@PathVariable("id")Long id,Model model,  HttpSession session) {
		if(session.getAttribute("userId")==null) {
			return "redirect:/";
		}
		Project project = mainService.findOneProject(id);
		model.addAttribute("project", project);
		return "showOneProject.jsp";
		
	}
}
