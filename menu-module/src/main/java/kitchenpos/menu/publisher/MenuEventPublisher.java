package kitchenpos.menu.publisher;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.event.CreateMenuEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MenuEventPublisher {

    private final ApplicationEventPublisher eventPublisher;


    public MenuEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void createMenuValidPublishEvent(MenuRequest menuRequest) {
        CreateMenuEvent createMenuEvent = new CreateMenuEvent(menuRequest);
        eventPublisher.publishEvent(createMenuEvent);
    }
}
