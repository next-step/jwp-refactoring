package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {
	@OneToMany(mappedBy = "id")
	private List<OrderTable> orderTables = new ArrayList<>();

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	public void addAllOrderTables(List<OrderTable> orderTables) {
		this.orderTables.addAll(orderTables);
	}
}
