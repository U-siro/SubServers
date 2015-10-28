package net.ME1312.SubServer.Executable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import net.ME1312.SubServer.SubAPI;
import org.apache.commons.lang3.StringEscapeUtils;

import net.ME1312.SubServer.Main;
import net.ME1312.SubServer.Libraries.Events.SubEvent;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Subserver Creator Class
 * 
 * @author ME1312
 *
 */
@SuppressWarnings("serial")
public class SubServer implements Serializable {
	public String Name;
	public int PID;
	public boolean Log;
    public boolean SharedChat;
	public boolean Temporary;
	public boolean Enabled;
	public int Port;
	
	protected Main Main;
	protected File Dir;
	protected Executable Exec;
	protected double StopAfter;
	
	private Process Process;
	private String StdIn;
	private SubServer Server = this;
	
	/** 
	 * Creates a SubServer
	 * 
	 * @param Enabled If the server is Enabled
	 * @param Name Server Name
	 * @param PID Server PID
	 * @param Port Server Port
	 * @param Log Toggle Console Log
     * @param SharedChat Toggle Shared Chat
	 * @param Dir Runtime Directory
	 * @param Exec Executable File
	 * @param StopAfter Stop After x Minutes
	 * @param Temporary Toggle Temporary Server Options
	 */
	public SubServer(Boolean Enabled, String Name, int PID, int Port, boolean Log, boolean SharedChat, File Dir, Executable Exec, double StopAfter, boolean Temporary, Main Main) {
		this.Enabled = Enabled;
		this.Name = Name;
		this.PID = PID;
		this.Port = Port;
		this.Log = Log;
        this.SharedChat = SharedChat;
		this.Temporary = Temporary;
		this.Dir = Dir;
		this.Exec = Exec;
		this.StopAfter = StopAfter;
		this.Main = Main;
	}
	
