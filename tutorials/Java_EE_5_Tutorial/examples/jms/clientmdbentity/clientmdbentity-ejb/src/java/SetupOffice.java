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


import java.io.Serializable;
import java.io.IOException;
import java.util.logging.Logger;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * The SetupOffice class implements the business methods of
 * the entity.
 */
@Entity
public class SetupOffice implements Serializable {
    static final Logger logger = Logger.getLogger("SetupOffice");
    private String equip;

    /*
     * There should be a list of replies for each message being
     * joined.  This example is joining the work of separate
     * departments on the same original request, so it is all
     * right to have only one reply destination.  In theory, this
     * should be a set of destinations, with one reply for each
     * unique destination.
     */
    private String id;
    private String name;
    private int officeNum;

    /**
     * no-argument constructor
     */
    public SetupOffice() {
    }

    /**
     * Constructor with two arguments
     *
     * @param newHireID  employee ID (primary key)
     * @param name       employee name
     */
    public SetupOffice(
        String newhireID,
        String name) {
        setEmployeeId(newhireID);
        setEmployeeName(name);
        setEquipmentList(null);
        setOfficeNumber(-1);
    }

    @Id
    public String getEmployeeId() {
        return id;
    }

    public void setEmployeeId(String id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return name;
    }

    public void setEmployeeName(String name) {
        this.name = name;
    }

    public int getOfficeNumber() {
        return officeNum;
    }

    public void setOfficeNumber(int officeNum) {
        this.officeNum = officeNum;
    }

    public String getEquipmentList() {
        return equip;
    }

    public void setEquipmentList(String equip) {
        this.equip = equip;
    }

    /**
     * The doEquipmentList method stores the assigned equipment
     * in the database, then determines if setup is complete.
     *
     * @param list    assigned equipment
     *
     * @return        true if setup is complete
     */
    public boolean doEquipmentList(String list) {
        boolean done = false;

        setEquipmentList(list);
        logger.info(
                "SetupOffice.doEquipmentList: Equipment for " + "employeeId "
                + getEmployeeId() + " is " + getEquipmentList()
                + " (office number " + getOfficeNumber() + ")");
        done = checkIfSetupComplete();

        return done;
    }

    /**
     * The doOfficeNumber method stores the assigned office
     * number in the database, then determines if setup is complete.
     *
     * @param officeNum    assigned office
     *
     * @return        true if setup is complete
     */
    public boolean doOfficeNumber(int officeNum) {
        boolean done = false;

        setOfficeNumber(officeNum);
        logger.info(
                "SetupOffice.doOfficeNumber: Office number for "
                + "employeeId " + getEmployeeId() + " is " + getOfficeNumber()
                + " (equipment " + getEquipmentList() + ")");
        done = checkIfSetupComplete();

        return done;
    }

    /**
     * The checkIfSetupComplete method determines whether
     * both the office and the equipment have been assigned.  If
     * so, it reports that the work of the entity is done.
     *
     * @return        true if setup is complete
     */
    private boolean checkIfSetupComplete() {
        boolean allDone = false;

        if ((getEquipmentList() != null) && (getOfficeNumber() != -1)) {
            logger.info(
                    "SetupOffice.checkIfSetupComplete: SCHEDULE"
                    + " employeeId=" + getEmployeeId() + ", Name="
                    + getEmployeeName() + " to be set up in office #"
                    + getOfficeNumber() + " with " + getEquipmentList());

            allDone = true;
        }

        return allDone;
    }
}
