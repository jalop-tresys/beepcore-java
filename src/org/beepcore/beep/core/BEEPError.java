
/*
 * BEEPError.java            $Revision: 1.1 $ $Date: 2001/04/02 08:56:06 $
 *
 * Copyright (c) 2001 Invisible Worlds, Inc.  All rights reserved.
 *
 * The contents of this file are subject to the Blocks Public License (the
 * "License"); You may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.invisible.net/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied.  See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 */
package org.beepcore.beep.core;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import org.xml.sax.SAXException;


/**
 * Class BEEPError
 *
 *
 * @author Eric Dixon
 * @author Huston Franklin
 * @author Jay Kint
 * @author Scott Pead
 * @version $Revision: 1.1 $, $Date: 2001/04/02 08:56:06 $
 */
public class BEEPError extends BEEPException {

    private static final String ERR_MALFORMED_XML_MSG = "Malformed XML";
    private static final String ERR_UNKNOWN_OPERATION_ELEMENT_MSG =
        "Unknown operation element";
    private static final String ERR_PCDATA_TOO_BIG_MSG =
        "Element's PCDATA exceeds the maximum size";

    private int code;
    private String xmlLang = null;

    /**
     * Constructor BEEPError
     *
     *
     * @param code
     * @param diagnostic
     * @param xmlLang
     *
     */
    public BEEPError(int code, String diagnostic, String xmlLang)
    {
        super(diagnostic);

        this.xmlLang = xmlLang;
        this.code = code;
    }

    /**
     * Constructor BEEPError
     *
     *
     * @param code
     * @param diagnostic
     *
     */
    public BEEPError(int code, String diagnostic)
    {
        super(diagnostic);

        this.code = code;
    }

    /**
     * Constructor BEEPError
     *
     *
     * @param code
     *
     */
    public BEEPError(int code)
    {

        /**
         * @todo change this and the other constructors to call
         *  super("<error code=" + code + "/>") or something similar.
         */
        super("");

        this.code = code;
    }

    /**
     * Method getCode
     *
     *
     * @return the BEEP ERR code
     *
     */
    public int getCode()
    {
        return this.code;
    }

    /**
     * Method getXMLLang
     *
     *
     * @return the BEEP ERR xmllang
     *
     */
    public String getXMLLang()
    {
        return this.xmlLang;
    }

    /**
     * Method getDiagnostic
     *
     *
     * @return the BEEP ERR diagnostic message
     *
     */
    public String getDiagnostic()
    {
        return this.getMessage();
    }

    /**
     * Method createErrorMessage
     *
     *
     * @return the XML error element that can be sent in a BEEP ERR message
     *
     */
    public String createErrorMessage()
    {
        StringBuffer sb = new StringBuffer(1024);

        sb.append(Constants.FRAGMENT_ERROR_PREFIX);
        sb.append(Constants.FRAGMENT_CODE_PREFIX);
        sb.append(this.getCode());

        String xmlLang = this.getXMLLang();

        if (xmlLang != null) {
            sb.append(Constants.FRAGMENT_QUOTE_SUFFIX);
            sb.append(Constants.FRAGMENT_XML_LANG_PREFIX);
            sb.append(xmlLang);
        }

        String diag = this.getMessage();

        if (diag != null) {
            sb.append(Constants.FRAGMENT_QUOTE_SUFFIX);
            sb.append(Constants.FRAGMENT_ANGLE_SUFFIX);
            sb.append(diag);
            sb.append(Constants.FRAGMENT_ERROR_SUFFIX);
        } else {
            sb.append(Constants.FRAGMENT_QUOTE_SLASH_ANGLE_SUFFIX);
        }

        return sb.toString();
    }

