package kitchenpos.ordertable.infra;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.Orders;

@Component
public class OrdersImpl implements Orders {
	private final OrderRepository orderRepository;

	public OrdersImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public List<kitchenpos.ordertable.domain.Order> findByOrderTableId(Long orderTableId) {
		List<Order> orders = orderRepository.findByOrderTableId(orderTableId);
		return orders.stream()
			.map(order -> kitchenpos.ordertable.domain.Order.from(order.getOrderStatus().name()))
			.collect(Collectors.toList());
	}
}
