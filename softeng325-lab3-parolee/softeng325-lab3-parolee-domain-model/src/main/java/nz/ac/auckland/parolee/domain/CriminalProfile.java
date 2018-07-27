package nz.ac.auckland.parolee.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/** 
 * Class to represent a criminal profile. A profile is essentially a series of
 * convictions.
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CriminalProfile {
	
	@XmlElement(name="convictions")
	private Set<Conviction> _convictions;
	
	public CriminalProfile() {
		_convictions = new HashSet<Conviction>();
	}
	
	public void addConviction(Conviction conviction) {
		_convictions.add(conviction);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CriminalProfile))
            return false;
        if (obj == this)
            return true;

        CriminalProfile rhs = (CriminalProfile) obj;
        return new EqualsBuilder().
            append(_convictions, rhs._convictions).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_convictions).
	            toHashCode();
	}
	
	@Override
	public String toString() {
		Set<Offence> allOffences = new HashSet<Offence>();
		Set<LocalDate> allConvictionDates = new HashSet<LocalDate>();
		
		for(Conviction conviction : _convictions) {
			for(Offence offence : conviction.getOffenceTags()) {
				allOffences.add(offence);
			}
			allConvictionDates.add(conviction.getDate());
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("Criminal history: ");

		DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM yyyy");
		
		for(LocalDate date : allConvictionDates) {
			buffer.append(formatter.print(date));
			buffer.append(", ");
		}
		buffer.delete(buffer.length()-2, buffer.length()-1);
		
		buffer.append("\n    Offences: ");
		for(Offence offence : allOffences) {
			buffer.append(offence);
			buffer.append(", ");
		}
		buffer.delete(buffer.length()-2, buffer.length()-1);
		
		return buffer.toString();
			
	}
	
	public enum Offence {
		THEFT, POSSESION_OF_OFFENSIVE_WEAPON, MURDER, RAPE, TAX_EVASION;
	}
	
	/**
	 * Class to represent a particular criminal conviction. A conviction is made up 
	 * of one or more CriminalProfile.Offence tags, the date of conviction, and a 
	 * description of the conviction.
	 *
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Conviction {
		
		@XmlElement(name="offence_tags")
		private Set<Offence> _offenceTags;
		
		@XmlElement(name="offence_date")
		private LocalDate _date;
		
		@XmlElement(name="offence_description")
		private String _description;
		
		protected Conviction() {
			// Required by JAXB.
			this(null,null);
		}
		
		public Conviction(LocalDate convictionDate,
				String description,
				Offence... offenceTags) {
			_date = convictionDate;
			_description = description;
			_offenceTags = new HashSet<Offence>(Arrays.asList(offenceTags));
		}
		
		public Set<Offence> getOffenceTags() {
			return Collections.unmodifiableSet(_offenceTags);
		}
		
		public LocalDate getDate() {
			return _date;
		}
		
		public String getDescription() {
			return _description;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Conviction))
	            return false;
	        if (obj == this)
	            return true;

	        Conviction rhs = (Conviction) obj;
	        return new EqualsBuilder().
	            append(_date, rhs._date).
	            append(_description, rhs._description).
	            append(_offenceTags, rhs._offenceTags).
	            isEquals();
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 31). 
		            append(_date).
		            append(_description).
		            append(_offenceTags).
		            toHashCode();
		}
	}
}
