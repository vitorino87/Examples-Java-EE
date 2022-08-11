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


import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.ActivationConfigProperty;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Entity;
import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;
import javax.jms.MessageProducer;
import javax.jms.MessageListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MapMessage;
import java.util.Random;
import java.util.logging.Logger;
import javax.annotation.Resource;


/**
 * The EquipmentMDB class is a message-driven bean.
 * It implements the javax.jms.MessageListener interface. It
 * is defined as public (but not final or abstract).
 */
@MessageDriven(mappedName = "jms/Topic")
public class EquipmentMDB implements MessageListener {
    static final Logger logger = Logger.getLogger("EquipmentMDB");
    @Resource
    public MessageDrivenContext mdc;
    @PersistenceContext
    EntityManager em;
    @Resource(mappedName = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    private Random processingTime = new Random();

    /**
     * Constructor, which is public and takes no arguments.
     */
    public EquipmentMDB() {
    }

    /**
     * onMessage method, declared as public (but not final or
     * static), with a return type of void, and with one argument
     * of type javax.jms.Message.
     *
     * Casts the incoming Message to a MapMessage, retrieves its
     * contents, and assigns equipment appropriate to the new
     * hire's position.  Calls the compose method to store the
     * information in the persistence entity and, if work is complete,
     * to send a reply message to the client.
     *
     * @param inMessage    the incoming message
     */
    public void onMessage(Message inMessage) {
        MapMessage msg = null;
        String key = null;
        String name = null;
        String position = null;
        String equipmentList = null;

        try {
            if (inMessage instanceof MapMessage) {
                msg = (MapMessage) inMessage;
                key = msg.getString("HireID");
                name = msg.getString("Name");
                position = msg.getString("Position");
                logger.info(
                        "EquipmentMDB.onMessage:"
                        + " Message received for employeeId " + key);

                if (position.equals("Programmer")) {
                    equipmentList = "Desktop System";
                } else if (position.equals("Senior Programmer")) {
                    equipmentList = "Laptop";
                } else if (position.equals("Manager")) {
                    equipmentList = "Pager";
                } else if (position.equals("Director")) {
                    equipmentList = "Java Phone";
                } else {
                    equipmentList = "Baton";
                }

                /* Simulate processing time taking 1 to 10
                 * seconds.
                 */
                Thread.sleep(processingTime.nextInt(10) * 1000);
                compose(key, name, equipmentList, msg);
            } else {
                logger.warning(
                        "EquipmentMDB.onMessage:" + "Message of wrong type: "
                        + inMessage.getClass().getName());
            }
        } catch (JMSException e) {
            logger.severe(
                    "EquipmentMDB.onMessage: JMSException: " + e.toString());
            mdc.setRollbackOnly();
        } catch (Throwable te) {
            logger.severe(
                    "EquipmentMDB.onMessage: Exception: " + te.toString());
        }
    }

    /**
     * compose method, helper to onMessage method.
     *
     * Locates the row of the database represented by the primary
     * key and adds the equipment allocated for the new hire.
     *
     * @param key           employee ID, primary key
     * @param name          employee name
     * @param equipmentList equipment allocated based on position
     * @param msg           the message received
     */
    void compose(
        String key,
        String name,
        String equipmentList,
        Message msg) {
        int num = 0;
        SetupOffice so = null;
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        MapMessage replyMsg = null;
        Destination replyDest = null;
        String replyCorrelationMsgId = null;
        boolean done = false;

        try {
            so = em.find(SetupOffice.class, key);

            if (so != null) {
                logger.info(
                        "EquipmentMDB.compose: "
                        + "Found join entity for employeeId " + key);
            }
        } catch (IllegalArgumentException iae) {
            logger.warning("EquipmentMDB.compose: No join entity found");
        } catch (Exception e) {
            logger.severe(
                    "EquipmentMDB.compose: em.find failed without"
                    + " throwing IllegalArgumentException");

            //e.printStackTrace();
        }

        // No entity found; create it
        if (so == null) {
            try {
                logger.info(
                        "EquipmentMDB.compose: "
                        + "Creating join entity for employeeId " + key);
                so = new SetupOffice(key, name);
                em.persist(so);
            } catch (Exception e) {
                logger.warning(
                        "EquipmentMDB.compose: "
                        + "Could not create join entity");
                mdc.setRollbackOnly();
            }
        }

        // Entity found or created, so add equipment
        if (so != null) {
            try {
                done = so.doEquipmentList(equipmentList);
            } catch (Exception e) {
                logger.severe(
                        "EquipmentMDB.compose: "
                        + "Could not get equipment for employeeId " + key);
                //e.printStackTrace();
                mdc.setRollbackOnly();
            }
        }

        /* Whichever bean receives the information that the setup is
         * complete sends a message back to the client and removes
         * the entity.
         */
        if (done) {
            try {
                connection = connectionFactory.createConnection();
            } catch (Exception ex) {
                logger.severe(
                        "EquipmentMDB.compose: "
                        + "Unable to connect to JMS provider: " + ex.toString());
            }

            try {
                /*
                 * Send reply to messages aggregated by this
                 * composite entity.  Call createReplyMsg
                 * to construct the reply.
                 */
                replyDest = msg.getJMSReplyTo();
                replyCorrelationMsgId = msg.getJMSMessageID();
                session = connection.createSession(true, 0);
                producer = session.createProducer(replyDest);
                replyMsg = createReplyMsg(so, session, replyCorrelationMsgId);
                producer.send(replyMsg);
                logger.info(
                        "EquipmentMDB.compose: "
                        + "Sent reply message for employeeId "
                        + so.getEmployeeId());
            } catch (JMSException je) {
                logger.severe(
                        "EquipmentMDB.compose: " + "JMSException: "
                        + je.toString());
            } catch (Exception e) {
                logger.severe(
                        "EquipmentMDB.compose: " + "Exception: " + e.toString());
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException je) {
                        logger.severe(
                                "EquipmentMDB.compose: " + "JMSException: "
                                + je.toString());
                    }

                    connection = null;
                }
            }

            logger.info(
                    "EquipmentMDB.compose: "
                    + "REMOVING SetupOffice entity employeeId="
                    + so.getEmployeeId() + ", Name=" + so.getEmployeeName());
            em.remove(so);
        }
    }

    /**
     * The createReplyMsg method composes the reply message
     * with the new hire information.
     *
     * @param session   the Session object for the message
     *                  producer
     * @param msgId        the reply correlation message ID
     *
     * @return  a MapMessage containing the reply message
     */
    private MapMessage createReplyMsg(
        SetupOffice so,
        Session session,
        String msgId) throws JMSException {
        MapMessage replyMsg = session.createMapMessage();
        replyMsg.setString(
            "employeeId",
            so.getEmployeeId());
        replyMsg.setString(
            "employeeName",
            so.getEmployeeName());
        replyMsg.setString(
            "equipmentList",
            so.getEquipmentList());
        replyMsg.setInt(
            "officeNumber",
            so.getOfficeNumber());
        replyMsg.setJMSCorrelationID(msgId);

        return replyMsg;
    }
}
