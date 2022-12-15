package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.order.exception.EmptyOrderTableException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.CannotChangeEmptyException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public void checkOrderTableIsNotEmpty(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ORDER_TABLE_NOT_FOUND));

        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException(ExceptionMessage.EMPTY_ORDER_TABLE);
        }
    }

    public void checkEmptyChangeable(Long orderTableId) {
        Orders orders = Orders.from(orderRepository.findAllByOrderTableId(orderTableId));
        checkOrdersNotCookingOrMeal(orders);
    }

    public void checkUnGroupable(List<Long> orderTableIds) {
        Orders orders = Orders.from(orderRepository.findAllByOrderTableIdIn(orderTableIds));
        checkOrdersNotCookingOrMeal(orders);
    }

    private void checkOrdersNotCookingOrMeal(Orders orders) {
        if (orders.anyMatchedIn(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new CannotChangeEmptyException(ExceptionMessage.CAN_NOT_CHANGE_EMPTY_WHEN_COOKING_OR_MEAL);
        }
    }
}
