package kitchenpos.ordertablegroup.infra;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertablegroup.domain.OrderTables;

@Component(value = "OrderTableGroupOrderTables")
public class OrderTablesImpl implements OrderTables {
	private final OrderTableRepository orderTableRepository;

	public OrderTablesImpl(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Override
	public List<kitchenpos.ordertablegroup.domain.OrderTable> findAllByIdIn(List<Long> ids) {
		List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(ids);
		return orderTables.stream()
			.map(orderTable -> kitchenpos.ordertablegroup.domain.OrderTable.of(
				orderTable.getId(),
				orderTable.getOrderTableGroupId(),
				orderTable.isEmpty()))
			.collect(Collectors.toList());
	}

	@Override
	public List<kitchenpos.ordertablegroup.domain.OrderTable> findByOrderTableGroupId(Long orderTableGroupId) {
		List<OrderTable> orderTables = orderTableRepository.findByOrderTableGroupId(orderTableGroupId);
		return orderTables.stream()
			.map(orderTable -> kitchenpos.ordertablegroup.domain.OrderTable.of(
				orderTable.getId(),
				orderTable.getOrderTableGroupId(),
				orderTable.isEmpty()))
			.collect(Collectors.toList());
	}
}
