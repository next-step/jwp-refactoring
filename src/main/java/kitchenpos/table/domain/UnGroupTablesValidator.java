package kitchenpos.table.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.exception.CannotUnGroupTablesException;

@Component
public class UnGroupTablesValidator {

	private final OrderRepository orderRepository;

	public UnGroupTablesValidator(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public void validate(TableGroup tableGroup) {
		List<Long> orderTableIdList = tableGroup.getOrderTables()
			.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());
		if (hasAnyInCompletedOrders(orderTableIdList)) {
			throw new CannotUnGroupTablesException();
		}
	}

	private boolean hasAnyInCompletedOrders(List<Long> orderTableIdList) {
		return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
			orderTableIdList,
			Arrays.asList(OrderStatus.MEAL, OrderStatus.COOKING));
	}
}
