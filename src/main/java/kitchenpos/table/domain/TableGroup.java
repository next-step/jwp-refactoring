package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table_group")
public class TableGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private OrderTables orderTables = new OrderTables();

	private LocalDateTime createdDate;

	protected TableGroup() {
	}

	public TableGroup(List<OrderTable> orderTables) {
		this.createdDate = LocalDateTime.now();
		addOrderTables(orderTables);
	}

	private void addOrderTables(List<OrderTable> orderTables) {
		this.orderTables.addAll(orderTables);
		orderTables.forEach(orderTable -> orderTable.changeTableGroup(this));

	}

	public static TableGroup group(List<OrderTable> orderTable,
								   GroupTablesValidator validator) {
		validator.validate(new OrderTables(orderTable));
		return new TableGroup(orderTable);
	}

	public Long getId() {
		return id;
	}

	public OrderTables getOrderTables() {
		return orderTables;
	}

	public void ungroup(UnGroupTablesValidator unGroupTablesValidator) {
		unGroupTablesValidator.validate(this);
		orderTables.ungroup();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TableGroup that = (TableGroup)o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
