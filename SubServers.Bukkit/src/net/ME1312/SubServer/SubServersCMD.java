package net.ME1312.SubServer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.ME1312.SubServer.Executable.Executable;
import net.ME1312.SubServer.Executable.SubServer;
import net.ME1312.SubServer.GUI.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SubServersCMD implements CommandExecutor {
	Main Main;
	
	public SubServersCMD(Main Main) {
		this.Main = Main;
	}
	
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) throws ArrayIndexOutOfBoundsException {
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("start")) {
				if (args.length == 2) {
					if (!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Start." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Start.*")) {
						if (args[1].equals("~Proxy")) {
							if (Main.config.getBoolean("Proxy.enabled") == true && !SubAPI.getSubServer(0).isRunning()) {
								if (sender instanceof Player) {
									SubAPI.getSubServer(args[1]).start((Player) sender);
									((Player) sender).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Commands.Proxy-Start"));
								} else {
									SubAPI.getSubServer(args[1]).start();
								}
							} else {
								if (sender instanceof Player) {
									((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Proxy-Start-Error"));
								} else {
									Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Proxy-Start-Error"));
								}
							}
						} else if (Main.config.getBoolean("Servers." + args[1] + ".enabled") == true && !SubAPI.getSubServer(args[1]).isRunning()) {
							if (sender instanceof Player) {
								SubAPI.getSubServer(args[1]).start((Player) sender);
								((Player) sender).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Commands.Server-Start"));
							} else {
								SubAPI.getSubServer(args[1]).start();
							}
						} else if (!Main.SubServers.contains(args[1])) {
							if (sender instanceof Player) {
								((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Start-Config-Error"));
							} else {
								Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Start-Config-Error"));
							}
						} else {
							if (sender instanceof Player) {
								((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Server-Start-Error"));
							} else {
								Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Server-Start-Error"));
							}
						}
					} else if (sender instanceof Player) {
						((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Start-Permission-Error"));
					}
				} else {
					if (sender instanceof Player) {
						((Player) sender).sendMessage("Usage:");
						((Player) sender).sendMessage("/SubServer Start <Server>");
					} else {
						Bukkit.getLogger().info("Usage:");
						Bukkit.getLogger().info("/SubServer Start <Server>");
					}
				}
			} else if (args[0].equalsIgnoreCase("cmd") || args[0].equalsIgnoreCase("command")) {
				if (args.length >= 3) {
					if (!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Send." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Send.*")) {
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
									((Player) sender).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Commands.Send-Command-Proxy"));
								} else {
									SubAPI.getSubServer(args[1]).sendCommand(str);
								}
							} else {
								if (sender instanceof Player) {
									((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Send-Command-Proxy-Error"));
								} else {
									Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Send-Command-Proxy-Error"));
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
									((Player) sender).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Commands.Send-Command-Server"));
								} else {
									SubAPI.getSubServer(args[1]).sendCommand(str);
								}
							} else {
								if (sender instanceof Player) {
									((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Send-Command-Server-Error"));
								} else {
									Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Send-Command-Server-Error"));
								}
							}
						} else {
							if (sender instanceof Player) {
								((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Send-Command-Config-Error"));
							} else {
								Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Send-Command-Config-Error"));
							}
						}
					} else if (sender instanceof Player) {
						((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Send-Command-Permission-Error"));
					}
				} else {
					if (sender instanceof Player) {
						((Player) sender).sendMessage("Usage:");
						((Player) sender).sendMessage("/SubServer Cmd <Server> <Command> [Args...]");
					} else {
						Bukkit.getLogger().info("Usage:");
						Bukkit.getLogger().info("/SubServer Cmd <Server> <Command> [Args...]");
					}
				}
			} else if (args[0].equalsIgnoreCase("stop")) {
				if (args.length == 2) {
					if (!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Stop." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Stop.*")) {
						if (args[1].equals("~Proxy")) {
							if (SubAPI.getSubServer(args[1]).isRunning()) {
								if (sender instanceof Player) {
									SubAPI.getSubServer(args[1]).stop((Player) sender);
									((Player) sender).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Commands.Proxy-Stop"));
								} else {
									SubAPI.getSubServer(args[1]).stop();
								}
							} else {
								if (sender instanceof Player) {
									((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Proxy-Stop-Error"));
								} else {
									Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Proxy-Stop-Error"));
								}
							}
						} else if (Main.SubServers.contains(args[1])) {
							if (SubAPI.getSubServer(args[1]).isRunning()) {
								if (sender instanceof Player) {
									SubAPI.getSubServer(args[1]).stop((Player) sender);
									((Player) sender).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Commands.Server-Stop"));
								} else {
									SubAPI.getSubServer(args[1]).stop();
								}
							} else {
								if (sender instanceof Player) {
									((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Server-Stop-Error"));
								} else {
									Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Server-Stop-Error"));
								}
							}
						} else {
							if (sender instanceof Player) {
								((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Stop-Config-Error"));
							} else {
								Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Stop-Config-Error"));
							}
						}
					} else if (sender instanceof Player) {
						((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Server-Stop-Permission-Error"));
					}
				} else {
					if (sender instanceof Player) {
						((Player) sender).sendMessage("Usage:");
						((Player) sender).sendMessage("/SubServer Stop <Server>");
					} else {
						Bukkit.getLogger().info("Usage:");
						Bukkit.getLogger().info("/SubServer Stop <Server>");
					}
				}
			} else if (args[0].equalsIgnoreCase("kill")) {
				if (args.length == 2) {
					if (!(sender instanceof Player) || (((Player) sender).hasPermission("SubServer.Command.Kill." + args[1])) || ((Player) sender).hasPermission("SubServer.Command.Kill.*")) {
						if (args[1].equals("~Proxy")) {
							if (SubAPI.getSubServer(args[1]).isRunning()) {
								if (sender instanceof Player) {
									SubAPI.getSubServer(args[1]).terminate((Player) sender);
									((Player) sender).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Commands.Proxy-Kill"));
								} else {
									SubAPI.getSubServer(args[1]).terminate();
								}
							} else {
								if (sender instanceof Player) {
									((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Proxy-Kill-Error"));
								} else {
									Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Proxy-Kill-Error"));
								}
							}
						} else if (Main.SubServers.contains(args[1])) {
							if (SubAPI.getSubServer(args[1]).isRunning()) {
								if (sender instanceof Player) {
									((Player) sender).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Commands.Server-Kill"));
									SubAPI.getSubServer(args[1]).terminate((Player) sender);
								} else {
									SubAPI.getSubServer(args[1]).terminate();
								}
							} else {
								if (sender instanceof Player) {
									((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Server-Kill-Error"));
								} else {
									Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Server-Kill-Error"));
								}
							}
						} else {
							if (sender instanceof Player) {
								((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Kill-Config-Error"));
							} else {
								Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Commands.Kill-Config-Error"));
							}
						}
					} else if (sender instanceof Player) {
						((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Kill-Permission-Error"));
					}
				} else {
					if (sender instanceof Player) {
						((Player) sender).sendMessage("Usage:");
						((Player) sender).sendMessage("/SubServer Kill <Server>");
					} else {
						Bukkit.getLogger().info("Usage:");
						Bukkit.getLogger().info("/SubServer Kill <Server>");
					}
				}
			} else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
                if (args.length == 3) {
                    if (!(Main.SubServers.contains(args[1]) || args[1].equalsIgnoreCase("~Lobby"))) {
                        if (sender instanceof Player) {
                            ((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Teleport-Config-Error"));
                        } else {
                            Bukkit.getLogger().info(String.valueOf(Main.lprefix) + Main.lang.getString("Lang.Commands.Teleport-Config-Error"));
                        }
                    } else if ((sender instanceof Player) && (!((Player) sender).hasPermission("subserver.command.teleport.others.*")) && (!((Player) sender).hasPermission("subserver.command.teleport.others." + args[1]))) {
                        if (sender instanceof Player) {
                            ((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Teleport-Permission-Error"));
                        } else {
                            Bukkit.getLogger().info(String.valueOf(Main.lprefix) + Main.lang.getString("Lang.Commands.Teleport-Permission-Error"));
                        }
                    } else if (!args[1].equalsIgnoreCase("~Lobby") && !(SubAPI.getSubServer(args[1]).isRunning())) {
                        if (sender instanceof Player) {
                            ((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Teleport-Offline-Error"));
                        } else {
                            Bukkit.getLogger().info(String.valueOf(Main.lprefix) + Main.lang.getString("Lang.Commands.Teleport-Offline-Error"));
                        }
                    } else if (!SubAPI.getSubServer("~Proxy").isRunning()) {
                        if (sender instanceof Player) {
                            ((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Teleport-Proxy-Error"));
                        } else {
                            Bukkit.getLogger().info(String.valueOf(Main.lprefix) + Main.lang.getString("Lang.Commands.Teleport-Proxy-Error"));
                        }
                    } else {
                        if (args[1].equalsIgnoreCase("~Lobby")) {
                            SubAPI.getSubServer("~Proxy").sendCommandSilently("subconf@proxy sendplayer " + args[2] + " ~Lobby");
                        } else {
                            SubAPI.getSubServer("~Proxy").sendCommandSilently("subconf@proxy sendplayer " + args[2] + " " + args[1]);
                        }
                        if (sender instanceof Player) {
                            ((Player) sender).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Commands.Teleport"));
                        } else {
                            Bukkit.getLogger().info(String.valueOf(Main.lprefix) + Main.lang.getString("Lang.Commands.Teleport"));
                        }
                    }
                } else if (args.length == 2) {
                    if (!(Main.SubServers.contains(args[1]) || args[1].equalsIgnoreCase("~Lobby"))) {
                        if (sender instanceof Player) {
                            ((Player)sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Teleport-Config-Error"));
                        } else {
                            Bukkit.getLogger().info(String.valueOf(Main.lprefix) + Main.lang.getString("Lang.Commands.Teleport-Config-Error"));
                        }
                    } else if ((sender instanceof Player) && (!((Player) sender).hasPermission("subserver.command.teleport." + args[1])) && (!((Player) sender).hasPermission("subserver.command.teleport.*"))) {
                        if (sender instanceof Player) {
                            ((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Teleport-Permission-Error"));
                        } else {
                            Bukkit.getLogger().info(String.valueOf(Main.lprefix) + Main.lang.getString("Lang.Commands.Teleport-Permission-Error"));
                        }
                    } else if (!args[1].equalsIgnoreCase("~Lobby") && !(SubAPI.getSubServer(args[1]).isRunning())) {
                        if (sender instanceof Player) {
                            ((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Teleport-Offline-Error"));
                        } else {
                            Bukkit.getLogger().info(String.valueOf(Main.lprefix) + Main.lang.getString("Lang.Commands.Teleport-Offline-Error"));
                        }
                    } else if (!SubAPI.getSubServer("~Proxy").isRunning()) {
                        if (sender instanceof Player) {
                            ((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Teleport-Proxy-Error"));
                        } else {
                            Bukkit.getLogger().info(String.valueOf(Main.lprefix) + Main.lang.getString("Lang.Commands.Teleport-Proxy-Error"));
                        }
                    } else if (!(sender instanceof Player)) {
                        if (sender instanceof Player) {
                            ((Player)sender).sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Commands.Teleport-Console-Error"));
                        } else {
                            Bukkit.getLogger().info(String.valueOf(Main.lprefix) + Main.lang.getString("Lang.Commands.Teleport-Console-Error"));
                        }
                    } else {
                        if (args[1].equalsIgnoreCase("~Lobby")) {
                            SubAPI.getSubServer("~Proxy").sendCommandSilently("subconf@proxy sendplayer " + ((Player) sender).getName() + " ~Lobby");
                        } else {
                            SubAPI.getSubServer("~Proxy").sendCommandSilently("subconf@proxy sendplayer " + ((Player) sender).getName() + " " + args[1]);
                        }
                        if (sender instanceof Player) {
                            ((Player)sender).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Commands.Teleport"));
                        } else {
                            Bukkit.getLogger().info(String.valueOf(Main.lprefix) + Main.lang.getString("Lang.Commands.Teleport"));
                        }
                    }
                } else if (sender instanceof Player) {
                    ((Player)sender).sendMessage("Usage:");
                    ((Player)sender).sendMessage("/SubServer Tp <Server> [Player]");
                } else {
                    Bukkit.getLogger().info("Usage:");
                    Bukkit.getLogger().info("/SubServer Tp <Server> <Player>");
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
				if (!(sender instanceof Player) || ((Player) sender).hasPermission("SubServer.Command.Reload")) {
					if (sender instanceof Player) {
						((Player) sender).sendMessage(ChatColor.GOLD + Main.lprefix + Main.lang.getString("Lang.Debug.Config-Reload-Warn"));
					} else {
						Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Debug.Config-Reload-Warn"));
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
					Main.config.reloadConfig();
					Main.lang.reloadConfig();
					
					if (Main.Servers.get(0) == null) {
						Main.Servers.put(0, new SubServer(Main.config.getBoolean("Proxy.enabled"), "~Proxy", 0, 25565, Main.config.getBoolean("Proxy.log"), false, new File(Main.config.getRawString("Proxy.dir")),
								new Executable(Main.config.getRawString("Proxy.exec")), 0, false, Main));
					}
					
					new BukkitRunnable() {
						@Override
						public void run() {
							int i = 0;
							if (SubAPI.getSubServer(0).isRunning()) {
								try {
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport " + Main.lang.getString("Lang.Commands.Teleport").replace(" ", "%20"));
									Thread.sleep(500);
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Server-List " + Main.lang.getString("Lang.Commands.Teleport-Server-List").replace(" ", "%20"));
									Thread.sleep(500);
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Player-Error " + Main.lang.getString("Lang.Commands.Teleport-Player-Error").replace(" ", "%20"));
									Thread.sleep(500);
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Config-Error " + Main.lang.getString("Lang.Commands.Teleport-Config-Error").replace(" ", "%20"));
									Thread.sleep(500);
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Permission-Error " + Main.lang.getString("Lang.Commands.Teleport-Permission-Error").replace(" ", "%20"));
									Thread.sleep(500);
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Offline-Error " + Main.lang.getString("Lang.Commands.Teleport-Offline-Error").replace(" ", "%20"));
									Thread.sleep(500);
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Console-Error " + Main.lang.getString("Lang.Commands.Teleport-Console-Error").replace(" ", "%20"));
									Thread.sleep(500);
								
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Register-Server " + Main.lang.getString("Lang.Proxy.Register-Server").replace(" ", "%20"));
									Thread.sleep(500);
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Remove-Server " + Main.lang.getString("Lang.Proxy.Remove-Server").replace(" ", "%20"));
									Thread.sleep(500);
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Reset-Storage " + Main.lang.getString("Lang.Proxy.Reset-Storage").replace(" ", "%20"));
									Thread.sleep(500);
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Chat-Format " + Main.lang.getString("Lang.Proxy.Chat-Format").replace(" ", "%20"));
                                    Thread.sleep(500);
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy lang Lang.Proxy.Teleport " + Main.lang.getString("Lang.Proxy.Teleport").replace(" ", "%20"));
									Thread.sleep(500);
									
									SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy addserver ~Lobby " + Main.config.getString("Settings.Server-IP") + " " + Main.config.getString("Settings.Lobby-Port") + " true");
									Thread.sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							for(Iterator<String> str = Main.config.getConfigurationSection("Servers").getKeys(false).iterator(); str.hasNext(); ) {
							    String item = str.next();
							    do {
							   		i++;
						    	} while (Main.Servers.keySet().contains(i));
							    
							    if (Main.SubServers.contains(item)) {
							    	i--;
							    } else {
							    	Main.SubServers.add(item);
							    	Main.PIDs.put(item, i);
							    	Main.Servers.put(i, new SubServer(Main.config.getBoolean("Servers." + item + ".enabled"), item, i, Main.config.getInt("Servers." + item + ".port"), 
							    			Main.config.getBoolean("Servers." + item + ".log"), Main.config.getBoolean("Servers." + item + ".use-shared-chat"), new File(Main.config.getRawString("Servers." + item + ".dir")),
							    			new Executable(Main.config.getRawString("Servers." + item + ".exec")), Main.config.getDouble("Servers." + item + ".stop-after"), false, Main));
							    }
							}

                            for(Iterator<String> str = Main.SubServers.iterator(); str.hasNext(); ) {
                                String item = str.next();
                                if (SubAPI.getSubServer(0).isRunning()) {
                                    SubAPI.getSubServer(0).sendCommandSilently("subconf@proxy addserver " + item + " " + Main.config.getString("Settings.Server-IP") + " " + SubAPI.getSubServer(item).Port + " " + SubAPI.getSubServer(item).SharedChat);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

							if (sender instanceof Player) {
								((Player) sender).sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Debug.Config-Reload"));
							}
							Bukkit.getLogger().info(Main.lprefix + Main.lang.getString("Lang.Debug.Config-Reload"));
						}
					}.runTaskAsynchronously(Main.Plugin);
				} else if (sender instanceof Player) {
					((Player) sender).sendMessage(ChatColor.RED + "You do not have permission to Reload this plugin.");
				}
			} else if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
				if (sender instanceof Player) {
					((Player) sender).sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + " " + Main.Plugin.getDescription().getName() + " v" + Main.Plugin.getDescription().getVersion() + ChatColor.GREEN + ChatColor.ITALIC + " © ME1312 EPIC 2015");
					((Player) sender).sendMessage(" ");
					((Player) sender).sendMessage(ChatColor.AQUA + " Project Page:" + ChatColor.ITALIC + " " + Main.Plugin.getDescription().getWebsite());
				} else {
					Bukkit.getLogger().info(Main.Plugin.getDescription().getName() + " v" + Main.Plugin.getDescription().getVersion() + " © ME1312 EPIC 2015");
					Bukkit.getLogger().info("Project Page: " + Main.Plugin.getDescription().getWebsite());
				}
			} else if (args[0].equalsIgnoreCase("help")) {
				if (sender instanceof Player) {
                    ((Player) sender).sendMessage(getHelp(true));
				} else {
                    for (Iterator<String> str = Arrays.<String>asList(getHelp(false)).iterator(); str.hasNext(); ) {
                        Bukkit.getLogger().info(str.next());
                    }
				}
			} else if ((sender instanceof Player) && ((Player) sender).hasPermission("SubServer.Command") && Main.config.getBoolean("Settings.GUI.Enabled")) {
				if (Main.SubServers.contains(args[0])) {
					new GUI((Player) sender, 0, args[0], Main);
				} else {
					((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + "Invalid Server Name");
				}
			} else {
				if (sender instanceof Player) {
					((Player) sender).sendMessage(ChatColor.RED + Main.lprefix + "Invalid Command Usage: /Subserver help");
				} else {
					Bukkit.getLogger().info("Invalid Command Usage: /Subserver help");
				}
			}
		} else {
			if ((sender instanceof Player) && ((Player) sender).hasPermission("SubServer.Command") && Main.config.getBoolean("Settings.GUI.Enabled")) {
                new GUI((Player) sender, 0, null, Main);
			} else {
				if (sender instanceof Player) {
                    ((Player) sender).sendMessage(getHelp(true));
				} else {
					for (Iterator<String> str = Arrays.<String>asList(getHelp(false)).iterator(); str.hasNext(); ) {
                        Bukkit.getLogger().info(str.next());
                    }
				}
			}
		}
		return true;
	}

    private String[] getHelp(boolean isPlayer) {
        return new String[]{
                ((isPlayer) ? ChatColor.AQUA.toString() : "") + "SubServers Command List:",
                ((isPlayer) ? ChatColor.AQUA.toString() : "") + "GUI: /SubServer [Server]",
                ((isPlayer) ? ChatColor.AQUA.toString() : "") + "Help: /SubServer Help",
                ((isPlayer) ? ChatColor.AQUA.toString() : "") + "Reload Plugin: /SubServer Reload",
                ((isPlayer) ? ChatColor.AQUA.toString() : "") + "Plugin Version: /SubServer Version",
                ((isPlayer) ? ChatColor.AQUA.toString() : "") + "Stop Server: /SubServer Stop <Server>",
                ((isPlayer) ? ChatColor.AQUA.toString() : "") + "Kill Server: /SubServer Kill <Server>",
                ((isPlayer) ? ChatColor.AQUA.toString() : "") + "Start Server: /SubServer Start <Server>",
                ((isPlayer) ? ChatColor.AQUA.toString() : "") + "Send Command: /SubServer Cmd <Server> <Command> " + ((isPlayer) ? ChatColor.ITALIC.toString() : "") + "[Args...]",
                ((isPlayer) ? // if
                        ((SubAPI.getSubServer(0).isRunning()) ? ChatColor.AQUA + "Teleport: /Go <Server> [Player]" : "")
                        : //    else
                        ((SubAPI.getSubServer(0).isRunning()) ? "Teleport: /Sub TP <Server> <Player>" : "")),
        };
    }
}
