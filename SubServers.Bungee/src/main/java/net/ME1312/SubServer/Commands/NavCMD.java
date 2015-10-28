package net.ME1312.SubServer.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.ChatColor;
import net.ME1312.SubServer.FakeProxyServer;

public class NavCMD extends Command {
	private FakeProxyServer FakeProxyServer;
	
	public NavCMD(FakeProxyServer FakeProxyServer, String Command) {
        super(Command, "bungeecord.command.server");
		this.FakeProxyServer = FakeProxyServer;
    }
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) throws ArrayIndexOutOfBoundsException {
		if (FakeProxyServer.getPlayer(sender.getName()) == null && args.length == 1) {
			FakeProxyServer.getLogger().info(FakeProxyServer.lang.get("Lang.Commands.Teleport-Console-Error"));
		} else if (args.length < 1) {
			String String = "";
			if (FakeProxyServer.ServerInfo.keySet().size() > 0) String = FakeProxyServer.ServerInfo.keySet().toString().replace("[", "").replace("]", "") + ", ";
			if (FakeProxyServer.ConfigServers.keySet().size() > 0) String = String + (FakeProxyServer.ConfigServers.keySet().toString().replace("[", "").replace("]", "") + ", ").replace("~Lobby, ", "");
			if (FakeProxyServer.PlayerServerInfo.keySet().size() > 0) String = String +
					FakeProxyServer.lang.get("Lang.Commands.Teleport-Server-List").split("\\|\\|\\|")[2].replace("$int$", Integer.toString(FakeProxyServer.PlayerServerInfo.keySet().size()));

			if (FakeProxyServer.getPlayer(sender.getName()) != null) {
				FakeProxyServer.getPlayer(sender.getName()).sendMessages(ChatColor.AQUA + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport-Server-List").split("\\|\\|\\|")[0] +
						FakeProxyServer.getPlayer(sender.getName()).getServer().getInfo().getName(),
						"", ChatColor.AQUA + FakeProxyServer.lang.get("Lang.Commands.Teleport-Server-List").split("\\|\\|\\|")[1], ChatColor.DARK_AQUA + String);
			} else {
				FakeProxyServer.getLogger().info(FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport-Server-List").split("\\|\\|\\|")[1]);
				FakeProxyServer.getLogger().info(String);
			}

		} else if (FakeProxyServer.ConfigServers.keySet().contains(args[0]) && !args[0].equalsIgnoreCase("~Lobby")) {
			if (args.length > 1) {
				if (FakeProxyServer.getPlayer(sender.getName()) == null || FakeProxyServer.getPlayer(sender.getName()).hasPermission("bungeecord.command.send") || FakeProxyServer.getPlayer(sender.getName()).hasPermission("SubServers.Teleport.Others")) {
					if (FakeProxyServer.getPlayer(args[1]) == null) {
						if (FakeProxyServer.getPlayer(sender.getName()) == null) {
							FakeProxyServer.getLogger().info(FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport-Player-Error"));
						} else {
							FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.RED + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport-Player-Error"));
						}
					} else {
						if (FakeProxyServer.getPlayer(sender.getName()) == null) {
							FakeProxyServer.getLogger().info(FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
						} else {
							FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
						}
						FakeProxyServer.getPlayer(args[1]).sendMessage(ChatColor.AQUA + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
						FakeProxyServer.getPlayer(args[1]).connect(FakeProxyServer.ConfigServers.get(args[0]));
					}
				} else {
					FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.RED + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport-Permission-Error"));
				}
			} else {
				FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
				FakeProxyServer.getPlayer(sender.getName()).connect(FakeProxyServer.ConfigServers.get(args[0]));
			}
		} else if (FakeProxyServer.ServerInfo.keySet().contains(args[0])) {
			if (args.length > 1) {
				if (FakeProxyServer.getPlayer(sender.getName()) == null || FakeProxyServer.getPlayer(sender.getName()).hasPermission("bungeecord.command.send") || FakeProxyServer.getPlayer(sender.getName()).hasPermission("SubServers.Teleport.Others")) {
					if (FakeProxyServer.getPlayer(args[1]) == null) {
						if (FakeProxyServer.getPlayer(sender.getName()) == null) {
							FakeProxyServer.getLogger().info(FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport-Player-Error"));
						} else {
							FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.RED + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport-Player-Error"));
						}
					} else {
						if (FakeProxyServer.getPlayer(sender.getName()) == null) {
							FakeProxyServer.getLogger().info(FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
						} else {
							FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
						}
						FakeProxyServer.getPlayer(args[1]).sendMessage(ChatColor.AQUA + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
						FakeProxyServer.getPlayer(args[1]).connect(FakeProxyServer.ServerInfo.get(args[0]));
					}
				} else {
					FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.RED + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport-Permission-Error"));
				}
			} else {
				FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
				FakeProxyServer.getPlayer(sender.getName()).connect(FakeProxyServer.ServerInfo.get(args[0]));
			}
		} else if (args[0].contains("!") && FakeProxyServer.PlayerServerInfo.keySet().contains(args[0].replace("!", ""))) {
			if (args.length > 1) {
				if (FakeProxyServer.getPlayer(sender.getName()) == null || FakeProxyServer.getPlayer(sender.getName()).hasPermission("bungeecord.command.send") || FakeProxyServer.getPlayer(sender.getName()).hasPermission("SubServers.Teleport.Others")) {
					if (FakeProxyServer.getPlayer(args[1]) == null) {
						if (FakeProxyServer.getPlayer(sender.getName()) == null) {
							FakeProxyServer.getLogger().info(FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport-Player-Error"));
						} else {
							FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.RED + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport-Player-Error"));
						}
					} else {
						if (FakeProxyServer.getPlayer(sender.getName()) == null) {
							FakeProxyServer.getLogger().info(FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
						} else {
							FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
						}
						FakeProxyServer.getPlayer(args[1]).sendMessage(ChatColor.AQUA + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
						FakeProxyServer.getPlayer(args[1]).connect(FakeProxyServer.PlayerServerInfo.get(args[0].replace("!", "")));
					}
				} else {
					FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.RED + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport-Permission-Error"));
				}
			} else {
				FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.AQUA + FakeProxyServer.lprefix + FakeProxyServer.lang.get("Lang.Commands.Teleport"));
				FakeProxyServer.getPlayer(sender.getName()).connect(FakeProxyServer.PlayerServerInfo.get(args[0].replace("!", "")));
			}
		} else {
			FakeProxyServer.getPlayer(sender.getName()).sendMessage(ChatColor.RED + FakeProxyServer.lang.get("Lang.Commands.Teleport-Config-Error"));
		}
	}
	
}
