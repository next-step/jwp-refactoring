package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTableRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTable().getId()).orElseThrow(IllegalArgumentException::new);
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        validationOfCreate(orderTable, orderLineItems);

        order.changeOrderTable(orderTable);
        order.changeOrderStatus(OrderStatus.COOKING);
        order.changeOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderRepository.save(order);
        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItem(order, orderLineItems);

        savedOrder.changeOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    private List<OrderLineItem> saveOrderLineItem(final Order order, final List<OrderLineItem> orderLineItems) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();

        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.changeOrder(order);
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }

        return savedOrderLineItems;
    }

    private void validationOfCreate(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        checkEmptyOfOrderLineItem(orderLineItems);
        checkExistOfMenu(orderLineItems);
        checkEmptyTable(orderTable);
    }

    private void checkEmptyTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void checkExistOfMenu(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenu)
                .map(Menu::getId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkEmptyOfOrderLineItem(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.changeOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                                                    .orElseThrow(IllegalArgumentException::new);

        validateionOfChageOrderStatus(savedOrder);

        savedOrder.changeOrderStatus(order.getOrderStatus());

        orderRepository.save(savedOrder);

        savedOrder.changeOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return savedOrder;
    }

    private void validateionOfChageOrderStatus(final Order savedOrder) {
        if (OrderStatus.COMPLETION.equals(savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }
}
