package kitchenpos.ordertable.domain;

import java.util.List;
import java.util.Optional;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.util.CollectionUtils;

public class OrderTables {

	private List<OrderTable> orderTables;

	public OrderTables(List<OrderTable> savedOrderTables, int size) {
		validate(savedOrderTables, size);
		this.orderTables = savedOrderTables;
	}

	public OrderTables(List<OrderTable> savedOrderTables) {
		this.orderTables = savedOrderTables;
	}

	public void group(TableGroup tableGroup) {
		this.orderTables.forEach(
			  orderTable -> orderTable.group(tableGroup)
		);
	}

//	public void unTableGroup() {
//		this.orderTables.forEach(OrderTable::unTableGroup);
//	}

	private void validate(List<OrderTable> savedOrderTables, int requestSize) {
		if (CollectionUtils.isEmpty(savedOrderTables) || savedOrderTables.size() < 2) {
			throw new IllegalArgumentException("단체 지정은 주문 테이블이 최소 2개 이상이어야 합니다.");
		}

		if (savedOrderTables.size() != requestSize) {
			throw new IllegalArgumentException("주문 테이블 정보를 찾을 수 없습니다.");
		}

		Optional<OrderTable> otherTableGroup = savedOrderTables.stream()
			  .filter(OrderTable::isJoinedTableGroup)
			  .findFirst();

		if (otherTableGroup.isPresent()) {
			throw new IllegalArgumentException("단체 지정이 불가능한 테이블입니다.");
		}
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	public void setTableGroup(TableGroup tableGroup) {
		this.orderTables.forEach(orderTable -> orderTable.setTableGroup(tableGroup));
	}

	public TableGroup getRespTableGroup() {
		return this.orderTables.get(0).getTableGroup();
	}
}
