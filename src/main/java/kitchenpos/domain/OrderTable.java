package kitchenpos.domain;

import kitchenpos.common.BaseIdEntity;
import kitchenpos.common.NumberOfGuests;
import kitchenpos.common.TableValidationException;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "order_table")
public class OrderTable extends BaseIdEntity {

	static final String MSG_TABLE_GROUP_NOT_NULL = "tableGroup must not be null";
	static final String MSG_ORDER_TABLE_ALREADY_GROUP = "OrderTable already has TableGroup";
	static final String MSG_ORDER_TABLE_ONGOING = "OrderTable's OrderStatus is ongoing";
	static final String MSG_ORDER_TABLE_EMPTY = "OrderTable must be empty";
	static final String MSG_CANNOT_CHANGE_EMPTY_ALREADY_GROUP = "Cannot change empty of already in TableGroup";
	static final String MSG_CANNOT_CHANGE_EMPTY_ONGOING_ORDER = "Cannot change empty while Order is ongoing";
	static final String MSG_CANNOT_CHANGE_GUEST_WHILE_EMPTY = "Cannot change guests while OrderTable is empty";
	static final String MSG_CANNOT_ADD_ORDER_TABLE_EMPTY = "Cannot add order while OrderTable is empty";

	@ManyToOne
	@JoinColumn(name = "table_group_id", nullable = true)
	private TableGroup tableGroup;

	@Embedded
	private NumberOfGuests numberOfGuests;

	@Column(name = "empty", nullable = false)
	private boolean empty;

	@Embedded
	private TableOrders orders;

	protected OrderTable() {
	}

	public OrderTable(int numberOfGuests, boolean empty) {
		this(numberOfGuests, empty, null);
	}

	private OrderTable(int numberOfGuests, boolean empty, TableGroup tableGroup) {
		this.tableGroup = tableGroup;
		this.numberOfGuests = new NumberOfGuests(numberOfGuests);
		this.empty = empty;
		this.orders = new TableOrders();
	}

	public void changeEmpty(boolean empty) {
		if (this.tableGroup != null) {
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

	void putIntoGroup(TableGroup tableGroup) {
		if (tableGroup == null) {
			throw new TableValidationException(MSG_TABLE_GROUP_NOT_NULL);
		}

		if (this.tableGroup != null) {
			throw new TableValidationException(MSG_ORDER_TABLE_ALREADY_GROUP);
		}

		if (!this.empty) {
			throw new TableValidationException(MSG_ORDER_TABLE_EMPTY);
		}

		this.tableGroup = tableGroup;
		this.empty = false;
	}

	void ungroup() {
		if (orders.hasOngoingOrder()) {
			throw new TableValidationException(MSG_ORDER_TABLE_ONGOING);
		}
		this.tableGroup = null;
	}

	public Order order(List<OrderItem> items) {
		if (this.empty) {
			throw new TableValidationException(MSG_CANNOT_ADD_ORDER_TABLE_EMPTY);
		}

		return this.orders.createNewOrder(this, items);
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public NumberOfGuests getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}
}
