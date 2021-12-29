package kitchenpos.table.application;

import kitchenpos.order.domain.OrderCompletionEvent;
import kitchenpos.table.dto.TableRequest;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderCompletionEventHandler implements ApplicationListener<OrderCompletionEvent> {

    private final TableService tableService;

    public OrderCompletionEventHandler(TableService tableService) {
        this.tableService = tableService;
    }

    @Async
    @Override
    public void onApplicationEvent(OrderCompletionEvent event) {
        tableService.changeEmpty(event.getTableId(), new TableRequest(0, true));
    }
}
