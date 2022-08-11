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
import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Key;
import java.net.PasswordAuthentication;
import java.util.ResourceBundle;
import java.util.Properties;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;


/**
 * The JAXRDelete class consists of a main method, a makeConnection
 * method, a createOrgKey method, and an executeRemove method. It
 * finds and deletes the organization that the JAXRPublish program
 * created.  Specify the string key ID value returned by the
 * JAXRPublish program.
 */
public class JAXRDelete {
    Connection connection = null;
    RegistryService rs = null;

    public JAXRDelete() {
    }

    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("JAXRExamples");
        Key key = null;

        String queryURL = bundle.getString("query.url");
        String publishURL = bundle.getString("publish.url");

        String username = bundle.getString("registry.username");
        String password = bundle.getString("registry.password");

        if (args.length < 1) {
            System.out.println("Argument required: " + "-Dkey-string=<value>");
            System.exit(1);
        }

        String keyString = args[0];
        System.out.println("Key string is " + keyString);

        JAXRDelete jd = new JAXRDelete();

        jd.makeConnection(queryURL, publishURL);

        key = jd.createOrgKey(keyString);

        if (key != null) {
            jd.executeRemove(key, username, password);
        } else {
            System.out.println("Key not found, nothing to remove");
        }
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
         * To delete, you need both the query URL and the
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
     * Creates a Key object from the user-supplied string.
     *
     * @param keyStr        the key of the published organization
     *
     * @return        the key of the organization found
     */
    public Key createOrgKey(String keyStr) {
        BusinessLifeCycleManager blcm = null;
        Key orgKey = null;

        try {
            rs = connection.getRegistryService();
            blcm = rs.getBusinessLifeCycleManager();
            System.out.println(
                    "Got registry service and " + "life cycle manager");

            orgKey = blcm.createKey(keyStr);
        } catch (Exception e) {
            e.printStackTrace();

            if (connection != null) {
                try {
                    connection.close();
                } catch (JAXRException je) {
                    System.err.println("Connection close failed");
                }
            }
        }

        return orgKey;
    }

    /**
     * Removes the organization with the specified key value.
     *
     * @param key        the Key of the organization
     * @param username  the username for the registry
     * @param password  the password for the registry
     */
    public void executeRemove(
        Key key,
        String username,
        String password) {
        BusinessLifeCycleManager blcm = null;

        try {
            blcm = rs.getBusinessLifeCycleManager();

            // Get authorization from the registry
            PasswordAuthentication passwdAuth = new PasswordAuthentication(
                        username,
                        password.toCharArray());

            HashSet<PasswordAuthentication> creds = new HashSet<PasswordAuthentication>();
            creds.add(passwdAuth);
            connection.setCredentials(creds);
            System.out.println("Established security credentials");

            String id = key.getId();
            System.out.println("Deleting organization with id " + id);

            Collection<Key> keys = new ArrayList<Key>();
            keys.add(key);

            BulkResponse response = blcm.deleteOrganizations(keys);
            Collection exceptions = response.getExceptions();

            if (exceptions == null) {
                System.out.println("Organization deleted");

                Collection retKeys = response.getCollection();

                for (Object k : retKeys) {
                    Key orgKey = (Key) k;
                    id = orgKey.getId();
                    System.out.println("Organization key was " + id);
                }
            } else {
                for (Object e : exceptions) {
                    Exception exception = (Exception) e;
                    System.err.println(
                            "Exception on delete: " + exception.toString());
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
