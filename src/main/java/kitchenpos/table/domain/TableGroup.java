package kitchenpos.table.domain;

import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.HAS_GROUP_TABLE;
import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.HAS_NO_ORDER_TABLE;
import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.INVALID_TABLE_COUNT;
import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.NOT_EMPTY_ORDER_TABLE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

import kitchenpos.table.exception.CannotCreateGroupTableException;

@Entity
@Table(name = "table_group")
public class TableGroup {

	public static final int MINIMUM_ORDER_TABLE_COUNT = 2;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime createdDate;

	@OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<OrderTable> orderTables = new ArrayList<>();

	protected TableGroup() {
	}

	public TableGroup(List<OrderTable> orderTables) {
		this.createdDate = LocalDateTime.now();
		addOrderTables(orderTables);
	}

	public static TableGroup create(List<OrderTable> orderTable) {
		validateCreateTableGroup(orderTable);
		return new TableGroup(orderTable);
	}

	public Long getId() {
		return id;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	private void addOrderTables(List<OrderTable> orderTables) {
		orderTables.forEach(this::addOrderTable);
	}

	private void addOrderTable(OrderTable orderTable) {
		if (!orderTables.contains(orderTable)) {
			orderTables.add(orderTable);
		}
		orderTable.changeTableGroup(this);
	}

	public void ungroup() {
		orderTables.forEach(OrderTable::detachTableGroup);
	}

	public static void validateCreateTableGroup(List<OrderTable> orderTables) {
		if (hasNoOrderTables(orderTables)) {
			throw new CannotCreateGroupTableException(HAS_NO_ORDER_TABLE);
		}
		if (isOrderTablesValidSize(orderTables)) {
			throw new CannotCreateGroupTableException(INVALID_TABLE_COUNT);
		}
		if (isOrderTableNotEmpty(orderTables)) {
			throw new CannotCreateGroupTableException(NOT_EMPTY_ORDER_TABLE);
		}
		if (hasOrderTableGroup(orderTables)) {
			throw new CannotCreateGroupTableException(HAS_GROUP_TABLE);
		}
	}

	private static boolean hasOrderTableGroup(List<OrderTable> orderTables) {
		return orderTables.stream().anyMatch(OrderTable::hasTableGroup);
	}

	private static boolean isOrderTableNotEmpty(List<OrderTable> orderTables) {
		return orderTables.stream().noneMatch(OrderTable::isEmpty);
	}

	private static boolean isOrderTablesValidSize(List<OrderTable> orderTables) {
		return orderTables.size() < MINIMUM_ORDER_TABLE_COUNT;
	}

	private static boolean hasNoOrderTables(List<OrderTable> orderTables) {
		return CollectionUtils.isEmpty(orderTables);
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
