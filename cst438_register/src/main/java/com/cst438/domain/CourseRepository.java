package com.cst438.domain;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends CrudRepository <Course, Integer> {
//   @Query("select c from Course where c.course.course_id=course_id")
//   public List<Course> findbyId(
//         @Param("course_id") int course_id
//         );
   
//   @Query("select c from Course where c.course.course_id=course_id")
//   Course findById(@Param("course_id") int course_id);
   
   
}