package me.fireballs.discord.listener;

import me.fireballs.discord.command.BradySlashCommand;
import me.fireballs.discord.command.DiscordSlashCommand;
import me.fireballs.server.query.ServerStatusProvider;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscordListener extends ListenerAdapter {
    private final JDA jda;
    private final ServerStatusProvider serverStatusProvider;
    private final Runnable onReadyCallback;
    public Map<String, DiscordSlashCommand> discordCommands;

    public DiscordListener(final JDA jda, final ServerStatusProvider serverStatusProvider, Runnable onReadyCallback) {
        this.jda = jda;
        this.serverStatusProvider = serverStatusProvider;
        this.onReadyCallback = onReadyCallback;
    }

    @Override
    public void onSlashCommandInteraction(final SlashCommandInteractionEvent event) {
        if (discordCommands == null) {
            event.reply("I cannot help you just yet").queue();
            return;
        }
        if (!discordCommands.containsKey(event.getName())) {
            event.reply("I don't think I can help you with that").queue();
            return;
        }
        discordCommands.get(event.getName()).execute(event);
    }

    @Override
    public void onReady(final ReadyEvent event) {
        this.registerCommands();
        this.onReadyCallback.run();
    }

    private void registerCommands() {
        final List<DiscordSlashCommand> commands = List.of(
            new BradySlashCommand(serverStatusProvider) // slash brady
        );
        final Map<String, DiscordSlashCommand> discordCommands = new HashMap<>();
        final List<SlashCommandData> slashCommandData = new ArrayList<>();
        for (final DiscordSlashCommand slashCommand : commands) {
            discordCommands.put(slashCommand.getCommandName(), slashCommand);
            slashCommandData.add(
                    Commands.slash(slashCommand.getCommandName(), slashCommand.getCommandDescription())
                            .setDefaultPermissions(DefaultMemberPermissions.ENABLED )
            );
        }

        Bukkit.getLogger().info("I am in " + jda.getGuilds().size() + " guild(s)");
        for (final Guild guild : jda.getGuilds()) {
            Bukkit.getLogger().info("Registered commands to " + guild.getName());
            guild.updateCommands().addCommands(slashCommandData).queue();
        }
        this.discordCommands = discordCommands;
    }
}
