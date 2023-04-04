package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.StudentRepository;
import com.cst438.domain.Student;



@SpringBootTest
public class EndToEndRegistrationTest
{
   @Autowired
   StudentRepository studentRepository;
   
   public static final String CHROME_DRIVER_FILE_LOCATION = "C:/Users/Luis/Downloads/chromedriver_win32/chromedriver.exe";
   public static final String URL = "http://localhost:3000";
   public static final String TEST_USER_EMAIL = "test2@csumb.edu";
   public static final int TEST_COURSE_ID = 40443;
   public static final String TEST_SEMESTER = "2021 Fall";
   public static final int SLEEP_DURATION = 1000;
   public static final String TEST_USER_NAME = "test";
   
   @Test
   public void addStudent() throws Exception {
      Student check = null;
      
      //Checks if the student is already in the repository
      do {
         check = studentRepository.findByEmail(TEST_USER_EMAIL);
         if (check != null)
            studentRepository.delete(check);
      } while(check != null);
      
      //Starts up the Chrome driver
      System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
      WebDriver driver = new ChromeDriver();
      
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      
      //Tries to add a new student
      try {
         driver.get(URL);
         Thread.sleep(SLEEP_DURATION);
         
         //Selects the semester
         WebElement we = driver.findElement(By.xpath("(//input [@type='radio']) [last ()]"));
         we.click();
         
         //Clicks on the add student button
         driver.findElement(By.xpath("//a[@id='add_student']")).click();
         Thread.sleep(SLEEP_DURATION);
         
         //Inputs the data into the value fields
         driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_USER_NAME);
         driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_USER_EMAIL);
         driver.findElement(By.xpath("//button")).click();
         Thread.sleep(SLEEP_DURATION);
         
         //Checks if the student was added to the database
         Student s = studentRepository.findByEmail(TEST_USER_EMAIL);
         assertNotNull(s, "Student not found in database.");
      } catch (Exception ex){
         throw ex;
      } finally {
         //removes the student from the repository and stops the Chrome driver
         Student remove = studentRepository.findByEmail(TEST_USER_EMAIL);
         
         if(remove != null)
            studentRepository.delete(remove);
         
         driver.quit();
      }
      
   }
}
