package net.buzz.listener;

import net.buzz.OPStaff;
import net.buzz.util.ChatUtil;
import net.md_5.bungee.api.ProxyServer;
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
    public void StaffConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        try {
            if(this.instance.toggle.contains(player)) {
                return;
            }
            final boolean isStaff = player.hasPermission("opcraft.staff");
            if(isStaff) {
                ProxyServer.getInstance().getPlayers().forEach(online ->
                        online.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Events.staffConnect"))
                        .replace("{user}", player.getName())
                        .replace("{server}", String.valueOf(event.getTarget().getName()))));
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

    @EventHandler
    public void StaffLeave(ServerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        try {
            for (ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                if (player.hasPermission("opcraft.staff") &&
                online.hasPermission("opcraft.staff")) {
                    if (this.instance.toggle.contains(player))
                        return;
                    online.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Events.staffLeave"))
                            .replace("{user}", player.getName())
                            .replace("{server}", String.valueOf(event.getTarget().getName())));
                }
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

    @EventHandler
    public void ToggleJoin(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        try {
            if (player.hasPermission("opcraft.admin")) {
                this.instance.toggle.add(player);
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        }
    }

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
