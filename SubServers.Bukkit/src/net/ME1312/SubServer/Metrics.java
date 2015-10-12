package net.ME1312.SubServer;

import net.ME1312.SubServer.Libraries.Version.Version;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

public class Metrics {
    private String BaseURL;
    private JavaPlugin Plugin;
    private static Main Main;

    private Version PluginVersion;
    private Version JavaVersion;
    private String ServerSoftware;
    private UUID ServerUUID;
    private Version MinecraftVersion;
    private String OperatingSystem;
    private Version OperatingSystemVersion;

    protected Metrics(Main Main) {
        this.Main = Main;
    }

    public Metrics(int ID, JavaPlugin Plugin) {
        this.BaseURL = "http://src.ME1312.net/Stats/?submit&type=1&id=" + ID + "&rev=1";
        this.Plugin = Plugin;

        this.PluginVersion = new Version(Plugin.getDescription().getVersion());
        this.JavaVersion = new Version(System.getProperty("java.version"));
        try {
            if (Class.forName("org.spigotmc.SpigotConfig") != null) {
                this.ServerSoftware = "Spigot";
            } else {
                this.ServerSoftware = Bukkit.getName();
            }
        } catch (ClassNotFoundException e) {
            this.ServerSoftware = Bukkit.getName();
        }
        this.ServerUUID = UUID.fromString(Main.config.getRawString("Settings.Server-UUID"));
        this.MinecraftVersion = API.getMinecraftVersion();
        this.OperatingSystem = System.getProperty("os.name");
        this.OperatingSystemVersion = new Version(System.getProperty("os.version"));

        try {
            int response = submitData();
            if (response != 200) {
                Bukkit.getLogger().severe("Problem sending info to ME1312.net: HTTP Error " + response);
                Bukkit.getLogger().severe("See http://src.ME1312.net/Error/?error=" + response + " For more information about this Error.");
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not Connect to ME1312.net! is it down?");
        }

        new BukkitRunnable() {
            public void run() {
                try {
                    int response = submitData();
                    if (response != 200) {
                        Bukkit.getLogger().severe("Problem sending info to ME1312.net: HTTP Error " + response);
                        Bukkit.getLogger().severe("See http://src.ME1312.net/Error/?error=" + response + " For more information about this Error.");
                    }
                } catch (IOException e) {
                    Bukkit.getLogger().severe("Could not Connect to ME1312.net! is it down?");
                }
            }
        }.runTaskTimerAsynchronously(Plugin, 0L, (long)((30 * 20) * 60));
    }

    private int submitData() throws IOException {
        if (Main.config.getBoolean("Settings.Metrics-Enabled")) {
            URL FullURL = new URL(BaseURL + URLEncoder.encode("&pluginversion=" + PluginVersion.toString() + "&javaversion=" + JavaVersion.toString() +
                    "&serversoftware=" + ServerSoftware + "&serverversion=" + MinecraftVersion.toString() + "&serveruuid=" + ServerUUID.toString() + "&os=" + OperatingSystem +
                    "&osversion=" + OperatingSystemVersion.toString(), "UTF-8").replace("%26", "&").replace("%3D", "="));
            HttpURLConnection connection = (HttpURLConnection) FullURL.openConnection();
            connection.connect();

            return connection.getResponseCode();
        } else {
            return 200;
        }
    }
}