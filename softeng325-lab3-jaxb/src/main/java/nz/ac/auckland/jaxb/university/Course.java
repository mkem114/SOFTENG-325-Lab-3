package nz.ac.auckland.jaxb.university;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * Class to represent a university Course.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Course {
	
	private static int NEXT_ID = 0;
	
	@XmlID
	@XmlAttribute(name="xml-id")
	private String _xmlId; 
	
	@XmlElement(name="lecturer")
	@XmlIDREF
	private List<Lecturer> _lecturingStaff;
	
	@XmlElement(name="name")
	private String _name;
	
	protected Course() {
		this(null);
	}
	
	public Course(String name) {
		_name = name;
		_lecturingStaff = new ArrayList<Lecturer>();
		
		NEXT_ID++;
		_xmlId = getClass().getName() + ":" + NEXT_ID;
	}
	
	public String getName() {
		return _name;
	}
	
	void addLecturer(Lecturer lecturer) {
		_lecturingStaff.add(lecturer);
	}
}
