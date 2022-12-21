package kitchenpos.order.domain;


import kitchenpos.exception.BadRequestException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.utils.Message.*;

@Component
public class OrderValidatorImpl implements OrderValidator {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;


    public OrderValidatorImpl(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(Long orderTableId) {
        OrderTable orderTable = findOrderTableById(orderTableId);

        if (orderTable.isEmpty()) {
            throw new BadRequestException(NOT_EXISTS_ORDER_TABLE);
        }
    }

    @Override
    public void checkEmptyChangeable(Long orderTableId) {
        Orders orders = Orders.from(orderRepository.findAllByOrderTableId(orderTableId));
        if (orders.anyMatchedIn(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new BadRequestException(INVALID_CHANGE_TO_EMPTY_ORDER_STATUS);
        }
    }

    @Override
    public void checkCanBeUngrouped(List<Long> orderTableIds) {
        Orders orders = Orders.from(orderRepository.findAllByOrderTableIdIn(orderTableIds));
        if (orders.anyMatchedIn(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new BadRequestException(INVALID_CANCEL_ORDER_TABLES_STATUS);
        }
    }


    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException(EMPTY_ORDER_TABLE));
    }
}
