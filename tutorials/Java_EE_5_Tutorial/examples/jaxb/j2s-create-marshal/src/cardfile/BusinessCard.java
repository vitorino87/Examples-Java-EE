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


/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://jwsdp.dev.java.net/CDDLv1.0.html
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://jwsdp.dev.java.net/CDDLv1.0.html  If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 */
package cardfile;

import javax.xml.bind.annotation.*;


@XmlRootElement
public class BusinessCard {
    private Address address;
    private String cellPhone;
    private String company;
    private String email;
    private String fax;
    private String name;
    private String phone;
    private String title;

    public BusinessCard() {
    }

    public BusinessCard(
        String name,
        String title,
        String company,
        Address address,
        String phone,
        String cellPhone,
        String fax,
        String email) {
        this.name = name;
        this.title = title;
        this.company = company;
        this.address = address;
        this.phone = phone;
        this.cellPhone = cellPhone;
        this.fax = fax;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        if (name != null) {
            s.append(name)
             .append('\n');
        }

        if (title != null) {
            s.append(title)
             .append('\n');
        }

        if (company != null) {
            s.append(company)
             .append('\n');
        }

        if (address != null) {
            s.append(address.toString())
             .append('\n');
        }

        if (phone != null) {
            s.append("phone: ")
             .append(phone)
             .append('\n');
        }

        if (cellPhone != null) {
            s.append("cell:  ")
             .append(cellPhone)
             .append('\n');
        }

        if (fax != null) {
            s.append("fax:  ")
             .append(fax)
             .append('\n');
        }

        if (email != null) {
            s.append(email)
             .append('\n');
        }

        return s.toString();
    }
}
