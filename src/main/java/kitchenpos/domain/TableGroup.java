package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime createdDate;

	@OneToMany(mappedBy = "id")
	private List<OrderTable> orderTables;

	public TableGroup() {
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

	public void setCreatedDate(final LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	public void setOrderTables(final List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TableGroup that = (TableGroup) o;
		return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate) && Objects.equals(orderTables, that.orderTables);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, createdDate, orderTables);
	}
}
