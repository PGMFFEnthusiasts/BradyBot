package me.fireballs.server.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerListener implements Listener {
    public ServerListenerCallback callback;

    public void load(final JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (callback == null) return;
        callback.onJoin(event.getPlayer());
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        if (callback == null) return;
        callback.onQuit(event.getPlayer());
    }

    public void unload() {
        HandlerList.unregisterAll(this);
    }
}
