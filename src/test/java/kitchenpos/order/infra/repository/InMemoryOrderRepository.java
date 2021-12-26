package kitchenpos.order.infra.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;

public class InMemoryOrderRepository implements OrderRepository {
	private final Map<Long, Order> orders = new HashMap<>();

	@Override
	public Order save(Order order) {
		orders.put(order.getId(), order);
		return order;
	}

	@Override
	public List<Order> findAll() {
		return new ArrayList<>(orders.values());
	}

	@Override
	public Optional<Order> findById(Long id) {
		return Optional.ofNullable(orders.get(id));
	}

	@Override
	public List<Order> findByOrderTableId(Long orderTableId) {
		return orders.values()
			.stream()
			.filter(order -> Objects.equals(order.getOrderTableId(), orderTableId))
			.collect(Collectors.toList());
	}
}
