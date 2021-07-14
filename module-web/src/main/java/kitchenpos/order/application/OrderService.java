package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.CannotOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    public static final String CANNOT_ORDER_AN_EMPTY_TABLE = "빈 테이블은 주문할 수 없습니다.";

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new CannotOrderException(CANNOT_ORDER_AN_EMPTY_TABLE);
        }
        Order order = new Order(orderTable.getId(), OrderLineItemRequest.toOrderLineItems(orderRequest.getOrderLineItems()));
        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.of(savedOrder);
    }
}
