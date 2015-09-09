package net.ME1312.SubServer.BetterGUI;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import djxy.api.MinecraftGuiService;
import net.ME1312.SubServer.API;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.ConfigDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Plugin(id="SubServersBetterGUI", name="SubServers BetterGUI", version="1.8.8f", dependencies = "required-after:MinecraftGUIServer")
public class Main {
    public File dataFolder;
    @Inject
    public PluginContainer Plugin;
    @Inject
    public Logger log;
    @Inject
    public Game game;
    @Inject
    @ConfigDir(sharedRoot=true)
    private File ConfigDir;

    @Subscribe
    public void onInitialization(GamePostInitializationEvent event) {
        dataFolder = new File(ConfigDir, "subservers");

        API.registerListener(new BetterGUI(this, API.getLang(), API.getSubServers(), game.getServiceManager().provide(MinecraftGuiService.class).get()), Plugin);
    }

    public void copyFromJar(String resource, String destination) {
        InputStream resStreamIn = Main.class.getClassLoader().getResourceAsStream(resource);
        File resDestFile = new File(destination);
        try {
            int readBytes;
            FileOutputStream resStreamOut = new FileOutputStream(resDestFile);
            byte[] buffer = new byte[4096];
            while ((readBytes = resStreamIn.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            resStreamOut.close();
            resStreamIn.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
