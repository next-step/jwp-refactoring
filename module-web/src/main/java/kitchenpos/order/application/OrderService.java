package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderStatusEvent;
import kitchenpos.order.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderValidator orderValidator;
    private final ApplicationEventPublisher publisher;

    public OrderService(final OrderRepository orderRepository,
        final MenuRepository menuRepository, OrderValidator orderValidator,
        ApplicationEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderValidator = orderValidator;
        this.publisher = publisher;
    }


    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {

        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems()
            .stream()
            .map(it -> it.toEntity(findMenu(it.getMenuId())))
            .collect(Collectors.toList());

        orderValidator.validateCreateOrder(orderRequest.getOrderTableId(), orderLineItems);

        final Order persist = orderRepository.save(orderRequest.toEntity(orderLineItems));

        publishOrderStatus(persist);

        return OrderResponse.of(persist);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final OrderStatusRequest orderStatus) {

        final Order savedOrder = findOrder(orderId);

        orderValidator.validateUpdateOrderStatus(savedOrder);
        savedOrder.updateOrderStatus(orderStatus.toStatus());

        publishOrderStatus(savedOrder);

        return OrderResponse.of(savedOrder);
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
            .orElseThrow(() -> new NotFoundException("해당하는 메뉴가 없습니다."));
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("해당하는 주문이 없습니다."));
    }

    private void publishOrderStatus(Order order) {
        publisher.publishEvent(OrderStatusEvent.of(this, order));
    }
}
