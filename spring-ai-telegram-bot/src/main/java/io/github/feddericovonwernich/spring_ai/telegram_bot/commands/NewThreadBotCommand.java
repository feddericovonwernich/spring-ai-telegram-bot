package io.github.feddericovonwernich.spring_ai.telegram_bot.commands;

import io.github.feddericovonwernich.spring_ai.function_calling_service.spi.AssistantResponse;
import io.github.feddericovonwernich.spring_ai.function_calling_service.spi.AssistantService;
import io.github.feddericovonwernich.spring_ai.telegram_bot.services.AssistantThreadService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NewThreadBotCommand implements BotCommand {

    private final AssistantService assistantService;
    private final AssistantThreadService assistantThreadService;

    public NewThreadBotCommand(AssistantService assistantService, AssistantThreadService assistantThreadService) {
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
