package net.buzz.commands;

import net.buzz.OPStaff;
import net.buzz.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffChat extends Command {
    private final OPStaff instance;

    public StaffChat(OPStaff instance) {
        super("sc");
        this.instance = instance;
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (sender.hasPermission("opcraft.staff")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("StaffChat.usage")));
                } else {
                    StringBuilder message = new StringBuilder("");
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
                    for (ProxiedPlayer online : this.instance.getProxy().getPlayers()) {
                        if (online.hasPermission("opcraft.staff"))
                            online.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("StaffChat.format"))
                                    .replace("{player}", player.getName())
                                    .replace("{server}", player.getServer().getInfo().getName())
                                    .replace("{rank}", this.instance.getStaffManager().getPrefix(player))
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
