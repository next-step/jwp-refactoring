package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

@Service
@Transactional
public class OrderService {
	private final MenuService menuService;
	private final OrderDao orderDao;

	public OrderService(
		final MenuService menuService,
		final OrderDao orderDao
	) {
		this.menuService = menuService;
		this.orderDao = orderDao;
	}

	public OrderResponse create(final OrderRequest request) {
		List<OrderLineItem> orderLineItems = Optional.ofNullable(request.getOrderLineItems())
			.orElseGet(Collections::emptyList)
			.stream()
			.map(it -> new OrderLineItem.Builder()
				.menu(menuService.findById(it.getMenuId()))
				.quantity(it.getQuantity())
				.build()
			).collect(Collectors.toList());

		return OrderResponse.from(orderDao.save(new Order.Builder()
			.orderTableId(request.getOrderTableId())
			.orderLineItems(orderLineItems)
			.orderStatus(OrderStatus.COOKING)
			.orderedTime(LocalDateTime.now())
			.build()));
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> list() {
		return OrderResponse.newList(orderDao.findAll());
	}

	public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
		final Order savedOrder = findById(orderId);
		savedOrder.changeOrderStatus(request.getOrderStatus());
		return OrderResponse.from(savedOrder);
	}

	public Order findById(final Long id) {
		return orderDao.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 Order가 존재하지 않습니다."));
	}
}
