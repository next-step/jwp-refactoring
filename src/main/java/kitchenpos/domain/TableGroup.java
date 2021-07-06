package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;

@Entity
public class TableGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreatedDate
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@OneToMany(mappedBy = "tableGroup")
	private List<OrderTable> orderTables;

	protected TableGroup() {
	}

	public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	public void addOrderTable(OrderTable orderTable) {
		this.orderTables.add(orderTable);
		orderTable.changeTableGroup(this);
	}

	public void setOrderTables(List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public void setCreatedDate(LocalDateTime now) {
		this.createdDate = now;
	}
}
