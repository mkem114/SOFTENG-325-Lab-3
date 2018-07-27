package nz.auckland.jaxb.utility;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

/**
 * Implementation of JAXB's ValidationHandler interface. A ValidationHandler can
 * be registered with a JAXBContext object. When registered, and when a XML 
 * schema is in use, the unmarshaller detects any problems with the structure of
 * the XML document being unmarshalled (i.e. occurrences where the document 
 * doesn't conform to the schema). The unmarshaller informs the registered 
 * ValidationHandler by calling its handleEvent() method.
 * 
 * This implementation simply writes to standard out any problems identified 
 * during validation of the XML document. 
 *
 */
public class DefaultValidationEventHandler implements ValidationEventHandler {

	@Override
	public boolean handleEvent(ValidationEvent event) {
		System.out.println("\nEVENT");
		System.out.println("SEVERITY:  " + event.getSeverity());
		System.out.println("MESSAGE:  " + event.getMessage());
		System.out.println("LINKED EXCEPTION:  " + event.getLinkedException());
		System.out.println("LOCATOR");
		System.out.println("    LINE NUMBER:  "
				+ event.getLocator().getLineNumber());
		System.out.println("    COLUMN NUMBER:  "
				+ event.getLocator().getColumnNumber());
		System.out.println("    OFFSET:  " + event.getLocator().getOffset());
		System.out.println("    OBJECT:  " + event.getLocator().getObject());
		System.out.println("    NODE:  " + event.getLocator().getNode());
		System.out.println("    URL:  " + event.getLocator().getURL());
		return true;
	}
}