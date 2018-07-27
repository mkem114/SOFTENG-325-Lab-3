package nz.ac.auckland.jaxb.library;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import nz.ac.auckland.jaxb.library.Book;
import nz.ac.auckland.jaxb.library.Library;
import nz.auckland.jaxb.utility.DefaultSchemaOutputResolver;
import nz.auckland.jaxb.utility.DefaultValidationEventHandler;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

/**
 * This test class demonstrates how to use JAXB to marshal/unmarshal a simple 
 * object (a Book) and a collection of Books objects (a Library). The XML 
 * representation of a Library uses containment - where a Library element 
 * contains Book elements.
 * 
 * This test class also shows how to generate an XML schema from Java classes,
 * and how the schema can used during unmarshalling of an XML document to 
 * validate the document.
 *
 */
public class JaxbTest {
	
	private static final String OUTPUT_PATH = "target/xml/";
	private static final String XML_FILE_SCHEMA = "simple.xsd";
	private static final String XML_FILE_SIMPLE = "simple.xml";
	
	private static JAXBContext _jaxbCxt = null;
	private static Marshaller _marshaller = null;
	private static Unmarshaller _unmarshaller = null;
	
	private Book _book;
	private Library _library;

	/**
	 * One-time set-up method called before the execution of the set of tests.
	 * 
	 * This method instantiates JAXB classes as necessary to handle marshalling
	 * and unmarshalling of Book and Library instances. A JAXBContext object is 
	 * necessarily initialised with classes Book and Library. 
	 * 
	 * Based on introspected knowledge of class Book, the JAXBContext instance 
	 * is able to serve as a factory to create Marshaller and Unmarshaller 
	 * objects that know how to convert Book objects to/from XML.
	 * 
	 */
	@BeforeClass
	public static void setUpJAXB() throws JAXBException, IOException, SAXException {
		_jaxbCxt = JAXBContext.newInstance(Book.class, Library.class);
		_marshaller = _jaxbCxt.createMarshaller();
		_unmarshaller = _jaxbCxt.createUnmarshaller();

		// Set Marshaller property so that generated XML is nicely formatted and
		// easy to read by humans. For machine-only processing, formatting isn't
		// required.
		_marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		// Generate the XML schema for classes known to the JAXB context.
		// Begin by creating the output directory if necessary.
		new File(OUTPUT_PATH).mkdirs();
		
		// Generate the schema, storing the resulting .xsd file in the output
		// directory.
		SchemaOutputResolver sor = new DefaultSchemaOutputResolver(
				OUTPUT_PATH, XML_FILE_SCHEMA);
		_jaxbCxt.generateSchema(sor);
		
		// Introduce the XML schema to the Unmarshaller. They Unmarshaller will
		// use the schema to validate XML documents during unmarshalling. Not
		// setting the schema, or passing in a null reference to setSchema()
		// disables validation. Start by creating a SchemaFactory, and use this
		// to create a Schema by loading the previously created .xsd file. 
		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = factory
				.newSchema(new File(OUTPUT_PATH + XML_FILE_SCHEMA));
		_unmarshaller.setSchema(schema);
		
		// Set an error handler on the unmarshaller. The handler will be called
		// by the Unmarshaller if the XML document being unmarshalled contains 
		// errors.
		_unmarshaller.setEventHandler(new DefaultValidationEventHandler());
	}
	
	/**
	 * Creates fixture objects used by the tests.
	 */
	@Before
	public void initialiseFixtureObjects() {
		_book = new Book("American Sniper");
		
		_library = new Library();
		_library.addBook(new Book("American sniper"));
		_library.addBook(new Book("Harry Potter and the Deathly Hallows"));
		_library.addBook(new Book("The Kite Runner"));
		_library.addBook(new Book("The Da Vinci Code"));
	}
	
	/**
	 * Marshals a Book instance to XML, and directs the XML output to the 
	 * console.
	 * 
	 */
	@Test
	public void marshalBook() throws JAXBException {
		_marshaller.marshal(_book, System.out);
	}
	
	/** 
	 * Marshals a Book instance, storing the generated XML in a ByteArray in
	 * memory. The XML representation is then unmarshalled to produce a copy of
	 * the original book instance.
	 * 
	 */
	@Test
	public void marshalAndUnmarshalBook() throws JAXBException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		_marshaller.marshal(_book, baos);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		Book copy = (Book)_unmarshaller.unmarshal(bais);
		
		// The two Book instances should have the same value (i.e. state).
		assertEquals(_book, copy);
		
		// The two Book instance are separate objects.
		assertNotSame(_book, copy);
	}
	
	/**
	 * Marshals a Library instance, which is essentiaslly a List of Book 
	 * objects. 
	 * 
	 * This test illustrates the effect of @XmlElementWrapper, as used by class
	 * Library.
	 * 
	 * Note that a List implementation cannot be passed as the first argument to
	 * marshal() because List implementations have no knowledge of the JAXB 
	 * framework - and hence are not annotated with @XmlRootElement. A List
	 * instance can therefore not be marshalled as a document root element.
	 * 
	 * To resolve this problem, Lists should be contained in some other object 
	 * whose class is annotated with @XmlRootElement. Class Library is 
	 * effectively such a container. 
	 *
	 */
	@Test
	public void marshalLibrary() throws JAXBException {
		_marshaller.marshal(_library,  System.out);
	}
	
	/**
	 * Marshals the Library object to and from a XML file.The XML file will be
	 * stored in the target/xml directory.
	 */
	@Test 
	public void marshalAndUnmarshalLibraryToFromFile() {
		File xmlFile = new File(OUTPUT_PATH + XML_FILE_SIMPLE);

		try {
			_marshaller.marshal(_library, xmlFile);

			// Unmarshal the Library.
			Library copy = (Library) _unmarshaller
					.unmarshal(xmlFile);

		} catch (JAXBException e) {
			fail();
		}
	}
}
