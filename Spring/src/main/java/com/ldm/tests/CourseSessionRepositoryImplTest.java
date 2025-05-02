//package com.ldm.tests;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import com.ldm.pojo.CourseSession;
//
//@SpringBootTest
//public class CourseSessionRepositoryImplTest {
//
//    @Autowired
//    private CourseSessionRepositoryImpl courseSessionRepository;
//
//    @Test
//    public void testAddOrUpdate() {
//        // Create a new CourseSession object
//        CourseSession courseSession = new CourseSession();
//        courseSession.setMaxSlots(30); // Set other fields if necessary
//        courseSession.setCourseId(new Course(1)); // Set a valid Course ID
//        courseSession.setTeacherId(new User(1)); // Set a valid Teacher ID
//
//        // Test addOrUpdate method
//        CourseSession result = courseSessionRepository.addOrUpdate(courseSession);
//
//        // Assert that the result is not null (object was persisted/updated)
//        assertNotNull(result);
//    }
//}
