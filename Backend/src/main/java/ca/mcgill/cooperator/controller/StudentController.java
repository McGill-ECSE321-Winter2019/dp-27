package ca.mcgill.cooperator.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("students")
public class StudentController {
	
	@Autowired StudentService studentService;

    @GetMapping("/{id}")
    public StudentDto getStudentById(@PathVariable int id) {
        return ControllerUtils.convertToDto(studentService.getStudentById(id));
    }
    
    @PostMapping("")
    public StudentDto createStudent(@RequestBody StudentDto studentDto) {
    	Student student = new Student();
    	student = studentService.createStudent(studentDto.getFirstName(), 
    										   studentDto.getLastName(), 
    										   studentDto.getEmail(), 
    										   studentDto.getStudentId());
    	
    	return ControllerUtils.convertToDto(student);
    }
}
