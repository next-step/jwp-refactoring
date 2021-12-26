package kitchenpos.ordertable.infra.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;

public class InMemoryOrderTableRepository implements OrderTableRepository {
	private final Map<Long, OrderTable> orderTables = new HashMap<>();

	@Override
	public OrderTable save(OrderTable orderTable) {
		orderTables.put(orderTable.getId(), orderTable);
		return orderTable;
	}

	@Override
	public List<OrderTable> findAll() {
		return new ArrayList<>(orderTables.values());
	}

	@Override
	public Optional<OrderTable> findById(Long id) {
		return Optional.ofNullable(orderTables.get(id));
	}

	@Override
	public List<OrderTable> findAllByIdIn(List<Long> ids) {
		return orderTables.values()
			.stream()
			.filter(orderTable -> ids.contains(orderTable.getId()))
			.collect(Collectors.toList());
	}
}
