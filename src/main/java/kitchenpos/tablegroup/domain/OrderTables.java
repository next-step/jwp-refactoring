package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

	@OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<OrderTable> orderTables = new ArrayList<>();

	public OrderTables() {
	}

	public OrderTables(List<OrderTable> orderTables, int requestSize) {
		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException();
		}
		if (orderTables.size() != requestSize) {
			throw new IllegalArgumentException();
		}

		this.orderTables = orderTables;
	}

	public void setOrderTables(List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	public void setTableGroup(TableGroup tableGroup) {
		this.orderTables.forEach(orderTable -> orderTable.setTableGroup(tableGroup));
	}

}