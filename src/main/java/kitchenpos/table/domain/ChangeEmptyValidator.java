package kitchenpos.table.domain;

import java.util.List;

import kitchenpos.order.domain.Order;

public class ChangeEmptyValidator {

	private final OrderTable orderTable;
	private final List<Order> orders;

	public ChangeEmptyValidator(OrderTable orderTable, List<Order> orders) {
		this.orderTable = orderTable;
		this.orders = orders;
	}

	void validate() {
		validateNotGrouped();
		validateCompletedOrders();
	}

	private void validateNotGrouped() {
		if (orderTable.isGrouped()) {
			throw new IllegalArgumentException("그룹 설정이 되어 있는 테이블은 주문 등록 불가 상태로 바꿀 수 없습니다.");
		}
	}

	private void validateCompletedOrders() {
		boolean isCompleteAll = orders.stream().allMatch(Order::isComplete);
		if (!isCompleteAll) {
			throw new IllegalArgumentException("조리상태이거나 식사상태주문의 주문테이블은 상태를 변경할 수 없습니다.");
		}
	}
}
