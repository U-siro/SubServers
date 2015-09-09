package net.ME1312.SubServer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.ME1312.SubServer.Executable.Executable;
import net.ME1312.SubServer.Executable.SubServer;
import net.ME1312.SubServer.Libraries.ChatColor;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import org.apache.commons.lang3.StringEscapeUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class SubServersCMD implements CommandExecutor {
    private Main Main;

    public SubServersCMD(Main Main) {
        this.Main = Main;
    }


    public CommandResult execute(final CommandSource sender, CommandContext arguments) throws CommandException {
        if (arguments.getOne("args").isPresent()) {
            String[] args = (((String) arguments.getOne("args").get())).split(" ");
            if (args[0].equalsIgnoreCase("start")) {
                if (args.length == 2) {
                    if (!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Start." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Start.*")) {
                        if (args[1].equals("~Proxy")) {
                            if (Main.config.getNode("Proxy", "enabled").getBoolean() == true && !API.getSubServer(0).isRunning()) {
                                if (sender instanceof Player) {
                                    API.getSubServer(args[1]).start((Player) sender);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Start").getString())));
                                } else {
                                    API.getSubServer(args[1]).start();
                                }
                            } else {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Start-Error").getString())));
                                } else {
                                    Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Start-Error").getString()));
                                }
                            }
                        } else if (Main.config.getNode("Servers", args[1], "enabled") != null && Main.config.getNode("Servers", args[1], "enabled").getBoolean() && !API.getSubServer(args[1]).isRunning()) {
                            if (sender instanceof Player) {
                                API.getSubServer(args[1]).start((Player) sender);
                                ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Start").getString())));
                            } else {
                                API.getSubServer(args[1]).start();
                            }
                        } else if (!Main.SubServers.contains(args[1])) {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Start-Config-Error").getString())));
                            } else {
                                Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Start-Config-Error").getString()));
                            }
                        } else {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Start-Error").getString())));
                            } else {
                                Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Start-Error").getString()));
                            }
                        }
                    } else if (sender instanceof Player) {
                        ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Start-Permission-Error").getString())));
                    }
                } else {
                    if (sender instanceof Player) {
                        ((Player) sender).sendMessage(Texts.of("Usage:"));
                        ((Player) sender).sendMessage(Texts.of("/SubServer Start <Server>"));
                    } else {
                        Main.log.info("Usage:");
                        Main.log.info("/SubServer Start <Server>");
                    }
                }
            } else if (args[0].equalsIgnoreCase("cmd") || args[0].equalsIgnoreCase("command")) {
                if (args.length >= 3) {
                    if (!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Send." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Send.*")) {
                        if (args[1].equals("~Proxy")) {
                            if (API.getSubServer(args[1]).isRunning()) {
                                int i = 2;
                                String str = args[2];
                                if (args.length != 3) {
                                    do {
                                        i++;
                                        str = str + " " + args[i];
                                    } while ((i + 1) != args.length);
                                }
                                if (sender instanceof Player) {
                                    API.getSubServer(args[1]).sendCommand((Player) sender, str);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Proxy").getString())));
                                } else {
                                    API.getSubServer(args[1]).sendCommand(str);
                                }
                            } else {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Proxy-Error").getString())));
                                } else {
                                    Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Proxy-Error").getString()));
                                }
                            }
                        } else if (Main.SubServers.contains(args[1])) {
                            if (API.getSubServer(args[1]).isRunning()) {
                                int i = 2;
                                String str = args[2];
                                if ((i + 1) != args.length) {
                                    do {
                                        i++;
                                        str = str + " " + args[i];
                                    } while ((i + 1) != args.length);
                                }
                                if (sender instanceof Player) {
                                    API.getSubServer(args[1]).sendCommand((Player) sender, str);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Server").getString())));
                                } else {
                                    API.getSubServer(args[1]).sendCommand(str);
                                }
                            } else {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Server-Error").getString())));
                                } else {
                                    Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Server-Error").getString()));
                                }
                            }
                        } else {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Config-Error").getString())));
                            } else {
                                Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Config-Error").getString()));
                            }
                        }
                    } else if (sender instanceof Player) {
                        ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Permission-Error").getString())));
                    }
                } else {
                    if (sender instanceof Player) {
                        ((Player) sender).sendMessage(Texts.of("Usage:"));
                        ((Player) sender).sendMessage(Texts.of("/SubServer Cmd <Server> <Command> [Args...]"));
                    } else {
                        Main.log.info("Usage:");
                        Main.log.info("/SubServer Cmd <Server> <Command> [Args...]");
                    }
                }
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (args.length == 2) {
                    if (!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Stop." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Stop.*")) {
                        if (args[1].equals("~Proxy")) {
                            if (API.getSubServer(args[1]).isRunning()) {
                                if (sender instanceof Player) {
                                    API.getSubServer(args[1]).stop((Player) sender);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Stop").getString())));
                                } else {
                                    API.getSubServer(args[1]).stop();
                                }
                            } else {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Stop-Error").getString())));
                                } else {
                                    Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Stop-Error").getString()));
                                }
                            }
                        } else if (Main.SubServers.contains(args[1])) {
                            if (API.getSubServer(args[1]).isRunning()) {
                                if (sender instanceof Player) {
                                    API.getSubServer(args[1]).stop((Player) sender);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Stop").getString())));
                                } else {
                                    API.getSubServer(args[1]).stop();
                                }
                            } else {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Stop-Error").getString())));
                                } else {
                                    Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Stop-Error").getString()));
                                }
                            }
                        } else {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Stop-Config-Error").getString())));
                            } else {
                                Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Stop-Config-Error").getString()));
                            }
                        }
                    } else if (sender instanceof Player) {
                        ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Stop-Permission-Error").getString())));
                    }
                } else {
                    if (sender instanceof Player) {
                        ((Player) sender).sendMessage(Texts.of("Usage:"));
                        ((Player) sender).sendMessage(Texts.of("/SubServer Stop <Server>"));
                    } else {
                        Main.log.info("Usage:");
                        Main.log.info("/SubServer Stop <Server>");
                    }
                }
            } else if (args[0].equalsIgnoreCase("kill")) {
                if (args.length == 2) {
                    if (!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Kill." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Kill.*")) {
                        if (args[1].equals("~Proxy")) {
                            if (API.getSubServer(args[1]).isRunning()) {
                                if (sender instanceof Player) {
                                    API.getSubServer(args[1]).terminate((Player) sender);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Kill").getString())));
                                } else {
                                    API.getSubServer(args[1]).terminate();
                                }
                            } else {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Kill-Error").getString())));
                                } else {
                                    Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Kill-Error").getString()));
                                }
                            }
                        } else if (Main.SubServers.contains(args[1])) {
                            if (API.getSubServer(args[1]).isRunning()) {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Kill").getString())));
                                    API.getSubServer(args[1]).terminate((Player) sender);
                                } else {
                                    API.getSubServer(args[1]).terminate();
                                }
                            } else {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Kill-Error").getString())));
                                } else {
                                    Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Kill-Error").getString()));
                                }
                            }
                        } else {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Kill-Config-Error").getString())));
                            } else {
                                Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Kill-Config-Error").getString()));
                            }
                        }
                    } else if (sender instanceof Player) {
                        ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Kill-Permission-Error").getString())));
                    }
                } else {
                    if (sender instanceof Player) {
                        ((Player) sender).sendMessage(Texts.of("Usage:"));
                        ((Player) sender).sendMessage(Texts.of("/SubServer Kill <Server>"));
                    } else {
                        Main.log.info("Usage:");
                        Main.log.info("/SubServer Kill <Server>");
                    }
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!(sender instanceof Player) || ((Player) sender).hasPermission("SubServer.Command.Reload")) {
                    if (sender instanceof Player) {
                        ((Player) sender).sendMessage(Texts.of(ChatColor.GOLD + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Debug", "Config-Reload-Warn").getString())));
                    } else {
                        Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Debug", "Config-Reload-Warn").getString()));
                    }

                    if (!Main.Servers.get(0).isRunning()) {
                        Main.Servers.remove(0);
                    } else {
                        API.getSubServer(0).sendCommandSilently("subconf@proxy resetplugin");
                    }

                    int i = 0;
                    List<String> SubServersStore = new ArrayList<String>();
                    SubServersStore.addAll(Main.SubServers);

                    for(Iterator<String> str = SubServersStore.iterator(); str.hasNext(); ) {
                        String item = str.next();
                        i++;
                        if (!Main.Servers.get(i).isRunning()) {
                            Main.Servers.remove(i);
                            Main.PIDs.remove(item);
                            Main.SubServers.remove(item);
                        }
                    }

                    try {
                        Main.config = HoconConfigurationLoader.builder().setFile(new File(Main.dataFolder, "config.conf")).build().load();
                        Main.lang = HoconConfigurationLoader.builder().setFile(new File(Main.dataFolder, "lang.conf")).build().load();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    if (Main.Servers.get(0) == null) {
                        Main.Servers.put(0, new SubServer(Main.config.getNode("Proxy", "enabled").getBoolean(), "~Proxy", 0, 25565, Main.config.getNode("Proxy", "log").getBoolean(),
                                new File(Main.config.getNode("Proxy", "dir").getString()), new Executable(Main.config.getNode("Proxy", "shell").toString()), 0, false, Main));
                    }

                    Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {
                        public void run() {
                            Main.deleteDir(new File(Main.dataFolder, "cache"));
                            new File(Main.dataFolder, "cache").mkdirs();

                            int i = 0;
                            if (API.getSubServer(0).isRunning()) {
                                try {
                                    API.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    API.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Server-List " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Server-List").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    API.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Player-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Player-Error").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    API.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Config-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Config-Error").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    API.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Permission-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Permission-Error").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    API.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Offline-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Offline-Error").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    API.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Console-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Console-Error").getString().replace(" ", "%20")));
                                    Thread.sleep(500);

                                    API.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Register-Server " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Register-Server").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    API.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Remove-Server " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Remove-Server").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    API.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Reset-Storage " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Reset-Storage").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    API.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Teleport " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Teleport").getString().replace(" ", "%20")));
                                    Thread.sleep(500);

                                    API.getSubServer(0).sendCommandSilently("subconf@proxy addserver ~Lobby " + Main.config.getNode("Settings", "Server-IP").getString() + " " + Main.config.getNode("Settings", "Lobby-Port").getString());
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            List SubServersStore = new ArrayList<Object>();
                            SubServersStore.addAll(Main.config.getNode("Servers").getChildrenMap().keySet());
                            Collections.sort(SubServersStore);

                            for(Iterator<Object> items = SubServersStore.iterator(); items.hasNext(); ) {
                                String item = ((String) items.next());
                                if (API.getSubServer(0).isRunning()) {
                                    API.getSubServer(0).sendCommandSilently("subconf@proxy addserver " + item + " " + Main.config.getNode("Settings", "Server-IP").getString() + " " + Main.config.getNode("Servers", item, "port").getString());
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                do {
                                    i++;
                                } while (Main.Servers.keySet().contains(i));

                                if (Main.SubServers.contains(item)) {
                                    i--;
                                } else {
                                    Main.SubServers.add(item);
                                    Main.PIDs.put(item, i);
                                    Main.Servers.put(i, new SubServer(Main.config.getNode("Servers", item, "enabled").getBoolean(), item, i, Main.config.getNode("Servers", item, "port").getInt(),
                                            Main.config.getNode("Servers", item, "log").getBoolean(), new File(Main.config.getNode("Servers", item, "dir").getString()),
                                            new Executable(Main.config.getNode("Servers", item, "shell").getString()), Main.config.getNode("Servers", item, "stop-after").getDouble(), false, Main));
                                }
                            }
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Debug", "Config-Reload").getString())));
                            }
                            Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Debug", "Config-Reload").getString()));
                        }
                    }).submit(Main.Plugin);
                } else if (sender instanceof Player) {
                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + "You do not have permission to Reload this plugin."));
                }
            } else if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
                if (sender instanceof Player) {
                    ((Player) sender).sendMessage(Texts.of(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + " " + Main.Plugin.getName() + " v" + Main.Plugin.getVersion() + ChatColor.GREEN + ChatColor.ITALIC + " © ME1312 EPIC 2015"));
                    ((Player) sender).sendMessage(Texts.of(" "));
                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + " Project Page:" + ChatColor.ITALIC + " " + Main.website.toString()));
                } else {
                    Main.log.info(Main.Plugin.getName() + " v" + Main.Plugin.getVersion() + " © ME1312 EPIC 2015");
                    Main.log.info("Project Page: " + Main.website.toString());
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                if (sender instanceof Player) {
                    ((Player) sender).sendMessage(Texts.of("Subservers Command List:"));
                    ((Player) sender).sendMessage(Texts.of("GUI: /Subserver [Server]"));
                    ((Player) sender).sendMessage(Texts.of("Reload Plugin: /SubServer Reload"));
                    ((Player) sender).sendMessage(Texts.of("Stop Server: /SubServer Stop <Server>"));
                    ((Player) sender).sendMessage(Texts.of("Kill Server: /SubServer Kill <Server>"));
                    ((Player) sender).sendMessage(Texts.of("Start Server: /SubServer Start <Server>"));
                    ((Player) sender).sendMessage(Texts.of("Send Command: /SubServer Cmd <Server> <Command> " + ChatColor.ITALIC + "[Args...]"));
                    ((Player) sender).sendMessage(Texts.of("Plugin Version: /SubServer Version"));
                    if (API.getSubServer(0).isRunning()) ((Player) sender).sendMessage(Texts.of("Teleport: /Go <Server> [Player]"));
                } else {
                    Main.log.info("Subservers Command List:");
                    Main.log.info("Reload Plugin: /SubServer Reload");
                    Main.log.info("Stop Server: /SubServer Stop <Server>");
                    Main.log.info("Kill Server: /SubServer Kill <Server>");
                    Main.log.info("Start Server: /SubServer Start <Server>");
                    Main.log.info("Send Command: /SubServer Cmd <Server> <Command> [Args...]");
                    Main.log.info("Plugin Version: /SubServer Version");
                    if (API.getSubServer(0).isRunning()) Main.log.info("Teleport: /Go <Server> <Player>");
                }
            } else if (Main.config.getNode("Settings", "GUI", "Enabled").getBoolean() && (sender instanceof Player)) {
                if (Main.SubServers.contains(args[0])) {
                    Main.GUI.ServerSelectionWindow((Player) sender, 0, API.getSubServer(args[0]));
                } else {
                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + "Invalid Server Name"));
                }
            } else {
                if (sender instanceof Player) {
                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + "Invalid Command Usage: /Subserver help"));
                } else {
                    Main.log.info("Invalid Command Usage: /Subserver help");
                }
            }
        } else {
            if ((sender instanceof Player) /*&& ((Player) sender).hasPermission("SubServer.Command")*/) {
                if (Main.config.getNode("Settings", "GUI", "Enabled").getBoolean()) {
                    Main.GUI.ServerSelectionWindow((Player) sender, 0, null);
                } else {
                    ((Player) sender).sendMessage(Texts.of("Subservers Command List:"));
                    ((Player) sender).sendMessage(Texts.of("GUI: /Subserver [Server]"));
                    ((Player) sender).sendMessage(Texts.of("Reload Plugin: /SubServer Reload"));
                    ((Player) sender).sendMessage(Texts.of("Stop Server: /SubServer Stop <Server>"));
                    ((Player) sender).sendMessage(Texts.of("Kill Server: /SubServer Kill <Server>"));
                    ((Player) sender).sendMessage(Texts.of("Start Server: /SubServer Start <Server>"));
                    ((Player) sender).sendMessage(Texts.of("Send Command: /SubServer Cmd <Server> <Command> " + ChatColor.ITALIC + "[Args...]"));
                    ((Player) sender).sendMessage(Texts.of("Plugin Version: /SubServer Version"));
                    if (API.getSubServer(0).isRunning()) ((Player) sender).sendMessage(Texts.of("Teleport: /Go <Server> [Player]"));
                }
            } else {
                if (sender instanceof Player) {
                    ((Player) sender).sendMessage(Texts.of("Subservers Command List:"));
                    ((Player) sender).sendMessage(Texts.of("GUI: /Subserver [Server]"));
                    ((Player) sender).sendMessage(Texts.of("Reload Plugin: /SubServer Reload"));
                    ((Player) sender).sendMessage(Texts.of("Stop Server: /SubServer Stop <Server>"));
                    ((Player) sender).sendMessage(Texts.of("Kill Server: /SubServer Kill <Server>"));
                    ((Player) sender).sendMessage(Texts.of("Start Server: /SubServer Start <Server>"));
                    ((Player) sender).sendMessage(Texts.of("Send Command: /SubServer Cmd <Server> <Command> " + ChatColor.ITALIC + "[Args...]"));
                    ((Player) sender).sendMessage(Texts.of("Plugin Version: /SubServer Version"));
                    if (API.getSubServer(0).isRunning()) ((Player) sender).sendMessage(Texts.of("Teleport: /Go <Server> [Player]"));
                } else {
                    Main.log.info("Main SubServer Command");
                    Main.log.info("Reload Plugin: /SubServer Reload");
                    Main.log.info("Stop Server: /SubServer Stop <Server>");
                    Main.log.info("Kill Server: /SubServer Kill <Server>");
                    Main.log.info("Start Server: /SubServer Start <Server>");
                    Main.log.info("Send Command: /SubServer Cmd <Server> <Command> [Args...]");
                    Main.log.info("Plugin Version: /SubServer Version");
                }
            }
        }
        return CommandResult.success();
    }
}