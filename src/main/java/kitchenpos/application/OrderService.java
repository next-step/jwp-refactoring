package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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

    public Order create(final Order order) {
        order.validateNullOrderLineItems();
        validateOrderLineItems(order);

        final OrderTable orderTable = tableService.findById(order.getOrderTableId());
        validateEmptyTrue(orderTable);

        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(savedOrder.getId(), order.getOrderLineItems());
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional(readOnly = true)
    public Order findById(final Long orderId) {
        return orderDao.findById(orderId).orElseThrow(IllegalArgumentException::new);
    }

    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = findById(orderId);
        savedOrder.isCompletionOrderStatus();

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }

    private void validateOrderLineItems(Order order){
        if (order.getOrderLineItemsSize() != menuDao.countByIdIn(order.getMenuIds())) {
            throw new IllegalArgumentException();
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
            throw new IllegalArgumentException();
        }
    }
}
