package net.buzz.commands;

import net.buzz.OPStaff;
import net.buzz.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SeniorStaffChat extends Command {
    private final OPStaff instance;

    public SeniorStaffChat(OPStaff instance) {
        super("src");
        this.instance = instance;
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (sender.hasPermission("opcraft.admin")) {
                if (AdminToggle.srtoggle.contains(player)) {
                    return;
                }
                if (args.length == 0) {
                    sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("SeniorChat.usage")));
                } else {
                    StringBuilder message = new StringBuilder();
                    byte b;
                    int i;
                    String[] arrayOfString;
                    for (i = (arrayOfString = args).length, b = 0; b < i; ) {
                        String part = arrayOfString[b];
                        if (!message.toString().equals(""))
                            message.append(" ");
                        message.append(part);
                        b++;
                    }
                    if (AdminToggle.srtoggle.contains(player)) {
                        return;
                    }
                    for (ProxiedPlayer online : this.instance.getProxy().getPlayers()) {
                        if (AdminToggle.srtoggle.contains(online)) {
                            sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("AdminToggle.UsedToggled"))
                            .replace("{mode}", "Senior Staff Chat"));
                            return;
                        }
                        if (online.hasPermission("opcraft.admin") && (!AdminToggle.srtoggle.contains(player)))
                            online.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("SeniorChat.format"))
                                    .replace("{player}", player.getName())
                                    .replace("{server}", player.getServer().getInfo().getName())
                                    .replace("{rank}", this.instance.getStaffManager().getPrefix(player)
                                            .replace("ForumsManager", "Forums Manager")
                                            .replace("StaffManager", "Staff Manager")
                                            .replace("SupportManager", "Support Manager")
                                            .replace("OperationsManager", "Operations"))
                                    .replace("{message}", message.toString()));
                    }
                }
            } else {
                sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Errors.NoPermission")));
            }
        } else {
            sender.sendMessage("You must be in game to use this");
        }
    }
}