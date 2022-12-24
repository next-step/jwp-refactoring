package kitchenpos.order.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.order.domain.Order;
import kitchenpos.order.order.domain.OrderLineItem;
import kitchenpos.order.order.domain.OrderLineItems;
import kitchenpos.order.order.domain.OrderRepository;
import kitchenpos.order.order.domain.event.OrderCreatedEvent;
import kitchenpos.order.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.order.ui.request.OrderRequest;
import kitchenpos.order.order.ui.request.OrderStatusRequest;
import kitchenpos.order.order.ui.response.OrderResponse;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final MenuRepository menuRepository;
	private final OrderValidator orderValidator;
	private final ApplicationEventPublisher eventPublisher;

	public OrderService(
		final OrderRepository orderRepository,
		final MenuRepository menuRepository,
		final OrderValidator orderValidator,
		final ApplicationEventPublisher eventPublisher
	) {
		this.orderRepository = orderRepository;
		this.menuRepository = menuRepository;
		this.orderValidator = orderValidator;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public OrderResponse create(final OrderRequest request) {
		orderValidator.validateCreateOrder(request.getOrderTableId());
		Order order = orderRepository.save(newOrder(request));
		eventPublisher.publishEvent(OrderCreatedEvent.from(order));
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
			request.getOrderTableId(),
			orderLineItems(request.getOrderLineItems())
		);
	}
}
