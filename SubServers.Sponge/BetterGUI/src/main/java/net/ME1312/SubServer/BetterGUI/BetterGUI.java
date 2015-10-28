package net.ME1312.SubServer.BetterGUI;

import djxy.api.MinecraftGuiService;
import djxy.controllers.CSSFactory;
import djxy.controllers.ComponentFactory;
import djxy.controllers.ResourceFactory;
import djxy.models.ComponentManager;
import djxy.models.Form;
import djxy.models.resource.Resource;
import net.ME1312.SubServer.SubAPI;
import net.ME1312.SubServer.Executable.SubServer;
import net.ME1312.SubServer.GUI.GUIHandler;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.entity.living.player.Player;

import java.io.*;
import java.util.*;

public class BetterGUI implements GUIHandler, ComponentManager {
    protected boolean Loading = false;
    private Main Main;
    private MinecraftGuiService GUI;


    public BetterGUI(Main Main, MinecraftGuiService GUI) {
        this.Main = Main;
        this.GUI = GUI;

        GUI.registerComponentManager(this, false);
    }

    public void CreateStyleSheet(File file) throws IOException {
        if (file.exists()) {
            file.delete();
        }

        CommentedConfigurationNode lang = SubAPI.getLang();

        PrintWriter writer = new PrintWriter(file, "UTF-8");

        writer.println("root > #__window {");
        writer.println("    WIDTH: 175;");
        writer.println("    HEIGHT: 100%;");
        writer.println("    BACKGROUND: 245,245,245,235;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > * {");
        writer.println("    FONT: AvenirNextLTPro-Regular;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_loader {");
        writer.println("    WIDTH: 75;");
        writer.println("    HEIGHT: 75;");
        writer.println("    IMAGE_TYPE: CUSTOM;");
        writer.println("    IMAGE_NAME: buffering.gif;");
        writer.println("    Y_RELATIVE: 75;");
        writer.println("    X_RELATIVE: 48;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_start,");
        writer.println("root > #__window > #_start_disabled {");
        writer.println("    WIDTH: 65%;");
        writer.println("    HEIGHT: 56;");
        writer.println("    Y_RELATIVE: 90;");
        writer.println("    X_RELATIVE: 30;");
        writer.println("    FONT: AvenirNextLTPro-It;");
        writer.println("    BACKGROUND: 57,202,57,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Start").getString() + ";");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_stop,");
        writer.println("root > #__window > #_stop_disabled {");
        writer.println("    WIDTH: 65%;");
        writer.println("    HEIGHT: 46;");
        writer.println("    Y_RELATIVE: 47;");
        writer.println("    X_RELATIVE: 30;");
        writer.println("    FONT: AvenirNextLTPro-It;");
        writer.println("    BACKGROUND: 202,57,57,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Stop").getString() + ";");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_kill,");
        writer.println("root > #__window > #_kill_disabled {");
        writer.println("    WIDTH: 65%;");
        writer.println("    HEIGHT: 46;");
        writer.println("    Y_RELATIVE: 93;");
        writer.println("    X_RELATIVE: 30;");
        writer.println("    FONT: AvenirNextLTPro-It;");
        writer.println("    BACKGROUND: 128,128,128,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Terminate").getString() + ";");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_send_cmd,");
        writer.println("root > #__window > #_send_cmd_disabled {");
        writer.println("    WIDTH: 65%;");
        writer.println("    HEIGHT: 46;");
        writer.println("    BACKGROUND: 91,192,222,191;");
        writer.println("    X_RELATIVE: 30;");
        writer.println("    Y_RELATIVE: 139;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_send_cmd > #_cmd {");
        writer.println("    WIDTH: 105;");
        writer.println("    HEIGHT: 12;");
        writer.println("    X_RELATIVE: 4;");
        writer.println("    Y_RELATIVE: 10;");
        writer.println("    FONT: AvenirNextLTPro-DemiIt;");
        writer.println("    FONT_SIZE: 6;");
        writer.println("    TEXT_ALIGNMENT: CENTER;");
        writer.println("    PADDING_SIDE: false,true,false,false;");
        writer.println("    PADDING_SIZE: 6;");
        writer.println("    BACKGROUND: 61,162,222,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("    MAX_TEXT_LINES: 1;");
        writer.println("    VALUE: /Command;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_send_cmd > #_send {");
        writer.println("    WIDTH: 109;");
        writer.println("    HEIGHT: 20;");
        writer.println("    Y_RELATIVE: 24;");
        writer.println("    X_RELATIVE: 2;");
        writer.println("    FONT: AvenirNextLTPro-It;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Send-CMD").getString() + ";");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_start:hover {");
        writer.println("    BACKGROUND: 40,180,40,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_stop:hover {");
        writer.println("    BACKGROUND: 180,40,40,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_kill:hover {");
        writer.println("    BACKGROUND: 108,108,108,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_send_cmd > #_send:hover {");
        writer.println("    BACKGROUND: 61,162,222,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_start_disabled,");
        writer.println("root > #__window > #_stop_disabled,");
        writer.println("root > #__window > #_kill_disabled,");
        writer.println("root > #__window > #_send_cmd_disabled {");
        writer.println("    BACKGROUND: 50,50,50,191;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #__header {");
        writer.println("    FONT: AvenirNextLTPro-Demi;");
        writer.println("    TEXT_COLOR: 0,0,0,150;");
        writer.println("    TEXT_ALIGNMENT: CENTER;");
        writer.println("    WIDTH: 143;");
        writer.println("    HEIGHT: 15;");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Server-List-Title").getString() + ";");
        writer.println("    Y_RELATIVE: 4;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #__close,");
        writer.println("root > #__window > #__minimize {");
        writer.println("    FONT_SIZE: 14;");
        writer.println("    TEXT_COLOR: 0,0,0,127;");
        writer.println("    WIDTH: 32;");
        writer.println("    HEIGHT: 15;");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Exit").getString() + ";");
        writer.println("    X_RELATIVE: 143;");
        writer.println("}");
        writer.println();

        //TODO

        writer.println("root > #__window > #_teleport,");
        writer.println("root > #__window > #_teleport_disabled,");
        writer.println("root > #__window > #__back,");
        writer.println("root > #__window > #__next,");
        writer.println("root > #__window > #__create_mid,");
        writer.println("root > #__window > #__create_busy_mid,");
        writer.println("root > #__window > #__create_left,");
        writer.println("root > #__window > #__create_busy_left,");
        writer.println("root > #__window > #__create_right,");
        writer.println("root > #__window > #__create_busy_right,");
        writer.println("root > #__window > #__create_full,");
        writer.println("root > #__window > #__create_busy_full {");
        writer.println("    TEXT_COLOR: 0,0,0,127;");
        writer.println("    WIDTH: 58;");
        writer.println("    HEIGHT: 20;");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Back").getString() + ";");
        writer.println("    Y_RELATIVE: 1120%;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #__next {");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Next").getString() + ";");
        writer.println("    X_RELATIVE: 117;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #__create_mid,");
        writer.println("root > #__window > #__create_busy_mid {");
        writer.println("    WIDTH: 59;");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Create-Server").getString() + ";");
        writer.println("    X_RELATIVE: 58;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #__create_left,");
        writer.println("root > #__window > #__create_busy_left {");
        writer.println("    WIDTH: 117;");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Create-Server").getString() + ";");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_teleport,");
        writer.println("root > #__window > #_teleport_disabled,");
        writer.println("root > #__window > #__create_right,");
        writer.println("root > #__window > #__create_busy_right {");
        writer.println("    WIDTH: 117;");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Create-Server").getString() + ";");
        writer.println("    X_RELATIVE: 58;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #__create_full,");
        writer.println("root > #__window > #__create_busy_full {");
        writer.println("    WIDTH: 100%;");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Create-Server").getString() + ";");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #__create_busy_mid,");
        writer.println("root > #__window > #__create_busy_left,");
        writer.println("root > #__window > #__create_busy_right,");
        writer.println("root > #__window > #__create_busy_full {");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Create-Server-Busy").getString() + ";");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_teleport,");
        writer.println("root > #__window > #_teleport_disabled {");
        writer.println("    VALUE: " + lang.getNode("Lang", "GUI", "Click-to-Join").getString() + ";");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #__close:hover {");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("    BACKGROUND: 255,0,0,191;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #__back:hover,");
        writer.println("root > #__window > #__next:hover {");
        writer.println("    BACKGROUND: 91,192,222,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #__create_mid:hover,");
        writer.println("root > #__window > #__create_left:hover,");
        writer.println("root > #__window > #__create_right:hover,");
        writer.println("root > #__window > #__create_full:hover {");
        writer.println("    BACKGROUND: 57,202,57,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #__create_busy_mid:hover,");
        writer.println("root > #__window > #__create_busy_left:hover,");
        writer.println("root > #__window > #__create_busy_right:hover,");
        writer.println("root > #__window > #__create_busy_full:hover {");
        writer.println("    BACKGROUND: 232,218,26,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > #_teleport:hover {");
        writer.println("    BACKGROUND: 47,121,224,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("}");
        writer.println("root > #__window > #_teleport_disabled:hover {");
        writer.println("    BACKGROUND: 50,50,50,191;");
        writer.println("    TEXT_COLOR: 255,255,255,255;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > list {");
        writer.println("    WIDTH: 175;");
        writer.println("    HEIGHT: 86%;");
        writer.println("    Y_RELATIVE: 20;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > list > button {");
        writer.println("    FONT: AvenirNextLTPro-It;");
        writer.println("    WIDTH: 100%;");
        writer.println("    HEIGHT: 12;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > list > .Online {");
        writer.println("    TEXT_COLOR: 0,200,0,255;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > list > .Offline {");
        writer.println("    TEXT_COLOR: 200,0,0,255;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > list > .Temporary {");
        writer.println("    TEXT_COLOR: 0,0,0,115;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > list > .Online:hover {");
        writer.println("    TEXT_COLOR: 0,200,0,255;");
        writer.println("    BACKGROUND: 200,200,200,191;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > list > .Offline:hover {");
        writer.println("    TEXT_COLOR: 200,0,0,255;");
        writer.println("    BACKGROUND: 200,200,200,191;");
        writer.println("}");
        writer.println();
        writer.println("root > #__window > list > .Temporary:hover {");
        writer.println("    TEXT_COLOR: 0,0,0,255;");
        writer.println("    BACKGROUND: 200,200,200,191;");
        writer.println("}");
        writer.println();

        writer.close();
    }

    public void ServerSelectionWindow(Player player, int page) {
        ServerSelectionWindow(player, page, false);
    }

    public void ServerSelectionWindow(Player player, int page, boolean remove) {
        List<SubServer> SubServers = new ArrayList<SubServer>();
        SubServers.addAll(SubAPI.getSubServers());
        if (true) {
            UUID uuid = player.getUniqueId();
            int min = (page * 17);
            int max = (min + 16);

            try {
                if (!(new File(new File(Main.dataFolder, "cache"), "__style.css").exists())) {
                    CreateStyleSheet(new File(new File(Main.dataFolder, "cache"), "__style.css"));
                }

                if (new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml").exists()) {
                    new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml").delete();
                }

                PrintWriter writer = new PrintWriter(new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml"), "UTF-8");

                writer.println("<root>");
                writer.println("    <panel id=\"__window\">");
                writer.println("        <paragraph id=\"__header\" />");
                writer.println("        <button id=\"__close\" />");
                writer.println("        <input id=\"__page\" type=\"invisible\" value=\"" + Integer.toString(page + 1) + "\" />");
                writer.println("        <list>");

                GUI.listenButton(this, "__close");

                for (Iterator<SubServer> items = SubServers.iterator(); items.hasNext(); ) {
                    SubServer item = items.next();
                    if (item.Enabled) {
                        if (SubServers.indexOf(item) >= min && SubServers.indexOf(item) <= max) {

                            String status;
                            if (item.Temporary) {
                                status = "Temporary";
                            } else if (item.isRunning()) {
                                status = "Online";
                            } else {
                                status = "Offline";
                            }

                            String name;
                            if (item.PID == 0) {
                                name = "Proxy";
                            } else {
                                name = item.Name;
                            }

                            String id;
                            if (item.PID == 0) {
                                id = "__server__Proxy";
                            } else {
                                id = "__server_" + item.Name;
                            }

                            writer.println("            <button class=\"" + status + "\" id=\"" + id + "\" value=\"" + name + "\" />");

                            GUI.listenButton(this, id);
                        }
                    }
                }

                writer.println("        </list>");

                if (min != 0) {
                    writer.println("        <button id=\"__back\">");
                    writer.println("            <inputToSend id=\"__page\" />");
                    writer.println("        </button>");
                    GUI.listenButton(this, "__back");
                }

                if (net.ME1312.SubServer.Main.ServerCreator == null || !net.ME1312.SubServer.Main.ServerCreator.isRunning()) {
                    if ((min != 0) && (SubServers.size() > max)) {
                        writer.println("        <button id=\"__create_mid\" />");
                        GUI.listenButton(this, "__create_mid");
                    } else if ((min != 0)) {
                        writer.println("        <button id=\"__create_right\" />");
                        GUI.listenButton(this, "__create_right");
                    } else if ((SubServers.size() > max)) {
                        writer.println("        <button id=\"__create_left\" />");
                        GUI.listenButton(this, "__create_left");
                    } else {
                        writer.println("        <button id=\"__create_full\" />");
                        GUI.listenButton(this, "__create_full");
                    }
                } else {
                    if ((min != 0) && (SubServers.size() > max)) {
                        writer.println("        <button id=\"__create_busy_mid\" />");
                        GUI.listenButton(this, "__create_busy_mid");
                    } else if ((min != 0)) {
                        writer.println("        <button id=\"__create_busy_right\" />");
                        GUI.listenButton(this, "__create_busy_right");
                    } else if ((SubServers.size() > max)) {
                        writer.println("        <button id=\"__create_busy_left\" />");
                        GUI.listenButton(this, "__create_busy_left");
                    } else {
                        writer.println("        <button id=\"__create_busy_full\" />");
                        GUI.listenButton(this, "__create_busy_full");
                    }
                }

                if (SubServers.size() > max) {
                    writer.println("        <button id=\"__next\">");
                    writer.println("            <inputToSend id=\"__page\" />");
                    writer.println("        </button>");
                    GUI.listenButton(this, "__next");
                }

                writer.println("    </panel>");
                writer.println("</root>");
                writer.close();

                if (remove) GUI.removeComponent(uuid.toString(), "__window");

                GUI.createComponent(uuid.toString(), ComponentFactory.load(new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml"),
                        CSSFactory.load(new File(new File(Main.dataFolder, "cache"), "__style.css"))));

            } catch (IOException e) {
                Main.log.error(e.getStackTrace().toString());
                ;
            }
        }
    }

    public void ServerAdminWindow(Player player, SubServer server) { ServerAdminWindow(player, server, false); }

    public void ServerAdminWindow(Player player, SubServer server, boolean remove) {
        CommentedConfigurationNode lang = SubAPI.getLang();
        UUID uuid = player.getUniqueId();
        if (true) {
            try {
                if (!(new File(new File(Main.dataFolder, "cache"), "__style.css").exists())) {
                    CreateStyleSheet(new File(new File(Main.dataFolder, "cache"), "__style.css"));
                }

                if (new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml").exists()) {
                    new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml").delete();
                }

                PrintWriter writer = new PrintWriter(new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml"), "UTF-8");

                writer.println("<root>");
                writer.println("    <panel id=\"__window\">");
                writer.println("        <paragraph id=\"__header\" value=\"" + lang.getNode("Lang", "GUI", "Server-Admin-Title").getString() + server.Name + "\" />");
                writer.println("        <button id=\"__close\" />");
                writer.println("        <input id=\"_server\" type=\"invisible\" value=\"" + server.PID + "\" />");

                GUI.listenButton(this, "__close");

                if (!server.isRunning()/* && (player.hasPermission("SubServer.Command.start." + server) || !player.hasPermission("SubServer.Command.start.*"))*/) { //TODO
                    writer.println("        <button id=\"_start\">");
                    writer.println("            <inputToSend id=\"_server\" />");
                    writer.println("        </button>");

                    GUI.listenButton(this, "_start");
                } else if (!server.isRunning()) {
                    writer.println("        <button id=\"_start_disabled\" />");
                }

                if (server.isRunning()/* && (player.hasPermission("SubServer.Command.stop." + server) || player.hasPermission("SubServer.Command.stop.*"))*/) { //TODO
                    writer.println("        <button id=\"_stop\">");
                    writer.println("            <inputToSend id=\"_server\" />");
                    writer.println("        </button>");

                    GUI.listenButton(this, "_stop");
                } else if (server.isRunning()) {
                    writer.println("        <button id=\"_stop_disabled\" />");
                }

                if (server.isRunning()/* && (player.hasPermission("SubServer.Command.kill." + server) || player.hasPermission("SubServer.Command.kill.*"))*/) { //TODO
                    writer.println("        <button id=\"_kill\">");
                    writer.println("            <inputToSend id=\"_server\" />");
                    writer.println("        </button>");

                    GUI.listenButton(this, "_kill");
                } else if (server.isRunning()) {
                    writer.println("        <button id=\"_kill_disabled\" />");
                }

                if (server.isRunning()/* && (player.hasPermission("SubServer.Command.send." + server) || player.hasPermission("SubServer.Command.send.*"))*/) { //TODO
                    writer.println("        <panel id=\"_send_cmd\">");
                    writer.println("            <input id=\"_cmd\" type=\"text\" />");
                    writer.println("            <button id=\"_send\">");
                    writer.println("                <inputToSend id=\"_cmd\" />");
                    writer.println("                <inputToSend id=\"_server\" />");
                    writer.println("            </button>");
                    writer.println("        </panel>");

                    GUI.listenButton(this, "_send");
                } else if (server.isRunning()) {
                    writer.println("        <button id=\"_send_cmd_disabled\" value=\"" + lang.getNode("Lang", "GUI", "Send-CMD").getString() + "\" />");
                }

                if (server.isRunning() && !server.Name.equalsIgnoreCase("~Proxy")/* && (player.hasPermission("SubServer.Command.teleport." + server) || player.hasPermission("SubServer.Command.teleport.*"))*/) { //TODO
                    writer.println("        <button id=\"_teleport\">");
                    writer.println("            <inputToSend id=\"_server\" />");
                    writer.println("        </button>");

                    GUI.listenButton(this, "_teleport");
                } else {
                    writer.println("        <button id=\"_teleport_disabled\" />");
                }

                writer.println("        <button id=\"__back\">");
                writer.println("            <inputToSend id=\"_server\" />");
                writer.println("        </button>");
                writer.println("    </panel>");
                writer.println("</root>");

                writer.close();
                GUI.listenButton(this, "__back");


                if (remove) GUI.removeComponent(uuid.toString(), "__window");

                GUI.createComponent(uuid.toString(), ComponentFactory.load(new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml"),
                        CSSFactory.load(new File(new File(Main.dataFolder, "cache"), "__style.css"))));

            } catch (IOException e) {
                Main.log.error(e.getStackTrace().toString());
                ;
            }
        }
    }

    public void LoaderWindow(Player player, int methodID, boolean remove, Object... args) {
        UUID uuid = player.getUniqueId();

        try {
            if (!(new File(new File(Main.dataFolder, "cache"), "__style.css").exists())) {
                CreateStyleSheet(new File(new File(Main.dataFolder, "cache"), "__style.css"));
            }

            if (new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml").exists()) {
                new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml").delete();
            }

            PrintWriter writer = new PrintWriter(new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml"), "UTF-8");

            writer.println("<root>");
            writer.println("    <panel id=\"__window\">");
            writer.println("        <image id=\"_loader\" />");
            writer.println("    </panel>");
            writer.println("</root>");

            writer.close();

            if (remove) GUI.removeComponent(uuid.toString(), "__window");

            GUI.createComponent(uuid.toString(), ComponentFactory.load(new File(new File(Main.dataFolder, "cache"), uuid.toString() + ".xml"),
                    CSSFactory.load(new File(new File(Main.dataFolder, "cache"), "__style.css"))));

            Loading = true;
        } catch (IOException e) {
            Main.log.error(e.getStackTrace().toString());;
        }
        Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    do {
                        Thread.sleep(25);
                    } while (Loading);
                } catch (InterruptedException e) {
                    Main.log.error(e.getStackTrace().toString());;
                }

                if (methodID > 0) {
                    if (methodID ==  1) {
                        ServerSelectionWindow(player, (int) args[0], true);
                    } else if (methodID == 2) {
                        ServerAdminWindow(player, (SubServer) args[0], true);
                    }
                }
            }
        }).submit(Main.Plugin);

    }

    @Override
    public void initPlayerGUI(String uuid) {
        if (!(new File(new File(Main.dataFolder, "cache"), "__resources.xml").exists()))
            Main.copyFromJar("resources.xml", new File(new File(Main.dataFolder, "cache"), "__resources.xml").getAbsolutePath());

        for(Iterator<Resource> items = ResourceFactory.load(new File(new File(Main.dataFolder, "cache"), "__resources.xml")).iterator(); items.hasNext(); ) {
            GUI.downloadResource(uuid, items.next());
        }
    }

    @Override
    public void receiveForm(String uuid, Form form) {
        List<SubServer> SubServers = new ArrayList<SubServer>();
        SubServers.addAll(SubAPI.getSubServers());

        final String button = form.getButtonId();
        final Player player = Main.game.getServer().getPlayer(UUID.fromString(uuid)).get();

        if (form.getInput("__page") != null) Main.log.info("Page: " + form.getInput("__page"));
        if (form.getInput("_server") != null) Main.log.info("Server: " + form.getInput("_server"));
        if (form.getInput("_cmd") != null) Main.log.info("Command: " + form.getInput("_cmd"));
        Main.log.info("Button: " + button);
        Main.log.info("");

        switch (button) {
            case "__close":
                GUI.removeComponent(uuid, "__window");
                break;

            case "__next":
                if (form.getInput("__page") != null && StringUtils.isNumeric(form.getInput("__page"))) {
                    ServerSelectionWindow(player, Integer.parseInt(form.getInput("__page")), true);

                } else {
                    ServerSelectionWindow(player, 0, true);
                }
                break;

            case "__back":
                if (form.getInput("__page") != null && StringUtils.isNumeric(form.getInput("__page"))) {
                    ServerSelectionWindow(player, Integer.parseInt(form.getInput("__page")) - 2, true);

                } else if (form.getInput("_server") != null && StringUtils.isNumeric(form.getInput("_server")) && SubServers.contains(SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))))) {
                    ServerSelectionWindow(player, (int) Math.floor(SubServers.indexOf(SubAPI.getSubServer(Integer.parseInt(form.getInput("_server")))) / 17), true);

                } else {
                    ServerSelectionWindow(player, 0, true);
                }
                break;
            case "_teleport":
                if (form.getInput("_server") != null && StringUtils.isNumeric(form.getInput("_server")) && SubServers.contains(SubAPI.getSubServer(Integer.parseInt(form.getInput("_server")))) && SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).isRunning()) {
                    GUI.removeComponent(uuid, "__window");
                    SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).sendPlayer(player);
                }
                break;

            case "_start":
                if (form.getInput("_server") != null && StringUtils.isNumeric(form.getInput("_server")) && SubServers.contains(SubAPI.getSubServer(Integer.parseInt(form.getInput("_server")))) && !SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).isRunning()) {
                    SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).start(player);
                    LoaderWindow(player, 2, true, SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))));
                    Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                Main.log.error(e.getStackTrace().toString());;
                            }
                            Loading = false;

                        }
                    }).submit(Main.Plugin);
                }
                break;

            case "_stop":
                if (form.getInput("_server") != null && StringUtils.isNumeric(form.getInput("_server")) && SubServers.contains(SubAPI.getSubServer(Integer.parseInt(form.getInput("_server")))) && SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).isRunning()) {
                    LoaderWindow(player, 2, true, SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))));
                    Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                if (SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).stop(player)) {
                                    SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).waitFor();
                                    Thread.sleep(500);
                                } else {
                                    Thread.sleep(1500);
                                }
                            } catch (InterruptedException e) {
                                Main.log.error(e.getStackTrace().toString());;
                            }
                            Loading = false;

                        }
                    }).submit(Main.Plugin);
                }
                break;

            case "_kill":
                if (form.getInput("_server") != null && StringUtils.isNumeric(form.getInput("_server")) && SubServers.contains(SubAPI.getSubServer(Integer.parseInt(form.getInput("_server")))) && SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).isRunning()) {
                    SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).terminate(player);
                    LoaderWindow(player, 2, true, SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))));
                    Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                Main.log.error(e.getStackTrace().toString());;
                            }
                            Loading = false;

                        }
                    }).submit(Main.Plugin);
                }
                break;

            case "_send":
                if (form.getInput("_server") != null && StringUtils.isNumeric(form.getInput("_server")) && SubServers.contains(SubAPI.getSubServer(Integer.parseInt(form.getInput("_server")))) && SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).isRunning()) {
                    if (form.getInput("_cmd") != null && StringUtils.isNotEmpty(form.getInput("_cmd"))) {
                        if (form.getInput("_cmd").startsWith("/")) {
                            SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).sendCommand(player, form.getInput("_cmd").substring(1));
                        } else {
                            SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))).sendCommand(player, form.getInput("_cmd"));
                        }
                    }
                    LoaderWindow(player, 2, true, SubAPI.getSubServer(Integer.parseInt(form.getInput("_server"))));
                    Main.game.getScheduler().createTaskBuilder().async().execute(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Thread.sleep(750);
                            } catch (InterruptedException e) {
                                Main.log.error(e.getStackTrace().toString());;
                            }
                            Loading = false;

                        }
                    }).submit(Main.Plugin);
                }
                break;

            default:
                if (SubServers.contains(SubAPI.getSubServer(StringUtils.stripEnd(button.replace("__server_", ""), " ")))) {
                    ServerAdminWindow(player, SubAPI.getSubServer(StringUtils.stripEnd(button.replace("__server_", ""), " ")), true);
                }
                break;
        }
    }
}