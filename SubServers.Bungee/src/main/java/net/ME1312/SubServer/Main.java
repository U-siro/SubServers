package net.ME1312.SubServer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

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
		lprefix = "[" + Plugin.getDescription().getName() + "] ";
		
		ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new SubDebugCommand(this));
		ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new NavCommand(this));
		ProxyServer.getInstance().getPluginManager().registerListener(Plugin, new PlayerListener(this));

		ProxyServer.getInstance().getLogger().info("Waiting for config...");
	}
}
