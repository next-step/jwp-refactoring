package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository,
                        MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest, LocalDateTime orderedTime) {
        OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        Menus menus = findMenus(orderRequest.getMenuIds());
        OrderLineItems orderLineItems = createOrderLineItems(orderRequest.getOrderLineItems(), menus);
        Order order = new Order(orderedTime, orderTable, orderLineItems);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findOrder(orderId);
        order.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));
        return OrderResponse.from(order);
    }

    private OrderLineItems createOrderLineItems(List<OrderLineItemRequest> requestOrderLineItems, Menus menus) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : requestOrderLineItems) {
            Menu menu = menus.getMenuBy(orderLineItemRequest.getMenuId());
            OrderLineItem orderLineItem = new OrderLineItem(menu.getId(),
                    new Quantity(orderLineItemRequest.getQuantity()));
            orderLineItems.add(orderLineItem);
        }
        return new OrderLineItems(orderLineItems);
    }

    private Menus findMenus(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        validateRequestMenus(menus, menuIds);
        return new Menus(menus);
    }

    private void validateRequestMenus(List<Menu> menus, List<Long> menuIds) {
        if (menus.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 등록된 메뉴가 없습니다.");
        }
        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException("[ERROR] 등록 되어있지 않은 메뉴가 있습니다.");
        }
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 주문이 등록되어있지 않습니다."));
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테이블이 등록되어있지 않습니다."));
    }

}
