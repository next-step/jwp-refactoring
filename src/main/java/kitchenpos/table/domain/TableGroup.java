package kitchenpos.table.domain;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.exception.ErrorMessage;

@Entity
public class TableGroup {
	private static int GROUP_MIN_SIZE = 2;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime createdDate;
	@Embedded
	private OrderTables orderTables = new OrderTables();

	protected TableGroup() {}

	private TableGroup(OrderTables orderTables) {
		validateOrderTablesSize(orderTables);
		validateOrderTablesGrouping(orderTables);
		validateOrderTablesEmpty(orderTables);
		this.createdDate = LocalDateTime.now();
		this.orderTables = orderTables;
	}

	public static TableGroup of(OrderTables orderTables) {
		TableGroup tableGroup = new TableGroup(orderTables);
		tableGroup.orderTables.updateGroup(tableGroup);
		return tableGroup;
	}

	private void validateOrderTablesEmpty(OrderTables orderTables) {
		if (!orderTables.isAllEmpty()) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_IS_NOT_ALL_EMPTY);
		}
	}

	private void validateOrderTablesSize(OrderTables orderTables) {
		if (orderTables.getSize() < GROUP_MIN_SIZE) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_SIZE_SIZE_IS_TOO_SMALL);
		}
	}

	private void validateOrderTablesGrouping(OrderTables orderTables) {
		if (orderTables.isAnyGrouped()) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_ALREADY_GROUPED);
		}
	}

	public void unGroup() {
		this.orderTables.updateGroup(null);
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
