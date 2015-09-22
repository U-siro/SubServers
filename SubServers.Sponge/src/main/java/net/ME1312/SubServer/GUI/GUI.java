package net.ME1312.SubServer.GUI;

import net.ME1312.SubServer.Executable.SubServer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;

/**
 * Created by ME1312 on 9/8/15.
 */
public class GUI implements GUIHandler {

    public void ServerSelectionWindow(Player player, int page) {
        player.sendMessage(Texts.of("SubServers GUI has not been added yet. Please use the Commands in /sub help for now."));
    }

    public void ServerAdminWindow(Player player, SubServer server) {
        player.sendMessage(Texts.of("SubServers GUI has not been added yet. Please use the Commands in /sub help for now."));
    }
}
