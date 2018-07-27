package nz.ac.auckland.parolee.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Class to represent a Parolee. A Parolee is described by:
 * - Personal details: lastname, firstname, gender, date-of-birth, home address;
 * - Curfew: any constraints on the Parolee's location;
 * - Criminal profile: criminal history of the Parolee;
 * - Dissassociates: other Parolees who the Parolee is not permitted to be with;
 * - Movements: a timestamped history of where the Parolee has been.
 * 
 * A Parolee is uniquely identified by an id value of type Long.
 * 
 */
public class Parolee {
	
	private long _id;
	private String _lastname;
	private String _firstname;
	private Gender _gender;
	private LocalDate _dateOfBirth;
	private Address _homeAddress;
	private Curfew _curfew;
	private CriminalProfile _criminalProfile;
	private Set<Parolee> _dissassociates;
	private List<Movement> _movements;
	
	
	public Parolee(long id,
			String lastname,
			String firstname,
			Gender gender,
			LocalDate dateOfBirth,
			Address address,
			Curfew curfew) {
		_id = id;
		_lastname = lastname;
		_firstname = firstname;
		_gender = gender;
		_dateOfBirth = dateOfBirth;
		_homeAddress = address;
		_curfew = curfew;
		_dissassociates = new HashSet<Parolee>();
		_movements = new ArrayList<Movement>();
	}
	
	public long getId() {
		return _id;
	}
	
	public void setId(long id) {
		_id = id;
	}
	
	public String getLastname() {
		return _lastname;
	}
	
	public void setLastname(String lastname) {
		_lastname = lastname;
	}
	
	public String getFirstname() {
		return _firstname;
	}
	
	public void setFirstname(String firstname) {
		_firstname = firstname;
	}
	
	public Gender getGender() {
		return _gender;
	}
	
	public void setGender(Gender gender) {
		_gender = gender;
	}
	
	public LocalDate getDateOfBirth() {
		return _dateOfBirth;
	}
	
	public void setDateOfBirth(LocalDate dateOfBirth) {
		_dateOfBirth = dateOfBirth;
	}
	
	public Address getHomeAddress() {
		return _homeAddress;
	}
	
	public void setHomeAddress(Address homeAddress) {
		_homeAddress = homeAddress;
	}
	
	public Curfew getCurfew() {
		return _curfew;
	}
	
	public void setCurfew(Curfew curfew) {
		_curfew = curfew;
	}
	
	public CriminalProfile getCriminalProfile() {
		return _criminalProfile;
	}
	
	public void setCriminalProfile(CriminalProfile profile) {
		_criminalProfile = profile;
	}
	
	public void addMovement(Movement movement) {
		// Store the new movement.
		_movements.add(movement);
		
		// Ensure that movements are sorted in descending order (i.e. that the
		// most recent movement appears first.
		Collections.sort(_movements, Collections.reverseOrder());
	}
	
	public List<Movement> getMovements() {
		// Returns the Parolee's movements in a read-only collection.
		return Collections.unmodifiableList(_movements);
	}
	
	public Movement getLastKnownPosition() {
		Movement movement = null;
		
		if(!_movements.isEmpty()) {
			movement = _movements.get(0);
		}
		return movement;
	}
	
	public void addDissassociate(Parolee parolee) {
		_dissassociates.add(parolee);
	}
	
	public void removeDissassociate(Parolee parolee) {
		_dissassociates.remove(parolee);
	}
	
	public Set<Parolee> getDissassociates() {
		return Collections.unmodifiableSet(_dissassociates);
	}
	
	public void updateDissassociates(Set<Parolee> dissassociates) {
		_dissassociates = dissassociates;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		DateTimeFormatter dOfBFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
		
		buffer.append("Parolee: { [");
		buffer.append(_id);
		buffer.append("]; ");
		if(_lastname != null) {
			buffer.append(_lastname);
			buffer.append(", ");
		}
		if(_firstname != null) {
			buffer.append(_firstname);
		}
		buffer.append("; ");
		if(_gender != null) {
			buffer.append(_gender);
		}
		buffer.append("; ");
		
		if(_dateOfBirth != null) {
			buffer.append(dOfBFormatter.print(_dateOfBirth));
		}
		buffer.append("\n  ");
		if(_homeAddress != null) {
			buffer.append(_homeAddress);
		}
		
		if(_curfew != null) {
			buffer.append("\n  Curfew from ");
			buffer.append(timeFormatter.print(_curfew.getStartTime()));
			buffer.append(" to ");
			buffer.append(timeFormatter.print(_curfew.getEndTime()));
			buffer.append(" @ ");
			
			if(_homeAddress != null && _homeAddress.equals(_curfew.getConfinementAddress())) {
				buffer.append("home");
			} else {
				buffer.append(_curfew.getConfinementAddress());
			}
		} else {
			buffer.append("No curfew conditions");
		}
		
		buffer.append("\n  ");
		if(_criminalProfile != null) {
			buffer.append(_criminalProfile);
		} else {
			buffer.append("No criminal profile");
		}
		
		buffer.append("\n");
		buffer.append("  Dissassociates: ");
		if(_dissassociates.isEmpty()) {
			buffer.append("none");
		} else {
			for(Parolee dissassociate : _dissassociates) {
				buffer.append("[");
				buffer.append(dissassociate._id);
				buffer.append("]");
				buffer.append(" ");
				if(dissassociate._lastname != null) {
					buffer.append(dissassociate._lastname);
					buffer.append(", ");
				}
				if(dissassociate._firstname != null) {
					buffer.append(dissassociate._firstname);
				}
				buffer.append(";");
			}
			buffer.deleteCharAt(buffer.length()-1);
		}
		
		if(!_movements.isEmpty()) {
			buffer.append("\n  Last known location: ");
			Movement lastMovement = _movements.get(0);
			buffer.append(lastMovement);
		}
		
		buffer.append(" }");
		
		return buffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Parolee))
            return false;
        if (obj == this)
            return true;

        Parolee other = (Parolee)obj;
        return _id == other._id;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_id).
	            toHashCode();
	}
}


