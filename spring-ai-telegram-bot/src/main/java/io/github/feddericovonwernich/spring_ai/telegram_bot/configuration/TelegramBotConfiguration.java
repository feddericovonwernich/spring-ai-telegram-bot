package io.github.feddericovonwernich.spring_ai.telegram_bot.configuration;

import io.github.feddericovonwernich.spring_ai.function_calling_service.spi.AssistantService;
import io.github.feddericovonwernich.spring_ai.telegram_bot.conditions.BotEnabledCondition;
import io.github.feddericovonwernich.spring_ai.telegram_bot.conditions.BotKeyPresentCondition;
import io.github.feddericovonwernich.spring_ai.telegram_bot.controllers.TelegramBotController;
import io.github.feddericovonwernich.spring_ai.telegram_bot.repositories.AssistantThreadRepository;
import io.github.feddericovonwernich.spring_ai.telegram_bot.services.AssistantThreadServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@AutoConfigurationPackage(basePackages = {
        "io.github.feddericovonwernich.spring_ai.telegram_bot.repositories",
        "io.github.feddericovonwernich.spring_ai.telegram_bot.models"
})
@Conditional({BotEnabledCondition.class, BotKeyPresentCondition.class})
public class TelegramBotConfiguration {

    Logger log = LoggerFactory.getLogger(TelegramBotConfiguration.class);

    @Value("${telegram.bot.key}")
    private String botKey;

    @Value("${telegram.bot.register:true}")
    private Boolean registerBot;

    @Bean
    @ConditionalOnBean(AssistantService.class)
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(botKey);
    }

    @Bean
    @ConditionalOnBean(AssistantService.class)
    public TelegramBotController telegramBotController(ApplicationContext applicationContext,
                                                       TelegramClient telegramClient,
                                                       AssistantService assistantService,
                                                       AssistantThreadServiceImpl assistantThreadService) throws TelegramApiException {
        TelegramBotController telegramBotController
                = new TelegramBotController(applicationContext, telegramClient, assistantService, assistantThreadService);

        if (registerBot) {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botKey, telegramBotController);
            log.info("Successfully registered bot.");
        }

        return telegramBotController;
    }

    @Bean
    public AssistantThreadServiceImpl assistantThreadService(AssistantThreadRepository assistantThreadRepository) {
        return new AssistantThreadServiceImpl(assistantThreadRepository);
    }

}
