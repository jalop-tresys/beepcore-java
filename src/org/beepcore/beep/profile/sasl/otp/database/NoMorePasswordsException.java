
/*
 * NoMoreTokensException.java            $Revision: 1.1 $ $Date: 2001/04/02 21:38:14 $
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
package org.beepcore.beep.profile.sasl.otp.database;


import org.beepcore.beep.profile.sasl.SASLException;

/**
 * This Exception represents the error case in which a
 * peer attempting to authenticate via SASL OTP finds
 * that no more one-time passwords are available.
 * 
 * @author Eric Dixon
 * @author Huston Franklin
 * @author Jay Kint
 * @author Scott Pead
 * @version $Revision: 1.1 $, $Date: 2001/04/02 21:38:14 $
 *
 */
public class NoMorePasswordsException extends SASLException {

    // Constants    
    public static final String MSG = "No remaining passwords available for ";

    /**
     * Default Constructor
     * 
     * @param String username denotes the identity of the peer
     * wishing to authenticate via SASL OTP
     *
     */
    public NoMorePasswordsException(String username)
    {
        super(MSG + username);
    }
}
