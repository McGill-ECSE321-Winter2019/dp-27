package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.StudentService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
    /**
     * Used to create the set of students each semester
     * 
     * @param file
     * @return List<StudentDto> of students added
     * @throws Exception
     */
    @PostMapping("")
    public List<Student> importCSVFile(@ModelAttribute("file") MultipartFile file) throws Exception {
    	String line;
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        List<Student> students = new ArrayList<>();
        br.readLine();
		while((line = br.readLine()) != null) {
			String[] csv = line.split(",");
			//The char replace is to remove random # in each entry of the csv
			students.add(studentService.createStudent(csv[3].replace("#", ""), csv[2].replace("#", ""), csv[4].replace("#", ""), csv[0].replace("#", "")));
		}

		return students;
    	
	}
}