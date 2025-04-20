package BlockPackage.Certificates;

public class Certificate {
    private String studentName;
    private final String course;
    private final String grade;

    public Certificate(String studentName, String course, String grade) {
        this.studentName = studentName;
        this.course = course;
        this.grade = grade;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourse() {
        return course;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "studentName='" + studentName + '\'' +
                ", course='" + course + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
       
    }
}
