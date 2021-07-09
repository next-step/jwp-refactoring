package kitchenpos.tablegroup.domain;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;

public class UngroupValidator {
	private final List<Order> orders;

	public UngroupValidator(List<Order> orders) {
		this.orders = orders;
	}

	public void validate(OrderTable orderTable) {
		final List<Order> ordersFromTable = findOrdersFrom(orderTable);
		validateNotEmpty(ordersFromTable);
		validateCompleteOrder(ordersFromTable);
	}

	private void validateNotEmpty(List<Order> ordersFromTable) {
		if (ordersFromTable == null || ordersFromTable.isEmpty()) {
			throw new IllegalArgumentException("주문테이블의 주문이 존재하지 않습니다.");
		}
	}

	private List<Order> findOrdersFrom(OrderTable orderTable) {
		return orders.stream()
			.filter(order -> order.isFrom(orderTable.getId()))
			.collect(Collectors.toList());
	}

	private void validateCompleteOrder(List<Order> ordersFromTable) {
		boolean isCompleteAll = ordersFromTable.stream().allMatch(Order::isComplete);
		if (!isCompleteAll) {
			throw new IllegalArgumentException("조리상태이거나 식사상태인 주문이 있는 주문테이블은 그룹해제를 할 수 없습니다.");
		}
	}
}