	private void start(boolean value) {
		if (value) {
			if (Name.equalsIgnoreCase("~Proxy") && Enabled) {
				Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {
					@Override
					public void run() {
						try {
							/**
							 * Process Creator,
							 * StreamGobbler Starter
							 */
							Process = Runtime.getRuntime().exec(Exec.toString(), null, Dir); //Whatever you want to execute
							Main.log.info(StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Debug", "Server-Logging-Start").getString()).replace("$Server$", "The Proxy").replace("$Shell$", Exec.toString()));
							final StreamGobbler read = new StreamGobbler(Process.getInputStream(), "OUTPUT", Log, Name, Main);
							read.start();
							final BufferedWriter cmd = new BufferedWriter(new OutputStreamWriter(Process.getOutputStream()));
							Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {
								public void run() {
									do {
										/**
										 * StdIn Functions
										 */
										if (StdIn != null) {
											  try {
												cmd.write(StdIn);
												cmd.newLine();
												cmd.flush();
											  } catch (IOException e) {
													Main.log.error(e.getStackTrace().toString());
											  }
								              StdIn = null;
										}
									} while (read.isAlive() == true);
								};
							}).submit(Main.Plugin);
							Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {
								@Override
								public void run() {
									try {
										sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport").getString().replace(" ", "%20")));
										Thread.sleep(500);
										sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Server-List " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Server-List").getString().replace(" ", "%20")));
										Thread.sleep(500);
										sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Player-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Player-Error").getString().replace(" ", "%20")));
										Thread.sleep(500);
										sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Config-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Config-Error").getString().replace(" ", "%20")));
										Thread.sleep(500);
										sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Permission-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Permission-Error").getString().replace(" ", "%20")));
										Thread.sleep(500);
										sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Offline-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Offline-Error").getString().replace(" ", "%20")));
										Thread.sleep(500);
										sendCommandSilently("subconf@proxy lang Lang.Commands.Teleport-Console-Error " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Commands", "Teleport-Console-Error").getString().replace(" ", "%20")));
										Thread.sleep(500);
									
										sendCommandSilently("subconf@proxy lang Lang.Proxy.Register-Server " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Register-Server").getString().replace(" ", "%20")));
										Thread.sleep(500);
										sendCommandSilently("subconf@proxy lang Lang.Proxy.Remove-Server " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Remove-Server").getString().replace(" ", "%20")));
										Thread.sleep(500);
										sendCommandSilently("subconf@proxy lang Lang.Proxy.Reset-Storage " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Reset-Storage").getString().replace(" ", "%20")));
										Thread.sleep(500);
                                        sendCommandSilently("subconf@proxy lang Lang.Proxy.Chat-Format " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Chat-Format").getString().replace(" ", "%20")));
                                        Thread.sleep(500);
										sendCommandSilently("subconf@proxy lang Lang.Proxy.Teleport " + StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Proxy", "Teleport").getString().replace(" ", "%20")));
										Thread.sleep(500);
										
										sendCommandSilently("subconf@proxy addserver ~Lobby " + Main.config.getNode("Settings", "Server-IP").getString() + " " + Main.config.getNode("Settings", "Lobby-Port").getString() + " true");
										Thread.sleep(500);
									} catch (InterruptedException e) {
										Main.log.error(e.getStackTrace().toString());
									}
									for(Iterator<String> str = Main.SubServers.iterator(); str.hasNext(); ) {
										String item = str.next();
										sendCommandSilently("subconf@proxy addserver " + item + " " + Main.config.getNode("Settings", "Server-IP").getString() + " " + SubAPI.getSubServer(item).Port + " " + SubAPI.getSubServer(item).SharedChat);
										try {
											Thread.sleep(500);
										} catch (InterruptedException e) {
											Main.log.error(e.getStackTrace().toString());
										}
									}
								};
							}).submit(Main.Plugin);
							try {
								Process.waitFor();
								SubAPI.executeEvent(SubEvent.Events.SubShellExitEvent, Server);
							} catch (InterruptedException e) {
								Main.log.error(e.getStackTrace().toString());
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
								Main.log.error(e.getStackTrace().toString());
							}
						} catch (IOException e) {
							Main.log.error(e.getStackTrace().toString());
						}
						
						/**
						 * Reset the Server's PID for future use
						 */
						Main.log.info(StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Debug", "Server-Logging-End").getString()).replace("$Server$", "Proxy"));
						Process = null;
						StdIn = null;
					};
				}).submit(Main.Plugin);
			} else if (Enabled) {
				Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {
					public void run() {
						try {
							/**
							 * Process Creator,
							 * StreamGobbler Starter
							 */
							Process = Runtime.getRuntime().exec(Exec.toString(), null, Dir); //Whatever you want to execute
							Main.log.info(StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Debug", "Server-Logging-Start").getString()).replace("$Server$", Name).replace("$Shell$", Exec.toString()));
							final StreamGobbler read = new StreamGobbler(Process.getInputStream(), "OUTPUT", Log, Name, Main);
							read.start();
							final BufferedWriter cmd = new BufferedWriter(new OutputStreamWriter(Process.getOutputStream()));
							Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {
								@Override
								public void run() {
									do {
										/**
										 * StdIn Functions
										 */
										if (StdIn != null) {
										  try {
											cmd.write(StdIn);
											cmd.newLine();
							              	cmd.flush();
										  } catch (IOException e) {
											Main.log.error(e.getStackTrace().toString());
										  }
							              StdIn = null;
										}
									} while (read.isAlive() == true);
								};
							}).submit(Main.Plugin);
							if (StopAfter > 0) {
								Main.game.getScheduler().createTaskBuilder().async().delay((long) ((StopAfter * 1000) * 60), TimeUnit.MILLISECONDS).execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        StdIn = "stop";
                                    };
								}).submit(Main.Plugin);
							}
							try {
								Process.waitFor();
								SubAPI.executeEvent(SubEvent.Events.SubShellExitEvent, Server);
							} catch (InterruptedException e) {
								Main.log.error(e.getStackTrace().toString());
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
								Main.log.error(e.getStackTrace().toString());
							}
						} catch (IOException e) {
							Main.log.error(e.getStackTrace().toString());
						}
						
						/**
						 * Reset the Server's PID for future use
						 */
						Main.log.info(StringEscapeUtils.unescapeJava(Main.lang.getNode("Lang", "Debug", "Server-Logging-End").getString()).replace("$Server$", Name));	
						Process = null;
						StdIn = null;
					};
				}).submit(Main.Plugin);
			}
		}
	}
	
	/**
	 * Outputs Server Name
	 */
	@Override
	public String toString() {
		return Name;
	}
	
	/**
	 * Waits for Shell to End
	 * 
	 * @throws InterruptedException
	 */
	public void waitFor() throws InterruptedException {
		Process.waitFor();
	}
	
	/**
	 * Starts a Subserver
	 */
	public boolean start() {
		try {
			if (SubAPI.executeEvent(SubEvent.Events.SubStartEvent, this, null)) {
				start(true);
				return true;
			} else {
				return false;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalStateException e) {
			Main.log.error(e.getStackTrace().toString());
			return false;
		}
	}
	
	/**
	 * Starts a Subserver
	 * 
	 * @param sender Player that sent this command
	 */
	public boolean start(final Player sender) {
		try {
			if (SubAPI.executeEvent(SubEvent.Events.SubStartEvent, this, sender)) {
				start(true);
				return true;
			} else {
				return false;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			Main.log.error(e.getStackTrace().toString());
			return false;
		}
	}
	
	/**
	 * Sends Command to Server
	 * @param cmd The Command to send
	 */
	public boolean sendCommand(String cmd) {
		try {
			if (SubAPI.executeEvent(SubEvent.Events.SubRunCommandEvent, this, null, cmd)) {
				StdIn = cmd;
				return true;
			} else {
				return false;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			Main.log.error(e.getStackTrace().toString());
			return false;
		}
	}
	
	/**
	 * Sends Command to Server
	 * @param sender Player that sent this command
	 * @param cmd The Command to send
	 */
	public boolean sendCommand(Player sender, String cmd) {
		try {
			if (SubAPI.executeEvent(SubEvent.Events.SubRunCommandEvent, this, sender, cmd)) {
				StdIn = cmd;
				return true;
			} else {
				return false;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			Main.log.error(e.getStackTrace().toString());
			return false;
		}
	}
	
	/**
	 * Sends Command to Server without executing the Event
	 * @param cmd The Command to send
	 */
	public void sendCommandSilently(String cmd) {
		StdIn = cmd;
	}
	
	/**
	 * Stops the Server
	 */
	public boolean stop() {
		try {
			if (SubAPI.executeEvent(SubEvent.Events.SubStopEvent, this, null)) {
				if (Name.equalsIgnoreCase("~Proxy")) {
					StdIn = "end";
				} else {
					StdIn = "stop";
				}
				return true;
			} else {
				return false;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			Main.log.error(e.getStackTrace().toString());
			return false;
		}
	}
	
	/**
	 * Stops a Server
	 * @param sender Player that sent this command
	 */
	public boolean stop(Player sender) {
		try {
			if (SubAPI.executeEvent(SubEvent.Events.SubStopEvent, this, sender)) {
				if (Name.equalsIgnoreCase("~Proxy")) {
					StdIn = "end";
				} else {
					StdIn = "stop";
				}
				return true;
			} else {
				return false;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			Main.log.error(e.getStackTrace().toString());
			return false;
		}
	}
	
	/**
	 * Terminates the Server
	 */
	public boolean terminate() {
		try {
			if (SubAPI.executeEvent(SubEvent.Events.SubStopEvent, this, null)) {
				Process.destroy();
				return true;
			} else {
				return false;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			Main.log.error(e.getStackTrace().toString());
			return false;
		}
	}
	
	/**
	 * Terminates the Server
	 * @param sender Player that sent this command
	 */
	public boolean terminate(Player sender) {
		try {
			if (SubAPI.executeEvent(SubEvent.Events.SubStopEvent, this, sender)) {
				Process.destroy();
				return true;
			} else {
				return false;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			Main.log.error(e.getStackTrace().toString());
			return false;
		}
	}
	
	/**
	 * Sends player to SubServer if Proxy and SubServer is Online
	 * 
	 * @param player
	 */
	public void sendPlayer(Player player) {
		if (SubAPI.getSubServer(0).isRunning()) {
			SubAPI.getSubServer("~Proxy").sendCommandSilently("subconf@proxy sendplayer " + player.getName() + " " + Name);
		}
	}
	
	/**
	 * Test if a Server is Running
	 *
	 * @return True if Server is Running, False if Offline
	 */
	public boolean isRunning() {
		boolean running = false;
		
		if (Process != null) {
			running = true;
		}
		
		return running;
	}
}
