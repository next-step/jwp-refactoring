package kitchenpos.order.application;

import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.dao.OrderLineItemRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItem : orderRequest.getOrderLineItems()) {
            Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴입니다."));
            orderLineItems.add(new OrderLineItem(menu, orderLineItem.getQuantity()));
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 테이블입니다."));

        if (orderTable.isEmpty()) {
           throw new IllegalArgumentException("빈 테이블은 주문할 수 없습니다.");
        }

        final Order savedOrder = orderRepository.save(new Order(orderTable, OrderStatus.COOKING));
        savedOrder.updateOrderLineItems(orderLineItems);

        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
        savedOrder.updateOrderStatus(orderStatus);

        return OrderResponse.of(orderRepository.save(savedOrder));
    }
}
