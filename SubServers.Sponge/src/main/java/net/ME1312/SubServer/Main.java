package net.ME1312.SubServer;

import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;
import com.google.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import net.ME1312.SubServer.Executable.Executable;
import net.ME1312.SubServer.Executable.SubServer;
import net.ME1312.SubServer.Executable.SubServerCreator;
import net.ME1312.SubServer.GUI.GUI;
import net.ME1312.SubServer.GUI.GUIHandler;
import net.ME1312.SubServer.Libraries.Events.SubListener;
import net.ME1312.SubServer.Libraries.Metrics;
import net.ME1312.SubServer.Libraries.Version.Version;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.args.CommandElement;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

@Plugin(id="SubServers", name="SubServers", version="1.8.8f")
public class Main {
    public static SubServerCreator ServerCreator;

    public HashMap<Integer, SubServer> Servers = new HashMap();
    public HashMap<String, Integer> PIDs = new HashMap();
    public HashMap<PluginContainer, List<SubListener>> EventHandlers = new HashMap();
    public List<String> SubServers = new ArrayList<String>();
    @Inject
    public PluginContainer Plugin;

    @Inject
    public Logger log;
    public String lprefix;
    public HoconConfigurationLoader configManager;
    public CommentedConfigurationNode config;
    public CommentedConfigurationNode lang;
    public GUIHandler GUI;

    @Inject
    @ConfigDir(sharedRoot=false)
    public File dataFolder;
    public URL website;
    @Inject
    public Game game;
    public Version PluginVersion;
    public Version MCVersion;

