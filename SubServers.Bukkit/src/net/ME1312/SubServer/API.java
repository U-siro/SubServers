package net.ME1312.SubServer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.ME1312.SubServer.Libraries.Config.ConfigFile;
import net.ME1312.SubServer.Libraries.Events.SubEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.ME1312.SubServer.Executable.Executable;
import net.ME1312.SubServer.Executable.SubServer;
import net.ME1312.SubServer.Libraries.Events.SubListener;
import net.ME1312.SubServer.Libraries.Version.Version;

/**
 * This is the API File for Subservers<br>
 * <br>
 * NOTES:<br>
 *   Methods ending in "All" don't effect the Proxy<br>
 *   Methods can be Requested<br>
 *
 * @author ME1312
 * @version 1.8.8e+
 *
 */
@SuppressWarnings("static-access")
public class API {
    private static Main Main;

    protected API(Main Main) {
        this.Main = Main;
    }

    /**
     * Execute Command on All Remote Servers
     *
     * @param Command The Command to Execute
     */
    public static void sendCommandToAll(String Command) {
        for(Iterator<String> str = Main.SubServers.iterator(); str.hasNext(); ) {
            String item = str.next();
            if (!item.equalsIgnoreCase("~Proxy") && Main.Servers.keySet().contains(Main.PIDs.get(item)) && getSubServer(item).isRunning()) {
                getSubServer(item).sendCommand(Command);
            }
        }
    }

    /**
     * Execute Command on All Remote Servers
     *
     * @param Command The Command to Execute
     * @param Sender The player who sent this Command
     */
    public static void sendCommandToAll(Player Sender, String Command) {
        for(Iterator<String> str = Main.SubServers.iterator(); str.hasNext(); ) {
            String item = str.next();
            if (!item.equalsIgnoreCase("~Proxy") && Main.Servers.keySet().contains(Main.PIDs.get(item)) && getSubServer(item).isRunning()) {
                getSubServer(item).sendCommand(Sender, Command);
            }
        }
    }

    /**
     * Stop All Remote Servers
     */
    public static void stopAll() {
        for(Iterator<String> str = Main.SubServers.iterator(); str.hasNext(); ) {
            String item = str.next();
            if (!item.equalsIgnoreCase("~Proxy") && Main.Servers.keySet().contains(Main.PIDs.get(item)) && getSubServer(item).isRunning()) {
                getSubServer(item).stop();
            }
        }
    }

    /**
     * Stop All Remote Servers
     *
     * @param Sender The player who sent this Command
     */
    public static void stopAll(Player Sender) {
        for(Iterator<String> str = Main.SubServers.iterator(); str.hasNext(); ) {
            String item = str.next();
            if (!item.equalsIgnoreCase("~Proxy") && Main.Servers.keySet().contains(Main.PIDs.get(item)) && getSubServer(item).isRunning()) {
                getSubServer(item).stop(Sender);
            }
        }
    }

    /**
     * Get SubServers from the Configuration
     *
     * @return List<SubServer> Of all Servers Defined in the Configuration
     */
    public static List<SubServer> getSubServers() {
        List<SubServer> Server = new ArrayList<SubServer>();
        Server.addAll(Main.Servers.values());
        return Server;
    }

    public static SubServer getSubServer(int PID) {
        return Main.Servers.get(PID);

    }

    public static SubServer getSubServer(String Name) {
        return Main.Servers.get(Main.PIDs.get(Name));

    }

