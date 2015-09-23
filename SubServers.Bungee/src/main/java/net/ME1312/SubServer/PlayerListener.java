package net.ME1312.SubServer;

import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by ME1312 on 9/23/15.
 */
public class PlayerListener implements Listener {
    Main Main;

    public PlayerListener(Main Main) {
        this.Main = Main;
    }

    @EventHandler
    public void onServerKickEvent(ServerKickEvent ev) {
        ServerInfo kickedFrom = null;
        if (ev.getPlayer().getServer() != null) {
            kickedFrom = ev.getPlayer().getServer().getInfo();
        } else if (Main.Plugin.getProxy().getReconnectHandler() != null) {
            kickedFrom = Main.Plugin.getProxy().getReconnectHandler().getServer(ev.getPlayer());
        } else {
            kickedFrom = AbstractReconnectHandler.getForcedHost((PendingConnection) ev.getPlayer().getPendingConnection());
            if (kickedFrom == null) {
                kickedFrom = ProxyServer.getInstance().getServerInfo(ev.getPlayer().getPendingConnection().getListener().getDefaultServer());
            }
        }

        ServerInfo kickTo = Main.ServerInfo.get("~Lobby");
        if (kickedFrom != null && kickedFrom.equals((Object)kickTo)) {
            return;
        }

        ev.setCancelled(true);
        ev.setCancelServer(kickTo);
    }
}
