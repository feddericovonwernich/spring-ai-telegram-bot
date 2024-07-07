package io.github.feddericovonwernich.spring_ai.telegram_bot.commands;

import io.github.feddericovonwernich.spring_ai.function_calling_service.spi.AssistantService;
import io.github.feddericovonwernich.spring_ai.telegram_bot.conditions.BotEnabledCondition;
import io.github.feddericovonwernich.spring_ai.telegram_bot.conditions.BotKeyPresentCondition;
import io.github.feddericovonwernich.spring_ai.telegram_bot.services.AssistantThreadServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional({BotEnabledCondition.class, BotKeyPresentCondition.class})
public class CommandConfiguration {

    @Bean
    public NewThreadBotCommand newThreadBotCommand(AssistantService assistantService,
                                                   AssistantThreadServiceImpl assistantThreadService) {
        return new NewThreadBotCommand(assistantService, assistantThreadService);
    }

}
