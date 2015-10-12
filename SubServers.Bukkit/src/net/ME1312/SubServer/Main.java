package net.ME1312.SubServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.Files;

import net.ME1312.SubServer.Executable.SubServerCreator;
import net.ME1312.SubServer.Executable.Executable;
import net.ME1312.SubServer.Executable.SubServer;
import net.ME1312.SubServer.Libraries.Config.ConfigFile;
import net.ME1312.SubServer.Libraries.Config.ConfigManager;
import net.ME1312.SubServer.Libraries.Events.SubListener;
import net.ME1312.SubServer.Libraries.Version.Version;

public class Main {
    public HashMap<Integer, SubServer> Servers = new HashMap<Integer, SubServer>();
    public HashMap<String, Integer> PIDs = new HashMap<String, Integer>();
    public HashMap<JavaPlugin, List<SubListener>> EventHandlers = new HashMap<JavaPlugin, List<SubListener>>();
    public List<String> SubServers = new ArrayList<String>();
    public JavaPlugin Plugin;
    public SubServerCreator ServerCreator;

    public String lprefix;
    public ConfigFile config;
    public ConfigFile lang;

    public Version PluginVersion;
    public Version MCVersion;

    private static ConfigManager confmanager;

    protected Main(JavaPlugin plugin) throws IllegalArgumentException {
        if (plugin != null && plugin.getDescription().getName().equalsIgnoreCase("SubServers")) {
            Plugin = plugin;
        } else {
            throw new IllegalArgumentException("Main Should only be called by SubServers Plugin.");
        }
    }

    protected void EnablePlugin() {
        confmanager = new ConfigManager(Plugin);
        PluginManager pm = Bukkit.getServer().getPluginManager();
        lprefix = Plugin.getDescription().getName() + " \u00BB ";
        if (!(new File(Plugin.getDataFolder().toString())).exists()) {
            new File(Plugin.getDataFolder().toString()).mkdirs();
        }

        new API(this);

        PluginVersion = new Version(Plugin.getDescription().getVersion());
        MCVersion = new Version(Bukkit.getServer().getVersion().split("\\(MC\\: ")[1].split("\\)")[0]);

        Bukkit.getLogger().info(lprefix + "Loading Libraries for " + MCVersion);

        /**
         * Updates Configs if needed
         */
        if (!(new File(Plugin.getDataFolder() + File.separator + "config.yml").exists())) {
            copyFromJar("config.yml", Plugin.getDataFolder() + File.separator + "config.yml");
            Bukkit.getLogger().info(lprefix + "Created Config.yml!");
        } else if (!confmanager.getNewConfig("config.yml").getString("Settings.config-version").equalsIgnoreCase("1.8.8k+")) {
            try {
                Files.move(new File(Plugin.getDataFolder() + File.separator + "config.yml"), new File(Plugin.getDataFolder() + File.separator + "old-config." + Math.round(Math.random() * 100000) + ".yml"));
                copyFromJar("config.yml", Plugin.getDataFolder() + File.separator + "config.yml");
                Bukkit.getLogger().info(lprefix + "Updated Config.yml!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!(new File(Plugin.getDataFolder() + File.separator + "lang.yml").exists())) {
            copyFromJar("lang.yml", Plugin.getDataFolder() + File.separator + "lang.yml");
            Bukkit.getLogger().info(lprefix + "Created Lang.yml!");
        } else if (!confmanager.getNewConfig("lang.yml").getString("config-version").equalsIgnoreCase("1.8.8j+")) {
            try {
                Files.move(new File(Plugin.getDataFolder() + File.separator + "lang.yml"), new File(Plugin.getDataFolder() + File.separator + "old-lang." + Math.round(Math.random() * 100000) + ".yml"));
                copyFromJar("lang.yml", Plugin.getDataFolder() + File.separator + "lang.yml");
                Bukkit.getLogger().info(lprefix + "Updated Lang.yml!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = confmanager.getNewConfig("config.yml");
        lang = confmanager.getNewConfig("lang.yml");
        SubServers.addAll(config.getConfigurationSection("Servers").getKeys(false));

        /**
         * Registers Listeners
         */
        pm.registerEvents(new net.ME1312.SubServer.GUI.GUIListener(this), Plugin);

        /**
         * Auto-Starts Servers,
         * Registers PIDs & Shells
         */
        PIDs.put("~Proxy", 0);
        Servers.put(0, new SubServer(config.getBoolean("Proxy.enabled"), "~Proxy", 0, 25565, config.getBoolean("Proxy.log"), false, new File(config.getRawString("Proxy.dir")),
                new Executable(config.getRawString("Proxy.exec")), 0, false, this));

        int i = 0;
        for(Iterator<String> str = SubServers.iterator(); str.hasNext(); ) {
            String item = str.next();
            i++;
            PIDs.put(item, i);
            Servers.put(i, new SubServer(config.getBoolean("Servers." + item + ".enabled"), item, i, config.getInt("Servers." + item + ".port"), config.getBoolean("Servers." + item + ".log"),
                    config.getBoolean("Servers." + item + ".use-shared-chat"), new File(config.getRawString("Servers." + item + ".dir")), new Executable(config.getRawString("Servers." + item + ".exec")), config.getDouble("Servers." + item + ".stop-after"), false, this));
            if (config.getBoolean("Servers." + item + ".enabled") && config.getBoolean("Servers." + item + ".run-on-launch")) {
                Servers.get(i).start();
            }
        }

        if ((config.getBoolean("Proxy.enabled") == true) && (config.getBoolean("Proxy.run-on-launch") == true)) {
            Servers.get(0).start();
        }

        /**
         * Registers Commands
         */
        Plugin.getCommand("subserver").setExecutor(new SubServersCMD(this));
        Plugin.getCommand("sub").setExecutor(new SubServersCMD(this));

        /**
         * ME1312.net Stats
         */
        new Metrics(1, Plugin);

    }

    protected void DisablePlugin() {
        Bukkit.getLogger().info(lprefix + "Stopping SubServers...");
        try {
            if (ServerCreator != null && ServerCreator.isRunning()) {
                ServerCreator.waitFor();
                Thread.sleep(1000);
            }

            if (API.getSubServer(0).isRunning()) {
                API.getSubServer(0).stop();
                Servers.get(PIDs.get("~Proxy")).waitFor();
            }

            List<String> SubServersStore = new ArrayList<String>();
            SubServersStore.addAll(SubServers);

            for(Iterator<String> str = SubServersStore.iterator(); str.hasNext(); ) {
                String item = str.next();
                if (API.getSubServer(item).isRunning()) {
                    API.getSubServer(item).stop();
                    Servers.get(PIDs.get(item)).waitFor();
                    if (Servers.get(PIDs.get(item)).Temporary) {
                        Thread.sleep(500);
                    }
                    Thread.sleep(1000);
                }
            }
            Bukkit.getLogger().info(lprefix + " Plugin Disabled.");
        } catch (InterruptedException e) {
            Bukkit.getLogger().severe(lprefix + "Problem Stopping Subservers.");
            Bukkit.getLogger().severe(lprefix + "Subservers will stay as Background Processes if not Stopped");
            e.printStackTrace();
            Bukkit.getLogger().warning(lprefix + "Config Not Saved: Preserved config from Invalid Changes.");
            Bukkit.getLogger().warning(lprefix + " Plugin Partially Disabled.");
        }
    }

    public void copyFromJar(String resource, String destination) {
        InputStream resStreamIn = Main.class.getClassLoader().getResourceAsStream(resource);
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
