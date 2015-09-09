package net.ME1312.SubServer.Executable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import net.ME1312.SubServer.Libraries.ChatColor;
import org.spongepowered.api.entity.living.player.Player;

import net.ME1312.SubServer.API;
import net.ME1312.SubServer.Main;
import net.ME1312.SubServer.Libraries.Events.SubEvent;
import net.ME1312.SubServer.Libraries.Version.Version;
import org.spongepowered.api.text.Texts;

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
    private Version Version;
    private Player Player;
    private ServerTypes Type;
    private Main Main;
    private boolean Running;
    private Process Process;

    public SubServerCreator(String Name, int Port, File Dir, Executable Exec, Player Player, ServerTypes Type, Version Version, Main Main) {
        this.Name = Name;
        this.Port = Port;
        this.Dir = Dir;
        this.Exec = Exec;
        this.Version = Version;
        this.Player = Player;
        this.Type = Type;
        this.Main = Main;
    }

    public boolean run() {
        try {
            if (SubEvent.RunEvent(Main, SubEvent.Events.SubCreateEvent, this, Player, Type)) {
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
            Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {

                @Override
                public void run() {
                    Player.sendMessage(Texts.of(ChatColor.GOLD + Main.lprefix + Main.lang.getNode("Lang", "Create-Server", "Server-Create-Loading").getString()));
                    try {
                        if (!Dir.exists()) {
                            Dir.mkdirs();
                        }

                        if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
                            String GitBash = new File(new File(Main.config.getNode("Settings", "Server-Creation", "git-dir").getString()), "bin" + File.separatorChar + "bash.exe").getAbsolutePath();
                            Process Process1 = Runtime.getRuntime().exec(GitBash + " --login -i -c \"curl -o build-subserver.sh http://minecraft.ME1312.net/lib/subservers/1.8.8e/build-subserver.sh\"", null, Dir);
                            try {
                                Process1.waitFor();
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (!(new File(Dir, "build-subserver.sh").exists())) {
                                Main.log.info("Problem Downloading Server Build Script from ME1312.net. Is it Down?");
                            } else {
                                Process = Runtime.getRuntime().exec(GitBash + " --login -i -c \"bash build-subserver.sh " + Version.toString() + " " + Type.toString() + "\"", null, Dir);
                                StreamGobbler read = new StreamGobbler(Process.getInputStream(), "OUTPUT", Main.config.getNode("Settings", "Server-Creation", "log").getBoolean(), Main.lang.getNode("Lang", "Create-Server", "Log-Prefix").getString() + Name, Main);
                                read.start();
                                try {
                                    Process.waitFor();
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (Process.exitValue() == 0) {
                                    Player.sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + Main.lang.getNode("Lang", "Create-Server", "Server-Create-Done").getString()));
                                    final int PID = (Main.SubServers.size() + 1);
                                    Main.Servers.put(PID, new SubServer(true, Name, PID, Port, true, Dir, Exec, PID, false, Main));
                                    Main.PIDs.put(Name, PID);
                                    Main.SubServers.add(Name);

                                    Main.Servers.get(PID).start();
                                    if (API.getSubServer(0).isRunning()) API.getSubServer(0).sendCommandSilently("subconf@proxy addserver " + Name + " " + Main.config.getNode("Settings", "Server-IP").getString() + " " + Port);

                                    Main.config.getNode("Servers", Name, "enabled").setValue(true);
                                    Main.config.getNode("Servers", Name, "port").setValue(Port);
                                    Main.config.getNode("Servers", Name, "run-on-launch").setValue(false);
                                    Main.config.getNode("Servers", Name, "log").setValue(true);
                                    Main.config.getNode("Servers", Name, "dir").setValue(Dir.getPath());
                                    Main.config.getNode("Servers", Name, "shell").setValue(Exec.toString());
                                    Main.config.getNode("Servers", Name, "stop-after").setValue(0);
                                    Main.configManager.save(Main.config);
                                } else {
                                    Main.log.error("build-subserver.sh exited with an errors. Please try again.");
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
                                Main.log.error("Problem Downloading Server Build Script from ME1312.net. Is it Down?");
                            } else {
                                Process Process2 = Runtime.getRuntime().exec("chmod +x build-subserver.sh", null, Dir);
                                try {
                                    Process2.waitFor();
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (Process2.exitValue() != 0) {
                                    Main.log.warn("Problem Setting Executable Permissions for build-subserver.sh");
                                    Main.log.warn("This may cause errors in the Build Process");
                                }

                                Process = Runtime.getRuntime().exec("bash build-subserver.sh " + Version.toString() + " " + Type.toString(), null, Dir);
                                StreamGobbler read = new StreamGobbler(Process.getInputStream(), "OUTPUT", Main.config.getNode("Settings", "Server-Creation", "log").getBoolean(), Main.lang.getNode("Lang", "Create-Server", "Log-Prefix").getString() + Name, Main);
                                read.start();
                                try {
                                    Process.waitFor();
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (Process.exitValue() == 0) {
                                    Player.sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + Main.lang.getNode("Lang", "Create-Server", "Server-Create-Done").getString().replace("$Server$", Name)));
                                    final int PID = (Main.SubServers.size() + 1);
                                    Main.Servers.put(PID, new SubServer(true, Name, PID, Port, true, Dir, Exec, PID, false, Main));
                                    Main.PIDs.put(Name, PID);
                                    Main.SubServers.add(Name);

                                    Main.Servers.get(PID).start();
                                    if (API.getSubServer(0).isRunning()) API.getSubServer(0).sendCommandSilently("subconf@proxy addserver " + Name + " " + Main.config.getNode("Settings", "Server-IP").getString() + " " + Port);

                                    Main.config.getNode("Servers", Name, "enabled").setValue(true);
                                    Main.config.getNode("Servers", Name, "port").setValue(Port);
                                    Main.config.getNode("Servers", Name, "run-on-launch").setValue(false);
                                    Main.config.getNode("Servers", Name, "log").setValue(true);
                                    Main.config.getNode("Servers", Name, "dir").setValue(Dir.getPath());
                                    Main.config.getNode("Servers", Name, "shell").setValue(Exec.toString());
                                    Main.config.getNode("Servers", Name, "stop-after").setValue(0);
                                    Main.configManager.save(Main.config);
                                } else {
                                    Main.log.error("build-subserver.sh exited with an errors. Please try again.");
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Running = false;
                }
            }).submit(Main.Plugin);
        }
    }

    public void waitFor() throws InterruptedException {
        Process.waitFor();
    }

    public boolean isRunning() {
        return Running;
    }
}