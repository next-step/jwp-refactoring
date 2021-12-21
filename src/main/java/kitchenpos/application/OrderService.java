package kitchenpos.application;


import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.event.OrderCreatedEvent;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("등록된 주문테이블이 아닙니다."));

        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItemRequests())) {
            throw new IllegalArgumentException("주문 항목 리스트가 비어있습니다.");
        }
        Order order = new Order(orderTable);

        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItemRequests()) {
            addOrderLineItemToOrder(order, orderLineItemRequest);
        }
        eventPublisher.publishEvent(new OrderCreatedEvent(order));
        return orderRepository.save(order);
    }

    private void addOrderLineItemToOrder(Order order, OrderLineItemRequest orderLineItemRequest) {
        OrderLineItem orderLineItem = new OrderLineItem(order, orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity());
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
