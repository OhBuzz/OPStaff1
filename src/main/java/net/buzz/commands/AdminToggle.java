package net.buzz.commands;

import net.buzz.OPStaff;
import net.buzz.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public class AdminToggle extends Command {
    private final OPStaff instance;

    public AdminToggle(OPStaff instance) {
        super("optoggle");
        this.instance = instance;
    }

    public static List<ProxiedPlayer> srtoggle = new ArrayList<>();
    public static List<ProxiedPlayer> stoggle = new ArrayList<>();

    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (!sender.hasPermission("opcraft.manager")) {
            sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Errors.NoPermission")));
            return;
        }
        if (player.hasPermission("opcraft.manager")) {
            if (args.length == 0) {
                player.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("AdminToggle.Usage")));
                return;
            }
            if (args[0].equalsIgnoreCase("staff")) {
                if (!stoggle.contains(player)) {
                    stoggle.add(player);
                    sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("AdminToggle.ToggleStaff"))
                            .replace("{toggle}", "disabled"));
                } else if (stoggle.contains(player)) {
                    stoggle.remove(player);
                    player.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("AdminToggle.ToggleStaff"))
                            .replace("{toggle}", "enabled"));
                }
            } else if (args[0].equalsIgnoreCase("senior")) {
                if (!srtoggle.contains(player)) {
                    srtoggle.add(player);
                    player.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("AdminToggle.ToggleSenior"))
                            .replace("{toggle}", "disabled"));
                } else if (srtoggle.contains(player)) {
                    srtoggle.remove(player);
                    player.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("AdminToggle.ToggleSenior"))
                            .replace("{toggle}", "enabled"));
                } else {
                    sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Errors.invalidUsage")));
                }
            } else {
                sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Errors.NoPermission")));
            }
        }
    }
}
