package net.ME1312.SubServer.GUI;

import java.util.Arrays;
import java.util.Iterator;

import net.ME1312.SubServer.SubAPI;
import net.ME1312.SubServer.Main;
import net.ME1312.SubServer.Libraries.Version.Version;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * GUI Renderer Class
 * 
 * @author ME1312
 *
 */
public class GUI implements Listener {
	protected boolean closeWindow = false;
	
	private ItemStack block = null;
	private ItemMeta  blockMeta = null;
	private Inventory inv = null;
	private Main Main;
	
	protected GUI(Main Main) {
		this.Main = Main;
	}
	
	/**
	 * Opens The GUI
	 * 
	 * @param player The Player Opening the GUI
	 * @param page The Page Number
	 * @param server The Server Name (if not null, opens Selection Window)
	 */
	
	@SuppressWarnings("deprecation")
	public GUI(Player player, int page, String server, Main Main) {
		this.Main = Main;
		inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-List-Title").replace("$Int$", Integer.toString(page + 1)));
		
		if (server == null) {
			int i = 0;
			int min = (page * 18);
			int max = (min + 17);
			block = null;
			blockMeta = null;
			for(Iterator<String> str = Main.SubServers.iterator(); str.hasNext(); ) {
			    String item = str.next();
			    if (SubAPI.getSubServer(item).Enabled) {
			    	if (Main.SubServers.indexOf(item) >= min && Main.SubServers.indexOf(item) <= max) {
			    		if (SubAPI.getSubServer(item).Temporary) {
			    			block = new ItemStack(289);
			    			blockMeta = block.getItemMeta();
			    			blockMeta.setDisplayName(ChatColor.YELLOW + item);
			    			blockMeta.setLore(Arrays.asList(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Online"), ChatColor.GRAY + Main.lang.getString("Lang.GUI.Temp-Server")));
			    			block.setItemMeta(blockMeta);
			    		} else if (SubAPI.getSubServer(item).isRunning()) {
			    			block = new ItemStack(Material.GLOWSTONE_DUST);
			    			blockMeta = block.getItemMeta();
			    			blockMeta.setDisplayName(ChatColor.YELLOW + item);
			    			blockMeta.setLore(Arrays.asList(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Online")));
			    			block.setItemMeta(blockMeta);
			    		} else {
			    			block = new ItemStack(Material.REDSTONE);
			    			blockMeta = block.getItemMeta();
			    			blockMeta.setDisplayName(ChatColor.YELLOW + item);
			    			blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Offline")));
			    			block.setItemMeta(blockMeta);
			    		}
			    		inv.setItem(i, block);
			    		block = null;
			    		blockMeta = null;
			    		i++;
			    	}
			    }
			}
			
			block = new ItemStack(Material.ENCHANTED_BOOK);
	    	blockMeta = block.getItemMeta();
	    	blockMeta.setDisplayName(ChatColor.GRAY + Main.Plugin.getName() + " v" + Main.Plugin.getDescription().getVersion());
	    	blockMeta.setLore(Arrays.asList("\u00A9 ME1312 EPIC 2015", "", ChatColor.DARK_AQUA + Main.lang.getString("Lang.GUI.Sub-Help-Book").split("\\|\\|\\|")[0], ChatColor.DARK_AQUA + Main.lang.getString("Lang.GUI.Sub-Help-Book").split("\\|\\|\\|")[1]));
	    	block.setItemMeta(blockMeta);
	    	inv.setItem(18, block);
	    	block = null;
	    	blockMeta = null;
	    	
	    	if (Main.SubServers.size() > max) {
	    		block = new ItemStack(Material.IRON_INGOT);
	    		blockMeta = block.getItemMeta();
	    		blockMeta.setDisplayName(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Next"));
	    		block.setItemMeta(blockMeta);
	    		inv.setItem(23, block);
	    		block = null;
	    		blockMeta = null;
	    	}
	    	
	    	if (Main.config.getBoolean("Proxy.enabled")) {
				block = new ItemStack(Material.EMERALD);
				blockMeta = block.getItemMeta();
				blockMeta.setDisplayName(ChatColor.YELLOW + "~Proxy");
				if (SubAPI.getSubServer(0).isRunning()) {
					blockMeta.setLore(Arrays.asList(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Online")));
				} else {
					blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Offline")));
				}
				block.setItemMeta(blockMeta);
				inv.setItem(22, block);
			}
	    	
	    	if (min != 0) {
	    		block = new ItemStack(Material.IRON_INGOT);
	    		blockMeta = block.getItemMeta();
	    		blockMeta.setDisplayName(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Back"));
	    		block.setItemMeta(blockMeta);
	    		inv.setItem(21, block);
	    		block = null;
	    		blockMeta = null;
	    	}
	    	
	    	if (Main.ServerCreator == null || !Main.ServerCreator.isRunning()) {
	    		if (Main.MCVersion.compareTo(new Version("1.8")) >= 0) {
	    			block = new ItemStack(168, 1, (short) 1);
	    		} else {
	    			block = new ItemStack(Material.DIAMOND_BLOCK);
	    		}
	    		blockMeta = block.getItemMeta();
	    		blockMeta.setDisplayName(ChatColor.AQUA + Main.lang.getString("Lang.GUI.Create-Server"));
	    		if (!player.hasPermission("SubServer.Command.create")) blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Permission-Error")));
	    		block.setItemMeta(blockMeta);
	    		inv.setItem(25, block);
	    		block = null;
	    		blockMeta = null;
	    	} else {
	    		block = new ItemStack(Material.GOLD_BLOCK);
	    		blockMeta = block.getItemMeta();
	    		blockMeta.setDisplayName(ChatColor.YELLOW + Main.lang.getString("Lang.GUI.Create-Server-Busy"));
	    		block.setItemMeta(blockMeta);
	    		inv.setItem(25, block);
	    		block = null;
	    		blockMeta = null;
	    	}
	    	
	    	if (Main.MCVersion.compareTo(new Version("1.8")) >= 0) {
	    		block = new ItemStack(166);
	    	} else {
	    		block = new ItemStack(Material.REDSTONE_BLOCK);
	    	}
	    	blockMeta = block.getItemMeta();
	    	blockMeta.setDisplayName(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Exit"));
	    	block.setItemMeta(blockMeta);
	    	inv.setItem(26, block);
	    	block = null;
	    	blockMeta = null;
	    	
	    	player.openInventory(inv);
			inv = null;
		} else if (Main.SubServers.contains(server) || server.equalsIgnoreCase("~Proxy")) {
			openServerWindow(player, server);
		}
	}
	
	/**
	 * Opens Selection Window
	 * 
	 * @param player The Player Opening The GUI
	 * @param server The Server Name (Required)
	 */
	@SuppressWarnings("deprecation")
	protected void openServerWindow(Player player, String server) {
		if (server != null) {
			inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW + server);

			if (SubAPI.getSubServer(server).isRunning()) {
				block = new ItemStack(Material.EMERALD_BLOCK);
		    	blockMeta = block.getItemMeta();
		    	blockMeta.setDisplayName(ChatColor.GRAY + Main.lang.getString("Lang.GUI.Start"));
		    	if (!player.hasPermission("SubServer.Command.start." + server) && !player.hasPermission("SubServer.Command.start.*")) {
		    		blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Already-Running"), ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Permission-Error")));
		    	} else {
		    		blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Already-Running")));
		    	}
		    	block.setItemMeta(blockMeta);
		    	inv.setItem(1, block);
		    	block = null;
		    	blockMeta = null;
		    	
		    	block = new ItemStack(Material.REDSTONE_BLOCK);
		    	blockMeta = block.getItemMeta();
		    	blockMeta.setDisplayName(ChatColor.RED + Main.lang.getString("Lang.GUI.Stop"));
		    	if (!player.hasPermission("SubServer.Command.stop." + server) && !player.hasPermission("SubServer.Command.stop.*")) {
		    		blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Permission-Error")));
		    	}
		    	block.setItemMeta(blockMeta);
		    	inv.setItem(3, block);
		    	block = null;
		    	blockMeta = null;
		    	
		    	block = new ItemStack(Material.GRAVEL);
		    	blockMeta = block.getItemMeta();
		    	blockMeta.setDisplayName(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Terminate"));
		    	if (!player.hasPermission("SubServer.Command.kill." + server) && !player.hasPermission("SubServer.Command.kill.*")) {
		    		blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Permission-Error")));
		    	}
		    	block.setItemMeta(blockMeta);
		    	inv.setItem(5, block);
		    	block = null;
		    	blockMeta = null;
		    	
		    	block = new ItemStack(356);
		    	blockMeta = block.getItemMeta();
		    	blockMeta.setDisplayName(ChatColor.AQUA + Main.lang.getString("Lang.GUI.Send-CMD"));
		    	if (!player.hasPermission("SubServer.Command.send." + server) && !player.hasPermission("SubServer.Command.send.*")) {
		    		blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Permission-Error")));
		    	}
		    	block.setItemMeta(blockMeta);
		    	inv.setItem(7, block);
		    	block = null;
		    	blockMeta = null;
		    	
		    	if (!SubAPI.getSubServer(server).Temporary) {
		    		block = new ItemStack(Material.GLOWSTONE_DUST);
		    		blockMeta = block.getItemMeta();
		    		blockMeta.setDisplayName(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Online"));
		    		if (SubAPI.getSubServer(0).isRunning() && (player.hasPermission("SubServer.Command.teleport." + server) || player.hasPermission("SubServer.Command.teleport.*")) && !server.equalsIgnoreCase("~Proxy")) {
		    			blockMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to Teleport"));
		    		}
		    		block.setItemMeta(blockMeta);
		    		inv.setItem(22, block);
		    		block = null;
		    		blockMeta = null;
		    	} else {
		    		block = new ItemStack(289);
		    		blockMeta = block.getItemMeta();
		    		blockMeta.setDisplayName(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Online"));
		    		if (SubAPI.getSubServer(0).isRunning() && (player.hasPermission("SubServer.Command.teleport." + server) || player.hasPermission("SubServer.Command.teleport.*")) && !server.equalsIgnoreCase("~Proxy")) {
		    			blockMeta.setLore(Arrays.asList(ChatColor.GRAY + Main.lang.getString("Lang.GUI.Temp-Server"), ChatColor.GRAY + "Click to Teleport"));
		    		} else {
		    			blockMeta.setLore(Arrays.asList(ChatColor.GRAY + Main.lang.getString("Lang.GUI.Temp-Server")));
		    		}
		    		block.setItemMeta(blockMeta);
		    		inv.setItem(22, block);
		    		block = null;
		    		blockMeta = null;
		    	}
			} else {
				if (!SubAPI.getSubServer(server).Temporary) {
					block = new ItemStack(Material.EMERALD_BLOCK);
					blockMeta = block.getItemMeta();
					blockMeta.setDisplayName(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Start"));
					if (!player.hasPermission("SubServer.Command.start." + server) && !player.hasPermission("SubServer.Command.start.*")) {
						blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Permission-Error")));
					}
					block.setItemMeta(blockMeta);
					inv.setItem(1, block);
					block = null;
					blockMeta = null;
				} else {
					block = new ItemStack(Material.EMERALD_BLOCK);
					blockMeta = block.getItemMeta();
					blockMeta.setDisplayName(ChatColor.GRAY + Main.lang.getString("Lang.GUI.Start"));
					if (player.hasPermission("SubServer.Command.start." + server) && player.hasPermission("SubServer.Command.start.*")) {
						blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Removed")));
					} else {
						blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Removed"), ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Permission-Error")));
					}
					block.setItemMeta(blockMeta);
					inv.setItem(1, block);
					block = null;
					blockMeta = null;
				}
		    	
		    	block = new ItemStack(Material.REDSTONE_BLOCK);
		    	blockMeta = block.getItemMeta();
		    	blockMeta.setDisplayName(ChatColor.GRAY + Main.lang.getString("Lang.GUI.Stop"));
		    	if (!player.hasPermission("SubServer.Command.stop." + server) && !player.hasPermission("SubServer.Command.stop.*")) {
		    		blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Not-Running"), ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Permission-Error")));
		    	} else {
		    		blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Not-Running")));
		    	}
		    	block.setItemMeta(blockMeta);
		    	inv.setItem(3, block);
		    	block = null;
		    	blockMeta = null;
		    	
		    	block = new ItemStack(Material.GRAVEL);
		    	blockMeta = block.getItemMeta();
		    	blockMeta.setDisplayName(ChatColor.GRAY + Main.lang.getString("Lang.GUI.Terminate"));
		    	if (!player.hasPermission("SubServer.Command.kill." + server) && !player.hasPermission("SubServer.Command.kill.*")) {
		    		blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Not-Running"), ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Permission-Error")));
		    	} else {
		    		blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Not-Running")));
		    	}
		    	block.setItemMeta(blockMeta);
		    	inv.setItem(5, block);
		    	block = null;
		    	blockMeta = null;
		    	
		    	block = new ItemStack(356);
	    		blockMeta = block.getItemMeta();
	    		blockMeta.setDisplayName(ChatColor.GRAY + Main.lang.getString("Lang.GUI.Send-CMD"));
	    		if (!player.hasPermission("SubServer.Command.send." + server) && !player.hasPermission("SubServer.Command.send.*")) {
	    			blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Not-Running"), ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Permission-Error")));
	    		} else {
	    			blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Not-Running")));
	    		}
	    		block.setItemMeta(blockMeta);
	    		inv.setItem(7, block);
	    		block = null;
	    		blockMeta = null;
	    		
