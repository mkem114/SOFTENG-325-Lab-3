package nz.ac.auckland.jaxb.university;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import nz.ac.auckland.jaxb.university.Course;
import nz.ac.auckland.jaxb.university.Lecturer;
import nz.ac.auckland.jaxb.university.University;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test class demonstrates how a graph structure (a University containing
 * Lecturer and Course objects) can be transformed to and from XML. Such graph
 * structures are more difficult to process because objects can be arbitrarily 
 * associated with one another. 
 *
 */
public class JaxbTest {
	
	private static JAXBContext _jaxbCxt = null;
	private static Marshaller _marshaller = null;
	private static Unmarshaller _unmarshaller = null;
	
	private University _university;

	/**
	 * One-time set-up method to instantiate JAXB classes.
	 * 
	 */
	@BeforeClass
	public static void setUpJAXB() throws JAXBException {
		_jaxbCxt = JAXBContext.newInstance(University.class, Lecturer.class);
		_marshaller = _jaxbCxt.createMarshaller();
		_unmarshaller = _jaxbCxt.createUnmarshaller();

		_marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	}
	
	/**
	 * Creates a University object and populates it.
	 */
	@Before
	public void initialiseUniversity() {
		_university = new University();
		
		Course se251 = new Course("SOFTENG 251");
		Course se325 = new Course("SOFTENG 325");
		Course se254 = new Course("SOFTENG 254");
		
		Lecturer ian = new Lecturer("Ian Warren");
		Lecturer andrew = new Lecturer("Andrew Meads");
		Lecturer ewan = new Lecturer("Ewan Tempero");
		
		_university.addCourse(se251);
		_university.addCourse(se325);
		_university.addCourse(se254);
		_university.addLecturer(ian);
		_university.addLecturer(andrew);
		_university.addLecturer(ewan);
		
		ian.assignCourse(se251);
		ian.assignCourse(se325);
		andrew.assignCourse(se325);
		ewan.assignCourse(se254);
		ewan.assignCourse(se251);
	}
	
	/** 
	 * Attempts to marshall a Lecturer. Note that Lecturer's associated Course
	 * instances are not marshalled, only their XML references are written out.
	 * 
	 */
	@Test
	public void attemptToMarshalLecturer() throws JAXBException {
		Lecturer ewan = _university.getLecturerByName("Ewan Tempero");
		
		_marshaller.marshal(ewan, System.out);
	}
	
	/**
	 * Marshal a University object. Note that since a University object contains
	 * a collection of Courses and a collection of Lecturers, the Course and 
	 * Lecturer items in each collection are marshalled to XML. When marshalling
	 * an individual Course object, any reference to a Lecturer object is 
	 * processed by writing out the XML ID only of the Lecturer in the XML 
	 * document. Likewise, when marshalling a Lecturer object, any references to
	 * Course objects resulting in the XML ID of the Course object being written 
	 * out.
	 * 
	 * The XML representation contains sufficient data to rebuild the object 
	 * graph correctly during unmarshalling.
	 * 
	 * @throws JAXBException
	 */
	@Test
	public void marshalAndUnmarshalUniversity() throws JAXBException {
		_marshaller.marshal(_university, System.out);
	}
	
}
