package kitchenpos.orders.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.orders.domain.OrderLineItem;
import kitchenpos.orders.domain.OrderStatus;
import kitchenpos.orders.domain.Orders;
import kitchenpos.table.domain.OrderTable;

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
    public Orders create(final Orders order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

		List<Long> menuIds = orderLineItems.stream()
			.map(OrderLineItem::getMenuId)
			.collect(Collectors.toList());

		if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
		Orders targetOrder = new Orders(order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime());

        final Orders savedOrder = orderDao.save(targetOrder);
		for (OrderLineItem orderLineItem : order.getOrderLineItems()) {
			orderLineItem.changeOrder(savedOrder);
			orderLineItemDao.save(orderLineItem);
		}
/*
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.changeOrder(savedOrder);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);*/

        return savedOrder;
    }


    public List<Orders> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Orders changeOrderStatus(final Long orderId, final Orders order) {
        final Orders savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());
        orderDao.save(savedOrder);
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
        return savedOrder;
    }
}
