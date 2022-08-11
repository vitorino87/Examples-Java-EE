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
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.FindQualifier;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;
import static javax.xml.registry.FindQualifier.SORT_BY_NAME_DESC;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.TelephoneNumber;
import javax.xml.registry.infomodel.PostalAddress;
import javax.xml.registry.infomodel.User;
import javax.xml.registry.infomodel.PersonName;
import javax.xml.registry.infomodel.EmailAddress;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.registry.infomodel.Slot;
import java.util.ResourceBundle;
import java.util.Properties;
import java.util.Collection;
import java.util.ArrayList;


/**
 * The JAXRQueryPostal class consists of a main method, a
 * makeConnection method, an executeQuery method, and some
 * private helper methods. It searches a registry for
 * information about organizations whose names contain a
 * user-supplied string.  It displays the postal addresses for
 * the contacts of the organizations using the user-supplied
 * postal address classification scheme UUID.
 */
public class JAXRQueryPostal {
    Connection connection = null;

    public JAXRQueryPostal() {
    }

    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("JAXRExamples");

        String queryURL = bundle.getString("query.url");
        String publishURL = bundle.getString("publish.url");

        if (args.length < 2) {
            System.out.println(
                    "Arguments required: "
                    + "-Dquery-string=<value> -Duuid-string=<value>");
            System.exit(1);
        }

        String queryString = args[0];
        System.out.println("Query string is " + queryString);

        String uuidString = args[1];
        System.out.println("UUID string is " + uuidString);

        JAXRQueryPostal jq = new JAXRQueryPostal();

        jq.makeConnection(queryURL, publishURL, uuidString);

        jq.executeQuery(queryString);
    }

    /**
     * Establishes a connection to a registry.
     *
     * @param queryUrl        the URL of the query registry
     * @param publishUrl        the URL of the publish registry
     * @param uuidString        the UUID string of the postal address scheme
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
        String userTaxonomyFilenames = bundle.getString(
                    "postal.taxonomy.filenames");

        /*
         * Define connection configuration properties.
         * For simple queries, you need the query URL.
         */
        Properties props = new Properties();
        props.setProperty("javax.xml.registry.queryManagerURL", queryUrl);
        props.setProperty("com.sun.xml.registry.http.proxyHost", httpProxyHost);
        props.setProperty("com.sun.xml.registry.http.proxyPort", httpProxyPort);

        // Define the taxonomy XML file (postalconcepts.xml)
        props.setProperty(
                "com.sun.xml.registry.userTaxonomyFilenames",
                userTaxonomyFilenames);

        // Set properties for postal address mapping: postal address
        //  classification scheme and mapping to JAXR scheme
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
     * Searches for organizations containing a string and
     * displays data about them, including the postal address in
     * either the JAXR PostalAddress format or the Slot format.
     *
     * @param qString        the string argument
     */
    public void executeQuery(String qString) {
        RegistryService rs = null;
        BusinessQueryManager bqm = null;

        try {
            // Get registry service and query manager
            rs = connection.getRegistryService();
            bqm = rs.getBusinessQueryManager();
            System.out.println("Got registry service and " + "query manager");

            // Define find qualifiers and name patterns
            Collection<String> findQualifiers = new ArrayList<String>();
            findQualifiers.add(SORT_BY_NAME_DESC);

            Collection<String> namePatterns = new ArrayList<String>();
            namePatterns.add("%" + qString + "%");

            // Find using the name
            BulkResponse response = bqm.findOrganizations(
                        findQualifiers,
                        namePatterns,
                        null,
                        null,
                        null,
                        null);
            Collection orgs = response.getCollection();

            // Display information about the organizations found
            for (Object o : orgs) {
                Organization org = (Organization) o;
                System.out.println("Org name: " + getName(org));
                System.out.println("Org description: " + getDescription(org));
                System.out.println("Org key id: " + getKey(org));

                // Display primary contact information
                User pc = org.getPrimaryContact();

                if (pc != null) {
                    PersonName pcName = pc.getPersonName();
                    System.out.println(
                            " Contact name: " + pcName.getFullName());

                    Collection phNums = pc.getTelephoneNumbers(null);

                    for (Object n : phNums) {
                        TelephoneNumber num = (TelephoneNumber) n;
                        System.out.println(
                                "  Phone number: " + num.getNumber());
                    }

                    Collection eAddrs = pc.getEmailAddresses();

                    for (Object a : eAddrs) {
                        EmailAddress eAd = (EmailAddress) a;
                        System.out.println(
                                "  Email Address: " + eAd.getAddress());
                    }

                    // Display postal addresses 
                    //   using PostalAddress methods
                    Collection pAddrs = pc.getPostalAddresses();

                    for (Object pa : pAddrs) {
                        PostalAddress pAd = (PostalAddress) pa;
                        System.out.println(
                                "  Postal Address (PostalAddress methods):\n    "
                                + pAd.getStreetNumber() + " " + pAd.getStreet()
                                + "\n    " + pAd.getCity() + ", "
                                + pAd.getStateOrProvince() + " "
                                + pAd.getPostalCode() + "\n    "
                                + pAd.getCountry());
                    }

                    // Display postal addresses 
                    //   using Slot methods
                    Collection pAddrs2 = pc.getPostalAddresses();

                    for (Object pa2 : pAddrs2) {
                        PostalAddress pAd = (PostalAddress) pa2;
                        Collection slots = pAd.getSlots();
                        System.out.println("  Postal Address (Slot methods):");

                        for (Object s : slots) {
                            Slot slot = (Slot) s;
                            Collection values = slot.getValues();

                            for (Object v : values) {
                                String line = (String) v;
                                System.out.println("    Line: " + line);
                            }
                        }
                    }
                }

                // Display service and binding information
                Collection services = org.getServices();

                for (Object s : services) {
                    Service svc = (Service) s;
                    System.out.println(" Service name: " + getName(svc));
                    System.out.println(
                            " Service description: " + getDescription(svc));

                    Collection serviceBindings = svc.getServiceBindings();

                    for (Object b : serviceBindings) {
                        ServiceBinding sb = (ServiceBinding) b;
                        System.out.println(
                                "  Binding " + "Description: "
                                + getDescription(sb));
                        System.out.println(
                                "  Access URI: " + sb.getAccessURI());
                    }
                }

                // Print spacer between organizations
                System.out.println(" --- ");
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

    /**
     * Returns the name value for a registry object.
     *
     * @param ro        a RegistryObject
     * @return                the String value
     */
    private String getName(RegistryObject ro) throws JAXRException {
        try {
            return ro.getName()
                     .getValue();
        } catch (NullPointerException npe) {
            return "No Name";
        }
    }

    /**
     * Returns the description value for a registry object.
     *
     * @param ro        a RegistryObject
     * @return                the String value
     */
    private String getDescription(RegistryObject ro) throws JAXRException {
        try {
            return ro.getDescription()
                     .getValue();
        } catch (NullPointerException npe) {
            return "No Description";
        }
    }

    /**
     * Returns the key id value for a registry object.
     *
     * @param ro        a RegistryObject
     * @return                the String value
     */
    private String getKey(RegistryObject ro) throws JAXRException {
        try {
            return ro.getKey()
                     .getId();
        } catch (NullPointerException npe) {
            return "No Key";
        }
    }
}
