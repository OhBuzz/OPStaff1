package net.buzz.manager;

import net.buzz.OPStaff;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class StaffManager implements Closeable {
    private final OPStaff instance;

    private final Set<UUID> hidden;

    private final Map<UUID, Long> loginTimes;

    public StaffManager(OPStaff instance) {
        this.instance = instance;
        this.hidden = Collections.newSetFromMap(new ConcurrentHashMap<>());
        this.loginTimes = new ConcurrentHashMap<>();
    }

    public String getPrefix(ProxiedPlayer player) {
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getUserManager().getUser(player.getUniqueId());
        try {
            QueryOptions queryOptions = api.getContextManager().getQueryOptions(player);
            assert user != null;
            return Objects.requireNonNull(user.getCachedData().getMetaData(queryOptions).getPrefix())
                    .replace(" ", "")
                    .replace("]", "")
                    .replace("[", "")
                    .replaceAll("&", "§");
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Map<Group, Set<User>>> getServerStaff() {
        LuckPerms api = LuckPermsProvider.get();
        Map<String, Map<Group, Set<User>>> servers = new LinkedHashMap<>();
        for (String groupName : this.instance.getMainConfiguration().getStringList("groups")) {
            Group group = api.getGroupManager().getGroup(groupName);
            if (group == null)
                continue;
            for (ProxiedPlayer player : this.instance.getProxy().getPlayers()) {
                String serverName = player.getServer().getInfo().getName();
                if (!this.instance.getMainConfiguration().getStringList("servers").contains(serverName))
                    continue;
                User user = api.getUserManager().getUser(player.getUniqueId());
                if (user == null)
                    continue;
                if (this.instance.getStaffManager().isHidden(player.getUniqueId()))
                    continue;
                Set<String> userGroups = user.getNodes().stream().filter(NodeType.INHERITANCE::matches).map(NodeType.INHERITANCE::cast).map(InheritanceNode::getGroupName).collect(Collectors.toSet());
                if (!userGroups.contains(group.getName()))
                    continue;
                Map<Group, Set<User>> staff = servers.computeIfAbsent(serverName, k -> new LinkedHashMap<>());
                staff.computeIfAbsent(group, k -> new LinkedHashSet()).add(user);
            }
        }
        return servers;
    }

    public void close() {}

    public void addHidden(UUID uuid) {
        this.hidden.add(uuid);
    }

    public boolean isHidden(UUID uuid) {
        return this.hidden.contains(uuid);
    }

    public void removeHidden(UUID uuid) {
        this.hidden.remove(uuid);
    }

    public void addLoginTime(UUID uuid) {
        this.loginTimes.put(uuid, System.currentTimeMillis());
    }

    public long getLoginTime(UUID uuid) {
        return this.loginTimes.getOrDefault(uuid, 0L);
    }

    public void removeLoginTime(UUID uuid) {
        this.loginTimes.remove(uuid);
    }
}
