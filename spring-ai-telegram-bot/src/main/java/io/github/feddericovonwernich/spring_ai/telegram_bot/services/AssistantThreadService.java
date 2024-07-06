package io.github.feddericovonwernich.spring_ai.telegram_bot.services;

import io.github.feddericovonwernich.spring_ai.function_calling_service.spi.AssistantResponse;
import io.github.feddericovonwernich.spring_ai.telegram_bot.models.AssistantThread;
import io.github.feddericovonwernich.spring_ai.telegram_bot.repositories.AssistantThreadRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AssistantThreadService extends BasicService<AssistantThread, Long, AssistantThreadRepository> {
    public AssistantThreadService(AssistantThreadRepository repository) {
        super(repository);
    }

    public AssistantThread getLatestThreadForUser(String userId) {
        return repository.findTopByUserIdOrderByCreationDateDesc(userId);
    }

    public void saveThreadIdForUser(String userId, AssistantResponse assistantResponse) {
        AssistantThread assistantThread;
        String threadId = assistantResponse.getThreadId();
        if (threadId != null) {
            assistantThread = AssistantThread.builder()
                    .userId(userId)
                    .threadId(threadId)
                    .creationDate(LocalDateTime.now())
                    .build();
            this.createOrUpdate(assistantThread);
        }
    }

}
