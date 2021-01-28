package kitchenpos.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final Order order) {
        this.validateOrderLineItems(order.getOrderLineItems());

        order.setOrderTableId(findOrderTable(order).getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);
        this.addPersistOrderLineItems(order, savedOrder);

        return savedOrder;
    }

    /**
     * 주문 항목을 저장하고, 주문에 추가합니다.
     * @param savedOrder
     */
    private void addPersistOrderLineItems(Order order, Order savedOrder) {
        final Long orderId = savedOrder.getId();
        for (final OrderLineItem orderLineItem : order.getOrderLineItems()) {
            orderLineItem.setOrderId(orderId);
            savedOrder.addOrderLineItems(orderLineItemDao.save(orderLineItem));
        }
    }

    /**
     * 해당 주문의 테이블을 찾습니다.
     * @param order 
     * @return
     */
    private OrderTable findOrderTable(Order order) {
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orderTable;
    }

    /**
     * 주문항목이 적합한지 체크합니다.
     * @param orderLineItems
     */
    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.checkEmptyOrderLineItems(orderLineItems);
        this.checkOrderLineItemsCount(orderLineItems);
    }

    /**
     * 주문항목의 개수가 메뉴의 수와 같은지 확인합니다.
     * @param orderLineItems
     */
    private void checkOrderLineItemsCount(List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 주문항목이 비었는지 확인합니다.
     * @param orderLineItems
     */
    private void checkEmptyOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        this.checkCompletionOrder(savedOrder);
        savedOrder.setOrderStatus(OrderStatus.valueOf(order.getOrderStatus()).name());
        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }

    /**
     * 주문이 완료된 상태인지 확인합니다.
     * @param savedOrder
     */
    private void checkCompletionOrder(Order savedOrder) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }
}
