package nz.ac.auckland.parolee.services;

import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import nz.ac.auckland.parolee.domain.Address;
import nz.ac.auckland.parolee.domain.Curfew;
import nz.ac.auckland.parolee.domain.Movement;
import nz.ac.auckland.parolee.domain.Parolee;

/**
 * ContextResolver implementation to return a customised JAXBContext for the
 * Parolee Web service. 
 * 
 * The JAX-RS run-time will create a default JAXBContext. To use a default
 * JAXBContext, this class isn't required and shouldn't be registered.
 * 
 * This class is included to show how a customised JAXBContext can be created. 
 * For the customised JAXBContext to be used, this class must be registered 
 * with JAX-RS, by class ParoleeApplication. As with registering any component,
 * the Application class (i.e. ParoleeApplication for this Web service), should
 * return this class from its getClasses() method.   
 *
 */
public class ParoleeResolver implements ContextResolver<JAXBContext> {
	private JAXBContext _context;

	public ParoleeResolver() {
		try {
			// The JAXB Context should be able to marshal and unmarshal the
			// specified classes.
			_context = JAXBContext.newInstance(Parolee.class, Curfew.class,
					Movement.class, Address.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JAXBContext getContext(Class<?> type) {
		if (type.equals(Parolee.class) || type.equals(Curfew.class)
				|| type.equals(Movement.class) || type.equals(Address.class)) {
			return _context;
		} else {
			return null;
		}
	}
}
