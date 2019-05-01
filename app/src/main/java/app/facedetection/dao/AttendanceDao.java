package app.facedetection.dao;

import java.io.Serializable;

/**
 * Created by Ansh on 04/10/2017 11:15 AM.
 */

public class AttendanceDao implements Serializable {

    @Override
    public String toString() {
        return "AttendanceDao{" +
                "studentName='" + studentName + '\'' +
                ", student_status='" + student_status + '\'' +
                ", students_roll='" + students_roll + '\'' +
                '}';
    }

    public String studentName, student_status, students_roll ;

    public AttendanceDao(String studentName, String student_status, String students_roll) {
        this.studentName = studentName;
        this.student_status = student_status;
        this.students_roll=students_roll;


    }



}
