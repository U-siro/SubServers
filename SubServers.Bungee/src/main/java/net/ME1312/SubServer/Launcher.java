package net.ME1312.SubServer;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.md_5.bungee.Bootstrap;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.command.ConsoleCommandSender;

public class Launcher {

    public static void main(String[] args) throws Exception {
        System.out.println("***  Warning, this build is Unofficial  ***");
        System.out.println("***                                     ***");
        System.out.println("*** Please report all issues to ME1312, ***");
        System.out.println("*** NOT the Spigot Staff!    Thank You! ***");
        System.out.println("");

        Security.setProperty("networkaddress.cache.ttl", "30");
        Security.setProperty("networkaddress.cache.negative.ttl", "10");
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        parser.acceptsAll(Arrays.asList(new String[]{"v", "version"}));
        parser.acceptsAll(Arrays.asList(new String[]{"noconsole"}));
        OptionSet options = parser.parse(args);
        if(options.has("version")) {
            System.out.println(Bootstrap.class.getPackage().getImplementationVersion());
        } else {
            if(BungeeCord.class.getPackage().getSpecificationVersion() != null && System.getProperty("IReallyKnowWhatIAmDoingISwear") == null) {
                Date bungee = (new SimpleDateFormat("yyyyMMdd")).parse(BungeeCord.class.getPackage().getSpecificationVersion());
                Calendar line = Calendar.getInstance();
                line.add(3, -4);
                if(bungee.before(line.getTime())) {
                    System.out.println("*** Warning, this build is outdated ***");
                    System.out.println("*** Please download a new build from http://ci.md-5.net/job/BungeeCord ***");
                    System.out.println("*** You will get NO support regarding this build ***");
                    System.out.println("*** Server will start in 10 seconds ***");
                    System.out.println("");
                    Thread.sleep(TimeUnit.SECONDS.toMillis(10L));
                }
            }

            FakeProxyServer bungee1 = new FakeProxyServer();
            ProxyServer.setInstance(bungee1);
            bungee1.getLogger().info("Enabled BungeeCord version " + bungee1.getVersion());
            bungee1.start();
            String line1;
            if(!options.has("noconsole")) {
                while(bungee1.isRunning && (line1 = bungee1.getConsoleReader().readLine(">")) != null) {
                    if(!bungee1.getPluginManager().dispatchCommand(ConsoleCommandSender.getInstance(), line1)) {
                        bungee1.getConsole().sendMessage(ChatColor.RED + "Command not found");
                    }
                }
            }

        }
    }
}
