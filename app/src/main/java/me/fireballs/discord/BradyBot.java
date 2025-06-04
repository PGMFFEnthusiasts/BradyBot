package me.fireballs.discord;

import me.fireballs.discord.listener.DiscordListener;
import me.fireballs.server.listener.ServerListenerCallback;
import me.fireballs.server.query.ServerStatusProvider;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Set;

public class BradyBot {
    private static final Set<Long> ALLOWLISTED_GUILDS = Set.of(
        416767292320514048L, // test
        1365177238320058409L // pgm ff
    );
    private final ServerStatusProvider serverStatusProvider;
    private final String token;
    private JDA jda;
    private DiscordListener listener;
    private int activityUpdateTask = -1;
    private boolean botReady;

    public BradyBot(final ServerStatusProvider serverStatusProvider, final String token) {
        this.serverStatusProvider = serverStatusProvider;
        this.token = token;
    }

    public void load() {
        this.serverStatusProvider.load();
        JDALogger.setFallbackLoggerEnabled(false);
        this.jda = JDABuilder.createDefault(token).build();
        this.listener = new DiscordListener(jda, serverStatusProvider, () -> botReady = true);
        this.jda.addEventListener(this.listener);
        this.activityUpdateTask =
                this.serverStatusProvider.scheduleRepeatingTask(this::updateActivity, 20L * 15L);
        this.serverStatusProvider.registerCallback(new ServerListenerCallback() {
            @Override
            public void onJoin(Player player) {
                BradyBot.this.updateActivity();
            }

            @Override
            public void onQuit(Player player) {
                BradyBot.this.updateActivity();
            }
        });
    }

    private void updateActivity() {
        if (!botReady) return;

        this.serverStatusProvider.scheduleAsynchronousTask(
            () -> {
                var onlineCount = this.serverStatusProvider.getPlayerCount();
                final String message;
                if (onlineCount == 0) {
                    message = "TB | Nobody :(";
                } else {
                    message = "TB | " + onlineCount + " online";
                }
                this.jda.getPresence().setActivity(
                    Activity.of(
                        Activity.ActivityType.PLAYING,
                       message
                    )
                );
            },
            1L
        );
    }

    public void unload() {
        this.serverStatusProvider.unload();
        this.jda.shutdownNow();
        if (this.activityUpdateTask != -1) {
            Bukkit.getScheduler().cancelTask(this.activityUpdateTask);
            this.activityUpdateTask = -1;
        }
        try {
            this.jda.awaitShutdown(Duration.ofMillis(65L));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
