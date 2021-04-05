package net.buzz;

import net.buzz.commands.SeniorStaffChat;
import net.buzz.commands.Staff;
import net.buzz.commands.StaffChat;
import net.buzz.listener.StaffListeners;
import net.buzz.manager.StaffManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;

public final class OPStaff extends Plugin {
    private Configuration mainConfiguration;

    private Configuration languageConfiguration;

    private StaffManager staffManager;

    public ArrayList<ProxiedPlayer> toggle = new ArrayList<>();

    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, (Listener)new StaffListeners(this));
        getProxy().getPluginManager().registerCommand(this, (Command)new Staff(this));
        getProxy().getPluginManager().registerCommand(this, (Command)new StaffChat(this));
        getProxy().getPluginManager().registerCommand(this, (Command)new SeniorStaffChat(this));
        getLogger().info("");
        getLogger().info("OPStaff has been successfully enabled!");
        getLogger().info("OPStaff made by buzz#9999");
        getLogger().info("");
        loadConfigurations();
        this.staffManager = new StaffManager(this);
    }

    public void onDisable() {
        getProxy().getPluginManager().unregisterCommands(this);
        this.staffManager.close();
        getLogger().info("OPStaff has been successfully disabled!");
        getLogger().info("OPStaff made by buzz#9999");
    }

    public void loadConfigurations() {
        try {
            if (!getDataFolder().exists())
                getLogger().info("OPStaff Folder Does Not Exist! Attempting to create!");
            getDataFolder().mkdir();
            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                getLogger().info("Config File Does Not Exist... Attempting to create!");
                try (InputStream in = getResourceAsStream("config.yml")) {
                    Files.copy(in, configFile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File langFile = new File(getDataFolder(), "lang.yml");
            if (!langFile.exists()) {
                getLogger().info("Language File Does Not Exist... Attempting to create!");
                try (InputStream in = getResourceAsStream("lang.yml")) {
                    Files.copy(in, langFile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mainConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
            languageConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "lang.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getMainConfiguration() {
        return this.mainConfiguration;
    }

    public Configuration getLanguageConfiguration() {
        return this.languageConfiguration;
    }

    public StaffManager getStaffManager() {
        return this.staffManager;
    }
}
