package me.fireballs.server.query;

import me.fireballs.server.listener.ServerListenerCallback;
import org.bukkit.scheduler.BukkitTask;

public interface ServerStatusProvider {
    void load();
    int getPlayerCount();
    void registerCallback(ServerListenerCallback callback);
    int scheduleRepeatingTask(Runnable task, long period);
    BukkitTask scheduleAsynchronousTask(Runnable task, long delay);
    void unload();
}
