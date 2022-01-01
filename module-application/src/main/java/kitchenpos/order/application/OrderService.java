package kitchenpos.order.application;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = orderRequest.toOrder();
        validateCreating(order);
        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order persistOrder = findOrderById(orderId);
        persistOrder.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(persistOrder);
    }

    private void validateCreating(Order order) {
        checkOrderTableIsValid(order.getOrderTableId());
        checkOrderLineItemMenuIsExists(order.getOrderLineItems());
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("해당 주문을 찾을 수 없습니다."));
    }

    private void checkOrderTableIsValid(Long orderTableId) {
        orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException("해당 주문 테이블을 찾을 수 없습니다."));
    }

    private void checkOrderLineItemMenuIsExists(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(it -> menuRepository.findById(it.getMenuId())
                .orElseThrow(() -> new NotFoundException("해당 메뉴를 찾을 수 없습니다.")));
    }
}
