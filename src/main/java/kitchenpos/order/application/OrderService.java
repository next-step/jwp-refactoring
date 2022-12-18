package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.dto.OrderResponse;
import kitchenpos.order.ui.dto.OrderStatusRequest;

@Service
@Transactional(readOnly = true)
public class OrderService {
	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Transactional
	public Order create(Order order) {
		order.startOrder();

		return orderRepository.save(order);
	}

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Transactional
	public OrderResponse changeOrderStatus(Long orderId, OrderStatusRequest statusRequest) {
		return new OrderResponse(changeOrderStatus(orderId, statusRequest.toOrderStatus()));
	}

	@Transactional
	public Order changeOrderStatus(Long orderId, OrderStatus toStatus) {
		Order savedOrder = findById(orderId);

		savedOrder.changeOrderStatus(toStatus);

		return savedOrder;
	}

	private Order findById(Long orderId) {
		return orderRepository.findById(orderId)
							  .orElseThrow(IllegalArgumentException::new);
	}
}
