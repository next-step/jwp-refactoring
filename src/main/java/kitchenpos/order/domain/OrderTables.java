package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.tablegroup.domain.TableGroup;

@Embeddable
public class OrderTables {

	private static final int ONE_TABLE_SIZE = 1;

	@OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private List<OrderTable> orderTables = new ArrayList<>();

	protected OrderTables() {
	}

	private OrderTables(final List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public static OrderTables from(final List<OrderTable> orderTables) {
		return new OrderTables(orderTables);
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	public void changeTableGroup(TableGroup tableGroup) {
		orderTables.forEach(it -> it.changeTableGroup(tableGroup));
	}

	public boolean isEmpty() {
		return orderTables.isEmpty();
	}

	public boolean findAnyNotEmptyTable() {
		return orderTables.stream()
			.anyMatch(it -> !it.isEmpty());
	}

	public boolean isOneTable() {
		return orderTables.size() == ONE_TABLE_SIZE;
	}

	public void unGroupOrderTables() {
		orderTables.forEach(OrderTable::unGroup);
	}
}
