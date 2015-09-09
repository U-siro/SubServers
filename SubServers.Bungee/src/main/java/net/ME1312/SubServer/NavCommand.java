package net.ME1312.SubServer;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.ChatColor;
import net.ME1312.SubServer.Main;

public class NavCommand extends Command {
	private Main Main;
	
	public NavCommand(Main Main) {
        super("go");
		this.Main = Main;
    }
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) throws ArrayIndexOutOfBoundsException {
		if (ProxyServer.getInstance().getPlayer(sender.getName()) == null && args.length == 1) {
			ProxyServer.getInstance().getLogger().info(Main.lang.get("Lang.Commands.Teleport-Console-Error"));
		} else if (args.length < 1) {
			String String = "";
			if (Main.ServerInfo.keySet().size() > 0) String = Main.ServerInfo.keySet().toString().replace("[", "").replace("]", "") + ", ";
			if (ProxyServer.getInstance().getServers().keySet().size() > 0) String = String + ProxyServer.getInstance().getServers().keySet().toString().replace("{", "").replace("}", "") + ", ";
			if (Main.PlayerServerInfo.keySet().size() > 0) String = String + 
					Main.lang.get("Lang.Commands.Teleport-Server-List").split("\\|\\|\\|")[2].replace("$int$", Integer.toString(Main.PlayerServerInfo.keySet().size()));
			
			if (ProxyServer.getInstance().getPlayer(sender.getName()) != null) {
				ProxyServer.getInstance().getPlayer(sender.getName()).sendMessages(ChatColor.AQUA + Main.lprefix + Main.lang.get("Lang.Commands.Teleport-Server-List").split("\\|\\|\\|")[0] + 
						ProxyServer.getInstance().getPlayer(sender.getName()).getServer().getInfo().getName(), 
						"", ChatColor.AQUA + Main.lang.get("Lang.Commands.Teleport-Server-List").split("\\|\\|\\|")[1], ChatColor.DARK_AQUA + String);
			} else {
				ProxyServer.getInstance().getLogger().info(Main.lprefix + Main.lang.get("Lang.Commands.Teleport-Server-List").split("\\|\\|\\|")[1]); 
				ProxyServer.getInstance().getLogger().info(String);
			}
		} else if (ProxyServer.getInstance().getServers().keySet().contains(args[0])) {
			if (args.length > 1) {
				if (ProxyServer.getInstance().getPlayer(sender.getName()) == null || ProxyServer.getInstance().getPlayer(sender.getName()).hasPermission("bungeecord.command.send") || ProxyServer.getInstance().getPlayer(sender.getName()).hasPermission("SubServers.Teleport.Others")) {
					if (ProxyServer.getInstance().getPlayer(args[1]) == null) {
						if (ProxyServer.getInstance().getPlayer(sender.getName()) == null) {
							ProxyServer.getInstance().getLogger().info(Main.lprefix + Main.lang.get("Lang.Commands.Teleport-Player-Error"));
						} else {
							ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.get("Lang.Commands.Teleport-Player-Error"));
						}
					} else {
						if (ProxyServer.getInstance().getPlayer(sender.getName()) == null) {
							ProxyServer.getInstance().getLogger().info(Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
						} else {
							ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
						}
						ProxyServer.getInstance().getPlayer(args[1]).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
						ProxyServer.getInstance().getPlayer(args[1]).connect(ProxyServer.getInstance().getServerInfo(args[0]));
					}
				} else {
					ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.get("Lang.Commands.Teleport-Permission-Error"));
				}
			} else {
				ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
				ProxyServer.getInstance().getPlayer(sender.getName()).connect(ProxyServer.getInstance().getServerInfo(args[0]));
			}
		} else if (Main.ServerInfo.keySet().contains(args[0])) {
			if (args.length > 1) {
				if (ProxyServer.getInstance().getPlayer(sender.getName()) == null || ProxyServer.getInstance().getPlayer(sender.getName()).hasPermission("bungeecord.command.send") || ProxyServer.getInstance().getPlayer(sender.getName()).hasPermission("SubServers.Teleport.Others")) {
					if (ProxyServer.getInstance().getPlayer(args[1]) == null) {
						if (ProxyServer.getInstance().getPlayer(sender.getName()) == null) {
							ProxyServer.getInstance().getLogger().info(Main.lprefix + Main.lang.get("Lang.Commands.Teleport-Player-Error"));
						} else {
							ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.get("Lang.Commands.Teleport-Player-Error"));
						}
					} else {
						if (ProxyServer.getInstance().getPlayer(sender.getName()) == null) {
							ProxyServer.getInstance().getLogger().info(Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
						} else {
							ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
						}
						ProxyServer.getInstance().getPlayer(args[1]).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
						ProxyServer.getInstance().getPlayer(args[1]).connect(Main.ServerInfo.get(args[0]));
					}
				} else {
					ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.get("Lang.Commands.Teleport-Permission-Error"));
				}
			} else {
				ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
				ProxyServer.getInstance().getPlayer(sender.getName()).connect(Main.ServerInfo.get(args[0]));
			}
		} else if (args[0].contains("!") && Main.PlayerServerInfo.keySet().contains(args[0].replace("!", ""))) {
			if (args.length > 1) {
				if (ProxyServer.getInstance().getPlayer(sender.getName()) == null || ProxyServer.getInstance().getPlayer(sender.getName()).hasPermission("bungeecord.command.send") || ProxyServer.getInstance().getPlayer(sender.getName()).hasPermission("SubServers.Teleport.Others")) {
					if (ProxyServer.getInstance().getPlayer(args[1]) == null) {
						if (ProxyServer.getInstance().getPlayer(sender.getName()) == null) {
							ProxyServer.getInstance().getLogger().info(Main.lprefix + Main.lang.get("Lang.Commands.Teleport-Player-Error"));
						} else {
							ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.get("Lang.Commands.Teleport-Player-Error"));
						}
					} else {
						if (ProxyServer.getInstance().getPlayer(sender.getName()) == null) {
							ProxyServer.getInstance().getLogger().info(Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
						} else {
							ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
						}
						ProxyServer.getInstance().getPlayer(args[1]).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
						ProxyServer.getInstance().getPlayer(args[1]).connect(Main.PlayerServerInfo.get(args[0]));
					}
				} else {
					ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.get("Lang.Commands.Teleport-Permission-Error"));
				}
			} else {
				ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.get("Lang.Commands.Teleport"));
				ProxyServer.getInstance().getPlayer(sender.getName()).connect(Main.PlayerServerInfo.get(args[0]));
			}
		} else {
			ProxyServer.getInstance().getPlayer(sender.getName()).sendMessage(ChatColor.RED + Main.lang.get("Lang.Commands.Teleport-Config-Error"));
		}
	}
	
}
