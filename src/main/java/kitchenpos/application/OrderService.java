package kitchenpos.application;

import static kitchenpos.exception.ErrorCode.CAN_NOT_ORDER;
import static kitchenpos.exception.ErrorCode.NOT_FOUND_ORDER;
import static kitchenpos.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.KitchenposException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final TableService tableService;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final TableService tableService
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.tableService = tableService;
    }

    public OrderResponse create(final Order order) {
        order.validateNullOrderLineItems();
        validateOrderLineItems(order);

        final OrderTable orderTable = tableService.findById(order.getOrderTableId());
        validateEmptyTrue(orderTable);

        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);
        final List<OrderLineItem> savedOrderLineItems =
                saveOrderLineItems(savedOrder.getId(), order.getOrderLineItems());

        return OrderResponse.of(savedOrder, savedOrderLineItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Order findById(final Long orderId) {
        return orderDao.findById(orderId).orElseThrow(() -> new KitchenposException(NOT_FOUND_ORDER));
    }

    public OrderResponse changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = findById(orderId);
        savedOrder.isCompletionOrderStatus();

        savedOrder.setOrderStatus(order.getOrderStatus());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return OrderResponse.of(savedOrder);
    }

    private void validateOrderLineItems(Order order){
        if (order.getOrderLineItemsSize() != menuDao.countByIdIn(order.getMenuIds())) {
            throw new KitchenposException(NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT);
        }
    }

    private List<OrderLineItem> saveOrderLineItems(Long orderId, List<OrderLineItem> orderLineItems){
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return savedOrderLineItems;
    }

    private void validateEmptyTrue(OrderTable orderTable){
        if (orderTable.isEmpty()) {
            throw new KitchenposException(CAN_NOT_ORDER);
        }
    }
}
