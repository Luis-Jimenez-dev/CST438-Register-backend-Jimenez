package com.cst438.domain;

import java.util.List;

import com.cst438.domain.ScheduleDTO.CourseDTO;

public class RegisterDTO
{
   public static class studentDTO {
      public int id;
      public String email;
      public String name;
      public int statusCode;
      
      @Override
      public String toString() {
         return "RegisterDTO [id =" + id +", Email=" + email + ", Name:" + name
               + ", Status Code="+ statusCode + "]";
      }
   }
   
   public List<studentDTO> students;
   public String email;
   public int id;
   public String name;
   public int statusCode;
}
