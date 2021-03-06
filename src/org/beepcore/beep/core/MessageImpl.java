/*
 * Message.java            $Revision: 1.2 $ $Date: 2004/01/01 16:40:38 $
 *
 * Copyright (c) 2001 Invisible Worlds, Inc.  All rights reserved.
 * Copyright (c) 2003 Huston Franklin.  All rights reserved.
 *
 * The contents of this file are subject to the Blocks Public License (the
 * "License"); You may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.beepcore.org/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied.  See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 */
package org.beepcore.beep.core;


/**
 * Message encapsulates the BEEP MSG, RPY, ERR and NUL message types.
 *
 * @author Eric Dixon
 * @author Huston Franklin
 * @author Jay Kint
 * @author Scott Pead
 * @version $Revision: 1.2 $, $Date: 2004/01/01 16:40:38 $
 */
public class MessageImpl implements Message {

    /**
     * Uninitialized BEEP message.
     */
    public static final int MESSAGE_TYPE_UNK = 0;

    /**
     * BEEP message type.
     */
    public static final int MESSAGE_TYPE_MSG = 1;

    /**
     * BEEP message type.
     */
    public static final int MESSAGE_TYPE_RPY = 2;

    /**
     * BEEP message type.
     */
    public static final int MESSAGE_TYPE_ERR = 3;

    /**
     * BEEP message type.
     */
    public static final int MESSAGE_TYPE_ANS = 4;

    /**
     * BEEP message type.
     */
    public static final int MESSAGE_TYPE_NUL = 5;

    /** BEEP message type of  <code>Message</code>. */
    int messageType = MESSAGE_TYPE_UNK;

    /** <code>Channel</code> to which <code>Message</code> belongs. */
    ChannelImpl channel;

    /** Message number of <code>Message</code>. */
    int msgno;

    /** Answer number of this BEEP message. */
    int ansno;

    /**
     * BEEP message type for utility only.
     */
    private static final int MESSAGE_TYPE_MAX = 6;

    private static final String NOT_MESSAGE_TYPE_MSG =
        "Message is not of type MSG";

    private boolean notified = false;

    /**
     * Payload of the <code>Message</code> stored as a
     * <code>InputDataStream</code>
     *
     * @see org.beepcore.beep.core.InputDataStream
     */
    private InputDataStream data;

    /**
     * Creates a new <code>Message</code>.
     *
     * @param channel <code>Channel</code> to which this <code>Message</code>
     *    belongs.
     * @param msgno Message number of the BEEP message.
     * @param data  <code>InputDataStream</code> containing the payload of the
     *    message.
     * @param messageType Message type of the BEEP message.
     *
     * @see InputDataStream
     * @see Channel
     */
    MessageImpl(ChannelImpl channel, int msgno, InputDataStream data,
                int messageType)
    {
        this.channel = channel;
        this.msgno = msgno;
        this.ansno = -1;
        this.data = data;
        this.messageType = messageType;
    }

    /**
     * Creates a BEEP message of type ANS
     *
     * @param channel <code>Channel</code> to which the message belongs.
     * @param msgno Message number of the message.
     * @param ansno
     * @param data  <code>InputDataStream</code> contains the payload of the
     *    message.
     *
     * @see Channel
     * @see InputDataStream
     */
    MessageImpl(ChannelImpl channel, int msgno, int ansno, InputDataStream data)
    {
        this(channel, msgno, data, MESSAGE_TYPE_ANS);

        this.ansno = ansno;
    }

    /**
     * Returns <code>InputDataStream</code> belonging to <code>Message</code>.
     *
     * @see InputDataStream
     */
    public InputDataStream getDataStream()
    {
        return this.data;
    }

    /**
     * Returns the <code>Channel</code> to which this <code>Message</code>
     * belongs.
     *
     * @see Channel
     */
    public Channel getChannel()
    {
        return this.channel;
    }

    /**
     * Returns the message number of this <code>Message</code>.
     */
    public int getMsgno()
    {
        return this.msgno;
    }

    /**
     * Returns the answer number of this <code>Message</code>.
     */
    public int getAnsno()
    {
        return this.ansno;
    }

    /**
     * Returns the message type of this <code>Message</code>.
     */
    public int getMessageType()
    {
        return this.messageType;
    }

    boolean isNotified()
    {
        return this.notified;
    }

    void setNotified()
    {
        this.notified = true;
    }
}
