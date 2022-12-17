package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

	@OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private final List<OrderTable> orderTables;

	protected OrderTables() {
		this.orderTables = new ArrayList<>();
	}

	private OrderTables(List<OrderTable> orderTables) {
		this.orderTables = Collections.unmodifiableList(orderTables);
	}

	public static OrderTables of(List<OrderTable> orderTables) {
		return new OrderTables(orderTables);
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	public void updateGroup(TableGroup tableGroup) {
		this.orderTables.forEach(it -> it.updateGroup(tableGroup));
	}

	public boolean isAllEmpty() {
		return orderTables.stream().allMatch(it -> it.isEmpty());
	}

	public boolean isAnyGrouped() {
		return orderTables.stream()
			.anyMatch(it -> Objects.nonNull(it.getTableGroup()));
	}

	public int getSize() {
		return orderTables.size();
	}

}
