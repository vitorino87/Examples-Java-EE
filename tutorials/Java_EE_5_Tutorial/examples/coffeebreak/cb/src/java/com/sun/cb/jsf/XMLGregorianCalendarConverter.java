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


package com.sun.cb.jsf;

import com.sun.faces.util.MessageFactory;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


public class XMLGregorianCalendarConverter implements Converter {
    public static final String CONVERSION_ERROR_MESSAGE_ID = "ConversionError";
    public static final String CONVERTER_ID = "com.sun.cb.jsf.XMLGregorianCalendar";

    /** Creates a new instance of XMLGregorianCalendarConverter */
    public XMLGregorianCalendarConverter() {
    }

    public Object getAsObject(
        FacesContext context,
        UIComponent component,
        String value) {
        try {
            String convertedValue = null;

            if (value == null) {
                return value;
            }

            value = value.trim();

            if (value.length() < 1) {
                return (null);
            }

            XMLGregorianCalendar calendar = DatatypeFactory.newInstance()
                                                           .newXMLGregorianCalendar(
                        value);

            return calendar;
        } catch (ConverterException e) {
            throw e;
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }

    public String getAsString(
        FacesContext context,
        UIComponent component,
        Object value) {
        XMLGregorianCalendar xmlCalendar = null;
        String result = null;

        try {
            if (value == null) {
                return null;
            }

            try {
                xmlCalendar = (XMLGregorianCalendar) value;
            } catch (ClassCastException ce) {
                FacesMessage errMsg = MessageFactory.getMessage(
                            CONVERSION_ERROR_MESSAGE_ID,
                            (new Object[] { value, xmlCalendar }));
                throw new ConverterException(errMsg.getSummary());
            }

            GregorianCalendar calendar = xmlCalendar.toGregorianCalendar();
            String[] dayNames = new DateFormatSymbols().getWeekdays();
            String[] monthNames = new DateFormatSymbols().getMonths();
            result = new String(
                        dayNames[calendar.get(Calendar.DAY_OF_WEEK)] + ", "
                        + monthNames[calendar.get(Calendar.MONTH)] + " "
                        + calendar.get(Calendar.DAY_OF_MONTH) + ", "
                        + calendar.get(Calendar.YEAR));

            return result;
        } catch (ConverterException e) {
            throw e;
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }
}
