package net.ME1312.SubServer;

import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Iterator;

/**
 * Created by ME1312 on 9/23/15.
 */
public class PlayerListener implements Listener {
    FakeProxyServer FakeProxyServer;

    public PlayerListener(FakeProxyServer FakeProxyServer) {
        this.FakeProxyServer = FakeProxyServer;
    }

    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        ServerInfo kickedFrom = null;
        if (event.getPlayer().getServer() != null) {
            kickedFrom = event.getPlayer().getServer().getInfo();
        } else if (FakeProxyServer.getReconnectHandler() != null) {
            kickedFrom = FakeProxyServer.getReconnectHandler().getServer(event.getPlayer());
        } else {
            kickedFrom = AbstractReconnectHandler.getForcedHost(event.getPlayer().getPendingConnection());
            if (kickedFrom == null) {
                kickedFrom = FakeProxyServer.getServerInfo(event.getPlayer().getPendingConnection().getListener().getDefaultServer());
            }
        }

        if (FakeProxyServer.ServerInfo.keySet().contains("~Lobby")) {
            ServerInfo kickTo = FakeProxyServer.ServerInfo.get("~Lobby");
            if (kickedFrom != null && !kickedFrom.equals(kickTo)) {
                event.getPlayer().setReconnectServer(kickTo);
                event.setCancelled(true);
                event.getPlayer().sendMessage(new TextComponent(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "Connection Lost: " + ChatColor.RESET.toString() + event.getKickReasonComponent()[0].toLegacyText()));
            }
        }
    }

    @EventHandler
    public void onMessageSend(ChatEvent event) {
        if (!event.isCancelled() && (event.getSender() instanceof ProxiedPlayer) && !event.getMessage().startsWith("/") && ((SubServerInfo) FakeProxyServer.getServerInfo(((ProxiedPlayer) event.getSender()).getServer().getInfo().getName())).isSharedChat()) {
            String message = event.getMessage();
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();

            for(Iterator<ServerInfo> servers = FakeProxyServer.getServers().values().iterator(); servers.hasNext(); ) {
                SubServerInfo server = (SubServerInfo)servers.next();
                if (server.isSharedChat()) {
                    String item = server.getName();
                    if (FakeProxyServer.getServers().keySet().contains(item) && player.getServer().getInfo().getAddress() != FakeProxyServer.getServerInfo(item).getAddress()) {
                        for (Iterator<ProxiedPlayer> players = FakeProxyServer.getServerInfo(item).getPlayers().iterator(); players.hasNext(); ) {
                            players.next().sendMessage(new TextComponent(FakeProxyServer.lang.get("Lang.Proxy.Chat-Format").replace("$displayname$", player.getDisplayName()).replace("$message$", message).replace("$server$", player.getServer().getInfo().getName())));
                        }
                    } if (FakeProxyServer.ServerInfo.keySet().contains(item) && player.getServer().getInfo().getAddress() != FakeProxyServer.ServerInfo.get(item).getAddress()) {
                        for (Iterator<ProxiedPlayer> players = FakeProxyServer.ServerInfo.get(item).getPlayers().iterator(); players.hasNext(); ) {
                            players.next().sendMessage(new TextComponent(FakeProxyServer.lang.get("Lang.Proxy.Chat-Format").replace("$displayname$", player.getDisplayName()).replace("$message$", message).replace("$server$", player.getServer().getInfo().getName().replace("~", ""))));
                        }
                    } if (FakeProxyServer.PlayerServerInfo.keySet().contains(item) && player.getServer().getInfo().getAddress() != FakeProxyServer.PlayerServerInfo.get(item).getAddress()) {
                        for (Iterator<ProxiedPlayer> players = FakeProxyServer.PlayerServerInfo.get(item).getPlayers().iterator(); players.hasNext(); ) {
                            players.next().sendMessage(new TextComponent(FakeProxyServer.lang.get("Lang.Proxy.Chat-Format").replace("$displayname$", player.getDisplayName()).replace("$message$", message).replace("$server$", player.getServer().getInfo().getName())));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (event.getTarget().getName().equalsIgnoreCase("~Lobby") && !event.getTarget().getMotd().equalsIgnoreCase("SubServer-~Lobby")) {
            event.setCancelled(true);
            if (FakeProxyServer.ServerInfo.keySet().contains("~Lobby")) {
                event.getPlayer().connect(FakeProxyServer.ServerInfo.get("~Lobby"));
            } else {
                event.getPlayer().disconnect(new TextComponent("Server \"~Lobby\" Unavailable. Please try again later."));
            }
        }
    }
}
