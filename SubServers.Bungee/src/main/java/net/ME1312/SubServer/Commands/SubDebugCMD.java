package net.ME1312.SubServer.Commands;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.ME1312.SubServer.Main;

public class SubDebugCMD extends Command {
    private Main Main;

    public SubDebugCMD(Main Main, String Command){
        super(Command, "SubServers.debug");
        this.Main = Main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws ArrayIndexOutOfBoundsException {
        if (args[0].equalsIgnoreCase("addserver")) {
            if (!args[1].contains("!")) {
                Main.ServerInfo.put(args[1],
                        ProxyServer.getInstance().constructServerInfo(args[1], new InetSocketAddress(args[2], Integer.parseInt(args[3])), "SubServer-" + args[1], false));
            } else if (args[1].contains("!")) {
                Main.PlayerServerInfo.put(args[1].replace("!", ""),
                        ProxyServer.getInstance().constructServerInfo(args[1].replace("!", ""), new InetSocketAddress(args[2], Integer.parseInt(args[3])), "PlayerServer-" + args[1].replace("!", ""), false));
            }

            Main.SubServers.add(args[1]);
            if (args[4].equalsIgnoreCase("true")) Main.SharedChat.add(args[1].replace("!", ""));
            ProxyServer.getInstance().getLogger().info(Main.lang.get("Lang.Proxy.Register-Server") + args[1]);
        } else if (args[0].equalsIgnoreCase("sendplayer")) {
            if (ProxyServer.getInstance().getPlayer(args[1]) != null) {
                if (args[2].contains("!") && Main.PlayerServerInfo.keySet().contains(args[2].replace("!", ""))) {
                    ProxyServer.getInstance().getPlayer(args[1]).connect(Main.PlayerServerInfo.get(
                            args[2].replace("!", "")));
                } else {
                    ProxyServer.getInstance().getPlayer(args[1]).connect(Main.ServerInfo.get(args[2]));
                }
                ProxyServer.getInstance().getLogger().info(Main.lang.get("Lang.Proxy.Teleport").replace("$Player$", args[1]).replace("$Server$", args[2]));
            }
        } else if (args[0].equalsIgnoreCase("removeserver")) {
            Main.ServerInfo.remove(args[1]);
            Main.PlayerServerInfo.remove(args[1].replace("!", ""));
            Main.SharedChat.remove(args[1].replace("!", ""));
            Main.SubServers.remove(args[1]);
            ProxyServer.getInstance().getLogger().info(Main.lang.get("Lang.Proxy.Remove-Server") + args[1]);
        } else if (args[0].equalsIgnoreCase("resetplugin")) {
            List<String> SubServersStore = new ArrayList<String>();
            SubServersStore.addAll(Main.SubServers);
            for(Iterator<String> str = SubServersStore.iterator(); str.hasNext(); ) {
                String item = str.next();
                Main.SubServers.remove(item);
                Main.ServerInfo.remove(item);
                Main.SharedChat.remove(item.replace("!", ""));
                Main.PlayerServerInfo.remove(item.replace("!", ""));
            }
            String str = Main.lang.get("Lang.Proxy.Reset-Storage");
            Main.lang.clear();
            ProxyServer.getInstance().getLogger().info(str);
            ProxyServer.getInstance().getLogger().info("Waiting for new config...");
        } else if (args[0].equalsIgnoreCase("lang")) {
            Main.lang.put(args[1], args[2].replace("%20", " "));
        }
    }

}