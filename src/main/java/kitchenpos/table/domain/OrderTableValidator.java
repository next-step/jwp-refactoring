package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTableValidator {
    private static final String ORDER_TABLE_IS_NOT_EXIST = "주문테이블이 존재하지 않습니다";
    private static final String NOT_COMPLETION_ORDER_IS_EXIST = "계산완료되지 않은 주문이 존재합니다";
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public OrderTable findExistsOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_TABLE_IS_NOT_EXIST));
    }

    public List<OrderTable> findExistsOrderTableByIdIn(List<Long> ids) {
        List<OrderTable> orderTables = orderTableRepository.findByIdIn(ids);
        if (orderTables.size() != ids.size()) {
            throw new IllegalArgumentException(ORDER_TABLE_IS_NOT_EXIST);
        }
        return orderTables;
    }

    public void checkOrderStatus(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        if (!isAllCompletion(orders)) {
            throw new IllegalArgumentException(NOT_COMPLETION_ORDER_IS_EXIST);
        }
    }

    public void checkOrderStatusIn(List<Long> orderTableIds) {
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        if (!isAllCompletion(orders)) {
            throw new IllegalArgumentException(NOT_COMPLETION_ORDER_IS_EXIST);
        }
    }

    private boolean isAllCompletion(List<Order> orders) {
        return orders.stream()
                .allMatch(this::isCompletion);
    }

    private boolean isCompletion(Order order) {
        return order.isCompletion();
    }
}
