package net.ME1312.SubServer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.ME1312.SubServer.Commands.FindCMD;
import net.ME1312.SubServer.Commands.ListCMD;
import net.ME1312.SubServer.Commands.NavCMD;
import net.ME1312.SubServer.Commands.SubDebugCMD;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main {
    public List<String> SubServers = new ArrayList<String>();
    public List<String> SharedChat =  new ArrayList<String>();
    public HashMap<String, ServerInfo> ServerInfo = new HashMap<String, ServerInfo>();
    public HashMap<String, ServerInfo> PlayerServerInfo = new HashMap<String, ServerInfo>();

    public String lprefix;
    public Configuration config;
    public HashMap<String, String> lang = new HashMap<String, String>();

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

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("./config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Iterator<String> str = config.getSection("servers").getKeys().iterator(); str.hasNext(); ) {
            String item = str.next();
            try {
                if (!item.equalsIgnoreCase("default") && config.getBoolean("servers." + item + ".use-shared-chat"))
                    SharedChat.add(item);
            } catch (NullPointerException e) {}
        }

        ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new SubDebugCMD(this, "subconf@proxy"));
        if (!config.getStringList("disabled_commands").contains("/server")) ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new NavCMD(this, "go"));
        if (!config.getStringList("disabled_commands").contains("/server")) ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new NavCMD(this, "server"));
        if (!config.getStringList("disabled_commands").contains("/glist")) ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new ListCMD(this, "glist"));
        if (!config.getStringList("disabled_commands").contains("/glist")) ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new ListCMD(this, "slist"));
        if (!config.getStringList("disabled_commands").contains("/find")) ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new FindCMD(this, "find"));
        if (!config.getStringList("disabled_commands").contains("/find")) ProxyServer.getInstance().getPluginManager().registerCommand(Plugin, new FindCMD(this, "sfind"));
        ProxyServer.getInstance().getPluginManager().registerListener(Plugin, new PlayerListener(this));
    }
}