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
	@Embedded
	private final OrderTables orderTables = new OrderTables();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime createdDate;

	protected TableGroup() {
	}

	public TableGroup(List<OrderTable> orderTables) {
		addOrderTables(orderTables);
		this.createdDate = LocalDateTime.now();
	}

	public static TableGroup group(List<OrderTable> orderTables,
		GroupTablesValidator validator) {
		validator.validate(new OrderTables(orderTables));
		return new TableGroup(orderTables);
	}

	private void addOrderTables(List<OrderTable> changingOrderTables) {
		orderTables.addAll(changingOrderTables);
		changingOrderTables.stream()
			.forEach(orderTable ->
				orderTable.changeTableGroup(this));

	}

	public Long getId() {
		return id;
	}

	public OrderTables getOrderTables() {
		return orderTables;
	}

	public void ungroup() {
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
