package kitchenpos.table.application;

import kitchenpos.order.application.OrderTableValidateEvent;
import kitchenpos.order.exception.OrderTableEmptyException;
import kitchenpos.table.domain.OrderTable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Component
public class OrderTableHandler {

    private final TableService tableService;

    public OrderTableHandler(TableService tableService) {
        this.tableService = tableService;
    }

    @EventListener
    public void validateOrder(OrderTableValidateEvent event) {
        Long orderTableId = event.getOrderTableId();

        validateOrderTableExists(orderTableId);
        validateOrderTableIsEmpty(orderTableId);
    }

    private void validateOrderTableExists(Long orderTableId) {
        if (!tableService.existsById(orderTableId)) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
    }

    private void validateOrderTableIsEmpty(Long orderTableId) {
        OrderTable orderTable = tableService.findById(orderTableId);
        if (orderTable.getEmpty().isEmpty()) {
            throw new OrderTableEmptyException();
        }
    }
}
