package kitchenpos.application;

import kitchenpos.domain.dto.OrderStatusRequest;
import kitchenpos.domain.*;
import kitchenpos.domain.dto.OrderLineItemRequest;
import kitchenpos.domain.dto.OrderRequest;
import kitchenpos.domain.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = getOrderLineItems(request.getOrderLineItems());

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order order = request.toEntity(orderTable, orderLineItems);
        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (order.isCompleted()) {
            throw new IllegalArgumentException();
        }
        order.setOrderStatus(request.getOrderStatus());

        return OrderResponse.of(order);
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> requests) {
        return requests.stream().map(this::getOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem getOrderLineItem(OrderLineItemRequest request) {
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(IllegalArgumentException::new);
        return request.toEntity(menu);
    }
}
