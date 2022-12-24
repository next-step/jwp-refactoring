package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {
    public static final String COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE = "주문완료일 경우 주문상태를 변경할 수 없다.";
    public static final String ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE = "주문 항목이 비어있을 수 없다.";
    public static final String ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE = "주문 항목의 수와 메뉴의 수는 같아야 한다.";

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository, final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderLineItems orderLineItems = request.toOrderLineItems();
        Order createOrder = new Order(request.getOrderTableId(), request.toOrderLineItems());
        createOrder.validate(orderValidator);
        Order savedOrder = orderRepository.save(createOrder);
        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = findOrder(orderId);
        savedOrder.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

//    private void validateOrderItems(OrderLineItems orderLineItems) {
//        if (orderLineItems.size() != menuRepository.findAllById(orderLineItems.getMenuIds()).size()) {
//            throw new IllegalArgumentException(ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE);
//        }
//    }
//
//    private OrderTable findOrderTable(Long orderTableId) {
//        return orderTableRepository.findById(orderTableId)
//                .orElseThrow(IllegalArgumentException::new);
//    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
