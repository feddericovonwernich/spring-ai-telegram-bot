package io.github.feddericovonwernich.spring_ai.telegram_bot.conditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Configuration class that checks if an OpenAI API key condition is met.
 * This class implements the {@link Condition} interface to determine
 * the presence or absence of the OpenAI API key within the application's
 * configuration or environment.
 *
 * @author Federico von Wernich
 */
@Configuration
public class BotKeyPresentCondition implements Condition {
    Logger log = LoggerFactory.getLogger(BotKeyPresentCondition.class);

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        try {
            boolean evaluation = env.getProperty("telegram.bot.key") != null;
            if (!evaluation) log.warn("Telegram bot key needs to be set.");
            return evaluation;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}