    @Subscribe
    public void onInitialization(GameInitializationEvent event) {
        /* Pre Setup Actions */
        lprefix = "[" + Plugin.getName() + "] ";
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        
        /* API Register */
        new API(this);
        log.info("Enabling " + Plugin.getName() + " Version " + Plugin.getVersion() + "!");
        
        /* Version Grab */
        PluginVersion = new Version(Plugin.getVersion());
        MCVersion = new Version(event.getGame().getPlatform().getMinecraftVersion().getName());
        log.info("Loading Libraries for " + MCVersion);
        
        /* Config Update & Register */
        try {
            if (!new File(dataFolder, "config.conf").exists()) {
                copyFromJar("config.conf", new File(dataFolder, "config.conf").getPath());
                log.info("Created Config.conf!");
                
            } else if (!((CommentedConfigurationNode)((HoconConfigurationLoader.Builder)HoconConfigurationLoader.builder().setFile(new File(dataFolder, "config.conf"))).build().load()).getNode("Settings", "config-version").getString().equalsIgnoreCase("1.8.8e+")) {
                Files.move((File)new File(dataFolder, "config.conf"), (File)new File(dataFolder, "old-config." + Math.round(Math.random() * 100000.0) + ".conf"));
                copyFromJar("config.conf", new File(dataFolder, "config.conf").getPath());
                log.info("Updated Config.conf!");
            }
            if (!new File(dataFolder, "lang.conf").exists()) {
                copyFromJar("lang.conf", new File(dataFolder, "lang.conf").getPath());
                log.info("Created Lang.conf!");
                
            } else if (!((CommentedConfigurationNode)((HoconConfigurationLoader.Builder)HoconConfigurationLoader.builder().setFile(new File(dataFolder, "lang.conf"))).build().load()).getNode("config-version").getString().equalsIgnoreCase("1.8.8e+")) {
                Files.move((File)new File(dataFolder, "lang.conf"), (File)new File(dataFolder, "old-lang." + Math.round(Math.random() * 100000.0) + ".conf"));
                copyFromJar("lang.conf", new File(dataFolder, "lang.conf").getPath());
                log.info("Updated Lang.conf!");
            }

            if (new File(dataFolder, "cache").exists()) {
                deleteDir(new File(dataFolder, "cache"));
            }
            new File(dataFolder, "cache").mkdirs();

            configManager = ((HoconConfigurationLoader.Builder)HoconConfigurationLoader.builder().setFile(new File(dataFolder, "config.conf"))).build();
            config = (CommentedConfigurationNode)configManager.load();
            lang = (CommentedConfigurationNode)((HoconConfigurationLoader.Builder)HoconConfigurationLoader.builder().setFile(new File(dataFolder, "lang.conf"))).build().load();
        
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Register SubServers */
        PIDs.put("~Proxy", 0);
        Servers.put(0, new SubServer(config.getNode("Proxy", "enabled").getBoolean(), "~Proxy", 0, 25565, config.getNode("Proxy", "log").getBoolean(), new File(config.getNode("Proxy", "dir").getString()), new Executable(config.getNode("Proxy", "shell").getString()), 0, false, this));

        List SubServersStore = new ArrayList<Object>();
        SubServersStore.addAll(config.getNode("Servers").getChildrenMap().keySet());
        Collections.sort(SubServersStore);

        int i = 0;
        for(Iterator<Object> items = SubServersStore.iterator(); items.hasNext(); ) {
            String item = ((String) items.next());
            i++;
            SubServers.add(item);
            PIDs.put(item, i);
            Servers.put(i, new SubServer(config.getNode("Servers", item, "enabled").getBoolean(), item, i, config.getNode("Servers", item, "port").getInt(),
                    config.getNode("Servers", item, "log").getBoolean(), new File(config.getNode("Servers", item, "dir").getString()),
                    new Executable(config.getNode("Servers", item, "shell").getString()), config.getNode("Servers", item, "stop-after").getDouble(), false, this));
        }

        /* Register Default GUI */
        GUI = new GUI();

        /* Register Commands */
        CommandSpec SubCommand = CommandSpec.builder().description((Text)Texts.of((String)"All SubServers Commands")).executor((CommandExecutor)new SubServersCMD(this)).arguments(GenericArguments.optional((CommandElement)GenericArguments.remainingJoinedStrings((Text)Texts.of((String)"args")))).build();
        game.getCommandDispatcher().register((Object) Plugin, (CommandCallable) SubCommand, new String[]{"subserver", "sub"});

        /* Metrics */
        try {
            new Metrics(game, Plugin);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onLoadComplete(GameLoadCompleteEvent event) {
        /* Start SubServers */
        for(Iterator<String> items = SubServers.iterator(); items.hasNext(); ) {
            String item = items.next();
            if (config.getNode("Servers", item, "enabled").getBoolean() && config.getNode("Servers", item, "run-on-launch").getBoolean()) {
                API.getSubServer(item).start();
            }
        }

        if (config.getNode("Proxy", "enabled").getBoolean() && config.getNode("Proxy", "run-on-launch").getBoolean()) {
            API.getSubServer(0).start();
        }
    }

    @Subscribe
    public void onServerStop(GameStoppingServerEvent event) {
        if (new File(dataFolder, "cache").exists()) {
            log.info("Clearing Cache...");
            deleteDir(new File(dataFolder, "cache"));
        }
        log.info("Stopping SubServers...");
        /* Stop SubServers */
        try {
            if (ServerCreator != null && ServerCreator.isRunning()) {
                ServerCreator.waitFor();
                Thread.sleep(1000);
            }

            if (API.getSubServer(0).isRunning()) {
                API.getSubServer(0).stop();
                Servers.get(PIDs.get("~Proxy")).waitFor();
            }
            ArrayList<String> SubServersStore = new ArrayList<String>();
            SubServersStore.addAll(SubServers);
            for (Iterator<String> items = SubServersStore.iterator(); items.hasNext();) {
                String item = items.next();
                if (API.getSubServer(item).isRunning()) {
                    API.getSubServer(item).stop();
                    Servers.get(PIDs.get(item)).waitFor();
                    if (Servers.get(PIDs.get(item)).Temporary) {
                        Thread.sleep(500);
                    }
                    Thread.sleep(1000);
                }
            }
            log.info("Plugin Disabled.");
        }
        catch (InterruptedException e) {
            log.error("Problem Stopping Subservers.");
            log.error("Subservers will stay as Background Processes if not Stopped");
            e.printStackTrace();
            log.warn("Config Not Saved: Preserved config from Invalid Changes.");
            log.warn("Plugin Partially Disabled.");
        }
    }

    public void copyFromJar(String resource, String destination) {
        InputStream resStreamIn = Main.class.getClassLoader().getResourceAsStream(resource);
        File resDestFile = new File(destination);
        try {
            int readBytes;
            FileOutputStream resStreamOut = new FileOutputStream(resDestFile);
            byte[] buffer = new byte[4096];
            while ((readBytes = resStreamIn.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            resStreamOut.close();
            resStreamIn.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean deleteDir(File dir)
    {
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                {
                    return false;
                }
            }
        }
        // The directory is now empty or this is a file so delete it
        return dir.delete();
    }
}