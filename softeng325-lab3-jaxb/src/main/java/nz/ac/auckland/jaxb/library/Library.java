package nz.ac.auckland.jaxb.library;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to represent a Library as a List of Books.
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Library {
	
	@XmlElementWrapper(name="books")
	@XmlElement(name="book") 
	private List<Book> _libraryCollection;
	
	public Library() {
		_libraryCollection = new ArrayList<Book>();
	}

	public void addBook(Book book) {
		_libraryCollection.add(book);
	}
}
