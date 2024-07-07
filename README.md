# spring-ai-telegram-bot
An AI powered Telegram bot, ready to plug into any system.

## Getting started.

Getting started is very simple, just having the following configurations set will do it:

`application.yml`
```yaml
telegram:
  bot:
    enabled: true
    key: dummykey
```

With these configurations, you'll have a fully functional bot backed by https://github.com/feddericovonwernich/spring-ai-function-calling-service

## Spring Data JPA

This repository can autoconfigure itself and work out-of-the-box if Spring Data JPA is on the classpath. If you're not 
using Spring Data JPA, then you need to provide your own implementation of 
`io.github.feddericovonwernich.spring_ai.telegram_bot.services.AssistantThreadService` for the bot to work properly.

## Registering commands.

You can further customize behavior of the bot by implementing commands. You need to create a class that implements 
`io.github.feddericovonwernich.spring_ai.telegram_bot.commands.BotCommand` and it should be registered as a bean, in any way
you want.

Example:

```java
public class NewThreadBotCommand implements BotCommand {

    private final AssistantService assistantService;
    private final AssistantThreadServiceImpl assistantThreadService;

    public NewThreadBotCommand(AssistantService assistantService, AssistantThreadServiceImpl assistantThreadService) {
        this.assistantService = assistantService;
        this.assistantThreadService = assistantThreadService;
    }

    @Override
    public String execute(Update update) {
        String userId = String.valueOf(update.getMessage().getChat().getId());

        // start a new thread with the assistant.
        AssistantResponse assistantResponse = assistantService.processRequest("Hey assistant.");
        assistantThreadService.saveThreadIdForUser(userId, assistantResponse);
        return assistantResponse.getResponse();
    }

    @Override
    public String getCommand() {
        return "/newThread";
    }

}
```

Above implementation starts a new thread with the configured assistant. You can make it do anything you want really.

Bean registration:

```
...

@Bean
public NewThreadBotCommand newThreadBotCommand(AssistantService assistantService,
                                               AssistantThreadServiceImpl assistantThreadService) {
    return new NewThreadBotCommand(assistantService, assistantThreadService);
}

...
```