package kitchenpos.order.application;

import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        validOrderLineItem(orderRequest.getOrderLineItems());
        final List<OrderLineItem> orderLineItems = orderRequest.toOrderLineItems();
        final OrderTable orderTable = findOrderTable(orderRequest);
        final Order persistOrder = orderRepository.save(new Order(orderTable, OrderStatus.COOKING, orderLineItems));
        return OrderResponse.of(persistOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest order) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.updateOrderStatus(order.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    private void validOrderLineItem(List<OrderLineItemRequest> orderLineItemsRequest) {
        List<Menu> menus = findMenuAllById(orderLineItemsRequest);
        validOrderLineItemEmpty(orderLineItemsRequest);
        validOrderLIneItemCount(orderLineItemsRequest, menus.size());
    }

    private OrderTable findOrderTable(OrderRequest orderRequest) {
        return orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("등록이 안된 주문 테이블에서는 주문할 수 없습니다."));
    }

    private void validOrderLineItemEmpty(List<OrderLineItemRequest> orderLineItemsRequest) {
        if (CollectionUtils.isEmpty(orderLineItemsRequest)) {
            throw new IllegalArgumentException("주문에는 메뉴가 1개 이상 필요합니다.");
        }
    }
    private void validOrderLIneItemCount(List<OrderLineItemRequest> orderLineItemsRequest, int menuSize) {
        if (orderLineItemsRequest.size() != menuSize) {
            throw new IllegalArgumentException("등록된 메뉴만 주문할 수 있습니다.");
        }
    }

    private List<Menu> findMenuAllById(List<OrderLineItemRequest> orderLineItemsRequest) {
        final List<Long> menuIds = orderLineItemsRequest.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        return menuRepository.findAllById(menuIds);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
    }
}
