package kitchenpos.table.validator;

import java.util.List;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderCreationValidator implements OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderCreationValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(Order order) {
        validateOrderTable(order);
        validateOrderLineItems(order.getOrderLineItems());
    }

    private void validateOrderTable(Order order) {
        final OrderTable orderTable = findOrderTableById(order.getOrderTableId());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에서 주문을 받을 수 없습니다.");
        }
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(
                () -> new IllegalArgumentException(String.format("주문 테이블(%d)을 찾을 수 없습니다.", orderTableId)));
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 없는 주문을 생성할 수 없습니다.");
        }
    }
}
