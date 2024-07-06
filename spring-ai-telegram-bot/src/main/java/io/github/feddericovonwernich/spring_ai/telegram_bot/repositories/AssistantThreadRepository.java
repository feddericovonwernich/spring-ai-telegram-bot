package io.github.feddericovonwernich.spring_ai.telegram_bot.repositories;

import io.github.feddericovonwernich.spring_ai.telegram_bot.models.AssistantThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistantThreadRepository extends JpaRepository<AssistantThread, Long> {
    AssistantThread findTopByUserIdOrderByCreationDateDesc(String userId);
}
