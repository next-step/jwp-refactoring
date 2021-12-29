package kitchenpos.orders.application;

import kitchenpos.orders.domain.Order;
import kitchenpos.orders.domain.OrderLineItem;
import kitchenpos.orders.domain.OrderLineItemRepository;
import kitchenpos.orders.domain.OrderLineItems;
import kitchenpos.orders.domain.OrderRepository;
import kitchenpos.orders.domain.OrderValidator;
import kitchenpos.orders.dto.OrderRequest;
import kitchenpos.orders.dto.OrderResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderValidator orderValidator;
	private final OrderLineItemRepository orderLineItemRepository;

	public OrderService(OrderRepository orderRepository, OrderValidator orderValidator,
		OrderLineItemRepository orderLineItemRepository) {
		this.orderRepository = orderRepository;
		this.orderValidator = orderValidator;
		this.orderLineItemRepository = orderLineItemRepository;
	}

	@Transactional
	public OrderResponse create(final OrderRequest orderRequest) {
		Order order = orderRequest.toOrder();
		OrderLineItems orderLineItems = orderRequest.toOrderLineItems();
		orderValidator.validateOrderCreate(order, orderLineItems);
		Order savedOrder = orderRepository.save(order);
		List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.saveAll(orderLineItems.setOrder(savedOrder).value());
		return OrderResponse.of(savedOrder, savedOrderLineItems);
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> list() {
		List<Order> orders = orderRepository.findAll();
		return orders.stream()
			.map(order -> OrderResponse.of(order, findOrderLineItemsByOrderId(order.getId())))
			.collect(Collectors.toList());
	}

	private List<OrderLineItem> findOrderLineItemsByOrderId(Long orderId) {
		return orderLineItemRepository.findAllByOrderId(orderId);
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
		final Order order = findOrderById(orderId);
		order.changeOrderStatus(orderRequest.getOrderStatus());
		return OrderResponse.of(order, findOrderLineItemsByOrderId(orderId));
	}

	@Transactional(readOnly = true)
	public Order findOrderById(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다"));
	}

}
