package com.cst438.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.RegisterDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.domain.RegisterDTO.studentDTO;
import com.cst438.service.GradebookService;

//Controller for registering students
@RestController
public class RegisterController
{
   @Autowired
   EnrollmentRepository enrollmentRepository;
   
   @Autowired
   StudentRepository studentRepository;
   
   @PostMapping("/addStudent")
   @Transactional
   
   //Registers students if email is not in database
   public RegisterDTO.studentDTO addStudent(@RequestBody RegisterDTO.studentDTO studentDTO) {
      String student_email = studentDTO.email;
      
      //Chekcs if student's email is registered
      Student check = studentRepository.findByEmail(student_email);
      
      //Registers new student
      if (check == null) {
         Student student = new Student();
         student.setEmail(student_email);
         student.setName(studentDTO.name);
         student.setStatusCode(0);
         Student saveStudent = studentRepository.save(student);
         RegisterDTO.studentDTO result = createStudentDTO(saveStudent);
         return result;
      }else {
         throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "There is already a student registered with that email "+studentDTO.email);
      }
   }
   
   
   // Was trying to add a function that returns a registered student
   /*@GetMapping("/register/findStudent")
   public RegisterDTO.studentDTO getStudent(@RequestParam("email")String email, @RequestParam("name") String name) {
      String student_email = email;
      Student student = studentRepository.findByEmail(student_email);
      
      if (student != null) {
         RegisterDTO.studentDTO result = createStudentDTO(student);
         return result;
      } else {
         throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student not found. " );
      }
   }*/
   
   
   //Changes Status code to a non zero number
   @PostMapping("/register/addHold")
   @Transactional
   public RegisterDTO.studentDTO addHold (@RequestBody RegisterDTO.studentDTO studentDTO){
      //Chekcs if student is registered
      String student_email = studentDTO.email;
      Student check = studentRepository.findByEmail(student_email);
      
      //If student is register then it changes the status code
      if (check != null) {
         check.setStatusCode(1);
         check.setStatus("Hold");
         RegisterDTO.studentDTO result = createStudentDTO(check);
         return result;
      } else {
         throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "There is no student registered with that email. " + studentDTO.email);
      }
   }
   
   //Removes a hold of a student by changing status code to 0
   @PostMapping("/register/removeHold")
   @Transactional
   public RegisterDTO.studentDTO removeHold (@RequestBody RegisterDTO.studentDTO studentDTO){
      //Checks if student is registered
      String student_email = studentDTO.email;
      Student check = studentRepository.findByEmail(student_email);
      
      //If student is registered then it sets status code to 0
      if (check != null) {
         check.setStatusCode(0);
         check.setStatus("Hold");
         RegisterDTO.studentDTO result = createStudentDTO(check);
         return result;
      } else {
         throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "There is no student registered with that email. " + studentDTO.email);
      }
   }
   
   
   /*
      Helper Functions
   */
   //Creates the students DTO
   private RegisterDTO.studentDTO createStudentDTO(Student s){
      RegisterDTO.studentDTO studentDTO = new RegisterDTO.studentDTO();
      studentDTO.id = s.getStudent_id();
      studentDTO.name = s.getName();
      studentDTO.email = s.getEmail();
      studentDTO.statusCode = s.getStatusCode();
      return studentDTO;
   }
   
   private RegisterDTO createStudent (int id, String email, String name, int statusCode) {
      RegisterDTO result = new RegisterDTO();
      result.id = id;
      result.email = email;
      result.name = name;
      result.statusCode = statusCode;
      return result;
      
      //Tried making a list of students 
      /*ArrayList<RegisterDTO.studentDTO> student = new ArrayList<>();
      
      for (Student s: students) {
         RegisterDTO.studentDTO studentDTO = createStudentDTO(s);
         student.add(studentDTO);
      }
      result.students = student;*/
   }
   

}
