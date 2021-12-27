package kitchenpos.ordertablegroup.domain;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class OrderTableGroupValidatorImpl implements OrderTableGroupValidator {
	private final OrderTables orderTables;
	private final Orders orders;

	public OrderTableGroupValidatorImpl(OrderTables orderTables, Orders orders) {
		this.orderTables = orderTables;
		this.orders = orders;
	}

	@Override
	public void validateOrderTablesAreGreaterThanOrEqualToTwo(List<Long> orderTableIds) {
		if (orderTables.findAllByIdIn(orderTableIds).size() < 2) {
			throw new IllegalArgumentException("주문 테이블이 2개 이상이어야 주문 테이블 그룹을 등록할 수 있습니다.");
		}
	}

	@Override
	public void validateNotGrouped(List<Long> orderTableIds) {
		boolean hasOrderTableGroup = orderTables.findAllByIdIn(orderTableIds)
			.stream()
			.anyMatch(OrderTable::hasOrderTableGroup);

		if (hasOrderTableGroup) {
			throw new IllegalArgumentException("이미 주문 테이블 그룹이 있는 경우 등록할 수 없습니다.");
		}
	}

	@Override
	public void validateOrderTableIsEmpty(List<Long> orderTableIds) {
		boolean isNotEmpty = orderTables.findAllByIdIn(orderTableIds)
			.stream()
			.anyMatch(orderTable -> !orderTable.isEmpty());

		if (isNotEmpty) {
			throw new IllegalArgumentException("주문 테이블이 비어있지 않은 경우 주문 테이블 그룹을 등록할 수 없습니다.");
		}
	}

	@Override
	public void validateNotCompletedOrderNotExist(Long orderTableGroupId) {
		boolean isNotCompleted = orderTables.findByOrderTableGroupId(orderTableGroupId)
			.stream()
			.map(orderTable -> orders.findByOrderTableId(orderTable.getId()))
			.flatMap(List::stream)
			.anyMatch(order -> !order.getOrderStatus().isCompletion());

		if (isNotCompleted) {
			throw new IllegalStateException("주문 테이블에 완료되지 않은 주문이 있는 경우 주문 테이블 그룹을 해제할 수 없습니다.");
		}
	}
}
