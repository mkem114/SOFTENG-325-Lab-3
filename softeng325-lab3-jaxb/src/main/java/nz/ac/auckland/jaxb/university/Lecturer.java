package nz.ac.auckland.jaxb.university;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to represent a university Lecturer.
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Lecturer {
	
	private static int NEXT_ID = 0;
	
	@XmlID
	@XmlAttribute(name="xml-id")
	private String _xmlId;
	
	@XmlElement(name="name")
	private String _name;
	
	@XmlElementWrapper(name="courses-taught")
	@XmlElement(name="course")
	@XmlIDREF
	private List<Course> _coursesTaught;
	
	protected Lecturer() {	
		this(null);
	}
	
	public Lecturer(String name) {
		_name = name;
		_coursesTaught = new ArrayList<Course>();
		
		// Generate new ID value.
		NEXT_ID++;
		_xmlId = getClass().getName() + ":" + NEXT_ID;
	}
	
	public String getName() {
		return _name;
	}
	
	public void assignCourse(Course course) throws IllegalArgumentException{
		_coursesTaught.add(course);
		course.addLecturer(this);
	}
}
