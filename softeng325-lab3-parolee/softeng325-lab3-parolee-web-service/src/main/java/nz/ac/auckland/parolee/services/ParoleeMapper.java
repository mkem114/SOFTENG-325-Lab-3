package nz.ac.auckland.parolee.services;

import nz.ac.auckland.parolee.domain.Parolee;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Parolees.
 *
 */
public class ParoleeMapper {

	static Parolee toDomainModel(nz.ac.auckland.parolee.dto.Parolee dtoParolee) {
		Parolee fullParolee = new Parolee(dtoParolee.getId(),
				dtoParolee.getLastname(),
				dtoParolee.getFirstname(),
				dtoParolee.getGender(),
				dtoParolee.getDateOfBirth(),
				dtoParolee.getHomeAddress(),
				dtoParolee.getCurfew());
		return fullParolee;
	}
	
	static nz.ac.auckland.parolee.dto.Parolee toDto(Parolee parolee) {
		nz.ac.auckland.parolee.dto.Parolee dtoParolee = 
				new nz.ac.auckland.parolee.dto.Parolee(
						parolee.getId(),
						parolee.getLastname(),
						parolee.getFirstname(),
						parolee.getGender(),
						parolee.getDateOfBirth(),
						parolee.getHomeAddress(),
						parolee.getCurfew(),
						parolee.getLastKnownPosition());
		return dtoParolee;
		
	}
}
