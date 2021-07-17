package kitchenpos.tableGroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.table.domain.OrderTable;

@Entity
public class TableGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime createdDate;

	@OneToMany(mappedBy = "order_table", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderTable> orderTables;

	public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void addOrderTables(List<OrderTable> orderTables) {
		validateOrderTablesSize(orderTables);
		validateOrderTableOccupied(orderTables);

		this.orderTables = orderTables;
	}

	private void validateOrderTableOccupied(List<OrderTable> orderTables) {
		for (OrderTable orderTable : orderTables) {
			if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
				throw new IllegalArgumentException();
			}
		}
	}

	private void validateOrderTablesSize(List<OrderTable> orderTables) {
		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException();
		}
	}
}
