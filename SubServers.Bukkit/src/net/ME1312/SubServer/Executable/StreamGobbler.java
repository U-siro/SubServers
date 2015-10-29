package net.ME1312.SubServer.Executable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.ME1312.SubServer.Main;
import org.bukkit.Bukkit;

public class StreamGobbler extends Thread {
    InputStream is;
    String type;
    boolean log;
    String id;
    Main Main;

    StreamGobbler(InputStream is, String type, boolean log, String id, Main Main) {
        this.is = is;
        this.type = type;
        this.log = log;
        this.id = id;
        this.Main = Main;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
            	if (id.equalsIgnoreCase("~Proxy")) {
            		if (log && !line.startsWith(">") && !line.contains("subconf@")) {
                        Bukkit.getLogger().info(Main.lang.getString("Lang.Debug.Server-Logging-Prefix").replace("$Server$", "Proxy") +
                                line.replace(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + " ", ""));
            		}
            	} else {
            		if (log && !line.startsWith(">") && !line.contains("subconf@")) {
                        Bukkit.getLogger().info(Main.lang.getString("Lang.Debug.Server-Logging-Prefix").replace("$Server$", id) + line
                                .replace("[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + " ", "[")
                                .replace("[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "] [Server thread/", "[")
                                .replace("[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "] ", ""));
            		}
            	}
        }
        catch (IOException ioe) {
           
        }
    }
}