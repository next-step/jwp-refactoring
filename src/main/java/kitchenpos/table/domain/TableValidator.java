package kitchenpos.table.domain;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {
    private static final String ERROR_MESSAGE_NOT_FOUND_BY_ORDER_TABLE_FORMAT = "주문이 존재하지 않습니다. Order Table ID : %d";

    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        orderTable.validateGrouped();
        Order order = findByOrderTableId(orderTable.id());
        order.validateCookingAndMeal();
    }

    private Order findByOrderTableId(Long id) {
        return orderRepository.findByOrderTableId(id)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ORDER_TABLE_FORMAT, id)));
    }
}
