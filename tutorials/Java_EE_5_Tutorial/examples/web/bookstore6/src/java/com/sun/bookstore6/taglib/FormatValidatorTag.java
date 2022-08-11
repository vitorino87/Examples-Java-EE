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


package com.sun.bookstore6.taglib;

import javax.faces.context.FacesContext;
import javax.el.*;
import javax.faces.webapp.ValidatorELTag;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;
import com.sun.bookstore6.validators.FormatValidator;


/**
 * FormatValidatorTag is the tag handler class for FormatValidator tag,
 * <code>format_validator</code>.
 *
 */
public class FormatValidatorTag extends ValidatorELTag {
    private static String validatorID = null;
    protected ValueExpression formatPatterns = null;

    public FormatValidatorTag() {
        super();

        if (validatorID == null) {
            validatorID = "FormatValidator";
        }
    }

    public void setValidatorID(String validatorID) {
        this.validatorID = validatorID;
    } // END setValidatorId
      // Attribute Instance Variables

    //
    // Class methods
    //
    public void setFormatPatterns(ValueExpression formatPatterns) {
        this.formatPatterns = formatPatterns;
    }

    protected Validator createValidator() throws JspException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FormatValidator result = null;

        if (validatorID != null) {
            result = (FormatValidator) facesContext.getApplication()
                                                   .createValidator(
                        validatorID);
        }

        String patterns = null;

        if (formatPatterns != null) {
            if (!formatPatterns.isLiteralText()) {
                patterns = (String) formatPatterns.getValue(
                            facesContext.getELContext());
            } else {
                patterns = formatPatterns.getExpressionString();
            }
        }

        result.setFormatPatterns(patterns);

        return result;
    }
} // end of class FormatValidatorTag