		    	if (!SubAPI.getSubServer(server).Temporary) {
		    		block = new ItemStack(Material.REDSTONE);
		    		blockMeta = block.getItemMeta();
		    		blockMeta.setDisplayName(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Offline"));
		    		block.setItemMeta(blockMeta);
		    		inv.setItem(22, block);
		    		block = null;
		    		blockMeta = null;
		    	} else {
		    		block = new ItemStack(289);
		    		blockMeta = block.getItemMeta();
		    		blockMeta.setDisplayName(ChatColor.GRAY + Main.lang.getString("Lang.GUI.Temp-Server"));
		    		blockMeta.setLore(Arrays.asList(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Removed")));
		    		block.setItemMeta(blockMeta);
		    		inv.setItem(22, block);
		    		block = null;
		    		blockMeta = null;
		    	}
			}
			block = new ItemStack(Material.ENCHANTED_BOOK);
	    	blockMeta = block.getItemMeta();
	    	blockMeta.setDisplayName(ChatColor.GRAY + Main.Plugin.getName() + " v" + Main.Plugin.getDescription().getVersion());
	    	blockMeta.setLore(Arrays.asList("\u00A9 ME1312 EPIC 2015", "", ChatColor.DARK_AQUA + Main.lang.getString("Lang.GUI.Sub-Help-Book").split("\\|\\|\\|")[0], ChatColor.DARK_AQUA + Main.lang.getString("Lang.GUI.Sub-Help-Book").split("\\|\\|\\|")[1]));
	    	block.setItemMeta(blockMeta);
	    	inv.setItem(18, block);
	    	block = null;
	    	blockMeta = null;
			
			block = new ItemStack(Material.IRON_INGOT);
	    	blockMeta = block.getItemMeta();
	    	blockMeta.setDisplayName(ChatColor.YELLOW + Main.lang.getString("Lang.GUI.Back"));
	    	block.setItemMeta(blockMeta);
	    	inv.setItem(25, block);
	    	block = null;
	    	blockMeta = null;
	    	
	    	if (Main.MCVersion.compareTo(new Version("1.8")) >= 0) {
	    		block = new ItemStack(166);
	    	} else {
	    		block = new ItemStack(Material.REDSTONE_BLOCK);
	    	}
	    	blockMeta = block.getItemMeta();
	    	blockMeta.setDisplayName(ChatColor.DARK_RED + Main.lang.getString("Lang.GUI.Exit"));
	    	block.setItemMeta(blockMeta);
	    	inv.setItem(26, block);
	    	block = null;
	    	blockMeta = null;
	    	
	    	player.openInventory(inv);
	    	inv = null;
		}
	}
	