    /**
     * Creates a SubServer
     *
     * @param Name Name of SubServer
     * @param Port Port of SubServer
     * @param Log Toggle Output to console
     * @param SharedChat Toggle Shared Chat
     * @param Dir Shell Directory
     * @param Exec Executable String or File
     * @param StopAfter Stop after x minutes
     * @param Temporary Toggles Temporary Server actions
     */
    public static void addServer(final String Name, int Port, boolean Log, boolean SharedChat, File Dir, Executable Exec, double StopAfter, boolean Temporary) {
        final int PID = (Main.SubServers.size() + 1);
        Main.Servers.put(PID, new SubServer(true, Name, PID, Port, Log, SharedChat, Dir, Exec, StopAfter, Temporary, Main));
        Main.PIDs.put(Name, PID);
        Main.SubServers.add(Name);
        Bukkit.getLogger().info("Servers: " + Main.Servers.toString());
        Bukkit.getLogger().info("PIDs: " + Main.PIDs.toString());
        Bukkit.getLogger().info("SubServers: " + Main.SubServers.toString());

        if (getSubServer(0).isRunning()) getSubServer(0).sendCommandSilently("subconf@proxy addserver " + Name + " " + Main.config.getString("Settings.Server-IP") + " " + Port + " " + SharedChat);

        if (Temporary) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Main.Servers.get(PID).start();
                    try {
                        Thread.sleep(1500);
                        Main.Servers.get(Main.PIDs.get(Name)).waitFor();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getSubServer(0).isRunning()) getSubServer(0).sendCommandSilently("subconf@proxy removeserver " + Name);
                    Main.Servers.remove(PID);
                    Main.PIDs.remove(Name);
                    Main.SubServers.remove(Name);
                }
            }.runTaskAsynchronously(Main.Plugin);
        }
    }

    /**
     * Creates a SubServer
     *
     * @param Sender The player who sent this Command
     * @param Name Name of SubServer
     * @param Port Port of SubServer
     * @param Log Toggle Output to console
     * @param SharedChat Toggle Shared Chat
     * @param Dir Shell Directory
     * @param Exec Executable String or File
     * @param StopAfter Stop after x minutes
     * @param Temporary Toggles Temporary Server actions
     */
    public static void addServer(Player Sender, final String Name, int Port, boolean Log, boolean SharedChat, File Dir, Executable Exec, double StopAfter, boolean Temporary) {
        final int PID = (Main.SubServers.size() + 1);
        Main.Servers.put(PID, new SubServer(true, Name, PID, Port, Log, SharedChat, Dir, Exec, StopAfter, Temporary, Main));
        Main.PIDs.put(Name, PID);
        Main.SubServers.add(Name);

        getSubServer(0).sendCommandSilently("subconf@proxy addserver " + Name + " " + Main.config.getString("Settings.Server-IP") + " " + Port + " " + SharedChat);

        if (Temporary) {
            Main.Servers.get(PID).start(Sender);
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        Main.Servers.get(Main.PIDs.get(Name)).waitFor();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getSubServer(0).isRunning()) getSubServer(0).sendCommandSilently("subconf@proxy removeserver " + Name);
                    Main.Servers.remove(PID);
                    Main.PIDs.remove(Name);
                    Main.SubServers.remove(Name);
                }
            }.runTaskAsynchronously(Main.Plugin);
        }
    }

    /**
     * Adds your Listener to Bukkit and/or Subservers
     *
     * @param Listener the Object that implements SubListener
     * @param Plugin The Plugin calling this SubListener
     * @param RegisterBukkit Register listener with Bukkit? (true/false)
     */
    public static void registerListener(SubListener Listener, JavaPlugin Plugin, boolean RegisterBukkit) {
        if (RegisterBukkit) Bukkit.getServer().getPluginManager().registerEvents(Listener, Plugin);

        List<SubListener> listeners = new ArrayList<SubListener>();
        if (Main.EventHandlers.keySet().contains(Plugin)) listeners.addAll(Main.EventHandlers.get(Plugin));
        listeners.add(Listener);
        Main.EventHandlers.put(Plugin, listeners);

    }

    /**
     * Adds your Listener to Bukkit and Subservers
     *
     * @param Listener the Object that implements SubListener
     * @param Plugin The Plugin calling this SubListener
     */
    public static void registerListener(SubListener Listener, JavaPlugin Plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(Listener, Plugin);

        List<SubListener> listeners = new ArrayList<SubListener>();
        if (Main.EventHandlers.keySet().contains(Plugin)) listeners.addAll(Main.EventHandlers.get(Plugin));
        listeners.add(Listener);
        Main.EventHandlers.put(Plugin, listeners);

    }
    /**
     * Gets the Lang File
     *
     * @return The lang file's nodes
     */
    public static ConfigFile getLang() { return Main.lang; }


    /**
     *
     * @param Event The Event to Execute
     * @param Args The Args required to execute this event
     * @return If the event was cancelled
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static boolean executeEvent(SubEvent.Events Event, Object... Args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return SubEvent.RunEvent(Main, Event, Args);
    }

    /**
     * Gets the SubServers Version
     *
     * @return The SubServers Version
     */
    public static Version getPluginVersion() { return Main.PluginVersion; }
}
