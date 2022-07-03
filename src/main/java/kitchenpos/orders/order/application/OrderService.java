package kitchenpos.orders.order.application;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.menus.menu.application.MenuService;
import kitchenpos.menus.menu.dto.OrderMenuRequest;
import kitchenpos.menus.menu.dto.OrderMenuResponse;
import kitchenpos.orders.order.domain.Order;
import kitchenpos.orders.order.domain.OrderRepository;
import kitchenpos.orders.order.dto.ChangeOrderStatusRequest;
import kitchenpos.orders.order.dto.OrderRequest;
import kitchenpos.orders.order.dto.OrderResponse;
import kitchenpos.orders.order.event.OrderCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final MenuService menuService;

    public OrderService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher,
                        MenuService menuService) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
        this.menuService = menuService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderMenuResponse> menuOrders = menuService.findMenuOrdersByIds(OrderMenuRequest.of(request.getMenuIds()));

        final Order savedOrder = orderRepository.save(request.toOrder(menuOrders));
        eventPublisher.publishEvent(new OrderCreatedEvent(request.getOrderTableId(), request.getMenuIds()));
        return OrderResponse.from(savedOrder);
    }


    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
        findOrder.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.from(findOrder);
    }
}
