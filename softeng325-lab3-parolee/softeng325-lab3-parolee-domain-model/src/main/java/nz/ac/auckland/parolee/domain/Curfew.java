package nz.ac.auckland.parolee.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalTime;

/**
 * Class to represent a Parolee's curfew. A curfew is described by a confinement
 * address and the period of time that the curfew is in effect (typically 
 * overnight).
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Curfew {
	
	@XmlElement(name="confinement_address")
	private Address _confinementAddress;
	
	@XmlElement(name="start_time")
	private LocalTime _startTime;
	
	@XmlElement(name="end_time")
	private LocalTime _endTime;
	
	protected Curfew() {
		// Default constructor required by JAXB.
	}
	
	public Curfew(Address confinementAddress, 
			LocalTime startTime, 
			LocalTime endTime) {
		_confinementAddress = confinementAddress;
		_startTime = startTime;
		_endTime = endTime;
	}
	
	public Address getConfinementAddress() {
		return _confinementAddress;
	}
	
	public void setConfinementAddress(Address confinementAddress) {
		_confinementAddress = confinementAddress;
	}
	
	public LocalTime getStartTime() {
		return _startTime;
	}
	
	public void setStartTime(LocalTime startTime) {
		_startTime = startTime;
	}
	
	public LocalTime getEndTime() {
		return _endTime;
	}
	
	public void setEndTime(LocalTime endTime) {
		_endTime = endTime;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Curfew))
            return false;
        if (obj == this)
            return true;

        Curfew rhs = (Curfew) obj;
        return new EqualsBuilder().
            append(_confinementAddress, rhs._confinementAddress).
            append(_startTime, rhs._startTime).
            append(_endTime, rhs._endTime).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_confinementAddress).
	            append(_startTime).
	            append(_endTime).
	            toHashCode();
	}
}
