package kitchenpos.order.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
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
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(
                ()-> new EntityNotFoundException("주문테이블", orderRequest.getOrderTableId())
        );
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemRequests()
                .stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList());
        Order saved = orderRepository.save(Order.of(orderTable, OrderLineItems.of(orderLineItems)));
        return OrderResponse.of(saved);
    }



    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final String orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()->new EntityNotFoundException("주문", orderId)
        );
        order.updateOrderStatus(OrderStatus.valueOf(orderStatus));

        return OrderResponse.of(order);
    }
    private OrderLineItem createOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId()).orElseThrow(
                ()->new EntityNotFoundException("메뉴", orderLineItemRequest.getMenuId())
        );
        return OrderLineItem.of(menu, orderLineItemRequest.getQuantity());
    }

}
