package net.ME1312.SubServer.Executable;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.ME1312.SubServer.API;
import net.ME1312.SubServer.Main;
import net.ME1312.SubServer.Libraries.Events.SubEvent;
import net.ME1312.SubServer.Libraries.Version.Version;

public class SubServerCreator {
    public enum ServerTypes {
        spigot,
        bukkit,
        vanilla,
    }

    private String Name;
    private int Port;
    private File Dir;
    private Executable Exec;
    private int Memory;
    private String Jar;
    private Version Version;
    private Player Player;
    private ServerTypes Type;
    private Main Main;
    private boolean Running;
    private Process Process;

    public SubServerCreator(String Name, int Port, File Dir, ServerTypes Type, Version Version, int Memory, Player Player, Main Main) {
        this.Name = Name;
        this.Port = Port;
        this.Dir = Dir;
        this.Memory = Memory;
        this.Version = Version;
        this.Player = Player;
        this.Type = Type;
        this.Main = Main;

        if (!Dir.exists()) Dir.mkdirs();

        if (Type == ServerTypes.spigot) {
            this.Jar = "Spigot.jar";
            this.Exec = new Executable("java -Xmx" + Memory + "M -Djline.terminal=jline.UnsupportedTerminal -Dcom.mojang.eula.agree=true -jar " + Jar);

            try {

                GenerateProperties();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else if (Type == ServerTypes.bukkit) {
            this.Jar = "Craftbukkit.jar";
            this.Exec = new Executable("java -Xmx" + Memory + "M -Djline.terminal=jline.UnsupportedTerminal -jar " + Jar + " -o false");

            try {
                GenerateEULA();
                GenerateProperties();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else if (Type == ServerTypes.vanilla) {
            this.Jar = "Vanilla.jar";
            this.Exec = new Executable("java -Xmx" + Memory + "M -jar " + Jar + " nogui");

            try {
                GenerateEULA();
                GenerateProperties();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void GenerateEULA() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(new File(Dir, "eula.txt"), "UTF-8");

        writer.println("#By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
        writer.println("#" + new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy").format(Calendar.getInstance().getTime()));
        writer.println("eula=true");
        writer.close();
    }

    private void GenerateProperties() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(new File(Dir, "server.properties"), "UTF-8");

        writer.println("#Minecraft server properties");
        writer.println("#" + new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy").format(Calendar.getInstance().getTime()));
        writer.println("generator-settings=");
        writer.println("op-permission-level=4");
        writer.println("allow-nether=true");
        writer.println("resource-pack-hash=");
        writer.println("level-name=world");
        writer.println("enable-query=false");
        writer.println("allow-flight=false");
        writer.println("announce-player-achievements=false");
        writer.println("server-port=" + Port);
        writer.println("max-world-size=29999984");
        writer.println("level-type=DEFAULT");
        writer.println("enable-rcon=false");
        writer.println("level-seed=");
        writer.println("force-gamemode=false");
        writer.println("server-ip=" + Main.config.getString("Settings.Server-IP"));
        writer.println("network-compression-threshold=256");
        writer.println("max-build-height=256");
        writer.println("spawn-npcs=true");
        writer.println("white-list=false");
        writer.println("spawn-animals=true");
        writer.println("snooper-enabled=true");
        writer.println("online-mode=false");
        writer.println("resource-pack=");
        writer.println("pvp=true");
        writer.println("difficulty=1");
        writer.println("enable-command-block=true");
        writer.println("gamemode=0");
        writer.println("player-idle-timeout=0");
        writer.println("max-players=20");
        writer.println("max-tick-time=60000");
        writer.println("spawn-monsters=true");
        writer.println("generate-structures=true");
        writer.println("view-distance=10");
        writer.println("motd=A Generated SubServer");
        writer.close();
    }

    public boolean run() {
        try {
            if (SubEvent.RunEvent(Main, SubEvent.Events.SubCreateEvent, new SubServer(true, Name, -1, Port, true, true, Dir, Exec, 0, false, Main), Player, Type)) {
                run(true);
                return true;
            } else {
                return false;
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void run(boolean value) {
        if (value) {
            Running = true;
            new BukkitRunnable() {

                @Override
                public void run() {
                    Player.sendMessage(ChatColor.GOLD + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Create-Loading"));
                    try {
                        if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
                            String GitBash = new File(new File(Main.config.getRawString("Settings.Server-Creation.git-dir")), "bin" + File.separatorChar + "bash.exe").getAbsolutePath();
                            Process Process1 = Runtime.getRuntime().exec(GitBash + " --login -i -c \"curl -o build-subserver.sh http://minecraft.ME1312.net/lib/subservers/1.8.8e/build-subserver.sh\"", null, Dir);
                            try {
                                Process1.waitFor();
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (!(new File(Dir, "build-subserver.sh").exists())) {
                                Bukkit.getLogger().severe(Main.lprefix + "Problem Downloading Server Build Script from ME1312.net. Is it Down?");
                            } else {
                                Process = Runtime.getRuntime().exec(GitBash + " --login -i -c \"bash build-subserver.sh " + Version.toString() + " " + Type.toString() + "\"", null, Dir);
                                StreamGobbler read = new StreamGobbler(Process.getInputStream(), "OUTPUT", Main.config.getBoolean("Settings.Server-Creation.log"), Main.lang.getString("Lang.Create-Server.Log-Prefix") + Name, Main);
                                read.start();
                                try {
                                    Process.waitFor();
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (Process.exitValue() == 0) {
                                    Player.sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Create-Done"));
                                    final int PID = (Main.SubServers.size() + 1);
                                    Main.Servers.put(PID, new SubServer(true, Name, PID, Port, true, true, Dir, Exec, 0, false, Main));
                                    Main.PIDs.put(Name, PID);
                                    Main.SubServers.add(Name);

                                    Main.Servers.get(PID).start();
                                    if (API.getSubServer(0).isRunning()) API.getSubServer(0).sendCommandSilently("subconf@proxy addserver " + Name + " " + Main.config.getString("Settings.Server-IP") + " " + Port + " true");

                                    Main.config.set("Servers." + Name + ".enabled", true);
                                    Main.config.set("Servers." + Name + ".port", Port);
                                    Main.config.set("Servers." + Name + ".run-on-launch", false);
                                    Main.config.set("Servers." + Name + ".log", true);
                                    Main.config.set("Servers." + Name + ".use-shared-chat", true);
                                    Main.config.set("Servers." + Name + ".dir", Dir.getPath());
                                    Main.config.set("Servers." + Name + ".exec", Exec.toString());
                                    Main.config.set("Servers." + Name + ".stop-after", 0);
                                    Main.config.saveConfig();
                                } else {
                                    Bukkit.getLogger().severe(Main.lprefix + "build-subserver.sh exited with an errors. Please try again.");
                                }
                            }
                        } else {
                            Process Process1 = Runtime.getRuntime().exec("curl -o build-subserver.sh http://minecraft.ME1312.net/lib/subservers/1.8.8e/build-subserver.sh", null, Dir);
                            try {
                                Process1.waitFor();
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (!(new File(Dir, "build-subserver.sh").exists())) {
                                Bukkit.getLogger().severe(Main.lprefix + "Problem Downloading Server Build Script from ME1312.net. Is it Down?");
                            } else {
                                Process Process2 = Runtime.getRuntime().exec("chmod +x build-subserver.sh", null, Dir);
                                try {
                                    Process2.waitFor();
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (Process2.exitValue() != 0) {
                                    Bukkit.getLogger().warning(Main.lprefix + "Problem Setting Executable Permissions for build-subserver.sh");
                                    Bukkit.getLogger().warning("This may cause errors in the Build Process");
                                }

                                Process = Runtime.getRuntime().exec("bash build-subserver.sh " + Version.toString() + " " + Type.toString(), null, Dir);
                                StreamGobbler read = new StreamGobbler(Process.getInputStream(), "OUTPUT", Main.config.getBoolean("Settings.Server-Creation.log"), Main.lang.getString("Lang.Create-Server.Log-Prefix") + Name, Main);
                                read.start();
                                try {
                                    Process.waitFor();
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (Process.exitValue() == 0) {
                                    Player.sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Create-Done").replace("$Server$", Name));
                                    final int PID = (Main.SubServers.size() + 1);
                                    Main.Servers.put(PID, new SubServer(true, Name, PID, Port, true, true, Dir, Exec, 0, false, Main));
                                    Main.PIDs.put(Name, PID);
                                    Main.SubServers.add(Name);

                                    Main.Servers.get(PID).start();
                                    if (API.getSubServer(0).isRunning()) API.getSubServer(0).sendCommandSilently("subconf@proxy addserver " + Name + " " + Main.config.getString("Settings.Server-IP") + " " + Port + " true");

                                    Main.config.set("Servers." + Name + ".enabled", true);
                                    Main.config.set("Servers." + Name + ".port", Port);
                                    Main.config.set("Servers." + Name + ".run-on-launch", false);
                                    Main.config.set("Servers." + Name + ".log", true);
                                    Main.config.set("Servers." + Name + ".use-shared-chat", true);
                                    Main.config.set("Servers." + Name + ".dir", Dir.getPath());
                                    Main.config.set("Servers." + Name + ".exec", Exec.toString());
                                    Main.config.set("Servers." + Name + ".stop-after", 0);
                                    Main.config.saveConfig();
                                } else {
                                    Bukkit.getLogger().severe(Main.lprefix + "build-subserver.sh exited with an errors. Please try again.");
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Running = false;
                }
            }.runTaskAsynchronously(Main.Plugin);
        }
    }

    public void waitFor() throws InterruptedException {
        Process.waitFor();
    }

    public boolean isRunning() {
        return Running;
    }
}