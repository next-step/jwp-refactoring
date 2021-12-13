package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrdersRepository;
import kitchenpos.dto.order.OrdersResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.utils.StreamUtils;

@Service
public class OrdersService {
    private final MenuRepository menuRepository;
    private final OrdersRepository ordersRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrdersService(
            final MenuRepository menuRepository,
            final OrdersRepository ordersRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.ordersRepository = ordersRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrdersResponse create(final Orders orders) {
        final List<OrderLineItem> orderLineItems = orders.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(item -> item.getMenu().getId())
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(orders.getOrderTable().getId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        orders.setOrderTableId(orderTable.getId());
        orders.setOrderStatus(OrderStatus.COOKING);
        orders.setOrderedTime(LocalDateTime.now());

        final Orders savedOrders = ordersRepository.save(orders);

        return OrdersResponse.from(savedOrders);
    }

    @Transactional(readOnly = true)
    public List<OrdersResponse> list() {
        final List<Orders> orders = ordersRepository.findAll();
        return StreamUtils.mapToList(orders, OrdersResponse::from);
    }

    @Transactional
    public OrdersResponse changeOrderStatus(final Long orderId, final Orders orders) {
        final Orders savedOrders = ordersRepository.findById(orderId)
                                                   .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrders.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        savedOrders.setOrderStatus(orders.getOrderStatus());

        return OrdersResponse.from(savedOrders);
    }
}
