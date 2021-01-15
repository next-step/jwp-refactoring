package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest_ChangeStatus;
import kitchenpos.dto.OrderRequest_Create;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
	private final MenuDao menuDao;
	private final OrderDao orderDao;
	private final OrderLineItemDao orderLineItemDao;
	private final OrderTableDao orderTableDao;

	public OrderService(final MenuDao menuDao,
	                    final OrderDao orderDao,
	                    final OrderLineItemDao orderLineItemDao,
	                    final OrderTableDao orderTableDao) {
		this.menuDao = menuDao;
		this.orderDao = orderDao;
		this.orderLineItemDao = orderLineItemDao;
		this.orderTableDao = orderTableDao;
	}

	@Transactional
	public OrderResponse create(OrderRequest_Create request) {
		List<OrderLineItemRequest> orderLineItems = request.getOrderLineItems();

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

		final Order savedOrder = orderDao.save(createOrder(request));

		final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
		for (final OrderLineItemRequest iterRequest : orderLineItems) {
			OrderLineItem orderLineItem = orderLineItemDao.save(createOrderLineItem(savedOrder, iterRequest));
			savedOrderLineItems.add(orderLineItem);
		}
		savedOrder.setOrderLineItems(savedOrderLineItems);
		return OrderResponse.of(savedOrder);
	}

	private Order createOrder(OrderRequest_Create request) {
		Order order = new Order();
		order.setOrderTableId(request.getOrderTableId());
		order.setOrderStatus(OrderStatus.COOKING.name());
		order.setOrderedTime(LocalDateTime.now());
		return order;
	}

	private OrderLineItem createOrderLineItem(Order order, OrderLineItemRequest request) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setOrderId(order.getId());
		orderLineItem.setMenuId(request.getMenuId());
		orderLineItem.setQuantity(request.getQuantity());
		return orderLineItem;
	}

	public List<OrderResponse> list() {
		final List<Order> orders = orderDao.findAll();

		for (final Order order : orders) {
			order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
			// TODO: 2021-01-15 orderLineItem 을 order 하위로
		}

		return orders.stream()
				.map(OrderResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(long orderId, OrderRequest_ChangeStatus request) {
		final Order savedOrder = orderDao.findById(orderId)
				.orElseThrow(IllegalArgumentException::new);

		if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
			throw new IllegalArgumentException();
		}

		final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
		savedOrder.setOrderStatus(orderStatus.name());

		orderDao.save(savedOrder);

		savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

		return OrderResponse.of(savedOrder);
	}
}
