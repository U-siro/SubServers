package net.ME1312.SubServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/*
    The Class which pretends to be Bungee's Main Class!
 */
public class FakeProxyServer extends BungeeCord {
    public List<String> SubServers = new ArrayList<String>();
    public HashMap<String, SubServerInfo> ConfigServers = new HashMap<String, SubServerInfo>();
    public HashMap<String, SubServerInfo> ServerInfo = new HashMap<String, SubServerInfo>();
    public HashMap<String, SubServerInfo> PlayerServerInfo = new HashMap<String, SubServerInfo>();
    
    public String lprefix;
    public Configuration configuration;
    public HashMap<String, String> lang = new HashMap<String, String>();

    public PluginDescription Plugin;

    protected FakeProxyServer() throws Exception {
        Plugin = new PluginDescription();
        Plugin.setName("SubServers");
        Plugin.setAuthor("ME1312");
        Plugin.setVersion("1.8.8m");

        lprefix = Plugin.getName() + " \u00BB ";

        System.out.println("Enabling " + Plugin.getName() + " v" + Plugin.getVersion() + " by " + Plugin.getAuthor());

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("./config.yml"));
        } catch (IOException e) {
            copyFromJar("config.yml", "./config.yml");
            try {
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("./config.yml"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        for(Iterator<String> str = configuration.getSection("servers").getKeys().iterator(); str.hasNext(); ) {
            String item = str.next();
            ConfigServers.put(item, new SubServerInfo((BungeeServerInfo)constructServerInfo(item, new InetSocketAddress(configuration.getString("servers." + item + ".address").split(":")[0],
                            Integer.parseInt(configuration.getString("servers." + item + ".address").split(":")[1])), configuration.getString("servers." + item + ".motd"),
                    configuration.getBoolean("servers." + item + ".restricted")), configuration.getBoolean("servers." + item + ".use-shared-chat")));
        }

        getPluginManager().registerCommand(null, new SubDebugCMD(this, "subconf@proxy"));
        getPluginManager().registerListener(null, new PlayerListener(this));
    }

    @Override
    public String getName() {
        return Plugin.getName() + " Proxy";
    }

    @Override
    public String getVersion() {
        return Plugin.getVersion();
    }

    @Override
    public Map<String, ServerInfo> getServers() {
        HashMap<String, ServerInfo> map = new HashMap<String, ServerInfo>();
        map.putAll(ConfigServers);
        map.remove("~Lobby");
        map.putAll(ServerInfo);
        map.putAll(PlayerServerInfo);
        return map;
    }

    @Override
    public ServerInfo getServerInfo(String server) {
        return getServers().get(server);
    }

    private void copyFromJar(String resource, String destination) {
        InputStream resStreamIn = FakeProxyServer.class.getClassLoader().getResourceAsStream(resource);
        File resDestFile = new File(destination);
        try {
            OutputStream resStreamOut = new FileOutputStream(resDestFile);
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = resStreamIn.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            resStreamOut.close();
            resStreamIn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}