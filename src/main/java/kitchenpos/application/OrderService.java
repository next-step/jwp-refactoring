package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderSaveRequest;
import kitchenpos.dto.OrderStatusUpdateRequest;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(MenuRepository menuRepository, OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(OrderSaveRequest request) {
        List<OrderLineItem> orderLineItems = toOrderLineItems(request);
        OrderTable orderTable = toOrderTable(request);
        Order order = orderRepository.save(new Order(orderTable, orderLineItems));
        return OrderResponse.of(order);
    }

    private OrderTable toOrderTable(OrderSaveRequest request) {
        return orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> toOrderLineItems(OrderSaveRequest request) {
        return request.getOrderLineItems().stream().map(it -> {
                    Menu menu = menuRepository.findById(it.getMenuId())
                            .orElseThrow(MenuNotFoundException::new);
                    return it.toEntity(menu);
                })
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAllJoinFetch();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.of(order);
    }
}
