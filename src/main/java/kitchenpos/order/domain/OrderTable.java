package kitchenpos.order.domain;

import kitchenpos.common.entity.BaseIdEntity;
import kitchenpos.common.entity.NumberOfGuests;
import kitchenpos.order.application.TableValidationException;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class OrderTable extends BaseIdEntity {

	static final String MSG_TABLE_GROUP_NOT_NULL = "tableGroupId must not be null";
	static final String MSG_ORDER_TABLE_ALREADY_GROUP = "OrderTable already has TableGroup";
	static final String MSG_ORDER_TABLE_ONGOING = "OrderTable's OrderStatus is ongoing";
	static final String MSG_ORDER_TABLE_EMPTY = "OrderTable must be empty";
	static final String MSG_CANNOT_CHANGE_EMPTY_ALREADY_GROUP = "Cannot change empty of already in TableGroup";
	static final String MSG_CANNOT_CHANGE_EMPTY_ONGOING_ORDER = "Cannot change empty while Order is ongoing";
	static final String MSG_CANNOT_CHANGE_GUEST_WHILE_EMPTY = "Cannot change guests while OrderTable is empty";
	static final String MSG_CANNOT_ADD_ORDER_TABLE_EMPTY = "Cannot add order while OrderTable is empty";

	@Column(name = "table_group_id", nullable = true, insertable = false, updatable = false)
	private Long tableGroupId;

	@Embedded
	private NumberOfGuests numberOfGuests;

	@Column(name = "empty", nullable = false)
	private boolean empty;

	@Embedded
	private TableOrders orders;

	protected OrderTable() {
	}

	public OrderTable(int numberOfGuests, boolean empty) {
		this.numberOfGuests = new NumberOfGuests(numberOfGuests);
		this.empty = empty;
		this.orders = new TableOrders();
	}

	public void changeEmpty(boolean empty) {
		if (this.tableGroupId != null) {
			throw new TableValidationException(MSG_CANNOT_CHANGE_EMPTY_ALREADY_GROUP);
		}

		if (this.orders.hasOngoingOrder()) {
			throw new TableValidationException(MSG_CANNOT_CHANGE_EMPTY_ONGOING_ORDER);
		}
		this.empty = empty;
	}

	public void changeNumberOfGuests(int numberOfGuests) {
		if (this.empty) {
			throw new TableValidationException(MSG_CANNOT_CHANGE_GUEST_WHILE_EMPTY);
		}
		this.numberOfGuests = new NumberOfGuests(numberOfGuests);
	}

	void putIntoGroup(Long tableGroupId) {
		if (tableGroupId == null) {
			throw new TableValidationException(MSG_TABLE_GROUP_NOT_NULL);
		}

		if (this.tableGroupId != null) {
			throw new TableValidationException(MSG_ORDER_TABLE_ALREADY_GROUP);
		}

		if (!this.empty) {
			throw new TableValidationException(MSG_ORDER_TABLE_EMPTY);
		}

		this.tableGroupId = tableGroupId;
		this.empty = false;
	}

	void ungroup() {
		if (orders.hasOngoingOrder()) {
			throw new TableValidationException(MSG_ORDER_TABLE_ONGOING);
		}
		this.tableGroupId = null;
	}

	public Order order(List<OrderItem> items) {
		if (this.empty) {
			throw new TableValidationException(MSG_CANNOT_ADD_ORDER_TABLE_EMPTY);
		}

		return this.orders.createNewOrder(this, items);
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public NumberOfGuests getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof OrderTable)) return false;
		if (!super.equals(o)) return false;
		OrderTable that = (OrderTable) o;
		return empty == that.empty &&
				Objects.equals(tableGroupId, that.tableGroupId) &&
				Objects.equals(numberOfGuests, that.numberOfGuests) &&
				Objects.equals(orders, that.orders);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), tableGroupId, numberOfGuests, empty, orders);
	}
}
