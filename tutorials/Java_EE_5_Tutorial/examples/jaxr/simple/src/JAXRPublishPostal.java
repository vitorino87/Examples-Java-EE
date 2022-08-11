/*
 * Copyright (c) 2006 Sun Microsystems, Inc.  All rights reserved.  U.S.
 * Government Rights - Commercial software.  Government users are subject
 * to the Sun Microsystems, Inc. standard license agreement and
 * applicable provisions of the FAR and its supplements.  Use is subject
 * to license terms.
 *
 * This distribution may include materials developed by third parties.
 * Sun, Sun Microsystems, the Sun logo, Java and J2EE are trademarks
 * or registered trademarks of Sun Microsystems, Inc. in the U.S. and
 * other countries.
 *
 * Copyright (c) 2006 Sun Microsystems, Inc. Tous droits reserves.
 *
 * Droits du gouvernement americain, utilisateurs gouvernementaux - logiciel
 * commercial. Les utilisateurs gouvernementaux sont soumis au contrat de
 * licence standard de Sun Microsystems, Inc., ainsi qu'aux dispositions
 * en vigueur de la FAR (Federal Acquisition Regulations) et des
 * supplements a celles-ci.  Distribue par des licences qui en
 * restreignent l'utilisation.
 *
 * Cette distribution peut comprendre des composants developpes par des
 * tierces parties. Sun, Sun Microsystems, le logo Sun, Java et J2EE
 * sont des marques de fabrique ou des marques deposees de Sun
 * Microsystems, Inc. aux Etats-Unis et dans d'autres pays.
 */


import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.RegistryService;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.LifeCycleManager;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.TelephoneNumber;
import javax.xml.registry.infomodel.PostalAddress;
import javax.xml.registry.infomodel.User;
import javax.xml.registry.infomodel.PersonName;
import javax.xml.registry.infomodel.EmailAddress;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Classification;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.registry.infomodel.Key;
import java.net.PasswordAuthentication;
import java.util.ResourceBundle;
import java.util.Properties;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;


/**
 * The JAXRPublishPostal class consists of a main method, a
 * makeConnection method, and an executePublish method.
 * It creates an organization and publishes it to a registry.
 * The organization's primary contact has a postal address.
 *
 * Edit the file postalconcepts.xml before you run this
 * program.
 */
public class JAXRPublishPostal {
    Connection connection = null;

    public JAXRPublishPostal() {
    }

    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("JAXRExamples");

        String queryURL = bundle.getString("query.url");
        String publishURL = bundle.getString("publish.url");

        String username = bundle.getString("registry.username");
        String password = bundle.getString("registry.password");

        if (args.length < 1) {
            System.out.println("Argument required: " + "-Duuid-string=<value>");
            System.exit(1);
        }

        String uuidString = args[0];
        System.out.println("UUID string is " + uuidString);

        JAXRPublishPostal jp = new JAXRPublishPostal();

        jp.makeConnection(queryURL, publishURL, uuidString);

