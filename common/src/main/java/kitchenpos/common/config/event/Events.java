package kitchenpos.common.config.event;

import org.springframework.context.ApplicationEventPublisher;

public class Events {
    private static ApplicationEventPublisher eventPublisher;

    static void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        Events.eventPublisher = eventPublisher;
    }

    static ApplicationEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    public static void raise(Object event) {
        if (eventPublisher != null) {
            eventPublisher.publishEvent(event);
        }
    }
}
