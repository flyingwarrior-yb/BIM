/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jitsi.impl.androidtray;

import android.support.v4.app.*;

import net.java.sip.communicator.service.systray.*;

import java.util.*;

/**
 * Popup notification that consists of few merged previous popups.
 *
 * @author Pawel Domas
 */
public class AndroidMergedPopup extends AndroidPopup {
    /**
     * List of merged popups.
     */
    private List<AndroidPopup> mergedPopups = new ArrayList<AndroidPopup>();

    /**
     * Creates new instance of <tt>AndroidMergedPopup</tt> with given
     * <tt>AndroidPopup</tt> as root.
     *
     * @param rootPopup root <tt>AndroidPopup</tt>.
     */
    AndroidMergedPopup(AndroidPopup rootPopup) {
        super(rootPopup.handler, rootPopup.popupMessage);

        this.id = rootPopup.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AndroidPopup mergePopup(PopupMessage popupMessage) {
        // Timing out notifications are replaced
        /*AndroidPopup replace = null;
        if(mergedPopups.size() > 0)
        {
            replace = mergedPopups.get(mergedPopups.size()-1);
            if(replace.timeoutHandler != null)
            {
                replace.cancelTimeout();
            }
        }
        if(replace != null)
        {
            mergedPopups.set(mergedPopups.indexOf(replace),
                             new AndroidPopup(handler, popupMessage));
        }
        else
        {*/
        mergedPopups.add(new AndroidPopup(handler, popupMessage));
        //}
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getMessage() {
        String msg = super.getMessage(); for (AndroidPopup popup : mergedPopups) {
            msg += "\n" + popup.getMessage();
        } return msg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    NotificationCompat.Builder buildNotification() {
        NotificationCompat.Builder builder = super.buildNotification();

        // Set number of events
        builder.setNumber(1 + mergedPopups.size());

        return builder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onBuildInboxStyle(NotificationCompat.InboxStyle inboxStyle) {
        super.onBuildInboxStyle(inboxStyle);

        for (AndroidPopup popup : mergedPopups) {
            inboxStyle.addLine(popup.getMessage());
        }
    }
}
