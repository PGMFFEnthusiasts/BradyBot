/*
 * This source file was generated by the Gradle 'init' task
 */
package me.fireballs;

import me.fireballs.discord.BradyBot;
import me.fireballs.server.query.ServerStatusProvider;
import me.fireballs.server.query.ServerStatusProviderImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BradyBotPlugin extends JavaPlugin {
    private static final String PROPERTIES_FILE = "brady.properties";
    private BradyBot bradyBot;

    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();
        final File propertiesFile = new File(getDataFolder(), PROPERTIES_FILE);
        if (!propertiesFile.exists()) {
            Bukkit.getLogger().warning("No brady.properties file!");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            final Properties props = new Properties();
            try {
                props.load(new FileInputStream(propertiesFile));
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            final String token = props.getProperty("token");
            if (token == null) {
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            ServerStatusProvider serverStatusProvider = new ServerStatusProviderImpl(this);
            this.bradyBot = new BradyBot(serverStatusProvider, token);
            this.bradyBot.load();
        }
    }

    @Override
    public void onDisable() {
        if (this.bradyBot != null) {
            this.bradyBot.unload();
        }
    }
}
