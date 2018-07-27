package nz.ac.auckland.parolee.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Class to represent a Parolee's movement. A Movement instance stores a 
 * timestamp and a latitude/longitude position. Movement instances are 
 * immutable.
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Movement implements Comparable<Movement> {
	
	@XmlElement(name="time_stamp")
	private LocalDateTime _timestamp;
	
	@XmlElement(name="geo_position")
	private GeoPosition _geoPosition;
	
	protected Movement() {
		// Required by JAXB for unmarshalling purposes.
	}
	
	public Movement(LocalDateTime timestamp, GeoPosition position) {
		_timestamp = timestamp;
		_geoPosition = position;
	}
	
	public LocalDateTime getTimestamp() {
		return _timestamp;
	}
	
	public GeoPosition getGeoPosition() {
		return _geoPosition;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Movement))
            return false;
        if (obj == this)
            return true;

        Movement rhs = (Movement) obj;
        return new EqualsBuilder().
            append(_timestamp, rhs._timestamp).
            append(_geoPosition, rhs._geoPosition).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_timestamp).
	            append(_geoPosition).
	            toHashCode();
	}
	
	@Override
	public int compareTo(Movement movement) {
		return _timestamp.compareTo(movement._timestamp);
	}
	
	@Override
	public String toString() {
		DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(_geoPosition);
		buffer.append(" @ ");
		buffer.append(timeFormatter.print(_timestamp));
		buffer.append( " on " );
		buffer.append(dateFormatter.print(_timestamp));
		
		return buffer.toString();
	}
}