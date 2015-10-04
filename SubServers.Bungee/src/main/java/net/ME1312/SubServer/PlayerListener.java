package net.ME1312.SubServer;

import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by ME1312 on 9/23/15.
 */
public class PlayerListener implements Listener {
    Main Main;

    public PlayerListener(Main Main) {
        this.Main = Main;
    }

    @EventHandler
    public void onServerKickEvent(ServerKickEvent event) {
        ServerInfo kickedFrom = null;
        if (event.getPlayer().getServer() != null) {
            kickedFrom = event.getPlayer().getServer().getInfo();
        } else if (ProxyServer.getInstance().getReconnectHandler() != null) {
            kickedFrom = ProxyServer.getInstance().getReconnectHandler().getServer(event.getPlayer());
        } else {
            kickedFrom = AbstractReconnectHandler.getForcedHost(event.getPlayer().getPendingConnection());
            if (kickedFrom == null) {
                kickedFrom = ProxyServer.getInstance().getServerInfo(event.getPlayer().getPendingConnection().getListener().getDefaultServer());
            }
        }

        if (Main.ServerInfo.keySet().contains("~Lobby")) {
            ServerInfo kickTo = Main.ServerInfo.get("~Lobby");
            if (kickedFrom != null && !kickedFrom.equals(kickTo)) {
                event.getPlayer().setReconnectServer(kickTo);
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "Connection Lost: " + ChatColor.RESET.toString() + event.getKickReasonComponent()[0].toLegacyText());
            }
        }
    }

    @EventHandler
    public void onPlayerMessage(ChatEvent event) {
        if (!event.isCancelled() && (event.getSender() instanceof ProxiedPlayer) && !event.getMessage().startsWith("/")) {
            String message = event.getMessage();
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();

            for(Iterator<String> str = Main.SharedChat.iterator(); str.hasNext(); ) {
                String item = str.next();
                if (Main.ServerInfo.keySet().contains(item) && player.getServer().getInfo().getAddress() != Main.ServerInfo.get(item).getAddress()) {
                    for (Iterator<ProxiedPlayer> players = Main.ServerInfo.get(item).getPlayers().iterator(); players.hasNext(); ) {
                        players.next().sendMessage(Main.lang.get("Lang.Proxy.Chat-Format").replace("$displayname$", player.getDisplayName()).replace("$message$", message).replace("$server$", player.getServer().getInfo().getName().replace("~", "")));
                    }
                } if (Main.PlayerServerInfo.keySet().contains(item) && player.getServer().getInfo().getAddress() != Main.PlayerServerInfo.get(item).getAddress()) {
                    for (Iterator<ProxiedPlayer> players = Main.PlayerServerInfo.get(item).getPlayers().iterator(); players.hasNext(); ) {
                        players.next().sendMessage(Main.lang.get("Lang.Proxy.Chat-Format").replace("$displayname$", player.getDisplayName()).replace("$message$", message).replace("$server$", player.getServer().getInfo().getName().replace("~", "")));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLogin(ServerConnectEvent event) {
        if (event.getTarget().getName().equalsIgnoreCase("default")) {
            event.setCancelled(true);
            if (Main.ServerInfo.keySet().contains("~Lobby")) {
                event.getPlayer().connect(Main.ServerInfo.get("~Lobby"));
            } else {
                event.getPlayer().disconnect("Server \"~Lobby\" Unavailable. Please try again later.");
            }
        }
    }
}
