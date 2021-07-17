package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderValidator orderValidator;

	public OrderService(
			final OrderRepository orderRepository,
			final OrderValidator orderValidator) {
		this.orderRepository = orderRepository;
		this.orderValidator = orderValidator;
	}

	@Transactional
	public OrderResponse create(final OrderRequest orderRequest) {
		orderValidator.validate(orderRequest);

		Order order = orderRepository.save(orderRequest.toOrder());

		List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemRequests().stream()
				.map(orderLineItemRequest -> new OrderLineItem(order.getId(), orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
				.collect(Collectors.toList());

		order.addOrderLineItems(orderLineItems);
		return OrderResponse.of(order);
	}

	public List<OrderResponse> list() {
		final List<Order> orders = orderRepository.findAll();
		return OrderResponse.of(orders);
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final String orderStatus) {
		final Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다. id: " + orderId));

		order.changeStatus(orderStatus);
		return OrderResponse.of(order);
	}
}
