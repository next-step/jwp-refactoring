package kitchenpos.application;

import kitchenpos.common.NotFoundException;
import kitchenpos.common.OrderValidationException;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest_ChangeStatus;
import kitchenpos.dto.OrderRequest_Create;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
	static final String MSG_CANNOT_FIND_ORDER = "Cannot find Order by orderId";
	static final String MSG_CANNOT_FIND_MENU = "Cannot find Menu by menuId";
	static final String MSG_CANNOT_FIND_ORDER_TABLE = "Cannot find OrderTable by orderTableId";
	static final String MSG_CANNOT_CREATE_EMPTY_ITEMS = "Cannot create Order By empty OrderLineItems";
	static final String MSG_CANNOT_CREATE_EMPTY_ORDER_TABLE = "Cannot create Order By empty OrderTable";
	static final String MSG_CANNOT_CHANGE_COMPLETION = "Cannot change orderStatus of already COMPLETION table";

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
			throw new OrderValidationException(MSG_CANNOT_CREATE_EMPTY_ITEMS);
		}

		final List<Long> menuIds = orderLineItems.stream()
				.map(OrderLineItemRequest::getMenuId)
				.collect(Collectors.toList());

		if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
			throw new NotFoundException(MSG_CANNOT_FIND_MENU);
		}

		final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER_TABLE));

		if (orderTable.isEmpty()) {
			throw new OrderValidationException(MSG_CANNOT_CREATE_EMPTY_ORDER_TABLE);
		}

		final Order savedOrder = orderDao.save(createOrder(orderTable));

		final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
		for (final OrderLineItemRequest iterRequest : orderLineItems) {
			OrderLineItem orderLineItem = orderLineItemDao.save(createOrderLineItem(savedOrder, iterRequest));
			savedOrderLineItems.add(orderLineItem);
		}
		return OrderResponse.of(savedOrder);
	}

	private Order createOrder(OrderTable orderTable) {
		return Order.createCookingOrder(orderTable);
	}

	private OrderLineItem createOrderLineItem(Order order, OrderLineItemRequest request) {
		final Menu menu = menuDao.findById(request.getMenuId()).orElseThrow(() ->
				new NotFoundException(MSG_CANNOT_FIND_ORDER_TABLE));
		return new OrderLineItem(order, menu, request.getQuantity());
	}

	public List<OrderResponse> list() {
		final List<Order> orders = orderDao.findAll();

		return orders.stream()
				.map(OrderResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(long orderId, OrderRequest_ChangeStatus request) {
		final Order savedOrder = orderDao.findById(orderId)
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER));

		if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
			throw new OrderValidationException(MSG_CANNOT_CHANGE_COMPLETION);
		}

		final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
		savedOrder.setOrderStatus(orderStatus);

		orderDao.save(savedOrder);
		return OrderResponse.of(savedOrder);
	}
}
