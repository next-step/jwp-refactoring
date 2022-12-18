package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        List<OrderLineItem> orderLineItems = toOrderLineItems(orderRequest.getOrderLineItemRequests());
        OrderValidator.validateCreateOrder(orderTable, orderLineItems);
        Order order = orderRepository.save(orderRequest.toOrder(orderLineItems));
        return OrderResponse.from(order);
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderRequest orderRequest) {
        Order order = findOrderById(orderId);
        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        validateNotDuplicatedMenuIds(orderLineItemRequests);
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> orderLineItemRequest.toOrderLineItem(findMenuById(orderLineItemRequest.getMenuId())))
                .collect(Collectors.toList());
    }

    private void validateNotDuplicatedMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        boolean duplicatedMenuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .distinct()
                .count() != orderLineItemRequests.size();
        if (duplicatedMenuIds) {
            throw new IllegalArgumentException("메뉴가 중복되었습니다.");
        }
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }

    public OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }

    public Menu findMenuById(Long id) {
        return menuRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));
    }
}
