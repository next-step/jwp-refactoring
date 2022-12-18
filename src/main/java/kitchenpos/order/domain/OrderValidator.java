package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;

@Component
public class OrderValidator {
    private static final String EXCEPTION_MESSAGE_ORDER_TABLE_IS_EMPTY = "빈 테이블에선 주문할 수 없습니다.";
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        validate(order, getOrderTable(order.getOrderTableId()));
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(NoResultException::new);
    }

    private void validate(Order order, OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalStateException(EXCEPTION_MESSAGE_ORDER_TABLE_IS_EMPTY);
        }
    }
}
