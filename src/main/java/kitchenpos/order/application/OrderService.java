package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
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
    public Order create(final Order order) {
        final List<OrderLineItem> orderLineItems = order.orderLineItems().readOnlyOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::menu)
                .map(Menu::id)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(order.orderTable().id())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setOrderTable(orderTable);
        order.setOrderStatus(COOKING);
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderRepository.save(order);
        
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(savedOrder);
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(OrderLineItems.of(savedOrderLineItems));

        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(OrderLineItems.of(orderLineItemRepository.findAllByOrderId(order.id())));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(COMPLETION, savedOrder.orderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = order.orderStatus();
        savedOrder.setOrderStatus(orderStatus);

        orderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(OrderLineItems.of(orderLineItemRepository.findAllByOrderId(orderId)));

        return savedOrder;
    }
}
