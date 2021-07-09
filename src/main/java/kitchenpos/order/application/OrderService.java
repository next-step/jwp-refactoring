package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.dto.OrderStatusResponse;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
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

    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.findAllById(menuIds).size()) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        /*order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());*/

        final Order savedOrder = orderRepository.save(new Order(orderRequest.getOrderTableId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderRequest.getOrderLineItems()));

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItem);
        }
        //savedOrder.setOrderLineItems(savedOrderLineItems);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderStatusResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order findedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), findedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        findedOrder.changeOrderStatus(orderStatus.name());

        Order changedOrder = orderRepository.save(findedOrder);

        return OrderStatusResponse.from(changedOrder.getOrderStatus());
    }
}
