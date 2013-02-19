//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2009.07.21 at 10:48:55 AM CEST
//

package org.jboss.as.test.integration.osgi.jaxb.bundle;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the
 * org.jboss.test.osgi.example.xml.jaxb package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content. The Java
 * representation of XML content can consist of schema derived interfaces and classes representing the binding of schema type
 * definitions, element declarations and model groups. Factory methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Booking_QNAME = new QName("", "booking");
    private final static QName _Student_QNAME = new QName("", "student");
    private final static QName _Company_QNAME = new QName("", "company");
    private final static QName _Contact_QNAME = new QName("", "contact");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * org.jboss.test.osgi.example.xml.jaxb
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StudentType }
     *
     */
    public StudentType createStudentType() {
        return new StudentType();
    }

    /**
     * Create an instance of {@link CompanyType }
     *
     */
    public CompanyType createCompanyType() {
        return new CompanyType();
    }

    /**
     * Create an instance of {@link CourseBooking }
     *
     */
    public CourseBooking createCourseBooking() {
        return new CourseBooking();
    }

    /**
     * Create an instance of {@link ContactType }
     *
     */
    public ContactType createContactType() {
        return new ContactType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CourseBooking }{@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "booking")
    public JAXBElement<CourseBooking> createBooking(CourseBooking value) {
        return new JAXBElement<CourseBooking>(_Booking_QNAME, CourseBooking.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StudentType }{@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "student")
    public JAXBElement<StudentType> createStudent(StudentType value) {
        return new JAXBElement<StudentType>(_Student_QNAME, StudentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompanyType }{@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "company")
    public JAXBElement<CompanyType> createCompany(CompanyType value) {
        return new JAXBElement<CompanyType>(_Company_QNAME, CompanyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContactType }{@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "contact")
    public JAXBElement<ContactType> createContact(ContactType value) {
        return new JAXBElement<ContactType>(_Contact_QNAME, ContactType.class, null, value);
    }

}
