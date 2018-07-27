/**
 * This is a package-info file. A package-info file applies to all classes 
 * within the package that contains the package-info file. In this case, the 
 * package-info file applies to the nz.ac.auckland.parolee.domain package.
 * 
 * This particular package-info file specifies that 3 XmlAdapter classes should
 * be used when marshalling/unmarshalling instances of the domain classes 
 * defined in this package. Where a domain object has a field of type
 * org.joda.time.LocalDate, it will be marshalled using class LocalDateAdapter.
 * Similarly, fields of type org.joda.time.LocalTime and 
 * org.joda.time.LocalDateTime are handled by classes LocalTimeAdapter and 
 * LocalDateTimeAdapter respectively.
 * 
 * In this case, the adapters allow Joda date/time objects to be marshalled as
 * standard XML strings, and during unmarshalling, the XML strings are
 * converted to Joda instances.
 * 
 * Using a package-info file to register XmlAdapters means that the fields in
 * individual classes need not be annotated with XmlJavaTypeAdapter.
 * 
 */
@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type=LocalDate.class, 
        value=LocalDateAdapter.class),
    @XmlJavaTypeAdapter(type=LocalTime.class,
        value=LocalTimeAdapter.class),
    @XmlJavaTypeAdapter(type=LocalDateTime.class,
        value=LocalDateTimeAdapter.class)
})

package nz.ac.auckland.parolee.domain;
 
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
 
import nz.ac.auckland.parolee.jaxb.LocalDateTimeAdapter;
import nz.ac.auckland.parolee.jaxb.LocalDateAdapter;
import nz.ac.auckland.parolee.jaxb.LocalTimeAdapter;


import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;