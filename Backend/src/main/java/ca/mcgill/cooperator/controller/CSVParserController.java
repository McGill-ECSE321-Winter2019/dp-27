package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.CourseOfferingService;
import java.io.BufferedReader;
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

    @Autowired private CoopService coopService;
    @Autowired private CourseOfferingService courseOfferingService;

    /**
     * Used to create the set of Students who have not registered on Cooperator
     *
     * @param file
     * @param courseOfferingId
     * @return List<StudentDto>
     * @throws Exception
     */
    @PostMapping("check-registered")
    public List<String> checkStudentsRegistered(
            @ModelAttribute("file") MultipartFile file,
            @RequestParam("course_id") int courseOfferingId)
            throws Exception {
        List<String> students = new ArrayList<>();

        String line;
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        br.readLine(); // Escapes the first line containing the headers

        while ((line = br.readLine()) != null) {
            String studentEmail = line.split("#")[2].split(",")[0];
            students.add(studentEmail);
        }

        CourseOffering courseOffering =
                courseOfferingService.getCourseOfferingById(courseOfferingId);
        List<Coop> coops = coopService.getAllCoopsForCourseOffering(courseOffering);
        for (Coop c : coops) {
            students.remove(c.getStudent().getEmail());
        }
        return students;
    }

    /**
     * Used to create the set of Students who have not enrolled on Minerva
     *
     * @param file
     * @param courseOfferingId
     * @return List<StudentDto>
     * @throws Exception
     */
    @PostMapping("/check-enrollment")
    public List<String> checkStudentsEnrolled(
            @ModelAttribute("file") MultipartFile file,
            @RequestParam("course_id") int courseOfferingId)
            throws Exception {
        List<String> students = new ArrayList<>();

        CourseOffering courseOffering =
                courseOfferingService.getCourseOfferingById(courseOfferingId);
        List<Coop> coops = coopService.getAllCoopsForCourseOffering(courseOffering);
        for (Coop c : coops) {
            if(c.getStatus() != CoopStatus.UNDER_REVIEW){
                students.add(c.getStudent().getEmail());
            }
        }

        String line;
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        br.readLine(); // Escapes the first line containing the headers

        while ((line = br.readLine()) != null) {
            String studentEmail = line.split("#")[2].split(",")[0];
            students.remove(studentEmail);
        }
        return students;
    }
}
