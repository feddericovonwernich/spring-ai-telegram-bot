package io.github.feddericovonwernich.spring_ai.telegram_bot;

import io.github.feddericovonwernich.spring_ai.function_calling_service.openia.StandardOpenIAAssistantService;
import io.github.feddericovonwernich.spring_ai.telegram_bot.controllers.TelegramBotController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("false")
@TestPropertySource(locations = "classpath:application-false.yml")
class BotDisabledTest extends BasicApplicationIntegrationTest {

    @Autowired(required = false)
    TelegramBotController telegramBotController;

    @Test
    void contextLoads() {
    }

    @Test
    void testAssistantServiceBeanExists() {
        assertThat(telegramBotController).isNull();
    }

}
