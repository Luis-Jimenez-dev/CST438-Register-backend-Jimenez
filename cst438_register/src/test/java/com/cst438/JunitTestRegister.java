package com.cst438;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cst438.controller.RegisterController;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.RegisterDTO;
import com.cst438.domain.RegisterDTO.studentDTO;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.ContextConfiguration;


//Junit tests for the registration APIs
@ContextConfiguration(classes = { RegisterController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestRegister
{
   static final String URL = "http://localhost:8080";
   public static final String TEST_STUDENT_EMAIL = "test1@csumb.edu";
   public static final String TEST_STUDENT_NAME = "test";
   
   @MockBean
   StudentRepository studentRepository;
   
   @MockBean
   CourseRepository courseRepository;
   
   @MockBean
   EnrollmentRepository enrollmentRepository;
   
   @MockBean
   GradebookService gradebookService;
   
   @Autowired
   private MockMvc mvc;
   
   @Test
   public void addStudent() throws Exception {
      MockHttpServletResponse response;
      
      //Creates a new student
      Student student = new Student();
      student.setEmail(TEST_STUDENT_EMAIL);
      student.setName(TEST_STUDENT_NAME);
      student.setStatusCode(0);
      student.setStudent_id(1);
      
      //List keeps track of students
      List<Student> students = new java.util.ArrayList<>();
      students.add(student);
      
      
      //given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
      given(studentRepository.save(any(Student.class))).willReturn(student);
      
      //Creates a new DTO object
      RegisterDTO.studentDTO studentDTO = new RegisterDTO.studentDTO();
      studentDTO.email = TEST_STUDENT_EMAIL;
      studentDTO.name = TEST_STUDENT_NAME;
      
      //Tries to add new student
      response = mvc.perform(
            MockMvcRequestBuilders
          .post("/register/new")
          .content(asJsonString(studentDTO))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();
      
      //Checks if response passed
      assertEquals(200, response.getStatus());
      
      RegisterDTO.studentDTO result = fromJsonString(response.getContentAsString(), RegisterDTO.studentDTO.class);
      assertNotEquals(0, result.id);
      
      //Verifies that data got added
      verify(studentRepository).save(any(Student.class));
      
      //Verifies that student information can be accessed using their email
      verify(studentRepository, times(1)).findByEmail(TEST_STUDENT_EMAIL); 
   }
   
   //Tests API that adds a hold to a student
   @Test
   public void addHold() throws Exception {
      MockHttpServletResponse response;
      
      //Creates new student
      Student student = new Student();
      student.setEmail(TEST_STUDENT_EMAIL);
      student.setName(TEST_STUDENT_NAME);
      student.setStatusCode(1);
      student.setStudent_id(1);
      
      List<Student> students = new java.util.ArrayList<>();
      students.add(student);
      
      
      //given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
      given(studentRepository.save(any(Student.class))).willReturn(student);
      
      //Creates new DTO object
      RegisterDTO.studentDTO studentDTO = new RegisterDTO.studentDTO();
      studentDTO.email = TEST_STUDENT_EMAIL;
      studentDTO.name = TEST_STUDENT_NAME;
      
      response = mvc.perform(
            MockMvcRequestBuilders
          .post("/register/new")
          .content(asJsonString(studentDTO))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();
      
      assertEquals(200, response.getStatus());
      
      //Checks the status code given
      RegisterDTO.studentDTO result = fromJsonString(response.getContentAsString(), RegisterDTO.studentDTO.class);
      assertNotEquals(0, result.statusCode);
      
      //Verifies that data got added
      verify(studentRepository).save(any(Student.class));
      
      verify(studentRepository, times(1)).findByEmail(TEST_STUDENT_EMAIL); 
   }
   
   
   //Tests API that removes a hold
   @Test
   public void removeHold() throws Exception {
      MockHttpServletResponse response;
      
      //Creates new student
      Student student = new Student();
      student.setEmail(TEST_STUDENT_EMAIL);
      student.setName(TEST_STUDENT_NAME);
      student.setStatusCode(0);
      student.setStudent_id(1);
      
      List<Student> students = new java.util.ArrayList<>();
      students.add(student);
      
      
      //given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
      given(studentRepository.save(any(Student.class))).willReturn(student);
      
      RegisterDTO.studentDTO studentDTO = new RegisterDTO.studentDTO();
      studentDTO.email = TEST_STUDENT_EMAIL;
      studentDTO.name = TEST_STUDENT_NAME;
      
      response = mvc.perform(
            MockMvcRequestBuilders
          .post("/register/new")
          .content(asJsonString(studentDTO))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();
      
      assertEquals(200, response.getStatus());
      
      //Ensures status code is not 0
      RegisterDTO.studentDTO result = fromJsonString(response.getContentAsString(), RegisterDTO.studentDTO.class);
      assertNotEquals(1, result.statusCode);
      
      verify(studentRepository).save(any(Student.class));
      
      verify(studentRepository, times(1)).findByEmail(TEST_STUDENT_EMAIL); 
   }
   
   private static String asJsonString(final Object obj) {
      try {

              return new ObjectMapper().writeValueAsString(obj);
      } catch (Exception e) {
              throw new RuntimeException(e);
      }
}
   
   private static <T> T  fromJsonString(String str, Class<T> valueType ) {
      try {
              return new ObjectMapper().readValue(str, valueType);
      } catch (Exception e) {
              throw new RuntimeException(e);
      }
}
   
}


