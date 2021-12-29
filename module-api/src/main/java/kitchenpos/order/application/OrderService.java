package kitchenpos.order.application;


import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderCreatedEvent;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {

        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItemRequests())) {
            throw new IllegalArgumentException("주문 항목 리스트가 비어있습니다.");
        }

        Order order = new Order(orderRequest.getOrderTableId());

        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItemRequests()) {
            addOrderLineItemToOrder(order, orderLineItemRequest);
        }
        eventPublisher.publishEvent(new OrderCreatedEvent(orderRequest.getOrderTableId()));

        return OrderResponse.from(orderRepository.save(order));
    }

    private void addOrderLineItemToOrder(Order order, OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuRepository.findById( orderLineItemRequest.getMenuId())
                                            .orElseThrow(() -> new IllegalArgumentException("등록된 메뉴가 아닙니다."));

        OrderLineItem orderLineItem = new OrderLineItem(order, menu, orderLineItemRequest.getQuantity());
        order.addOrderLineItems(orderLineItem);
    }

    @Transactional(readOnly=true)
    public List<OrderResponse> list() {

        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 주문 ID가 아닙니다."));

        savedOrder.changeStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }
}
