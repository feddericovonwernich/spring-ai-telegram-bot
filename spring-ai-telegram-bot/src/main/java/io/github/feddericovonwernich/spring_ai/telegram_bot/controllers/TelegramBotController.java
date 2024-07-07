package io.github.feddericovonwernich.spring_ai.telegram_bot.controllers;

import io.github.feddericovonwernich.spring_ai.function_calling_service.spi.AssistantResponse;
import io.github.feddericovonwernich.spring_ai.function_calling_service.spi.AssistantService;
import io.github.feddericovonwernich.spring_ai.telegram_bot.commands.BotCommand;
import io.github.feddericovonwernich.spring_ai.telegram_bot.models.AssistantThread;
import io.github.feddericovonwernich.spring_ai.telegram_bot.services.AssistantThreadService;
import io.github.feddericovonwernich.spring_ai.telegram_bot.services.AssistantThreadServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TelegramBotController implements LongPollingSingleThreadUpdateConsumer {

    private static final Logger log = LoggerFactory.getLogger(TelegramBotController.class);

    private final TelegramClient telegramClient;

    private final AssistantService assistantService;

    private final AssistantThreadService assistantThreadService;

    private final ApplicationContext applicationContext;

    private Map<String, BotCommand> botCommandMap;

    public TelegramBotController(ApplicationContext applicationContext, TelegramClient telegramClient, AssistantService assistantService, AssistantThreadService assistantThreadService) {
        this.assistantThreadService = assistantThreadService;
        log.info("Initializing Telegram bot.");
        this.telegramClient = telegramClient;
        this.assistantService = assistantService;
        this.applicationContext = applicationContext;
    }

    private Map<String, BotCommand> resolveCommands() {
        Map<String, BotCommand> commands = applicationContext.getBeansOfType(BotCommand.class);
        Map<String, BotCommand> registeredCommands = commands.values().stream().collect(Collectors.toMap(BotCommand::getCommand, Function.identity()));
        registeredCommands.forEach((command, botCommand) -> log.info("Registered Command: " + command + ", Class: " + botCommand.getClass().getSimpleName()));
        return registeredCommands;
    }

    public Map<String, BotCommand> getBotCommandMap() {
        if (botCommandMap == null) {
            botCommandMap = resolveCommands();
        }
        return botCommandMap;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String chatText = update.getMessage().getText();
            String chatId = String.valueOf(update.getMessage().getChatId());
            String userId = String.valueOf(update.getMessage().getChat().getId());

            AssistantResponse assistantResponse;

            String messageResponse;
            Map<String, BotCommand> commands = getBotCommandMap();
            if (commands.containsKey(chatText)) {
                // Execute the command.
                BotCommand command = commands.get(chatText);
                messageResponse = command.execute(update);
            } else {
                 // look for existing thread.
                 AssistantThread assistantThread = assistantThreadService.getLatestThreadForUser(userId);
                 if (assistantThread == null) {
                    assistantResponse = assistantService.processRequest(chatText);
                    assistantThreadService.saveThreadIdForUser(userId, assistantResponse);
                    messageResponse = assistantResponse.getResponse();
                 } else {
                    messageResponse = assistantService.processRequest(chatText, assistantThread.getThreadId());
                 }
            }

            // Create your send message object
            SendMessage sendMessage = new SendMessage(chatId, messageResponse);

            try {
                // Execute it
                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Error from telegram client.", e);
            }
        }
    }

}
