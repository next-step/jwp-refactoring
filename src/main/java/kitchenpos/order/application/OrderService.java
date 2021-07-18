package kitchenpos.order.application;

import kitchenpos.exception.OrderException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.publisher.OrderEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private static final String NOT_FOUND_ORDER_ERROR_MESSAGE = "주문 정보를 찾을 수 없습니다.";
    private static final String EMPTY_ORDER_ITEM_ERROR_MESSAGE = "주문 항목이 존재하지 않습니다.";

    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public OrderResponse create(final OrderRequest request) {
        validOrderLineItemCount(request);
        eventPublisher.createOrderValidPublishEvent(request);
        Orders orders = request.createNewOrder();
        orders.addOrderLineItems(request.toOrderLineItems());
        return OrderResponse.of(orderRepository.save(orders));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Orders> orders = orderRepository.findAll();
        return orders.stream().map(OrderResponse::of).collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new OrderException(NOT_FOUND_ORDER_ERROR_MESSAGE));
        orders.updateOrderStatus(request.getOrderStatus());
        return OrderResponse.of(orders);
    }

    private void validOrderLineItemCount(final OrderRequest request) {
        if (CollectionUtils.isEmpty(request.getOrderLineItemRequests())) {
            throw new OrderException(EMPTY_ORDER_ITEM_ERROR_MESSAGE);
        }
    }
}
