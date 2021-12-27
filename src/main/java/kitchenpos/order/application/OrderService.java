package kitchenpos.order.application;

import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    // todo : orderTableService 로 바꾸기
    private final OrderTableRepository orderTableRepository;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
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
                .orElseThrow(NotFoundEntityException::new);
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
                .orElseThrow(NotFoundEntityException::new);
    }
}
