package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.exception.OrderException;

@Service
@Transactional(readOnly = true)
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
	public Order create(final OrderRequest orderRequest) {
		orderValidator.validate(orderRequest);
		final Order savedOrder = Order.of(orderRequest.getOrderTableId(), OrderStatus.COOKING);
		savedOrder.addOrderLineItems(savedOrderLineItems(orderRequest.getOrderLineItems(), savedOrder));
		return orderRepository.save(savedOrder);
	}

	public List<Order> list() {
		return orderRepository.findAll();
	}

	@Transactional
	public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
		final Order savedOrder = orderFindById(orderId);
		savedOrder.updateOrderStatus(orderStatusRequest.getOrderStatus());
		return orderRepository.save(savedOrder);
	}

	List<OrderLineItem> savedOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests,
		Order savedOrder) {
		return orderLineItemRequests.stream()
			.map(it -> (OrderLineItem.of(savedOrder, it.getMenuId(), it.getQuantity())))
			.collect(Collectors.toList());
	}

	private Order orderFindById(Long id) {
		return orderRepository.findById(id)
			.orElseThrow(() -> {
				throw new OrderException(ErrorCode.ORDER_IS_NULL);
			});
	}
}
