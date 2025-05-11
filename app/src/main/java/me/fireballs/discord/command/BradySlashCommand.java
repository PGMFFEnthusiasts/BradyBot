package me.fireballs.discord.command;

import me.fireballs.server.query.ServerStatusProvider;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class BradySlashCommand implements DiscordSlashCommand {
    private final ServerStatusProvider serverStatusProvider;

    public BradySlashCommand(final ServerStatusProvider serverStatusProvider) {
        this.serverStatusProvider = serverStatusProvider;
    }

    @Override
    public String getCommandName() {
        return "brady";
    }

    @Override
    public String getCommandDescription() {
        return "Check how many people are on the server";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(final SlashCommandInteractionEvent event) {
        int playerCount = serverStatusProvider.getPlayerCount();
        event.reply(
            String.format("There %s online", playerCount == 1 ?
                "is 1 Brady'er" :
                "are " + playerCount + " Brady'ers"
            )
        ).queue();
    }
}
