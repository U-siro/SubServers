package net.ME1312.SubServer.Commands;

import net.ME1312.SubServer.FakeProxyServer;
import net.ME1312.SubServer.SubServerInfo;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

/**
 * Created by ME1312 on 10/16/15.
 */
public class ListCMD extends Command {
    private FakeProxyServer FakeProxyServer;

    public ListCMD(FakeProxyServer FakeProxyServer, String Command) {
        super(Command, "bungeecord.command.list");
        this.FakeProxyServer = FakeProxyServer;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        int total = 0;
        HashMap<String, Integer> playerlist = new HashMap<String, Integer>();
        HashMap<String, List<ProxiedPlayer>> players = new HashMap<String, List<ProxiedPlayer>>();

        for (Iterator<SubServerInfo> servers = FakeProxyServer.ConfigServers.values().iterator(); servers.hasNext(); ) {
            ServerInfo server = servers.next();
            if (!server.getName().equalsIgnoreCase("default")) {
                players.put(server.getName(), new ArrayList<ProxiedPlayer>(server.getPlayers()));
                playerlist.put(server.getName(), server.getPlayers().size());
                total = total + server.getPlayers().size();
            }
        }

        for (Iterator<SubServerInfo> servers = FakeProxyServer.ServerInfo.values().iterator(); servers.hasNext(); ) {
            ServerInfo server = servers.next();
            players.put(server.getName(), new ArrayList<ProxiedPlayer>(server.getPlayers()));
            playerlist.put(server.getName(), server.getPlayers().size());
            total = total + server.getPlayers().size();
        }

        if (!FakeProxyServer.PlayerServerInfo.isEmpty()) {
            List<ProxiedPlayer> pslist = new ArrayList<ProxiedPlayer>();

            for (Iterator<SubServerInfo> servers = FakeProxyServer.PlayerServerInfo.values().iterator(); servers.hasNext(); ) {
                ServerInfo server = servers.next();
                pslist.addAll(server.getPlayers());
                if (!playerlist.keySet().contains("!PlayerServers")) playerlist.put("!PlayerServers", 0);
                playerlist.put("!PlayerServers", (playerlist.get("!PlayerServers") + server.getPlayers().size()));
                total = total + server.getPlayers().size();
            }

            players.put("!PlayerServers", pslist);
        }

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            player.sendMessage(new TextComponent(ChatColor.AQUA + FakeProxyServer.lprefix + "Players Online: " + total));
            player.sendMessage(new TextComponent(""));
            for (Iterator<String> servers = playerlist.keySet().iterator(); servers.hasNext(); ) {
                String server = servers.next();
                player.sendMessage(new TextComponent(ChatColor.AQUA + "[" + server + "] " + ChatColor.YELLOW + "(" + playerlist.get(server) + ") " + ChatColor.RESET + players.get(server).toString().replace("[", "").replace("]", "")));
            }
        } else {
            ProxyServer.getInstance().getLogger().info(FakeProxyServer.lprefix + "Players Online: " + total);
            ProxyServer.getInstance().getLogger().info("");
            for (Iterator<String> servers = playerlist.keySet().iterator(); servers.hasNext(); ) {
                String server = servers.next();
                ProxyServer.getInstance().getLogger().info("[" + server + "] (" + playerlist.get(server) + ") " + players.get(server).toString().replace("[", "").replace("]", ""));
            }
        }
    }
}
