package kitchenpos.table.domain;

import kitchenpos.order.service.OrderStatusService;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {
    private final OrderStatusService orderStatusService;

    public TableValidator(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public void isPossibleUnGroup(OrderTable tableId) {
        if (orderStatusService.isNotCompleteOrder(tableId)) {
            throw new IllegalArgumentException("주문이 완료되지 않아 그룹 해제가 불가능합니다.");
        }
    }

}
