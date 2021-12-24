package kitchenpos.application.order;


import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderCreatedEvent;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.order.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
    public Order create(final OrderRequest orderRequest) {

        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItemRequests())) {
            throw new IllegalArgumentException("주문 항목 리스트가 비어있습니다.");
        }

        Order order = new Order(orderRequest.getOrderTableId());

        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItemRequests()) {
            addOrderLineItemToOrder(order, orderLineItemRequest);
        }
        eventPublisher.publishEvent(new OrderCreatedEvent(orderRequest.getOrderTableId()));

        return orderRepository.save(order);
    }

    private void addOrderLineItemToOrder(Order order, OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuRepository.findById( orderLineItemRequest.getMenuId())
                                            .orElseThrow(() -> new IllegalArgumentException("등록된 메뉴가 아닙니다."));

        OrderLineItem orderLineItem = new OrderLineItem(order, menu, orderLineItemRequest.getQuantity());
        order.addOrderLineItems(orderLineItem);
    }

    @Transactional(readOnly=true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 주문 ID가 아닙니다."));

        savedOrder.changeStatus(orderRequest);
        return savedOrder;
    }
}
