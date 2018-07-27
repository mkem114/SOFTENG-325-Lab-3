package nz.ac.auckland.parolee.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class to represent an Address.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Address {
	
	@XmlElement(name="street_number")
	private String _streetNumber;
	
	@XmlElement(name="street_name")
	private String _streetName;
	
	@XmlElement(name="suburb")
	private String _suburb;
	
	@XmlElement(name="city")
	private String _city;
	
	@XmlElement(name="zip_code")
	private String _zipCode;
	
	// Optional latitude/longitude data.
	@XmlElement(name="location")
	private GeoPosition _location;
	
	protected Address() {
		// Default constructor required by JAXB.
	}
	
	public Address(String streetNumber,
			String streetName,
			String suburb,
			String city,
			String zipCode) {
		_streetNumber = streetNumber;
		_streetName = streetName;
		_suburb = suburb;
		_city = city;
		_zipCode =zipCode;
	}
	
	public Address(String streetNumber,
			String streetName,
			String suburb,
			String city,
			String zipCode,
			GeoPosition location) {
		this(streetNumber, streetName, suburb, city, zipCode);
		_location = location;
	}
	
	public String getStreetNumber() {
		return _streetNumber;
	}
	
	public void setStreetNumber(String streetNumber) {
		_streetNumber = streetNumber;
	}
	
	public String getStreetName() {
		return _streetName;
	}
	
	public void setStreetName(String streetName) {
		_streetName = streetName;
	}
	
	public String getSuburb() {
		return _suburb;
	}
	
	public void setSuburb(String suburb) {
		_suburb = suburb;
	}
	
	public String getCity() {
		return _city;
	}
	
	public void setCity(String city) {
		_city = city;
	}
	
	public String getZipCode() {
		return _zipCode;
	}
	
	public void setZipCode(String zipCode) {
		_zipCode = zipCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Address))
            return false;
        if (obj == this)
            return true;

        Address rhs = (Address) obj;
        return new EqualsBuilder().
            append(_streetNumber, rhs._streetNumber).
            append(_streetName, rhs._streetName).
            append(_suburb, rhs._suburb).
            append(_city, rhs._city).
            append(_zipCode, rhs._zipCode).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_streetNumber).
	            append(_streetName).
	            append(_suburb).
	            append(_city).
	            append(_zipCode).
	            toHashCode();
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Address: ");
		buffer.append(_streetNumber + " ");
		buffer.append(_streetName + ", ");
		buffer.append(_suburb + ", ");
		buffer.append(_city + ", ");
		buffer.append(_zipCode);
		
		return buffer.toString();
	}
}
