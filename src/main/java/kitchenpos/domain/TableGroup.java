package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime createdDate;

	@Embedded
	private OrderTables orderTables = new OrderTables();

	protected TableGroup() {
	}

	private TableGroup(List<OrderTable> orderTables) {
		add(orderTables);
		this.createdDate = LocalDateTime.now();
	}

	private void add(List<OrderTable> orderTables) {
		this.orderTables.add(this.id, orderTables);

	}

	public static TableGroup create(List<OrderTable> orderTables) {
		return new TableGroup(orderTables);
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables.getOrderTables();
	}

	public void ungroup() {
		this.orderTables.ungroup();
	}
}
