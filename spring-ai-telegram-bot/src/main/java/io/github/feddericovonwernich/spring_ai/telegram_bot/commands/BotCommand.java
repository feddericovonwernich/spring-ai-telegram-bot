package io.github.feddericovonwernich.spring_ai.telegram_bot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Provides the structure for bot commands within the application.
 * This interface defines essential methods that bot commands must implement
 * to interact with and respond to incoming updates from Telegram.
 */
public interface BotCommand {

    /**
     * Executes the action associated with this bot command.
     *
     * @param update The incoming update from Telegram that triggers this command.
     * @return A String result of the command execution, which may vary depending on the command's nature.
     */
    String execute(Update update);

    /**
     * Retrieves the command identifier as a String. This identifier is used to match incoming
     * update messages with the appropriate command.
     *
     * @return The String identifier of the bot command.
     */
    String getCommand();

}
