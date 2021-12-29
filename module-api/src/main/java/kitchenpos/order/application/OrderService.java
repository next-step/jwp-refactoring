package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.dto.OrderRequest;

@Service
@Transactional(readOnly = true)
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderValidator orderValidator;

	public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
		this.orderRepository = orderRepository;
		this.orderValidator = orderValidator;
	}

	@Transactional
	public OrderDto create(OrderRequest request) {
		Order order = orderRepository.save(Order.of(
			request.getOrderTableId(),
			request.getOrderLineItems(),
			orderValidator));
		return OrderDto.from(order);
	}

	public List<OrderDto> list() {
		List<Order> orders = orderRepository.findAll();
		return orders.stream()
			.map(OrderDto::from)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderDto changeOrderStatus(Long id, OrderRequest request) {
		Order order = orderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
		order.changeOrderStatus(request.getOrderStatus());
		return OrderDto.from(order);
	}
}
