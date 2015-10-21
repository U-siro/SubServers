package net.ME1312.SubServer.Commands;

import net.ME1312.SubServer.FakeProxyServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created by ME1312 on 10/17/15.
 */
public class FindCMD extends Command {
    private FakeProxyServer FakeProxyServer;

    public FindCMD(FakeProxyServer FakeProxyServer, String Command) {
        super(Command, "bungeecord.command.find");
        this.FakeProxyServer = FakeProxyServer;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            boolean Online = false;
            String Server = null;

            if (ProxyServer.getInstance().getPlayer(args[0]) != null) {
                Server = ProxyServer.getInstance().getPlayer(args[0]).getServer().getInfo().getName();
                Online = true;
            }

            if (sender instanceof ProxiedPlayer) {
                if (Online) {
                    ((ProxiedPlayer) sender).sendMessage(new TextComponent(ChatColor.YELLOW + FakeProxyServer.lprefix + args[0] + " is " + ChatColor.GREEN + "Online" + ChatColor.YELLOW + " at " + Server));
                } else {
                    ((ProxiedPlayer) sender).sendMessage(new TextComponent(ChatColor.YELLOW + FakeProxyServer.lprefix + args[0] + " is " + ChatColor.RED + "Offline"));
                }
            } else {
                if (Online) {
                    ProxyServer.getInstance().getLogger().info(FakeProxyServer.lprefix + args[0] + " is Online at " + Server);
                } else {
                    ProxyServer.getInstance().getLogger().info(FakeProxyServer.lprefix + args[0] + " is Offline");
                }
            }
        } else {
            if (sender instanceof ProxiedPlayer) {
                ((ProxiedPlayer) sender).sendMessage(new TextComponent(ChatColor.RED + FakeProxyServer.lprefix + "Please follow this command by a user's name"));
            } else {
                ProxyServer.getInstance().getLogger().info(FakeProxyServer.lprefix + "Please follow this command by a user's name");
            }
        }
    }
}
