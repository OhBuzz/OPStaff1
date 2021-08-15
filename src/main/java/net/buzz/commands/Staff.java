package net.buzz.commands;

import net.buzz.OPStaff;
import net.buzz.util.ChatUtil;
import net.buzz.util.TimeUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

public class Staff extends Command {
    private final OPStaff instance;

    public Staff(OPStaff instance) {
        super("staff");
        this.instance = instance;
    }


    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (args.length == 0) {
                if (!sender.hasPermission("opcraft.staff")) {
                    sender.sendMessage(new TextComponent(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Errors.NoPermission"))));
                    return;
                }
                LuckPerms api = LuckPermsProvider.get();
                sender.sendMessage(new TextComponent(" "));
                sender.sendMessage(new TextComponent(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Staff.title"))));
                sender.sendMessage(new TextComponent(" "));
                for (String serverName : this.instance.getMainConfiguration().getStringList("servers")) {
                    Map<Group, Set<User>> serverMapEntry = this.instance.getStaffManager().getServerStaff().get(serverName);
                    sender.sendMessage(new TextComponent(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Staff.server")
                            .replace("{server}", serverName))
                            .replace("{COUNT}", String.valueOf(ProxyServer.getInstance().getServerInfo(serverName).getPlayers().size()))));
                    if (serverMapEntry == null || serverMapEntry.isEmpty()) {
                        sender.sendMessage(new TextComponent(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Staff.NoneOnline"))));
                        sender.sendMessage(new TextComponent(" "));
                        continue;
                    }
                    for (Map.Entry<Group, Set<User>> groupSetEntry : serverMapEntry.entrySet()) {
                        for (User user : groupSetEntry.getValue()) {
                            Optional<QueryOptions> queryOptions = api.getContextManager().getQueryOptions(user);
                            if (!queryOptions.isPresent())
                                continue;
                            CachedMetaData metaData = groupSetEntry.getKey().getCachedData().getMetaData(queryOptions.get());
                            ProxiedPlayer player = this.instance.getProxy().getPlayer(user.getUniqueId());
                            if (player == null)
                                continue;
                            long loginTime = this.instance.getStaffManager().getLoginTime(player.getUniqueId());
                            sender.sendMessage(new TextComponent(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Staff.staffMember")
                                    .replace("{rank}", Objects.requireNonNull(metaData.getPrefix())
                                            .replace(" ", "")
                                            .replace("-", " ")
                                            .replace("StaffManager", "Staff Manager")
                                            .replace("SupportManager", "Support Manager")
                                            .replace("ForumsManager", "Forums Manager")
                                            .replace("OperationsManager", "Operations"))
                                    .replace("{user}", player.getDisplayName())
                                    .replace("{playtime}", (loginTime > 0L) ? TimeUtil.formatDHMS(System.currentTimeMillis() - loginTime) : "N/A"))));
                        }
                    }
                    sender.sendMessage(new TextComponent(" "));
                }
            } else if (args[0].equalsIgnoreCase("hide")) {
                if (!sender.hasPermission("opcraft.admin")) {
                    sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Errors.NoPermission")));
                    return;
                }
                UUID uuid = ((ProxiedPlayer) sender).getUniqueId();
                if (this.instance.getStaffManager().isHidden(uuid)) {
                    this.instance.getStaffManager().removeHidden(uuid);
                    sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("StaffHide.Hide")
                            .replace("{toggle}", "enabled")));
                } else {
                    this.instance.getStaffManager().addHidden(uuid);
                    sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("StaffHide.Hide")
                            .replace("{toggle}", "disabled")));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("opcraft.owner")) {
                    sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Errors.NoPermission")));
                    return;
                }
                this.instance.loadConfigurations();
                sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("configReloaded")));
            } else if (args[0].equalsIgnoreCase("toggle")) {
                if (sender.hasPermission("opcraft.admin")) {
                    if (!this.instance.toggle.contains(p)) {
                        this.instance.toggle.add(p);
                        p.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Events.toggle")
                                .replace("{toggle}", "disabled")));
                    } else {
                        if (this.instance.toggle.contains(p)) {
                            this.instance.toggle.remove(p);
                            p.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Events.toggle")
                                    .replace("{toggle}", "enabled")));
                        }
                    }
                } else {
                    sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Errors.NoPermission")));
                }
            } else {
                sender.sendMessage(ChatUtil.colorize(this.instance.getLanguageConfiguration().getString("Errors.invalidUsage")));
            }
        } else {
            sender.sendMessage("You must be in game to do this!");
        }
    }
}
