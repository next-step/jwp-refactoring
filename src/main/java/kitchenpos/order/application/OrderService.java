package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException("주문 메뉴가 없습니다.");
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블 정보가 없습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }

        final Order savedOrder = orderRepository.save(new Order(orderRequest.getOrderTableId(), OrderStatus.COOKING));

        addOrderLineItems(savedOrder, orderRequest.getOrderLineItems());

        return OrderResponse.of(savedOrder);
    }

    private void addOrderLineItems(Order order, List<OrderLineItemRequest> orderLineItems) {
        for(OrderLineItemRequest orderLineItemRequest : orderLineItems){
            Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("메뉴 정보가 없습니다."));
            order.addOrderLineItem(menu.getId(), orderLineItemRequest.getQuantity());
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> OrderResponse.of(order))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(orderRepository.save(savedOrder));
    }
}
