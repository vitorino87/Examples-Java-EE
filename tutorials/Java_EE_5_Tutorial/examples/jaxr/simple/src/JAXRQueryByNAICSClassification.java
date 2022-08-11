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
import static javax.xml.registry.FindQualifier.SORT_BY_NAME_DESC;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.User;
import javax.xml.registry.infomodel.PersonName;
import javax.xml.registry.infomodel.TelephoneNumber;
import javax.xml.registry.infomodel.EmailAddress;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Classification;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import java.util.ResourceBundle;
import java.util.Properties;
import java.util.Collection;
import java.util.ArrayList;


/**
 * The JAXRQueryByNAICSClassification class consists of a main
 * method, a makeConnection method, an executeQuery method, and
 * some private helper methods. It searches a registry for
 * information about organizations using an NAICS classification.
 */
public class JAXRQueryByNAICSClassification {
    Connection connection = null;

    public JAXRQueryByNAICSClassification() {
    }

    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("JAXRExamples");

        String queryURL = bundle.getString("query.url");
        String publishURL = bundle.getString("publish.url");

        JAXRQueryByNAICSClassification jq = new JAXRQueryByNAICSClassification();

        jq.makeConnection(queryURL, publishURL);

        jq.executeQuery();
    }

    /**
     * Establishes a connection to a registry.
     *
     * @param queryUrl        the URL of the query registry
     * @param publishUrl        the URL of the publish registry
     */
    public void makeConnection(
        String queryUrl,
        String publishUrl) {
        /*
         * Specify proxy information in case you
         *  are going beyond your firewall.
         */
        ResourceBundle bundle = ResourceBundle.getBundle("JAXRExamples");
        String httpProxyHost = bundle.getString("http.proxyHost");
        String httpProxyPort = bundle.getString("http.proxyPort");
        String httpsProxyHost = bundle.getString("https.proxyHost");
        String httpsProxyPort = bundle.getString("https.proxyPort");

        /*
         * Define connection configuration properties.
         * For simple queries, you need the query URL.
         * To use a life cycle manager, you need the publish URL.
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
     * Searches for organizations corresponding to an NAICS
     * classification and displays data about them.
     */
    public void executeQuery() {
        RegistryService rs = null;
        BusinessQueryManager bqm = null;
        BusinessLifeCycleManager blcm = null;

        try {
            // Get registry service and managers
            rs = connection.getRegistryService();
            bqm = rs.getBusinessQueryManager();
            blcm = rs.getBusinessLifeCycleManager();
            System.out.println(
                    "Got registry service, query "
                    + "manager, and lifecycle manager");

            ResourceBundle bundle = ResourceBundle.getBundle("JAXRExamples");

            // Find using an NAICS classification
            // Set classification scheme to NAICS, using
            // well-known UUID of ntis-gov:naics:1997
            String uuid_naics = "uuid:C0B9FE13-179F-413D-8A5B-5004DB8E5BB2";
            ClassificationScheme cScheme = (ClassificationScheme) bqm
                .getRegistryObject(
                        uuid_naics,
                        LifeCycleManager.CLASSIFICATION_SCHEME);

            Collection<Classification> classifications = new ArrayList<Classification>();

            if (cScheme != null) {
                // Create and add classification
                InternationalString sn = blcm.createInternationalString(
                            bundle.getString("classification.name"));
                Classification classification = blcm.createClassification(
                            cScheme,
                            sn,
                            bundle.getString("classification.value"));
                classifications.add(classification);
            } else {
                System.out.println("Classification scheme not found");
            }

            BulkResponse response = bqm.findOrganizations(
                        null,
                        null,
                        classifications,
                        null,
                        null,
                        null);
            Collection orgs = response.getCollection();

            // Display information about the organizations found
            int numOrgs = 0;

            if (orgs.isEmpty()) {
                System.out.println("No organizations found");
            } else {
                for (Object o : orgs) {
                    numOrgs++;

                    Organization org = (Organization) o;
                    System.out.println("Org name: " + getName(org));
                    System.out.println(
                            "Org description: " + getDescription(org));
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
                    }

                    // Display classifications
                    Collection classList = org.getClassifications();

                    for (Object cl : classList) {
                        Classification c = (Classification) cl;
                        System.out.println(
                                " Classification name: " + getName(c));
                        System.out.println(
                                " Classification value: " + c.getValue());

                        ClassificationScheme sch = c.getClassificationScheme();
                        System.out.println(
                                " Classification scheme key: " + getKey(sch));
                    }

                    // Print spacer between organizations
                    System.out.println(" --- ");
                }
            }

            System.out.println("Found " + numOrgs + " organization(s)");
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
