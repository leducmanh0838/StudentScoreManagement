package com.ldm.dto;

import java.util.List;

public class GradeInputDTO {
    private Integer courseSessionId;
    private List<StudentGradeDTO> students;

    /**
     * @return the courseSessionId
     */
    public Integer getCourseSessionId() {
        return courseSessionId;
    }

    /**
     * @param courseSessionId the courseSessionId to set
     */
    public void setCourseSessionId(Integer courseSessionId) {
        this.courseSessionId = courseSessionId;
    }

    /**
     * @return the students
     */
    public List<StudentGradeDTO> getStudents() {
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(List<StudentGradeDTO> students) {
        this.students = students;
    }

    
}