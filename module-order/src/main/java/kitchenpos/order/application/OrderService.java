package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderCreateEvent;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository,
                        MenuRepository menuRepository,
                        ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest, LocalDateTime orderedTime) {
        Menus menus = findMenus(orderRequest.getMenuIds());
        OrderLineItems orderLineItems = createOrderLineItems(orderRequest.getOrderLineItems(), menus);
        Order order = new Order(orderedTime, orderRequest.getOrderTableId(), orderLineItems);
        eventPublisher.publishEvent(new OrderCreateEvent(order.getOrderTableId()));
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
        return OrderLineItems.create(requestOrderLineItems, menus);
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

}
