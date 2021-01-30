package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = new Order.Builder()
                .orderTable(findOrderTable(orderRequest.getOrderTableId()))
                .orderedTime(LocalDateTime.now())
                .orderLineItems(findOrderLineItems(orderRequest.getOrderLineItems()))
                .build();
        return OrderResponse.of(orderRepository.save(order));
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 테이블 입니다."));
    }

    private List<OrderLineItem> findOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(item -> new OrderLineItem(findMenuId(item), item.getQuantity()))
                .collect(Collectors.toList());
    }

    private Long findMenuId(OrderLineItemRequest orderLineItem) {
        final Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴 입니다."));
        return menu.getId();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 입니다."));

        savedOrder.updateOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }
}
