package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.OrderTableService;
import kitchenpos.tablegroup.exception.CanNotGroupException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableIdsTableGroupValidator {
    private final OrderTableService orderTableService;

    public OrderTableIdsTableGroupValidator(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    public void validate(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableService.getOrderTablesByIdIn(orderTableIds);
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty()) {
                throw new CanNotGroupException("주문 테이블이 빈상태일 떄만 단체지정을 생성할 수 있습니다.");
            }
        }
    }
}
