package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository,
                        OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = createOrderLineItems(orderRequest.getOrderLineItems());
        verifyMenuCount(orderRequest);
        OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        verifyAvailableOrderTable(orderTable);
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING, orderLineItems);
        Order saveOrder = orderRepository.save(order);
        return OrderResponse.of(saveOrder);
    }

    private void verifyAvailableOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문테이블이 빈테이블입니다.");
        }
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문테이블이 존재하지 않습니다."));
    }

    private void verifyMenuCount(OrderRequest orderRequest) {
        List<Long> menuIds = orderRequest.getOrderLineItems().stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("요청한 주문의 메뉴와 디비의 메뉴가 불일치합니다.");
        }
    }

    private List<OrderLineItem> createOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItemRequests.stream()
                .forEach(orderLineItemRequest -> orderLineItems.add(new OrderLineItem(orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity())));
        return orderLineItems;
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }
}
