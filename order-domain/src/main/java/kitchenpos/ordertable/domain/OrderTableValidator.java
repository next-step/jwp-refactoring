package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderTableValidator {

    public static final String MESSAGE_VALIDATE_EMPTY_CHANGABLE = "테이블 그룹에 추가되어 있지 않아야 합니다.";
    public static final String MESSAGE_VALIDATE_ORDER_TABLE_CHANGABLE = "주문 테이블이 변경 가능해야 합니다.";

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateEmptyChangable(OrderTable orderTable) {
        validateEmptyChangable(orderTable.getTableGroupId(), findOrderTables(orderTable));
    }

    void validateEmptyChangable(Long tableGroupId, Orders orders) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_EMPTY_CHANGABLE);
        }
        orders.validateEmptyChangable();
    }

    public void validateNumberOfGuestsChangable(Empty empty) {
        empty.validateNumberOfGuestsChangable();
    }

    public void validateOrderTablUngroupable(OrderTable orderTable) {
        validateOrderTablUngroupable(findOrderTables(orderTable));
    }

    void validateOrderTablUngroupable(Orders orders) {
        if (!orders.isChangable()) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_ORDER_TABLE_CHANGABLE);
        }
    }

    private Orders findOrderTables(OrderTable orderTable) {
        return new Orders(orderRepository.findAllByOrderTableId(orderTable.getId()));
    }
}
