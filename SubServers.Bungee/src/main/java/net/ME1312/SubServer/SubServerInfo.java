package net.ME1312.SubServer;

import net.md_5.bungee.BungeeServerInfo;

/**
 * Created by ME1312 on 10/20/15.
 */
public class SubServerInfo extends BungeeServerInfo {
    private boolean sharedChat;

    public SubServerInfo(BungeeServerInfo connection, boolean sharedChat) {
        super(connection.getName(), connection.getAddress(), connection.getMotd(), connection.isRestricted());
        this.sharedChat = sharedChat;
    }

    public boolean isSharedChat() {
        return sharedChat;
    }

    public void setSharedChat(boolean value) {
        sharedChat = value;
    }
}
