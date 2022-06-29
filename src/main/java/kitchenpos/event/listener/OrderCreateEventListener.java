package kitchenpos.event.listener;

import kitchenpos.event.customEvent.OrderCreateEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreateEventListener implements ApplicationListener<OrderCreateEvent> {

    @Override
    public void onApplicationEvent(OrderCreateEvent event) {
        System.out.println("ORDERCREATEEVENT PUBLISHED");

    }
}
