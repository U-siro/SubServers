package net.ME1312.SubServer.Executable;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.ME1312.SubServer.Libraries.ChatColor;
import net.ME1312.SubServer.SubAPI;
import org.spongepowered.api.entity.living.player.Player;

import net.ME1312.SubServer.Main;
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
                GenerateSpigotYAML();
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
        writer.println("server-ip=" + Main.config.getNode("Settings.Server-IP").toString());
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

    private void GenerateSpigotYAML() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(new File(Dir, "spigot.yml"), "UTF-8");

        writer.println("# This is the main configuration file for Spigot.");
        writer.println("# As you can see, there's tons to configure. Some options may impact gameplay, so use");
        writer.println("# with caution, and make sure you know what each option does before configuring.");
        writer.println("# For a reference for any variable inside this file, check out the Spigot wiki at");
        writer.println("# http://www.spigotmc.org/wiki/spigot-configuration/");
        writer.println("#");
        writer.println("# If you need help with the configuration or have any questions related to Spigot,");
        writer.println("# join us at the IRC or drop by our forums and leave a post.");
        writer.println("#");
        writer.println("# IRC: #spigot @ irc.spi.gt ( http://www.spigotmc.org/pages/irc/ )");
        writer.println("# Forums: http://www.spigotmc.org/");
        writer.println();
        writer.println("config-version: 8");
        writer.println("settings:");
        writer.println("  debug: false");
        writer.println("  save-user-cache-on-stop-only: false");
        writer.println("  bungeecord: true");
        writer.println("  late-bind: false");
        writer.println("  sample-count: 12");
        writer.println("  player-shuffle: 0");
        writer.println("  filter-creative-items: true");
        writer.println("  user-cache-size: 1000");
        writer.println("  int-cache-limit: 1024");
        writer.println("  moved-wrongly-threshold: 0.0625");
        writer.println("  moved-too-quickly-threshold: 100.0");
        writer.println("  timeout-time: 60");
        writer.println("  restart-on-crash: false");
        writer.println("  restart-script: ./start.sh");
        writer.println("  netty-threads: 4");
        writer.println("  attribute:");
        writer.println("    maxHealth:");
        writer.println("      max: 2048.0");
        writer.println("    movementSpeed:");
        writer.println("      max: 2048.0");
        writer.println("    attackDamage:");
        writer.println("      max: 2048.0");
        writer.println("commands:");
        writer.println("  tab-complete: 0");
        writer.println("  log: true");
        writer.println("  spam-exclusions:");
        writer.println("  - /skill");
        writer.println("  silent-commandblock-console: true");
        writer.println("  replace-commands:");
        writer.println("  - setblock");
        writer.println("  - summon");
        writer.println("  - testforblock");
        writer.println("  - tellraw");
        writer.println("messages:");
        writer.println("  whitelist: You are not whitelisted on this server!");
        writer.println("  unknown-command: Unknown command. Type \"/help\" for help.");
        writer.println("  server-full: The server is full!");
        writer.println("  outdated-client: Outdated client! Please use {0}");
        writer.println("  outdated-server: Outdated server! I'm still on {0}");
        writer.println("  restart: Server is restarting");
        writer.println("stats:");
        writer.println("  disable-saving: false");
        writer.println("  forced-stats: {}");
        writer.println("world-settings:");
        writer.println("  default:");
        writer.println("    verbose: true");
        writer.println("    wither-spawn-sound-radius: 0");
        writer.println("    view-distance: 10");
        writer.println("    item-despawn-rate: 6000");
        writer.println("    merge-radius:");
        writer.println("      item: 2.5");
        writer.println("      exp: 3.0");
        writer.println("    arrow-despawn-rate: 1200");
        writer.println("    enable-zombie-pigmen-portal-spawns: true");
        writer.println("    zombie-aggressive-towards-villager: true");
        writer.println("    hanging-tick-frequency: 100");
        writer.println("    max-bulk-chunks: 10");
        writer.println("    max-entity-collisions: 8");
        writer.println("    random-light-updates: false");
        writer.println("    save-structure-info: true");
        writer.println("    mob-spawn-range: 4");
        writer.println("    anti-xray:");
        writer.println("      enabled: true");
        writer.println("      engine-mode: 1");
        writer.println("      hide-blocks:");
        writer.println("      - 14");
        writer.println("      - 15");
        writer.println("      - 16");
        writer.println("      - 21");
        writer.println("      - 48");
        writer.println("      - 49");
        writer.println("      - 54");
        writer.println("      - 56");
        writer.println("      - 73");
        writer.println("      - 74");
        writer.println("      - 82");
        writer.println("      - 129");
        writer.println("      - 130");
        writer.println("      replace-blocks:");
        writer.println("      - 1");
        writer.println("      - 5");
        writer.println("    dragon-death-sound-radius: 0");
        writer.println("    seed-village: 10387312");
        writer.println("    seed-feature: 14357617");
        writer.println("    hunger:");
        writer.println("      walk-exhaustion: 0.2");
        writer.println("      sprint-exhaustion: 0.8");
        writer.println("      combat-exhaustion: 0.3");
        writer.println("      regen-exhaustion: 3.0");
        writer.println("    max-tnt-per-tick: 100");
        writer.println("    max-tick-time:");
        writer.println("      tile: 50");
        writer.println("      entity: 50");
        writer.println("    entity-activation-range:");
        writer.println("      animals: 32");
        writer.println("      monsters: 32");
        writer.println("      misc: 16");
        writer.println("    entity-tracking-range:");
        writer.println("      players: 48");
        writer.println("      animals: 48");
        writer.println("      monsters: 48");
        writer.println("      misc: 32");
        writer.println("      other: 64");
        writer.println("    ticks-per:");
        writer.println("      hopper-transfer: 8");
        writer.println("      hopper-check: 8");
        writer.println("    hopper-amount: 1");
        writer.println("    growth:");
        writer.println("      cactus-modifier: 100");
        writer.println("      cane-modifier: 100");
        writer.println("      melon-modifier: 100");
        writer.println("      mushroom-modifier: 100");
        writer.println("      pumpkin-modifier: 100");
        writer.println("      sapling-modifier: 100");
        writer.println("      wheat-modifier: 100");
        writer.println("      netherwart-modifier: 100");
        writer.println("    nerf-spawner-mobs: false");
        writer.println("    chunks-per-tick: 650");
        writer.println("    clear-tick-list: false");
        writer.println();
        writer.close();
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
                                Main.log.error(e.getStackTrace().toString());
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
                                    Main.log.error(e.getStackTrace().toString());
                                }

                                if (Process.exitValue() == 0) {
                                    Player.sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + Main.lang.getNode("Lang", "Create-Server", "Server-Create-Done").getString()));
                                    final int PID = (Main.SubServers.size() + 1);
                                    Main.Servers.put(PID, new SubServer(true, Name, PID, Port, true, true, Dir, Exec, PID, false, Main));
                                    Main.PIDs.put(Name, PID);
                                    Main.SubServers.add(Name);

                                    Main.Servers.get(PID).start();
                                    if (SubAPI.getSubServer(0).isRunning()) SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy addserver " + Name + " " + Main.config.getNode("Settings", "Server-IP").getString() + " " + Port);

                                    Main.config.getNode("Servers", Name, "enabled").setValue(true);
                                    Main.config.getNode("Servers", Name, "port").setValue(Port);
                                    Main.config.getNode("Servers", Name, "run-on-launch").setValue(false);
                                    Main.config.getNode("Servers", Name, "log").setValue(true);
                                    Main.config.getNode("Servers", Name, "use-shared-chat").setValue(true);
                                    Main.config.getNode("Servers", Name, "dir").setValue(Dir.getPath());
                                    Main.config.getNode("Servers", Name, "exec").setValue(Exec.toString());
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
                                Main.log.error(e.getStackTrace().toString());
                            }

                            if (!(new File(Dir, "build-subserver.sh").exists())) {
                                Main.log.error("Problem Downloading Server Build Script from ME1312.net. Is it Down?");
                            } else {
                                Process Process2 = Runtime.getRuntime().exec("chmod +x build-subserver.sh", null, Dir);
                                try {
                                    Process2.waitFor();
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    Main.log.error(e.getStackTrace().toString());
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
                                    Main.log.error(e.getStackTrace().toString());
                                }

                                if (Process.exitValue() == 0) {
                                    Player.sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + Main.lang.getNode("Lang", "Create-Server", "Server-Create-Done").getString().replace("$Server$", Name)));
                                    final int PID = (Main.SubServers.size() + 1);
                                    Main.Servers.put(PID, new SubServer(true, Name, PID, Port, true, true, Dir, Exec, PID, false, Main));
                                    Main.PIDs.put(Name, PID);
                                    Main.SubServers.add(Name);

                                    Main.Servers.get(PID).start();
                                    if (SubAPI.getSubServer(0).isRunning()) SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy addserver " + Name + " " + Main.config.getNode("Settings", "Server-IP").getString() + " " + Port);

                                    Main.config.getNode("Servers", Name, "enabled").setValue(true);
                                    Main.config.getNode("Servers", Name, "port").setValue(Port);
                                    Main.config.getNode("Servers", Name, "run-on-launch").setValue(false);
                                    Main.config.getNode("Servers", Name, "use-shared-chat").setValue(true);
                                    Main.config.getNode("Servers", Name, "log").setValue(true);
                                    Main.config.getNode("Servers", Name, "dir").setValue(Dir.getPath());
                                    Main.config.getNode("Servers", Name, "exec").setValue(Exec.toString());
                                    Main.config.getNode("Servers", Name, "stop-after").setValue(0);
                                    Main.configManager.save(Main.config);
                                } else {
                                    Main.log.error("build-subserver.sh exited with an errors. Please try again.");
                                }
                            }
                        }
                    } catch (IOException e) {
                        Main.log.error(e.getStackTrace().toString());
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
