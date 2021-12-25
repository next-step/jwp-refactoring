package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    // todo : orderTableService 로 바꾸기
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        Order order = orderRepository.save(orderRequest.toOrder(orderTable));

        return OrderResponse.of(order);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findById(orderId);

        order.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));

        return OrderResponse.of(order);
    }

    private Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
