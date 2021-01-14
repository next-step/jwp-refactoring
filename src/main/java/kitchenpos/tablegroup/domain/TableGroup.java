package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import kitchenpos.ordertable.domain.OrderTables;

@Entity(name = "table_group")
public class TableGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime createdDate;

	@Embedded
	private OrderTables orderTables = new OrderTables();

	public TableGroup() {
	}

	public TableGroup(OrderTables orderTables) {
		this.orderTables = orderTables;
		this.orderTables.setTableGroup(this);
	}

	public void unTableGroup() {
		this.orderTables.unTableGroup();
	}

	@PrePersist
	public void prePersist() {
		this.createdDate = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public OrderTables getOrderTables() {
		return orderTables;
	}
}
