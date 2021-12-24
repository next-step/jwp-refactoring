package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.exception.OrderLineItemNotFoundException;
import kitchenpos.order.application.exception.OrderNotFoundException;
import kitchenpos.order.application.exception.OrderTableNotFoundException;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final List<OrderLineItem> orderLineItems = getOrderLineItems(request.getItems());
        final OrderTable orderTable = getOrderTable(request.getTableId());

        Order order = request.toEntity(orderTable, orderLineItems);
        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order order = getOrder(orderId);
        order.setOrderStatus(request.getOrderStatus());
        return OrderResponse.of(order);
    }

    private OrderTable getOrderTable(Long tableId) {
        return orderTableRepository.findById(tableId)
                .orElseThrow(OrderTableNotFoundException::new);
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> requests) {
        return requests.stream().map(this::getOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem getOrderLineItem(OrderLineItemRequest request) {
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(OrderLineItemNotFoundException::new);
        return request.toEntity(menu);
    }
}
