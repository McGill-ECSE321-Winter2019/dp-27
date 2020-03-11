package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.CourseOfferingService;
import ca.mcgill.cooperator.service.StudentService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("csv-parser")
@RestController
@CrossOrigin(origins = "*")
public class CSVParserController {

    @Autowired private StudentService studentService;
    @Autowired private CoopService coopService;
    @Autowired private CourseOfferingService courseOfferingService;
    
    /**
     * Used to create the set of students each semester
     * 
     * @param file
     * @return List<StudentDto> of students added
     * @throws Exception
     */
    @PostMapping("")
    public List<String> importCSVFile(@ModelAttribute("file") MultipartFile file, @RequestParam("course_id") int courseOfferingId) throws Exception {
    	List<String> students = new ArrayList<>();
    	
    	String line;
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        br.readLine(); //Escapes the first line containing the headers
        
		while((line = br.readLine()) != null) {
			String studentEmail = line.split("#")[2].split(",")[0];
			students.add(studentEmail);
			System.out.println(studentEmail);
		}
				
    	CourseOffering courseOffering = courseOfferingService.getCourseOfferingById(courseOfferingId);
    	List<Coop> coops = coopService.getAllCoopsForCourseOffering(courseOffering);
    	for(Coop c: coops) {
    		students.remove(c.getStudent().getEmail());
    	}
		
		return students;
    	
	}
}