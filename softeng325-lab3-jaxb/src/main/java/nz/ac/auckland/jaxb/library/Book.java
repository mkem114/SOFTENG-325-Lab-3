package nz.ac.auckland.jaxb.library;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to represent a Book. This class is annotated with @XmlRootElement to
 * indicate that a Book instance can serve as the root element of an XML 
 * document.
 * 
 * The class conforms to the Java bean conventions, where each property has a
 * get/set method pair. Book has one property: title.
 * 
 */
@XmlRootElement
public class Book {
	private String _title;
	
	// Default constructor is required by JAXB.
	public Book() {
		this(null);
	}
	
	
	public Book(String title) {
		_title = title;
	}
	
	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}
	
	public boolean equals(Object other) {
		boolean equals = false;
		
		if(other != null && other instanceof Book) {
			Book otherBook = (Book)other;
			equals = _title.equals(otherBook._title);
		}
		return equals;
	}
}