    /**
     * Method convertMessageERRToException
     *
     *
     * @param message
     *
     * @return New <code>BEEPError</code> for the specified BEEP ERR message
     *
     * @throws BEEPException
     *
     */
    protected static BEEPError convertMessageERRToException(Message message)
            throws BEEPException
    {
        if (message.getMessageType() != Message.MESSAGE_TYPE_ERR) {
            throw new IllegalArgumentException("messageType != ERR");
        }

        // parse the stream
        Document doc = null;

        try {
            DocumentBuilder builder =
                DocumentBuilderFactory.newInstance().newDocumentBuilder();

            doc = builder.parse(message.getDataStream().getInputStream());
        } catch (ParserConfigurationException e) {
            throw new BEEPException("Invalid parser configuration");
        } catch (SAXException e) {
            throw new BEEPException(ERR_MALFORMED_XML_MSG);
        } catch (IOException ioe) {
            throw new BEEPException(ERR_MALFORMED_XML_MSG);
        }

        if (doc == null) {
            throw new BEEPException(ERR_MALFORMED_XML_MSG);
        }

        Element topElement = doc.getDocumentElement();

        if (topElement == null) {
            throw new BEEPException(ERR_MALFORMED_XML_MSG);
        }

        // check for <error>
        String elementName = topElement.getTagName();

        if (elementName == null) {
            throw new BEEPException(ERR_MALFORMED_XML_MSG);
        } else if (!elementName.equals(Constants.TAG_ERROR)) {
            throw new BEEPException(ERR_UNKNOWN_OPERATION_ELEMENT_MSG);
        }

        String code = topElement.getAttribute(Constants.TAG_CODE);

        if (code == null) {
            throw new BEEPException(ERR_MALFORMED_XML_MSG);
        }

        // this attribute is implied
        String xmlLang = topElement.getAttribute(Constants.TAG_XML_LANG);
        Node dataNode = topElement.getFirstChild();
        String data = null;

        if (dataNode != null) {
            data = dataNode.getNodeValue();

            if (data.length() > Constants.MAX_PCDATA_SIZE) {
                throw new BEEPException(ERR_PCDATA_TOO_BIG_MSG);
            }
        }

        return new BEEPError(Integer.parseInt(code), data, xmlLang);
    }

    /** Success */
    public static final int CODE_SUCCESS = 200;

    /** Service not available */
    public static final int CODE_SERVICE_NOT_AVAILABLE = 421;

    /** Requested action not taken (e.g., lock already in use) */
    public static final int CODE_REQUESTED_ACTION_NOT_TAKEN = 450;

    /** Requested action aborted (e.g., local error in processing) */
    public static final int CODE_REQUESTED_ACTION_ABORTED = 451;

    /** Temporary authentication failure */
    public static final int CODE_TEMPORARY_AUTHENTICATION_FAILURE = 454;

    /** General syntax error (e.g., poorly-formed XML) */
    public static final int CODE_GENERAL_SYNTAX_ERROR = 500;

    /** Syntax error in parameters (e.g., non-valid XML) */
    public static final int CODE_PARAMETER_ERROR = 501;

    /** Parameter not implemented */
    public static final int CODE_PARAMETER_NOT_IMPLEMENTED = 504;

    /** Authentication required */
    public static final int CODE_AUTHENTICATION_REQUIRED = 530;

    /**
     * Authentication mechanism insufficient (e.g., too weak,
     * sequence exhausted, etc.)
     */
    public static final int CODE_AUTHENTICATION_INSUFFICIENT = 534;

    /** Authentication failure */
    public static final int CODE_AUTHENTICATION_FAILURE = 535;

    /** Action not authorized for user */
    public static final int CODE_ACTION_NOT_AUTHORIZED = 537;

    /** Authentication mechanism requires encryption */
    public static final int CODE_AUTHENTICATION_REQUIRES_ENCRYPTION = 538;

    /**
     * requested action not taken (e.g., no requested profiles are
     * acceptable)
     */
    public static final int CODE_REQUESTED_ACTION_NOT_TAKEN2 = 550;

    /** Parameter invalid */
    public static final int CODE_PARAMETER_INVALID = 553;

    /** Transaction failed (e.g. policy violation) */
    public static final int CODE_TRANSACTION_FAILED = 554;
}