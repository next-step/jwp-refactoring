package kitchenpos.application.order;


import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.event.OrderCreatedEvent;
import kitchenpos.repository.order.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {

        Order order = new Order(orderRequest.getOrderTableId());

        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItemRequests()) {
            addOrderLineItemToOrder(order, orderLineItemRequest);
        }
        orderValidator.checkOrderValidate(order);

        eventPublisher.publishEvent(new OrderCreatedEvent(order.getOrderLineItems()));
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
