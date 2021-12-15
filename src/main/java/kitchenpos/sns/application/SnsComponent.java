package kitchenpos.sns.application;

import kitchenpos.sns.strategy.SenderStrategy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SnsComponent {

    @Async
    @EventListener
    public void send(SenderStrategy strategy) {
        strategy.send();
    }

}
