package net.ME1312.SubServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

public class Main {
	public String lprefix;
	public List<String> SubServers = new ArrayList<String>();
	public HashMap<String, String> lang = new HashMap<String, String>();
	public HashMap<String, ServerInfo> ServerInfo = new HashMap<String, ServerInfo>();
	public HashMap<String, ServerInfo> PlayerServerInfo = new HashMap<String, ServerInfo>();
	public Plugin Plugin;
	
	protected Main(Plugin plugin) throws IllegalArgumentException {
		if (plugin != null && plugin.getDescription().getName().equalsIgnoreCase("SubServers")) {
			Plugin = plugin;
		} else {
			throw new IllegalArgumentException("Main Should only be called by SubServers Plugin.");
		}
	}
	
	protected void EnablePlugin() {
		lprefix = Plugin.getDescription().getName() + " \u00BB ";
		
		ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new SubDebugCommand(this, "subconf@proxy"));
		ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new NavCommand(this, "go"));
		ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new NavCommand(this, "server"));

		ProxyServer.getInstance().getPluginManager().registerListener(Plugin, new PlayerListener(this));
	}
}
