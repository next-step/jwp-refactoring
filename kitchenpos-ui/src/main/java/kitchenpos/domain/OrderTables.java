package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.exception.AlreadyTableGroupException;

@Embeddable
public class OrderTables {

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "table_group_id")
	private List<OrderTable> orderTables = new ArrayList<>();

	public void add(Long tableGroupId, List<OrderTable> orderTables) {
		for (OrderTable orderTable : orderTables) {
			validateOrderTable(orderTable);
			orderTable.saveGroupInfo(tableGroupId);
			this.orderTables.add(orderTable);
		}
	}

	private void validateOrderTable(OrderTable orderTable) {
		if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
			throw new AlreadyTableGroupException("비어 있지 않은 테이블이거나 이미 단체테이블이 지정된 테이블입니다.");
		}
	}

	public void ungroup() {
		for (final OrderTable orderTable : this.orderTables) {
			orderTable.ungroup();
		}
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}
}
