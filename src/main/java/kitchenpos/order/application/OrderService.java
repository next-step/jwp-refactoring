package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;
	private final MenuRepository menuRepository;

	public OrderService(
		final OrderRepository orderRepository,
		final OrderTableRepository orderTableRepository,
		final MenuRepository menuRepository
	) {
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
		this.menuRepository = menuRepository;
	}

	@Transactional
	public OrderResponse create(final OrderRequest request) {
		Order order = orderRepository.save(newOrder(request));
		return OrderResponse.from(order);
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> list() {
		final List<Order> orders = orderRepository.findAll();
		return OrderResponse.listFrom(orders);
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
		final Order order = orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);

		order.updateStatus(request.status());
		return OrderResponse.from(order);
	}

	public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> asList) {
		return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, asList);
	}

	private OrderLineItems orderLineItems(List<OrderLineItemRequest> orderLineItems) {
		return orderLineItems.stream()
			.map(this::orderLineItem)
			.collect(Collectors.collectingAndThen(Collectors.toList(), OrderLineItems::from));
	}

	private OrderLineItem orderLineItem(OrderLineItemRequest request) {
		return OrderLineItem.of(menu(request.getMenuId()), request.getQuantity());
	}

	private Menu menu(long menuId) {
		return menuRepository.menu(menuId);
	}

	private Order newOrder(OrderRequest request) {
		return Order.of(
			orderTable(request.getOrderTableId()),
			orderLineItems(request.getOrderLineItems())
		);
	}

	private OrderTable orderTable(long orderTableId) {
		return orderTableRepository.orderTable(orderTableId);
	}
}
