package me.fireballs.server.listener;

import org.bukkit.entity.Player;

public interface ServerListenerCallback {
    default void onJoin(final Player player) {}
    default void onQuit(final Player player) {}
}
