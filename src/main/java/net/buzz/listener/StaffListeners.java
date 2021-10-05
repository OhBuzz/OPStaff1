package net.buzz.listener;

import net.buzz.OPStaff;
import net.buzz.util.ChatUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class StaffListeners implements Listener
{
    private final OPStaff instance;

    public StaffListeners(final OPStaff instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerLogin(final PostLoginEvent event) {
        if (event.getPlayer().hasPermission("opcraft.staff")) {
            this.instance.getStaffManager().addLoginTime(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void StaffLeave(final ServerConnectedEvent event) {
        final ProxiedPlayer p = event.getPlayer();
        try {
            for (final ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                if (p.hasPermission("opcraft.staff") && online.hasPermission("opcraft.staff")) {
                    try {
                        online.sendMessage(new TextComponent(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Events.staffLeave")).replace("{user}", event.getPlayer().getName()).replace("{server}", p.getServer().getInfo().getName())));
                    }
                    catch (NullPointerException ex) {}
                }
            }
        }
        catch (NullPointerException a) {
            a.printStackTrace();
        }
    }

    @EventHandler
    public void StaffJoin2(final ServerSwitchEvent event) {
        final ProxiedPlayer p = event.getPlayer();
        try {
            for (final ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                if (p.hasPermission("opcraft.staff") && online.hasPermission("opcraft.staff")) {
                    online.sendMessage(new TextComponent(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Events.staffConnect")).replace("{user}", event.getPlayer().getName()).replace("{server}", p.getServer().getInfo().getName())));
                }
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerDisconnect(final PlayerDisconnectEvent event) {
        if (event.getPlayer().hasPermission("opcraft.staff")) {
            this.instance.getStaffManager().removeLoginTime(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerServerSwitch(final ServerSwitchEvent event) {
        if (event.getPlayer().hasPermission("opcraft.staff")) {
            this.instance.getStaffManager().addLoginTime(event.getPlayer().getUniqueId());
        }
    }
}