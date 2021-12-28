package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@Entity
@Table(name = "table_group")
public class TableGroup {

	private static final int MINIMUM = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime createdDate;

	@Embedded
	private OrderTables orderTables;

	protected TableGroup() {
	}

	private TableGroup(Long id, OrderTables orderTables, LocalDateTime createdDate) {
		this.id = id;
		this.orderTables = orderTables;
		this.createdDate = createdDate;
	}

	public static TableGroup of(Long id, List<OrderTable> orderTables) {
		validate(orderTables);
		return new TableGroup(id, OrderTables.of(orderTables), LocalDateTime.now());
	}

	private static void validate(List<OrderTable> orderTables) {
		if (Objects.isNull(orderTables)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "주문 테이블이 Null 이면 안됩니다");
		}
		if (orderTables.size() < MINIMUM) {
			throw new AppException(ErrorCode.WRONG_INPUT, "단체 지정은 주문 테이블이 {} 개 이상이어야 합니다", MINIMUM);
		}
		if (hasAnyEmptyTable(orderTables)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "단체 지정은 빈 테이블이 있으면 안됩니다");
		}
	}

	private static boolean hasAnyEmptyTable(List<OrderTable> orderTables) {
		return orderTables.stream().anyMatch(OrderTable::isEmpty);
	}

	public static TableGroup create(List<OrderTable> orderTableIds) {
		return of(null, orderTableIds);
	}

	public void unGroup() {
		orderTables.unGroup();
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
		return id.hashCode();
	}
}
