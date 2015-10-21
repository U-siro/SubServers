package net.ME1312.SubServer.Commands;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.ME1312.SubServer.FakeProxyServer;
import net.ME1312.SubServer.SubServerInfo;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class SubDebugCMD extends Command {
    private net.ME1312.SubServer.FakeProxyServer FakeProxyServer;

    public SubDebugCMD(FakeProxyServer FakeProxyServer, String Command){
        super(Command, "SubServers.debug");
        this.FakeProxyServer = FakeProxyServer;
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws ArrayIndexOutOfBoundsException {
        if (args[0].equalsIgnoreCase("addserver")) {
            if (!args[1].contains("!")) {
                FakeProxyServer.ServerInfo.put(args[1],
                        new SubServerInfo((BungeeServerInfo)FakeProxyServer.constructServerInfo(args[1], new InetSocketAddress(args[2], Integer.parseInt(args[3])), "SubServer-" + args[1], false), Boolean.parseBoolean(args[4])));
            } else if (args[1].contains("!")) {
                FakeProxyServer.PlayerServerInfo.put(args[1].replace("!", ""),
                        new SubServerInfo((BungeeServerInfo)FakeProxyServer.constructServerInfo(args[1].replace("!", ""), new InetSocketAddress(args[2], Integer.parseInt(args[3])), "PlayerServer-" + args[1].replace("!", ""), false), Boolean.parseBoolean(args[4])));
            }

            FakeProxyServer.SubServers.add(args[1]);
            FakeProxyServer.getLogger().info(FakeProxyServer.lang.get("Lang.Proxy.Register-Server") + args[1]);
        } else if (args[0].equalsIgnoreCase("sendplayer")) {
            if (FakeProxyServer.getPlayer(args[1]) != null) {
                if (args[2].contains("!") && FakeProxyServer.PlayerServerInfo.keySet().contains(args[2].replace("!", ""))) {
                    FakeProxyServer.getPlayer(args[1]).connect(FakeProxyServer.PlayerServerInfo.get(
                            args[2].replace("!", "")));
                } else {
                    FakeProxyServer.getPlayer(args[1]).connect(FakeProxyServer.ServerInfo.get(args[2]));
                }
                FakeProxyServer.getLogger().info(FakeProxyServer.lang.get("Lang.Proxy.Teleport").replace("$Player$", args[1]).replace("$Server$", args[2]));
            }
        } else if (args[0].equalsIgnoreCase("removeserver")) {
            FakeProxyServer.ServerInfo.remove(args[1]);
            FakeProxyServer.PlayerServerInfo.remove(args[1].replace("!", ""));
            FakeProxyServer.SubServers.remove(args[1]);
            FakeProxyServer.getLogger().info(FakeProxyServer.lang.get("Lang.Proxy.Remove-Server") + args[1]);
        } else if (args[0].equalsIgnoreCase("resetplugin")) {
            List<String> SubServersStore = new ArrayList<String>();
            SubServersStore.addAll(FakeProxyServer.SubServers);
            for(Iterator<String> str = SubServersStore.iterator(); str.hasNext(); ) {
                String item = str.next();
                FakeProxyServer.SubServers.remove(item);
                FakeProxyServer.ServerInfo.remove(item);
                FakeProxyServer.PlayerServerInfo.remove(item.replace("!", ""));
            }
            String str = FakeProxyServer.lang.get("Lang.Proxy.Reset-Storage");
            FakeProxyServer.lang.clear();
            FakeProxyServer.getLogger().info(str);
            FakeProxyServer.getLogger().info("Waiting for new configuration...");
        } else if (args[0].equalsIgnoreCase("lang")) {
            FakeProxyServer.lang.put(args[1], args[2].replace("%20", " "));
        }
    }

}