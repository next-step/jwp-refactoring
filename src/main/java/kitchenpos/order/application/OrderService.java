package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrdersRepository ordersRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
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
    public Orders create(final Orders orders) {
        final List<OrderLineItem> orderLineItems = orders.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
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

        final Long orderId = savedOrders.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        savedOrders.setOrderLineItems(savedOrderLineItems);

        return savedOrders;
    }

    public List<Orders> list() {
        final List<Orders> orders = ordersRepository.findAll();

        for (final Orders order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrders(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Orders changeOrderStatus(final Long orderId, final Orders orders) {
        final Orders savedOrders = ordersRepository.findById(orderId)
                                                   .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrders.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        savedOrders.setOrderStatus(orders.getOrderStatus());

        ordersRepository.save(savedOrders);

        savedOrders.setOrderLineItems(orderLineItemRepository.findAllByOrders(orderId));

        return savedOrders;
    }
}
