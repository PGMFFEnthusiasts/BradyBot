package me.fireballs.discord.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface DiscordSlashCommand {
    String getCommandName();
    String getCommandDescription();
    List<OptionData> getOptions();
    void execute(SlashCommandInteractionEvent event);
}
