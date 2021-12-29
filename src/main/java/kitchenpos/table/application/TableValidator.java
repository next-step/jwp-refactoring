package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class TableValidator {

    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrderStatus(OrderTable orderTable) {
        Order order = orderRepository.findByOrderTableId(orderTable.getId());
        boolean isNotCooking = order.isNotCooking();
        if (isNotCooking) {
            throw new IllegalArgumentException("주문 테이블의 상태가 조리이거나, 식사이면 변경할 수 없습니다.");
        }
    }
}
