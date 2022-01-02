package kitchenpos.common.event.orderCompletion;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public abstract class OrderCompletionEventHandler implements ApplicationListener<OrderCompletionEvent> {

    @Async
    @Override
    @TransactionalEventListener
    public void onApplicationEvent(OrderCompletionEvent event) {
    }
}
