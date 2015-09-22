package net.ME1312.SubServer.GUI;

import net.ME1312.SubServer.Executable.SubServer;
import net.ME1312.SubServer.Libraries.Events.SubListener;
import org.spongepowered.api.entity.living.player.Player;

public interface GUIHandler extends SubListener {

    /**
     * Selection Window Handler
     *
     * @param player The Player Opening this GUI
     * @param page The Page Number
     */
    void ServerSelectionWindow(Player player, int page);

    /**
     * Server Admin Window
     *
     * @param player The Player Opening this GUI
     * @param server The SubServer to be Managed
     */
    void ServerAdminWindow(Player player, SubServer server);

}
