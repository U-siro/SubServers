package net.ME1312.SubServer.GUI;

import net.ME1312.SubServer.API;
import net.ME1312.SubServer.Main;
import net.ME1312.SubServer.Executable.SubServerCreator;
import net.ME1312.SubServer.Executable.SubServerCreator.ServerTypes;
import net.ME1312.SubServer.Executable.Executable;
import net.ME1312.SubServer.Libraries.Version.Version;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * GUI Listener
 *
 * @author ME1312
 *
 */
@SuppressWarnings("deprecation")
public class GUIListener implements Listener {
    protected boolean chatEnabled = true;
    protected String chatText = "";
    private Main Main;

    public GUIListener(Main Main) {
        this.Main = Main;
    }

    /**
     * GUI Trigger Item Listener
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action a = event.getAction();
        ItemStack is = event.getItem();

        if ((a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) && is != null && is.getType() == new ItemStack(Main.config.getBlockID("Settings.GUI.Trigger-Item")[0], 1, (short) Main.config.getBlockID("Settings.GUI.Trigger-Item")[1]).getType() && event.getPlayer().hasPermission("SubServer.Command") && Main.config.getBoolean("Settings.GUI.Enabled")) {
            new GUI(event.getPlayer(), 0, null, Main);
            event.setCancelled(true);
        }
    }

    /**
     * Server Selector Listener
     */
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().getName().contains(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-List-Title").replace("$Int$", ""))) {
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && event.getCurrentItem().hasItemMeta()) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED.toString() + Main.lang.getString("Lang.GUI.Exit"))) {
                    player.closeInventory();
                } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN.toString() + Main.lang.getString("Lang.GUI.Back"))) {
                    new GUI(player, (Integer.parseInt(event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN.toString() + Main.lang.getString("Lang.GUI.Server-List-Title").replace("$Int$", ""), "")) - 2), null, Main);
                } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN.toString() + Main.lang.getString("Lang.GUI.Next"))) {
                    new GUI(player, Integer.parseInt(event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN.toString() + Main.lang.getString("Lang.GUI.Server-List-Title").replace("$Int$", ""), "")), null, Main);
                } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA.toString() + Main.lang.getString("Lang.GUI.Create-Server"))) {
                    if (player.hasPermission("SubServer.Command.create")) {
                        new GUI(Main).openMojangAgreement(player);
                    } else {
                        player.sendMessage("Fail!");
                    }
                } else if (!event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY + Main.Plugin.getDescription().getName() + " v" + Main.Plugin.getDescription().getVersion())) {
                    new GUI(player, 0, event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.YELLOW.toString(), ""), Main);
                }
            }
            event.setCancelled(true);
        }

        /**
         * Server Editor Listener
         */
        if (event.getInventory().getName().contains(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title"))) {
            final Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && event.getCurrentItem().hasItemMeta()) {
                String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
                if ((ChatColor.DARK_GREEN.toString() + Main.lang.getString("Lang.GUI.Start")).equals(displayName)) {
                    if (player.hasPermission("SubServer.Command.start.*") || player.hasPermission("SubServer.Command.start." + event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, ""))) {
                        API.getSubServer(event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, "")).start((Player)event.getWhoClicked());
                        player.closeInventory();
                        new GUI(Main).openLoader(player, event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, ""), "openServerWindow");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                GUI.stopLoader = true;
                            }
                        }.runTaskAsynchronously(Main.Plugin);
                    }
                } else if ((ChatColor.RED.toString() + Main.lang.getString("Lang.GUI.Stop")).equals(displayName)) {
                    if (player.hasPermission("SubServer.Command.stop.*") || player.hasPermission("SubServer.Command.stop." + event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, ""))) {
                        final boolean stopped = API.getSubServer(event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, "")).stop((Player)event.getWhoClicked());
                        player.closeInventory();
                        new GUI(Main).openLoader(player, event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, ""), "openServerWindow");
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                try {
                                    if (stopped) Main.Servers.get(Main.PIDs.get(event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, ""))).waitFor();
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                GUI.stopLoader = true;
                            }
                        }.runTaskAsynchronously(Main.Plugin);
                    }
                } else if ((ChatColor.DARK_RED.toString() + Main.lang.getString("Lang.GUI.Terminate")).equals(displayName)) {
                    if (player.hasPermission("SubServer.Command.kill.*") || player.hasPermission("SubServer.Command.kill." + event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, ""))) {
                        API.getSubServer(event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, "")).terminate((Player)event.getWhoClicked());
                        player.closeInventory();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                new GUI(player, 0, event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, ""), Main);
                            }
                        }.runTaskAsynchronously(Main.Plugin);
                    }
                } else if ((ChatColor.YELLOW.toString() + Main.lang.getString("Lang.GUI.Back")).equals(displayName)) {
                    player.closeInventory();
                    int i = (int) Math.floor(Main.SubServers.indexOf(event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, "")) / 18);
                    new GUI(player, i, null, Main);
                } else if ((ChatColor.DARK_RED.toString() + Main.lang.getString("Lang.GUI.Exit")).equals(displayName)) {
                    player.closeInventory();
                } else if ((ChatColor.AQUA.toString() + Main.lang.getString("Lang.GUI.Send-CMD")).equals(displayName)) {
                    if (player.hasPermission("SubServer.Command.send.*") || player.hasPermission("SubServer.Command.send." + event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, ""))) {
                        player.closeInventory();
                        chatEnabled = false;
                        player.sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.GUI.Enter-CMD"));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                try {
                                    do {
                                        Thread.sleep(25);
                                    } while (chatEnabled == false);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                API.getSubServer(event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, "")).sendCommand((Player)event.getWhoClicked(), chatText);
                                chatText = "";
                                new GUI(Main).openSentCommand(player, event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, ""));
                            }
                        }.runTaskAsynchronously(Main.Plugin);
                    }
                } else if ((ChatColor.DARK_GREEN.toString() + Main.lang.getString("Lang.GUI.Online")).equals(displayName)) {
                    String server = event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Server-Admin-Title") + ChatColor.YELLOW, "");
                    if (API.getSubServer(0).isRunning() && (player.hasPermission("SubServer.Command.teleport." + server) || player.hasPermission("SubServer.Command.teleport.*")) && !server.equalsIgnoreCase("~Proxy")) {
                        player.closeInventory();
                        API.getSubServer(0).sendCommandSilently("subconf@proxy sendplayer " + player.getName() + " " + server);
                        player.sendMessage(ChatColor.AQUA + Main.lprefix +  Main.lang.getString("Lang.Commands.Teleport"));
                    }
                }
            }
            event.setCancelled(true);
        }

        /**
         * Other Listeners
         */
        if (event.getInventory().getName().contains(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Success"))) {
            final Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && event.getCurrentItem().hasItemMeta()) {
                player.closeInventory();
                new GUI(player, 0, event.getClickedInventory().getName().replace(ChatColor.DARK_GREEN + Main.lang.getString("Lang.GUI.Success") + ChatColor.YELLOW, ""), Main);
            }
            event.setCancelled(true);
        }
        if (event.getInventory().getName().equals(Main.lang.getString("Lang.GUI.Loading"))) {
            event.setCancelled(true);
        }

        /**
         * CreateServer Listeners
         */

        if (event.getInventory().getName().contains(Main.lang.getString("Lang.Create-Server.Mojang-Agreement"))) {
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && event.getCurrentItem().hasItemMeta()) {
                String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
                if (displayName.equals(ChatColor.GREEN + Main.lang.getString("Lang.Create-Server.Mojang-Agreement-Accept"))) {
                    new GUI(Main).openServerTypeSelector(player);

                } else if (displayName.equals(ChatColor.AQUA + Main.lang.getString("Lang.Create-Server.Mojang-Agreement-Link"))) {
                    player.closeInventory();
                    player.sendMessage(ChatColor.AQUA + Main.lprefix + Main.lang.getString("Lang.Create-Server.Mojang-Agreement-Link-Message"));
                    player.sendMessage(ChatColor.AQUA + "https://account.mojang.com/documents/minecraft_eula");

                } else if (displayName.equals(ChatColor.RED + Main.lang.getString("Lang.Create-Server.Mojang-Agreement-Decline"))) {
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Create-Server.Mojang-Agreement-Decline-Message"));
                }
            }
            event.setCancelled(true);
        }

        if (event.getInventory().getName().contains(Main.lang.getString("Lang.Create-Server.Server-Type"))) {
            final Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && event.getCurrentItem().hasItemMeta()) {
                final ServerTypes Type = ServerTypes.valueOf(event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GRAY.toString(), "").toLowerCase());
                final String Jar = event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GRAY.toString(), "") + ".jar";
                player.closeInventory();

                chatEnabled = false;
                player.sendMessage(ChatColor.YELLOW + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Version"));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            do {
                                Thread.sleep(25);
                            } while (chatEnabled == false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Version Version = new Version(chatText);
                        if (Version.compareTo(new Version("1.8")) < 0) {
                            player.sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Version-Unsupported"));
                        } else {
                            player.sendMessage(ChatColor.YELLOW + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Name"));
                            chatEnabled = false;
                            try {
                                do {
                                    Thread.sleep(25);
                                } while (chatEnabled == false);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String Name = chatText;
                            if (!StringUtils.isAlphanumericSpace(Name)) {
                                player.sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Name-Alphanumeric"));
                            } else {
                                player.sendMessage(ChatColor.YELLOW + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Memory"));
                                chatEnabled = false;
                                try {
                                    do {
                                        Thread.sleep(25);
                                    } while (chatEnabled == false);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                int Memory = 0;
                                try {
                                    Memory = Integer.parseInt(chatText);
                                } catch (NumberFormatException e) {
                                    player.sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Memory-Invalid"));
                                }
                                if (Memory == 0) {
                                    player.sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Memory-Invalid"));
                                } else {
                                    chatEnabled = false;
                                    player.sendMessage(ChatColor.YELLOW + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Port"));
                                    try {
                                        do {
                                            Thread.sleep(25);
                                        } while (chatEnabled == false);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    int Port = 0;
                                    try {
                                        Port = Integer.parseInt(chatText);
                                    } catch (NumberFormatException e) {
                                        player.sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Port-Invalid"));
                                    }
                                    if (Port == 0 || Port > 65535) {
                                        player.sendMessage(ChatColor.RED + Main.lprefix + Main.lang.getString("Lang.Create-Server.Server-Port-Invalid"));
                                    } else {
                                        Main.ServerCreator = new SubServerCreator(Name, Port, new File("./" + Name), Type, Version, Memory, player, Main);
                                        Main.ServerCreator.run();
                                        new GUI(player, 0, null, Main);
                                    }
                                }
                            }
                        }
                    }
                }.runTaskAsynchronously(Main.Plugin);

            }
            event.setCancelled(true);
        }
    }

    /**
     * Chat Listener
     */
    @EventHandler
    public void onPlayerCommand(PlayerChatEvent event) {
        if (chatEnabled == false) {
            chatText = event.getMessage();
            chatEnabled = true;
            event.setCancelled(true);
        }
    }

}