package nz.ac.auckland.parolee.services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * Application subclass for the Parolee Web service.
 *
 */
@ApplicationPath("/services")
public class ParoleeApplication extends Application
{
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> classes = new HashSet<Class<?>>();

   public ParoleeApplication()
   {
	  // Register the ParoleeResource singleton to handle HTTP requests.
	  ParoleeResource resource = new ParoleeResource();
      singletons.add(resource);
      
      // Register the ContextResolver class for JAXB.
      //classes.add(ParoleeResolver.class);
   }

   @Override
   public Set<Object> getSingletons()
   {
      return singletons;
   }
   
   @Override
   public Set<Class<?>> getClasses()
   {
      return classes;
   }
}
