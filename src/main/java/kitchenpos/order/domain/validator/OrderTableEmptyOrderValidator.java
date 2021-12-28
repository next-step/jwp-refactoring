package kitchenpos.order.domain.validator;

import kitchenpos.order.exception.CanNotOrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.application.OrderTableService;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEmptyOrderValidator implements OrderTableValidator {
    private static final String EMPTY_ERROR_MESSAGE = "테이블이 비어있을 경우 주문을 할 수 없습니다.";
    private final OrderTableService orderTableService;

    public OrderTableEmptyOrderValidator(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    public void validate(Long orderTableId) {
        final OrderTable orderTable = orderTableService.getOrderTable(orderTableId);
        if (orderTable.isEmpty()) {
            throw new CanNotOrderException(EMPTY_ERROR_MESSAGE);
        }
    }
}
