package kitchenpos.order.domain;

import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.TableChangeEmptyValidator;
import kitchenpos.table.domain.TableUngroupValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderCompletionValidator implements TableChangeEmptyValidator, TableUngroupValidator {
    private static final String NOT_COMPLETION_ORDER_IS_EXIST = "계산완료되지 않은 주문이 존재합니다";
    private final OrderRepository orderRepository;

    public OrderCompletionValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        if (isAnyIncompletion(orders)) {
            throw new IllegalArgumentException(NOT_COMPLETION_ORDER_IS_EXIST);
        }
    }

    @Override
    public void validate(List<Long> orderTableIds) {
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        if (isAnyIncompletion(orders)) {
            throw new IllegalArgumentException(NOT_COMPLETION_ORDER_IS_EXIST);
        }
    }

    private boolean isAnyIncompletion(List<Order> orders) {
        return orders.stream()
                .anyMatch(this::isIncompletion);
    }

    private boolean isIncompletion(Order order) {
        return !order.isCompletion();
    }
}
