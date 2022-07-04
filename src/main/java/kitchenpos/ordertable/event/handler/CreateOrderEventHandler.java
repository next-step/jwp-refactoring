package kitchenpos.ordertable.event.handler;

import kitchenpos.order.event.CreateOrderEvent;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateOrderEventHandler {
    private final TableService tableService;

    public static final String ERROR_ORDER_TABLE_EMPTY = "주문테이블은 비어있을 수 없습니다.";

    public CreateOrderEventHandler(TableService tableService) {
        this.tableService = tableService;
    }

    @Transactional
    @EventListener
    public void handle(CreateOrderEvent event) {
        OrderTable orderTable = tableService.findOrderTableById(event.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_EMPTY);
        }
    }
}
