package kitchenpos.sns.application;

import kitchenpos.sns.strategy.SenderStrategy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SnsEventHandler {

    private final SnsService snsService;

    public SnsEventHandler(SnsService snsService) {
        this.snsService = snsService;
    }

    @EventListener
    public void sns(SenderStrategy message) {
        snsService.send(message);
    }

}