        jp.executePublish(username, password);
    }

    /**
     * Establishes a connection to a registry.
     *
     * @param queryUrl        the URL of the query registry
     * @param publishUrl        the URL of the publish registry
     */
    public void makeConnection(
        String queryUrl,
        String publishUrl,
        String uuidString) {
        /*
         * Specify proxy information in case you
         *  are going beyond your firewall.
         */
        ResourceBundle bundle = ResourceBundle.getBundle("JAXRExamples");
        String httpProxyHost = bundle.getString("http.proxyHost");
        String httpProxyPort = bundle.getString("http.proxyPort");
        String httpsProxyHost = bundle.getString("https.proxyHost");
        String httpsProxyPort = bundle.getString("https.proxyPort");
        String userTaxonomyFilenames = bundle.getString(
                    "postal.taxonomy.filenames");

        /*
         * Define connection configuration properties.
         * To publish, you need both the query URL and the
         * publish URL.
         */
        Properties props = new Properties();
        props.setProperty("javax.xml.registry.queryManagerURL", queryUrl);
        props.setProperty("javax.xml.registry.lifeCycleManagerURL", publishUrl);
        props.setProperty("com.sun.xml.registry.http.proxyHost", httpProxyHost);
        props.setProperty("com.sun.xml.registry.http.proxyPort", httpProxyPort);
        props.setProperty(
                "com.sun.xml.registry.https.proxyHost",
                httpsProxyHost);
        props.setProperty(
                "com.sun.xml.registry.https.proxyPort",
                httpsProxyPort);

        // Define the taxonomy XML file (postalconcepts.xml)
        props.setProperty(
                "com.sun.xml.registry.userTaxonomyFilenames",
                userTaxonomyFilenames);

        // Set properties for postal address mapping using my scheme
        props.setProperty("javax.xml.registry.postalAddressScheme", uuidString);
        props.setProperty(
                "javax.xml.registry.semanticEquivalences",
                "urn:uuid:PostalAddressAttributes/StreetNumber," + "urn:"
                + uuidString + "/MyStreetNumber|"
                + "urn:uuid:PostalAddressAttributes/Street," + "urn:"
                + uuidString + "/MyStreet|"
                + "urn:uuid:PostalAddressAttributes/City," + "urn:"
                + uuidString + "/MyCity|"
                + "urn:uuid:PostalAddressAttributes/State," + "urn:"
                + uuidString + "/MyState|"
                + "urn:uuid:PostalAddressAttributes/PostalCode," + "urn:"
                + uuidString + "/MyPostalCode|"
                + "urn:uuid:PostalAddressAttributes/Country," + "urn:"
                + uuidString + "/MyCountry");

        try {
            // Create the connection, passing it the 
            // configuration properties
            ConnectionFactory factory = ConnectionFactory.newInstance();
            factory.setProperties(props);
            connection = factory.createConnection();
            System.out.println("Created connection to registry");
        } catch (Exception e) {
            e.printStackTrace();

            if (connection != null) {
                try {
                    connection.close();
                } catch (JAXRException je) {
                }
            }
        }
    }

    /**
     * Creates an organization, its classification, and its
     * services, and saves it to the registry.  The primary
     * contact has a postal address.
     *
     * @param username  the username for the registry
     * @param password  the password for the registry
     */
    public void executePublish(
        String username,
        String password) {
        RegistryService rs = null;
        BusinessLifeCycleManager blcm = null;
        BusinessQueryManager bqm = null;

        try {
            rs = connection.getRegistryService();
            blcm = rs.getBusinessLifeCycleManager();
            bqm = rs.getBusinessQueryManager();
            System.out.println(
                    "Got registry service, query "
                    + "manager, and life cycle manager");

            // Get authorization from the registry
            PasswordAuthentication passwdAuth = new PasswordAuthentication(
                        username,
                        password.toCharArray());

            HashSet<PasswordAuthentication> creds = new HashSet<PasswordAuthentication>();
            creds.add(passwdAuth);
            connection.setCredentials(creds);
            System.out.println("Established security credentials");

            ResourceBundle bundle = ResourceBundle.getBundle("JAXRExamples");

            // Create organization name and description
            InternationalString s = blcm.createInternationalString(
                        bundle.getString("postal.org.name"));
            Organization org = blcm.createOrganization(s);
            s = blcm.createInternationalString(
                        bundle.getString("org.description"));
            org.setDescription(s);

            // Create primary contact, set name
            User primaryContact = blcm.createUser();
            PersonName pName = blcm.createPersonName(
                        bundle.getString("postal.person.name"));
            primaryContact.setPersonName(pName);

            // Set primary contact phone number
            TelephoneNumber tNum = blcm.createTelephoneNumber();
            tNum.setNumber(bundle.getString("phone.number"));

            Collection<TelephoneNumber> phoneNums = new ArrayList<TelephoneNumber>();
            phoneNums.add(tNum);
            primaryContact.setTelephoneNumbers(phoneNums);

            // Set primary contact email address
            EmailAddress emailAddress = blcm.createEmailAddress(
                        bundle.getString("postal.email.address"));
            Collection<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();
            emailAddresses.add(emailAddress);
            primaryContact.setEmailAddresses(emailAddresses);

            // create postal address for primary contact
            String streetNumber = bundle.getString("postal.streetNumber");
            String street = bundle.getString("postal.street");
            String city = bundle.getString("postal.city");
            String state = bundle.getString("postal.state");
            String country = bundle.getString("postal.country");
            String postalCode = bundle.getString("postal.postalCode");
            String type = bundle.getString("postal.type");
            PostalAddress postAddr = blcm.createPostalAddress(
                        streetNumber,
                        street,
                        city,
                        state,
                        country,
                        postalCode,
                        type);
            Collection<PostalAddress> postalAddresses = new ArrayList<PostalAddress>();
            postalAddresses.add(postAddr);
            primaryContact.setPostalAddresses(postalAddresses);

            // Set primary contact for organization
            org.setPrimaryContact(primaryContact);

            // Set classification scheme to NAICS, using
            // well-known UUID of ntis-gov:naics:1997
            String uuid_naics = "uuid:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2";
            ClassificationScheme cScheme = (ClassificationScheme) bqm
                .getRegistryObject(
                        uuid_naics,
                        LifeCycleManager.CLASSIFICATION_SCHEME);

            // Create and add classification
            if (cScheme != null) {
                InternationalString sn = blcm.createInternationalString(
                            bundle.getString("classification.name"));
                Classification classification = blcm.createClassification(
                            cScheme,
                            sn,
                            bundle.getString("classification.value"));
                Collection<Classification> classifications = new ArrayList<Classification>();
                classifications.add(classification);
                org.addClassifications(classifications);
            } else {
                System.out.println(
                        "Classification scheme not found, "
                        + "not classifying organization");
            }

            // Create services and service
            Collection<Service> services = new ArrayList<Service>();
            s = blcm.createInternationalString(
                        bundle.getString("service.name"));

            Service service = blcm.createService(s);
            s = blcm.createInternationalString(
                        bundle.getString("service.description"));
            service.setDescription(s);

            // Create service bindings; don't validate this fake URL
            Collection<ServiceBinding> serviceBindings = new ArrayList<ServiceBinding>();
            ServiceBinding binding = blcm.createServiceBinding();
            s = blcm.createInternationalString(
                        bundle.getString("svcbinding.description"));
            binding.setDescription(s);

            // Allow us to publish a fictitious URI without an error
            binding.setValidateURI(false);
            binding.setAccessURI(bundle.getString("svcbinding.accessURI"));
            serviceBindings.add(binding);

            // Add service bindings to service
            service.addServiceBindings(serviceBindings);

            // Add service to services, then add services to organization
            services.add(service);
            org.addServices(services);

            // Add organization and submit to registry
            // Retrieve key if successful
            Collection<Organization> orgs = new ArrayList<Organization>();
            orgs.add(org);

            BulkResponse response = blcm.saveOrganizations(orgs);
            Collection exceptions = response.getExceptions();

            if (exceptions == null) {
                System.out.println("Organization saved");

                Collection keys = response.getCollection();

                for (Object k : keys) {
                    Key orgKey = (Key) k;
                    String id = orgKey.getId();
                    System.out.println("Organization key is " + id);
                }
            } else {
                for (Object e : exceptions) {
                    Exception exception = (Exception) e;
                    System.err.println(
                            "Exception on save: " + exception.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // At end, close connection to registry
            if (connection != null) {
                try {
                    connection.close();
                } catch (JAXRException je) {
                }
            }
        }
    }
}
