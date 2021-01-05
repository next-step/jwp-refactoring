package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;

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
	public Order create(final OrderRequest orderRequest) {
		final List<Long> menuIds = orderRequest.getMenuIds();

		if (CollectionUtils.isEmpty(menuIds)) {
			throw new IllegalArgumentException();
		}

		if (menuIds.size() != menuDao.countByIdIn(menuIds)) {
			throw new IllegalArgumentException();
		}

		final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
			.orElseThrow(IllegalArgumentException::new);

		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException();
		}

		final Order savedOrder = orderDao.save(Order.create(orderTable));

		final Long orderId = savedOrder.getId();
		final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
		final List<Menu> menus = menuDao.findAllById(menuIds);
		for (final Menu menu : menus) {
			savedOrderLineItems.add(orderLineItemDao.save(OrderLineItem.create(savedOrder, menu, orderRequest.getQuantity())));
		}
		savedOrder.setOrderLineItems(savedOrderLineItems);

		return savedOrder;
	}

	public List<Order> list() {
		final List<Order> orders = orderDao.findAll();

		/*for (final Order order : orders) {
			order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
		}*/

		return orders;
	}

	@Transactional
	public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
		final Order savedOrder = orderDao.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);

		if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
			throw new IllegalArgumentException();
		}

		final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
		savedOrder.setOrderStatus(orderStatus.name());

		orderDao.save(savedOrder);

		savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

		return savedOrder;
	}
}
