package kitchenpos.tableGroup.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

	@OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderTable> orderTables;

	public TableGroup() {
	}

	public TableGroup(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

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

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	public void addOrderTables(List<OrderTable> orderTables) {
		validateOrderTablesSize(orderTables);
		validateOrderTableOccupied(orderTables);

		for (OrderTable orderTable : orderTables) {
			orderTable.mapTableGroup(this);
		}

		this.orderTables = orderTables;
	}

	private void validateOrderTableOccupied(List<OrderTable> orderTables) {
		if(isNotEmptyOrTableGroupExists(orderTables)) {
			throw new IllegalArgumentException();
		}
	}

	private boolean isNotEmptyOrTableGroupExists(List<OrderTable> orderTables) {
		return orderTables.stream()
			.filter(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())).findAny()
			.isPresent();
	}

	private void validateOrderTablesSize(List<OrderTable> orderTables) {
		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException();
		}
	}
}
