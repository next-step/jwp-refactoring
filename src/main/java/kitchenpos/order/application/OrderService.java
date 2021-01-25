package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;

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
	public OrderResponse create(final OrderRequest request) {
		final List<OrderLineItemRequest> orderLineItems = request.getOrderLineItems();

		if (CollectionUtils.isEmpty(orderLineItems)) {
			throw new IllegalArgumentException();
		}

		final List<Long> menuIds = orderLineItems.stream()
			.map(OrderLineItemRequest::getMenuId)
			.collect(Collectors.toList());

		if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
			throw new IllegalArgumentException();
		}

		final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
			.orElseThrow(IllegalArgumentException::new);

		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException();
		}

		final Order savedOrder = orderDao.save(new Order.Builder()
			.orderTable(orderTable)
			.orderStatus(OrderStatus.COOKING)
			.orderedTime(LocalDateTime.now())
			.build());

		final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
		for (final OrderLineItemRequest orderLineItem : orderLineItems) {
			Menu menu = menuDao.findById(orderLineItem.getMenuId()).orElseThrow(IllegalArgumentException::new);
			savedOrderLineItems.add(
				orderLineItemDao.save(new OrderLineItem.Builder().menu(menu).order(savedOrder).build()));
		}
		savedOrder.setOrderLineItems(savedOrderLineItems);

		return OrderResponse.from(savedOrder);
	}

	public List<OrderResponse> list() {
		final List<Order> orders = orderDao.findAll();

		for (final Order order : orders) {
			order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
		}

		return OrderResponse.newList(orders);
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
		final Order savedOrder = orderDao.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);

		if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
			throw new IllegalArgumentException();
		}

		savedOrder.setOrderStatus(request.getOrderStatus());

		orderDao.save(savedOrder);

		savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

		return OrderResponse.from(savedOrder);
	}
}
