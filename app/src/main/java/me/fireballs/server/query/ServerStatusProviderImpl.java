package me.fireballs.server.query;

import me.fireballs.server.listener.ServerListener;
import me.fireballs.server.listener.ServerListenerCallback;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class ServerStatusProviderImpl implements ServerStatusProvider, Listener {
    private final JavaPlugin plugin;
    private final ServerListener listener;

    public ServerStatusProviderImpl(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.listener = new ServerListener();
    }

    @Override
    public void load() {
        listener.load(plugin);
    }

    @Override
    public int getPlayerCount() {
        return plugin.getServer().getOnlinePlayers().size();
    }

    @Override
    public void registerCallback(ServerListenerCallback callback) {
        listener.callback = callback;
    }

    @Override
    public int scheduleRepeatingTask(Runnable task, long period) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, 0L, period);
    }

    @Override
    public BukkitTask scheduleAsynchronousTask(Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
    }

    @Override
    public void unload() {
        listener.unload();
    }
}
