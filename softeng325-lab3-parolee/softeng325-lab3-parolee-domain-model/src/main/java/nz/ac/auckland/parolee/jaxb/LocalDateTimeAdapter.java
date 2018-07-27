package nz.ac.auckland.parolee.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.LocalDateTime;

/**
 * JAXB XML adapter to convert between Joda LocalDateTime instances and 
 * Strings. DateTime objects are marshalled as Strings, and unmarshalled back 
 * into LocalDateTime instances.
 *
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

	@Override
	public LocalDateTime unmarshal(String dateTimeAsString) throws Exception {
		if(dateTimeAsString == null) {
			return null;
		}
		return new LocalDateTime(dateTimeAsString);
	}

	@Override
	public String marshal(LocalDateTime dateTime) throws Exception {
		if(dateTime == null) {
			return null;
		}
		return dateTime.toString();
	}
}