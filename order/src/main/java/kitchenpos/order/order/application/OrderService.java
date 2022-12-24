package kitchenpos.order.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.menu.applciation.OrderMenuService;
import kitchenpos.order.menu.domain.Menu;
import kitchenpos.order.order.domain.Order;
import kitchenpos.order.order.domain.OrderLineItem;
import kitchenpos.order.order.domain.OrderLineItemMenu;
import kitchenpos.order.order.domain.OrderLineItems;
import kitchenpos.order.order.domain.OrderRepository;
import kitchenpos.order.order.domain.Price;
import kitchenpos.order.order.domain.event.OrderCreatedEvent;
import kitchenpos.order.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.order.ui.request.OrderRequest;
import kitchenpos.order.order.ui.request.OrderStatusRequest;
import kitchenpos.order.order.ui.response.OrderResponse;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderValidator orderValidator;
	private final ApplicationEventPublisher eventPublisher;
	private final OrderMenuService menuService;

	public OrderService(
		final OrderRepository orderRepository,
		final OrderValidator orderValidator,
		final ApplicationEventPublisher eventPublisher,
		final OrderMenuService menuService
	) {
		this.orderRepository = orderRepository;
		this.orderValidator = orderValidator;
		this.eventPublisher = eventPublisher;
		this.menuService = menuService;
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

	private OrderLineItemMenu menu(long menuId) {
		Menu menu = menuService.menu(menuId);
		return OrderLineItemMenu.of(menu.id(), menu.name(), Price.from(menu.price()));
	}

	private Order newOrder(OrderRequest request) {
		return Order.of(
			request.getOrderTableId(),
			orderLineItems(request.getOrderLineItems())
		);
	}
}
