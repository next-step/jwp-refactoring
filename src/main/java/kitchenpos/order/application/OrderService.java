package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableValidator;

@Service
@Transactional(readOnly = true)
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderTableValidator orderTableValidator;

	public OrderService(OrderRepository orderRepository, OrderTableValidator orderTableValidator) {
		this.orderRepository = orderRepository;
		this.orderTableValidator = orderTableValidator;
	}

	@Transactional
	public Order create(Order order) {
		order.place(orderTableValidator);
		return orderRepository.save(order);
	}

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Transactional
	public Order changeOrderStatus(Long orderId, OrderStatus toStatus) {
		Order order = findById(orderId);

		order.changeOrderStatus(toStatus);

		return orderRepository.save(order);
	}

	private Order findById(Long orderId) {
		return orderRepository.findById(orderId)
							  .orElseThrow(IllegalArgumentException::new);
	}
}
