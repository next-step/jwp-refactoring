package kitchenpos.order.application;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        final OrderTable persistOrderTable = findOrderTableById(orderRequest.getOrderTableId());
        final List<OrderLineItem> orderLineItems = getOrderLineItems(orderRequest.getOrderLineItemRequests());
        final Order order = Order.of(persistOrderTable);
        order.addOrderLineItems(orderLineItems);

        final Order persistOrder = orderRepository.save(order);

        return OrderResponse.of(persistOrder);
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

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("해당 주문을 찾을 수 없습니다."));
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(it -> OrderLineItem.of(findMenuById(it.getMenuId()), Quantity.of(it.getQuantity())))
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException("해당 주문 테이블을 찾을 수 없습니다."));
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(() -> new NotFoundException("해당 메뉴를 찾을 수 없습니다."));
    }
}
