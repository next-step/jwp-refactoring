package kitchenpos.application.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.*;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order order = Order.of(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now());
        for (final OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItemRequests()) {
            Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId()).orElseThrow(IllegalArgumentException::new);
            order.addOrderLineItem(OrderLineItem.of(order, menu, orderLineItemRequest.getQuantity()));
        }

        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        savedOrder.setOrderStatus(OrderStatus.valueOf(orderStatus).name());
        return OrderResponse.of(savedOrder);
    }
}
