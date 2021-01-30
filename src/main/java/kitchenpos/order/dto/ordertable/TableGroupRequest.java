package kitchenpos.order.dto.ordertable;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.ordertable.OrderTable;
import kitchenpos.order.domain.ordertable.TableGroup;

public class TableGroupRequest {

	private List<OrderTableRequest> orderTables;

	public List<OrderTableRequest> getOrderTables() {
		return orderTables;
	}

	protected TableGroupRequest() {
	}

	public TableGroupRequest(final List<OrderTableRequest> orderTables) {
		this.orderTables = orderTables;
	}

	public void setOrderTables(final List<OrderTableRequest> orderTables) {
		this.orderTables = orderTables;
	}

	public TableGroup toTableGroup(final List<OrderTable> orderTables) {
		validate(orderTables);
		return TableGroup.createTableGroup(orderTables);
	}

	private void validate(final List<OrderTable> orderTables) {
		if (this.orderTables.size() != orderTables.size()) {
			throw new IllegalArgumentException("요청한 주문 테이블 중 정보가 없는 주문 테이블이 있습니다.");
		}
	}

	public List<Long> getOrderTableIds() {
		if (this.orderTables == null) {
			throw new IllegalArgumentException("주문 테이블 요청 정보가 없습니다.");
		}
		return this.orderTables.stream()
			.map(OrderTableRequest::getId)
			.collect(Collectors.toList());
	}
}
