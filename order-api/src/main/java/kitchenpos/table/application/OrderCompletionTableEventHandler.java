package kitchenpos.table.application;

import kitchenpos.common.event.orderCompletion.OrderCompletionEvent;
import kitchenpos.common.event.orderCompletion.OrderCompletionEventHandler;
import kitchenpos.table.dto.TableRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderCompletionTableEventHandler extends OrderCompletionEventHandler {

    private final TableService tableService;

    public OrderCompletionTableEventHandler(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public void onApplicationEvent(OrderCompletionEvent event) {
        tableService.changeEmpty(event.getTableId(), new TableRequest(0, true));
    }
}
