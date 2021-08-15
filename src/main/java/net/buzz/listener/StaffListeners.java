package net.buzz.listener;

import net.buzz.OPStaff;
import net.buzz.util.ChatUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class StaffListeners implements Listener {
    private final OPStaff instance;

    public StaffListeners(OPStaff instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerLogin(PostLoginEvent event) {
        if (event.getPlayer().hasPermission("opcraft.staff"))
            this.instance.getStaffManager().addLoginTime(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void StaffLeave(ServerConnectedEvent event) {
        ProxiedPlayer p = event.getPlayer();
        try {
            for (ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                if (p.hasPermission("opcraft.staff") && (online.hasPermission("opcraft.staff")))
                    online.sendMessage(new TextComponent(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Events.staffLeave"))
                            .replace("{user}", event.getPlayer().getName())
                            .replace("{server}", p.getServer().getInfo().getName())));
                            //.replace("{server}", event.getPlayer().getServer().getInfo().getName())));
            }
        } catch (NullPointerException a) {
            a.printStackTrace();
        }
    }

    @EventHandler
    public void StaffJoin2(ServerSwitchEvent event) {
        ProxiedPlayer p = event.getPlayer();
        try {
            for (ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                if (p.hasPermission("opcraft.staff") && online.hasPermission("opcraft.staff"))
                    online.sendMessage(new TextComponent(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Events.staffConnect"))
                            .replace("{user}", event.getPlayer().getName())
                            .replace("{server}", p.getServer().getInfo().getName())));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

//    @EventHandler
//    public void StaffLeave(ServerDisconnectEvent event) {
//        ProxiedPlayer p = event.getPlayer();
//        try {
//            for (ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
//                if (p.hasPermission("opcraft.staff") && (online.hasPermission("opcraft.staff")))
//                    online.sendMessage(new TextComponent(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Events.staffLeave"))
//                            .replace("{user}", p.getName())
//                            .replace("{server}", String.valueOf(event.getTarget().getName()))));
//            }
//        } catch (NullPointerException a) {
//            a.printStackTrace();
//        }
//    }

//    @EventHandler
//    public void StaffJoin(ServerSwitchEvent event) {
//        ProxiedPlayer p = event.getPlayer();
//        try {
//            for (ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
//                if (p.hasPermission("opcraft.staff") && (online.hasPermission("opcraft.staff")))
//                    online.sendMessage(new TextComponent(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Events.staffConnect"))
//                            .replace("{user}", p.getName())
//                            .replace("{server}", String.valueOf(p.getServer().getInfo().getName()))));
//            }
//        } catch (NullPointerException a) {
//            a.printStackTrace();
//            System.err.format("IOException: %s%n", a);
//        }
//    }

    @EventHandler
    public void  onPlayerDisconnect(PlayerDisconnectEvent event) {
        if (event.getPlayer().hasPermission("opcraft.staff"))
            this.instance.getStaffManager().removeLoginTime(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerServerSwitch(ServerSwitchEvent event) {
        if (event.getPlayer().hasPermission("opcraft.staff"))
           this.instance.getStaffManager().addLoginTime(event.getPlayer().getUniqueId());
    }
}
