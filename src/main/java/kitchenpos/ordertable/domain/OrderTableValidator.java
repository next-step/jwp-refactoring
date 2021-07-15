package kitchenpos.ordertable.domain;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static kitchenpos.order.domain.OrderStatus.*;

import org.springframework.stereotype.Component;

import kitchenpos.generic.exception.IllegalOperationException;
import kitchenpos.generic.exception.OrderNotCompletedException;
import kitchenpos.generic.exception.OrderTableNotFoundException;
import kitchenpos.order.domain.OrderRepository;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateChangeTableStatus(OrderTable orderTable) {
        if (!exists(orderTable)) {
            throw new OrderTableNotFoundException("해당 ID의 테이블이 존재하지 않습니다.");
        }

        if (orderTable.hasTableGroup()) {
            throw new IllegalOperationException("테이블 그룹에 포함되어 있어 변경할 수 없습니다.");
        }

        if (includeOrderInProgress(orderTable)) {
            throw new OrderNotCompletedException("테이블에 완결되지 않은 주문이 존재합니다.");
        }
    }

    private boolean includeOrderInProgress(OrderTable orderTable) {
        return orderRepository.existsAllByOrderTableIdInAndOrderStatusIn(
            singletonList(orderTable.getId()), asList(COOKING, MEAL));
    }

    public void validateChangeNumberOfGuests(OrderTable orderTable) {
        if (!exists(orderTable)) {
            throw new OrderTableNotFoundException("해당 ID의 테이블이 존재하지 않습니다.");
        }

        if (orderTable.isEmpty()) {
            throw new IllegalOperationException("빈 테이블 입니다.");
        }
    }

    private boolean exists(OrderTable orderTable) {
        return orderTableRepository.existsById(orderTable.getId());
    }
}
