package net.ME1312.SubServer;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
                    if (true/*!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Start." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Start.*")*/) { //TODO
                        if (args[1].equals("~Proxy")) {
                            if (Main.config.getNode("Proxy", "enabled").getBoolean() == true && !SubAPI.getSubServer(0).isRunning()) {
                                if (sender instanceof Player) {
                                    SubAPI.getSubServer(args[1]).start((Player) sender);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Start").getString())));
                                } else {
                                    SubAPI.getSubServer(args[1]).start();
                                }
                            } else {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Start-Error").getString())));
                                } else {
                                    Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Start-Error").getString()));
                                }
                            }
                        } else if (Main.config.getNode("Servers", args[1], "enabled") != null && Main.config.getNode("Servers", args[1], "enabled").getBoolean() && !SubAPI.getSubServer(args[1]).isRunning()) {
                            if (sender instanceof Player) {
                                SubAPI.getSubServer(args[1]).start((Player) sender);
                                ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Start").getString())));
                            } else {
                                SubAPI.getSubServer(args[1]).start();
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
                    if (true/*!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Send." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Send.*")*/) { //TODO
                        if (args[1].equals("~Proxy")) {
                            if (SubAPI.getSubServer(args[1]).isRunning()) {
                                int i = 2;
                                String str = args[2];
                                if (args.length != 3) {
                                    do {
                                        i++;
                                        str = str + " " + args[i];
                                    } while ((i + 1) != args.length);
                                }
                                if (sender instanceof Player) {
                                    SubAPI.getSubServer(args[1]).sendCommand((Player) sender, str);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Proxy").getString())));
                                } else {
                                    SubAPI.getSubServer(args[1]).sendCommand(str);
                                }
                            } else {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Proxy-Error").getString())));
                                } else {
                                    Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Proxy-Error").getString()));
                                }
                            }
                        } else if (Main.SubServers.contains(args[1])) {
                            if (SubAPI.getSubServer(args[1]).isRunning()) {
                                int i = 2;
                                String str = args[2];
                                if ((i + 1) != args.length) {
                                    do {
                                        i++;
                                        str = str + " " + args[i];
                                    } while ((i + 1) != args.length);
                                }
                                if (sender instanceof Player) {
                                    SubAPI.getSubServer(args[1]).sendCommand((Player) sender, str);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Send-Command-Server").getString())));
                                } else {
                                    SubAPI.getSubServer(args[1]).sendCommand(str);
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
                    if (true/*!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Stop." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Stop.*")*/) { //TODO
                        if (args[1].equals("~Proxy")) {
                            if (SubAPI.getSubServer(args[1]).isRunning()) {
                                if (sender instanceof Player) {
                                    SubAPI.getSubServer(args[1]).stop((Player) sender);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Stop").getString())));
                                } else {
                                    SubAPI.getSubServer(args[1]).stop();
                                }
                            } else {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Stop-Error").getString())));
                                } else {
                                    Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Stop-Error").getString()));
                                }
                            }
                        } else if (Main.SubServers.contains(args[1])) {
                            if (SubAPI.getSubServer(args[1]).isRunning()) {
                                if (sender instanceof Player) {
                                    SubAPI.getSubServer(args[1]).stop((Player) sender);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Stop").getString())));
                                } else {
                                    SubAPI.getSubServer(args[1]).stop();
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
                    if (true/*!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Kill." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Kill.*")*/) { //TODO
                        if (args[1].equals("~Proxy")) {
                            if (SubAPI.getSubServer(args[1]).isRunning()) {
                                if (sender instanceof Player) {
                                    SubAPI.getSubServer(args[1]).terminate((Player) sender);
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Kill").getString())));
                                } else {
                                    SubAPI.getSubServer(args[1]).terminate();
                                }
                            } else {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Kill-Error").getString())));
                                } else {
                                    Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Proxy-Kill-Error").getString()));
                                }
                            }
                        } else if (Main.SubServers.contains(args[1])) {
                            if (SubAPI.getSubServer(args[1]).isRunning()) {
                                if (sender instanceof Player) {
                                    ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Server-Kill").getString())));
                                    SubAPI.getSubServer(args[1]).terminate((Player) sender);
                                } else {
                                    SubAPI.getSubServer(args[1]).terminate();
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
            } else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
                    if (args.length == 3) {
                        if (!(Main.SubServers.contains(args[1]) || args[1].equalsIgnoreCase("~Lobby"))) {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + Main.lang.getNode("Lang", "Commands", "Teleport-Config-Error").getString()));
                            } else {
                                Main.log.info(String.valueOf(Main.lprefix) + Main.lang.getNode("Lang", "Commands", "Teleport-Config-Error").getString());
                            }
                        } else if ((sender instanceof Player) && (!((Player) sender).hasPermission("subserver.command.teleport.others.*")) && (!((Player) sender).hasPermission("subserver.command.teleport.others." + args[1]))) {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + Main.lang.getNode("Lang", "Commands", "Teleport-Permission-Error").getString()));
                            } else {
                                Main.log.info(String.valueOf(Main.lprefix) + Main.lang.getNode("Lang", "Commands", "Teleport-Permission-Error").getString());
                            }
                        } else if (!args[1].equalsIgnoreCase("~Lobby") && !(SubAPI.getSubServer(args[1]).isRunning())) {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + Main.lang.getNode("Lang", "Commands", "Teleport-Offline-Error").getString()));
                            } else {
                                Main.log.info(String.valueOf(Main.lprefix) + Main.lang.getNode("Lang", "Commands", "Teleport-Offline-Error").getString());
                            }
                        } else if (!SubAPI.getSubServer("~Proxy").isRunning()) {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + Main.lang.getNode("Lang", "Commands", "Teleport-Proxy-Error").getString()));
                            } else {
                                Main.log.info(String.valueOf(Main.lprefix) + Main.lang.getNode("Lang", "Commands", "Teleport-Proxy-Error").getString());
                            }
                        } else {
                            if (args[1].equalsIgnoreCase("~Lobby")) {
                                SubAPI.getSubServer("~Proxy").sendCommandSilently("subconf@proxy sendplayer " + args[2] + " ~Lobby");
                            } else {
                                SubAPI.getSubServer("~Proxy").sendCommandSilently("subconf@proxy sendplayer " + args[2] + " " + args[1]);
                            }
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + Main.lang.getNode("Lang", "Commands", "Teleport").getString()));
                            } else {
                                Main.log.info(String.valueOf(Main.lprefix) + Main.lang.getNode("Lang", "Commands", "Teleport").getString());
                            }
                        }
                    } else if (args.length == 2) {
                        if (!(Main.SubServers.contains(args[1]) || args[1].equalsIgnoreCase("~Lobby"))) {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + Main.lang.getNode("Lang", "Commands", "Teleport-Config-Error").getString()));
                            } else {
                                Main.log.info(String.valueOf(Main.lprefix) + Main.lang.getNode("Lang", "Commands", "Teleport-Config-Error").getString());
                            }
                        } else if ((sender instanceof Player) && (!((Player) sender).hasPermission("subserver.command.teleport." + args[1])) && (!((Player) sender).hasPermission("subserver.command.teleport.*"))) {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + Main.lang.getNode("Lang", "Commands", "Teleport-Permission-Error").getString()));
                            } else {
                                Main.log.info(String.valueOf(Main.lprefix) + Main.lang.getNode("Lang", "Commands", "Teleport-Permission-Error").getString());
                            }
                        } else if (!args[1].equalsIgnoreCase("~Lobby") && !(SubAPI.getSubServer(args[1]).isRunning())) {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + Main.lang.getNode("Lang", "Commands", "Teleport-Offline-Error").getString()));
                            } else {
                                Main.log.info(String.valueOf(Main.lprefix) + Main.lang.getNode("Lang", "Commands", "Teleport-Offline-Error").getString());
                            }
                        } else if (!SubAPI.getSubServer("~Proxy").isRunning()) {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + Main.lang.getNode("Lang", "Commands", "Teleport-Proxy-Error").getString()));
                            } else {
                                Main.log.info(String.valueOf(Main.lprefix) + Main.lang.getNode("Lang", "Commands", "Teleport-Proxy-Error").getString());
                            }
                        } else if (!(sender instanceof Player)) {
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.RED + Main.lprefix + Main.lang.getNode("Lang", "Commands", "Teleport-Console-Error").getString()));
                            } else {
                                Main.log.info(String.valueOf(Main.lprefix) + Main.lang.getNode("Lang", "Commands", "Teleport-Console-Error").getString());
                            }
                        } else {
                            if (args[1].equalsIgnoreCase("~Lobby")) {
                                SubAPI.getSubServer("~Proxy").sendCommandSilently("subconf@proxy sendplayer " + ((Player) sender).getName() + " ~Lobby");
                            } else {
                                SubAPI.getSubServer("~Proxy").sendCommandSilently("subconf@proxy sendplayer " + ((Player) sender).getName() + " " + args[1]);
                            }
                            if (sender instanceof Player) {
                                ((Player) sender).sendMessage(Texts.of(ChatColor.AQUA + Main.lprefix + Main.lang.getNode("Lang", "Commands", "Teleport").getString()));
                            } else {
                                Main.log.info(String.valueOf(Main.lprefix) + Main.lang.getNode("Lang", "Commands", "Teleport").getString());
                            }
                        }
                    } else if (sender instanceof Player) {
                        ((Player) sender).sendMessage(Texts.of("Usage:"));
                        ((Player) sender).sendMessage(Texts.of("/SubServer Tp <Server> [Player]"));
                    } else {
                        Main.log.info("Usage:");
                        Main.log.info("/SubServer Tp <Server> <Player>");
                    }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (true/*!(sender instanceof Player) || ((Player) sender).hasPermission("SubServer.Command.Reload")*/) { //TODO
                    if (sender instanceof Player) {
                        ((Player) sender).sendMessage(Texts.of(ChatColor.GOLD + Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Debug", "Config-Reload-Warn").getString())));
                    } else {
                        Main.log.info(Main.lprefix + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Debug", "Config-Reload-Warn").getString()));
                    }

                    if (!Main.Servers.get(0).isRunning()) {
                        Main.Servers.remove(0);
                    } else {
                        SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy resetplugin");
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
                        Main.Servers.put(0, new SubServer(Main.config.getNode("Proxy", "enabled").getBoolean(), "~Proxy", 0, 25565, Main.config.getNode("Proxy", "log").getBoolean(), false,
                                new File(Main.config.getNode("Proxy", "dir").getString()), new Executable(Main.config.getNode("Proxy", "exec").toString()), 0, false, Main));
                    }

                    Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {
                        public void run() {
                            Main.deleteDir(new File(Main.dataFolder, "cache"));
                            new File(Main.dataFolder, "cache").mkdirs();

                            int i = 0;
                            if (SubAPI.getSubServer(0).isRunning()) {
                                try {
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Server-List " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Server-List").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Player-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Player-Error").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Config-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Config-Error").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Permission-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Permission-Error").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Offline-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Offline-Error").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Console-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Console-Error").getString().replace(" ", "%20")));
                                    Thread.sleep(500);

                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Register-Server " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Register-Server").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Remove-Server " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Remove-Server").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Reset-Storage " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Reset-Storage").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Chat-Format " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Chat-Format").getString().replace(" ", "%20")));
                                    Thread.sleep(500);
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Teleport " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Teleport").getString().replace(" ", "%20")));
                                    Thread.sleep(500);

                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy addserver ~Lobby " + Main.config.getNode("Settings", "Server-IP").getString() + " " + Main.config.getNode("Settings", "Lobby-Port").getString() + " true");
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    Main.log.error(e.getStackTrace().toString());
                                }
                            }

                            List SubServersStore = new ArrayList<Object>();
                            SubServersStore.addAll(Main.config.getNode("Servers").getChildrenMap().keySet());

                            Collections.sort(SubServersStore);

                            for(Iterator<Object> items = SubServersStore.iterator(); items.hasNext(); ) {
                                String item = ((String) items.next());
                                do {
                                    i++;
                                } while (Main.Servers.keySet().contains(i));

                                if (Main.SubServers.contains(item)) {
                                    i--;
                                } else {
                                    Main.SubServers.add(item);
                                    Main.PIDs.put(item, i);
                                    Main.Servers.put(i, new SubServer(Main.config.getNode("Servers", item, "enabled").getBoolean(), item, i, Main.config.getNode("Servers", item, "port").getInt(),
                                            Main.config.getNode("Servers", item, "log").getBoolean(), Main.config.getNode("Servers", item, "use-shared-chat").getBoolean(), new File(Main.config.getNode("Servers", item, "dir").getString()),
                                            new Executable(Main.config.getNode("Servers", item, "exec").getString()), Main.config.getNode("Servers", item, "stop-after").getDouble(), false, Main));
                                }
                            }

                            for(Iterator<String> str = Main.SubServers.iterator(); str.hasNext(); ) {
                                String item = str.next();
                                if (SubAPI.getSubServer(0).isRunning()) {
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy addserver " + item + " " + Main.config.getNode("Settings", "Server-IP").getString() + " " + SubAPI.getSubServer(item).Port + " " + SubAPI.getSubServer(item).SharedChat);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        Main.log.error(e.getStackTrace().toString());
                                    }
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
                    for (Iterator<String> str = Arrays.<String>asList(getHelp(true)).iterator(); str.hasNext(); ) {
                        ((Player) sender).sendMessage(Texts.of(str.next()));
                    }
                } else {
                    for (Iterator<String> str = Arrays.<String>asList(getHelp(false)).iterator(); str.hasNext(); ) {
                        Main.log.info(str.next());
                    }
                }
            } else if (Main.config.getNode("Settings", "GUI", "Enabled").getBoolean() && (sender instanceof Player) /*&& ((Player) sender).hasPermission("SubServer.Command")*/) { //TODO
                if (Main.SubServers.contains(args[0])) {
                    Main.GUI.ServerAdminWindow((Player) sender, SubAPI.getSubServer(args[0]));
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
            if (Main.config.getNode("Settings", "GUI", "Enabled").getBoolean() && (sender instanceof Player) /*&& ((Player) sender).hasPermission("SubServer.Command")*/) { //TODO
                Main.GUI.ServerSelectionWindow((Player) sender, 0);
            } else {
                if (sender instanceof Player) {
                    for (Iterator<String> str = Arrays.<String>asList(getHelp(true)).iterator(); str.hasNext(); ) {
                        ((Player) sender).sendMessage(Texts.of(str.next()));
                    }
                } else {
                    for (Iterator<String> str = Arrays.<String>asList(getHelp(false)).iterator(); str.hasNext(); ) {
                        Main.log.info(str.next());
                    }
                }
            }
        }
        return CommandResult.success();
    }

    private String[] getHelp(boolean isPlayer) {
        return new String[]{
                ((isPlayer) ? ChatColor.AQUA : "") + "SubServers Command List:",
                ((isPlayer) ? ChatColor.AQUA : "") + "GUI: /SubServer [Server]",
                ((isPlayer) ? ChatColor.AQUA : "") + "Help: /SubServer Help",
                ((isPlayer) ? ChatColor.AQUA : "") + "Reload Plugin: /SubServer Reload",
                ((isPlayer) ? ChatColor.AQUA : "") + "Plugin Version: /SubServer Version",
                ((isPlayer) ? ChatColor.AQUA : "") + "Stop Server: /SubServer Stop <Server>",
                ((isPlayer) ? ChatColor.AQUA : "") + "Kill Server: /SubServer Kill <Server>",
                ((isPlayer) ? ChatColor.AQUA : "") + "Start Server: /SubServer Start <Server>",
                ((isPlayer) ? ChatColor.AQUA : "") + "Send Command: /SubServer Cmd <Server> <Command> " + ((isPlayer) ? ChatColor.ITALIC : "") + "[Args...]",
                ((isPlayer) ? // if
                        ((SubAPI.getSubServer(0).isRunning()) ? ChatColor.AQUA + "Teleport: /Go <Server> [Player]" : "")
                        : //    else
                        ((SubAPI.getSubServer(0).isRunning()) ? "Teleport: /Sub TP <Server> <Player>" : "")),
        };
    }
}