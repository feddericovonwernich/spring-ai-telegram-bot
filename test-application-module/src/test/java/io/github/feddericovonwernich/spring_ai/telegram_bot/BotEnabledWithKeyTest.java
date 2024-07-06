package io.github.feddericovonwernich.spring_ai.telegram_bot;

import io.github.feddericovonwernich.spring_ai.telegram_bot.commands.BotCommand;
import io.github.feddericovonwernich.spring_ai.telegram_bot.controllers.TelegramBotController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("true-with-key")
@TestPropertySource(locations = "classpath:application-true-with-key.yml")
class BotEnabledWithKeyTest extends BasicApplicationIntegrationTest {

    @Autowired
    private TelegramBotController telegramBotController;

    @Test
    void contextLoads() {
    }

    @Test
    void testBotServiceBeanExists() {
        assertThat(telegramBotController).isNotNull();
    }

    @Test
    void testRegisteredCommands() {
        assertThat(telegramBotController.getBotCommandMap().size())
                .isEqualTo(1);

        assertThat(telegramBotController.getBotCommandMap().containsKey("/newThread"))
                .isTrue();
    }

}
