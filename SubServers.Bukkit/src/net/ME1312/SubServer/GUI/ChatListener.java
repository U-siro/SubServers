package net.ME1312.SubServer.GUI;

import net.ME1312.SubServer.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ME1312 on 10/15/15.
 */
@SuppressWarnings("deprecation")
public class ChatListener implements Listener {
    public boolean chatEnabled = true;
    public String chatText = "";

    private Main Main;
    private Player Player;

    protected ChatListener(Player Player, Main Main) {
        this.Main = Main;
        this.Player = Player;
        Bukkit.getPluginManager().registerEvents(this, Main.Plugin);
    }

    /**
     * Chat Listener
     */
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerCommand(org.bukkit.event.player.PlayerChatEvent event) {
        if (chatEnabled == false && event.getPlayer() == Player) {
            chatText = event.getMessage();
            chatEnabled = true;
            event.setCancelled(true);
        }
    }
}
