package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.exception.TableGroupException;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private OrderTables orderTables;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;

	protected TableGroup() {
	}

	private TableGroup(OrderTables orderTables) {
		validateNullOrderTables(orderTables);
		validateOneTable(orderTables);
		validateNotEmptyOrderTable(orderTables);
		orderTables.changeTableGroup(this);
		this.orderTables = orderTables;
	}

	public static TableGroup from(OrderTables orderTables) {
		return new TableGroup(orderTables);
	}

	public static TableGroup from(List<OrderTable> orderTables) {
		return new TableGroup(OrderTables.from(orderTables));
	}

	private void validateNullOrderTables(OrderTables orderTables) {
		if (orderTables.isEmpty()) {
			throw new TableGroupException(ErrorCode.ORDER_TABLE_IS_NULL);
		}
	}

	private void validateNotEmptyOrderTable(OrderTables orderTables) {
		if (orderTables.findAnyNotEmptyTable()) {
			throw new TableGroupException(ErrorCode.ORDER_TABLE_IS_EMPTY);
		}
	}

	private void validateOneTable(OrderTables orderTables) {
		if (orderTables.isOneTable()) {
			throw new TableGroupException(ErrorCode.NEED_MORE_ORDER_TABLES);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public OrderTables getOrderTables() {
		return orderTables;
	}

	public void unGroup() {
		orderTables.unGroupOrderTables();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TableGroup that = (TableGroup)o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
