package kitchenpos.application.order;

import static kitchenpos.domain.order.OrderStatus.STARTED_ORDER_READY_STATUS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderMenu;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderTableValidateEvent;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {


    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, MenuService menuService,
        ApplicationEventPublisher eventPublisher) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = this.convertOrderLineItemEntity(orderRequest.getOrderLineItems());
        eventPublisher.publishEvent(new OrderTableValidateEvent(orderRequest.getOrderTableId()));

        Order order = new Order(orderRequest.getOrderTableId(), orderLineItems);
        orderRepository.save(order);

        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public boolean existsUnCompleteStatusByOrderTableIdIn(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, STARTED_ORDER_READY_STATUS);
    }

    @Transactional(readOnly = true)
    public boolean existsUnCompleteStatusByOrderTableId(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, STARTED_ORDER_READY_STATUS);
    }

    private List<OrderLineItem> convertOrderLineItemEntity(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuService.findMenuById(orderLineItemRequest.getMenuId());
            orderLineItems.add(new OrderLineItem(OrderMenu.of(menu), orderLineItemRequest.getQuantity()));
        }
        return orderLineItems;
    }

}
