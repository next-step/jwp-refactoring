package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.ChangeOrderStatusRequest;
import kitchenpos.dto.order.CreateOrderRequest;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {


    public static final String EMPTY_ORDER_TABLE_ERROR_MESSAGE = "존재하지 않는 주문 테이블 입니다.";
    public static final String ORDER_NOT_FOUND_ERROR_MESSAGE = "존재하지 않는 주문 입니다.";

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderCreateValidator orderCreateValidator;

    public OrderService(
        OrderRepository orderRepository,
        OrderTableRepository orderTableRepository,
        OrderCreateValidator orderCreateValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderCreateValidator = orderCreateValidator;
    }

    @Transactional
    public OrderResponse create(final CreateOrderRequest createOrderRequest) {
        List<OrderLineItem> orderLineItems = orderCreateValidator
            .validateExistsMenusAndCreate(createOrderRequest.getOrderLineItemRequests());
        final OrderTable orderTable = orderTableRepository.findById(createOrderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException(EMPTY_ORDER_TABLE_ERROR_MESSAGE));

        Order order = createOrderRequest.toOrder(orderTable, orderLineItems);
        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest changeOrderStatusRequest) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.changeOrderStatus(changeOrderStatusRequest.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND_ERROR_MESSAGE));
    }
}
