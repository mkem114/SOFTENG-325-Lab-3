package nz.ac.auckland.jaxb.university;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to represent a University, comprising Lecturers and Courses.
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class University {
	@XmlElementWrapper(name="lecturers")
	@XmlElement(name="lecturer")
	private List<Lecturer> _lecturers;
	
	@XmlElementWrapper(name="courses")
	@XmlElement(name="course")
	private List<Course> _courses;
	
	public University() {
		_lecturers = new ArrayList<Lecturer>();
		_courses = new ArrayList<Course>();
	}
	
	public void addLecturer(Lecturer lecturer) {
		_lecturers.add(lecturer);
	}
	
	public void addCourse(Course course) {
		_courses.add(course);
	}
	
	public Lecturer getLecturerByName(String name) {
		for(Lecturer lecturer : _lecturers) {
			if(lecturer.getName().equals(name)) {
				return lecturer;
			}
		}
		return null;
	}
	
	public Course getCourseByName(String name) {
		for(Course course : _courses) {
			if(course.getName().equals(name)) {
				return course;
			}
		}
		return null;
	}
}
