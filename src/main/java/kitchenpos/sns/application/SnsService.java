package kitchenpos.sns.application;

import kitchenpos.sns.strategy.SenderStrategy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SnsService {

    @Async
    @EventListener
    public void send(SenderStrategy strategy) {
        strategy.send();
    }

}
