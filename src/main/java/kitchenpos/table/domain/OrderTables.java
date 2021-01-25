package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

/**
 * @author : byungkyu
 * @date : 2021/01/25
 * @description :
 **/
@Embeddable
public class OrderTables {
	@OneToMany(mappedBy = "tableGroup")
	private List<OrderTable> orderTables = new ArrayList<>();

	public OrderTables() {
	}

	public OrderTables(List<OrderTable> orderTables) {
		this.orderTables.addAll(orderTables);
	}

	public OrderTables(List<OrderTable> orderTables, TableGroup tableGroup) {
		this.orderTables.addAll(orderTables);
		changeOrderTablesTableGroup(tableGroup);
	}

	private void changeOrderTablesTableGroup(TableGroup tableGroup) {
		this.orderTables.forEach(
			orderTable -> orderTable.changeTableGroup(tableGroup)
		);
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}
}
