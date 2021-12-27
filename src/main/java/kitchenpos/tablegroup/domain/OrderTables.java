package kitchenpos.tablegroup.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import kitchenpos.ordertable.domain.OrderTable;

@Embeddable
public class OrderTables {

	@OneToMany(mappedBy = "tableGroup", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private List<OrderTable> orderTables;

	public OrderTables() {
	}

	public OrderTables(final List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public void group(TableGroup tableGroup) {
		orderTables.forEach(orderTable -> orderTable.toGroup(tableGroup));
	}

	public List<OrderTable> value() {
		return new ArrayList<>(orderTables);
	}

	public void validateOrderTableIsUseOrIsGrouped() {
		if (isUseOrIsGrouped(orderTables)) {
			throw new IllegalArgumentException("이미 사용중이거나 그룹화된 테이블은 그룹생성 할 수 없습니다");
		}
	}

	private boolean isUseOrIsGrouped(List<OrderTable> orderTables) {
		return orderTables.stream()
			.anyMatch(orderTable -> orderTable.isUseOrIsGrouped());
	}

	public boolean isOrderCompletion() {
		return orderTables.stream()
			.allMatch(OrderTable::isOrderCompletion);
	}

	public void ungroup() {
		orderTables.forEach(OrderTable::ungroup);
	}

	public int size() {
		return orderTables.size();
	}
}