	protected static boolean stopLoader = false;
	@SuppressWarnings("deprecation")
	private static ItemStack invBlock = new ItemStack(168, 1, (short) 1);
	
	/**
	 * Opens Loader Animation
	 * 
	 * NOTE: Set stopLoader to false to stop loading.
	 * 
	 * @param player Player opening the GUI
	 * @param server Server Name
	 * @param done Callback Method Name
	 */
	@SuppressWarnings("deprecation")
	protected void openLoader(final Player player, final String args, final String done) {
		final Inventory inventory = Bukkit.createInventory(null, 9, Main.lang.getString("Lang.GUI.Loading"));
		
		if (Main.MCVersion.compareTo(new Version("1.8")) < 0) invBlock = new ItemStack(35, 1, (short) 9);
    	ItemMeta invBlockMeta = invBlock.getItemMeta();
    	invBlockMeta.setDisplayName("");
    	invBlock.setItemMeta(invBlockMeta);
    	
		final ItemStack Block = new ItemStack(Material.DIAMOND_BLOCK);
    	ItemMeta BlockMeta = Block.getItemMeta();
    	BlockMeta.setDisplayName("");
    	Block.setItemMeta(BlockMeta);
    	
    	inventory.setItem(0, invBlock);
    	inventory.setItem(1, invBlock);
    	inventory.setItem(2, invBlock);
    	inventory.setItem(3, invBlock);
    	inventory.setItem(4, invBlock);
    	inventory.setItem(5, invBlock);
    	inventory.setItem(6, invBlock);
    	inventory.setItem(7, invBlock);
    	inventory.setItem(8, invBlock);
    	
    	new BukkitRunnable() {
    		@Override
			public void run() {
				do {
					try {
						Thread.sleep(125);
						player.closeInventory();
						inventory.setItem(0, Block);
						player.openInventory(inventory);
						if (stopLoader == false) {
							Thread.sleep(75);
							player.closeInventory();
							inventory.setItem(1, Block);
							player.openInventory(inventory);
							if (stopLoader == false) {
								Thread.sleep(75);
								player.closeInventory();
								inventory.setItem(2, Block);
								player.openInventory(inventory);
								if (stopLoader == false) {
									Thread.sleep(75);
									player.closeInventory();
									inventory.setItem(0, invBlock);
									inventory.setItem(3, Block);
									player.openInventory(inventory);
									if (stopLoader == false) {
										Thread.sleep(75);
										player.closeInventory();
										inventory.setItem(1, invBlock);
										inventory.setItem(4, Block);
										player.openInventory(inventory);
										if (stopLoader == false) {
											Thread.sleep(75);
											player.closeInventory();
											inventory.setItem(2, invBlock);
											inventory.setItem(5, Block);
											player.openInventory(inventory);
											if (stopLoader == false) {
												Thread.sleep(75);
												player.closeInventory();
												inventory.setItem(3, invBlock);
												inventory.setItem(6, Block);
												player.openInventory(inventory);
												if (stopLoader == false) {
													Thread.sleep(75);
													player.closeInventory();
													inventory.setItem(4, invBlock);
													inventory.setItem(7, Block);
													player.openInventory(inventory);
													if (stopLoader == false) {
														Thread.sleep(75);
														player.closeInventory();
														inventory.setItem(5, invBlock);
														inventory.setItem(8, Block);
														player.openInventory(inventory);
														if (stopLoader == false) {
															Thread.sleep(75);
															player.closeInventory();
															inventory.setItem(6, invBlock);
															player.openInventory(inventory);
															if (stopLoader == false) {
																Thread.sleep(75);
																player.closeInventory();
																inventory.setItem(7, invBlock);
																player.openInventory(inventory);
																if (stopLoader == false) {
																	Thread.sleep(75);
																	player.closeInventory();
																	inventory.setItem(8, invBlock);
																	player.openInventory(inventory);
																	if (stopLoader == false) Thread.sleep(125);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (stopLoader == false);
				
				stopLoader = false;
				player.closeInventory();
				if (done.equalsIgnoreCase("openServerWindow")) {
					new GUI(player, 0, args, Main);
				} else if (done.equalsIgnoreCase("openSelectionWindow")) {
					new GUI(player, Integer.parseInt(args), null, Main);
				}
			}
		}.runTaskAsynchronously(Main.Plugin);
	}
	
	/**
	 * 
	 * 
	 * @param player Player Opening GUI
	 * @param server Server Name
	 */
	protected void openSentCommand(Player player, String server) {
		inv = Bukkit.createInventory(null, 18, ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Success") + ChatColor.YELLOW + server);
		block = new ItemStack(Material.EMERALD_BLOCK);
    	blockMeta = block.getItemMeta();
    	blockMeta.setDisplayName(ChatColor.GREEN + Main.lang.getString("Lang.GUI.OK"));
    	block.setItemMeta(blockMeta);
    	inv.setItem(5, block);
    	inv.setItem(4, block);
    	inv.setItem(3, block);
    	inv.setItem(14, block);
    	inv.setItem(13, block);
    	inv.setItem(12, block);
		player.openInventory(inv);
		block = null;
		blockMeta = null;
		inv = null;
	}
	
	@SuppressWarnings("deprecation")
	protected void openMojangAgreement(Player player) {
		inv = Bukkit.createInventory(null, 18, Main.lang.getString("Lang.Create-Server.Mojang-Agreement"));
		block = new ItemStack(Material.EMERALD_BLOCK);
		blockMeta = block.getItemMeta();
		blockMeta.setDisplayName(ChatColor.GREEN + Main.lang.getString("Lang.Create-Server.Mojang-Agreement-Accept"));
		block.setItemMeta(blockMeta);
		inv.setItem(0, block);
		inv.setItem(1, block);
		inv.setItem(2, block);
		inv.setItem(9, block);
		inv.setItem(10, block);
		inv.setItem(11, block);
		block = null;
		blockMeta = null;
		
		if (Main.MCVersion.compareTo(new Version("1.8")) >= 0) {
    		block = new ItemStack(168, 1, (short) 1);
    	} else {
    		block = new ItemStack(Material.DIAMOND_BLOCK);
    	}
    	blockMeta = block.getItemMeta();
    	blockMeta.setDisplayName(ChatColor.AQUA + Main.lang.getString("Lang.Create-Server.Mojang-Agreement-Link"));
    	block.setItemMeta(blockMeta);
    	inv.setItem(4, block);
    	inv.setItem(13, block);
    	block = null;
		blockMeta = null;
		
		block = new ItemStack(Material.REDSTONE_BLOCK);
    	blockMeta = block.getItemMeta();
    	blockMeta.setDisplayName(ChatColor.RED + Main.lang.getString("Lang.Create-Server.Mojang-Agreement-Decline"));
    	block.setItemMeta(blockMeta);
    	inv.setItem(6, block);
    	inv.setItem(7, block);
    	inv.setItem(8, block);
    	inv.setItem(15, block);
    	inv.setItem(16, block);
    	inv.setItem(17, block);
    	block = null;
		blockMeta = null;
		
		player.openInventory(inv);
		inv = null;
	}
	
	protected void openServerTypeSelector(Player player) {
		inv = Bukkit.createInventory(null, 9, Main.lang.getString("Lang.Create-Server.Server-Type"));
		block = new ItemStack(Material.WATER_BUCKET);
		blockMeta = block.getItemMeta();
		blockMeta.setDisplayName(ChatColor.GRAY + "Spigot");
		block.setItemMeta(blockMeta);
		inv.setItem(2, block);
		block = null;
		blockMeta = null;
		
		block = new ItemStack(Material.LAVA_BUCKET);
		blockMeta = block.getItemMeta();
		blockMeta.setDisplayName(ChatColor.GRAY + "Bukkit");
		block.setItemMeta(blockMeta);
		inv.setItem(4, block);
		block = null;
		blockMeta = null;
		
		block = new ItemStack(Material.BEACON);
		blockMeta = block.getItemMeta();
		blockMeta.setDisplayName(ChatColor.GRAY + "Vanilla");
		block.setItemMeta(blockMeta);
		inv.setItem(6, block);
		block = null;
		blockMeta = null;
		
		player.openInventory(inv);
		inv = null;
	}
}
