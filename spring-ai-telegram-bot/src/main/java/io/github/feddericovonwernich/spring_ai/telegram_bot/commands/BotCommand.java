package io.github.feddericovonwernich.spring_ai.telegram_bot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotCommand {

    String execute(Update update);

    String getCommand();

}
