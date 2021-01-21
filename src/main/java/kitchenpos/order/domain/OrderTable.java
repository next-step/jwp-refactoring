package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "order_table")
public class OrderTable {

	private static int MIN_NUMBER_OF_GUESTS = 0;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int numberOfGuests;
	private boolean empty;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "table_group_id")
	private TableGroup tableGroup;

	@OneToMany(mappedBy = "orderTable")
	private List<Orders> orders = new ArrayList<>();

	protected OrderTable() {
	}

	public OrderTable(boolean empty) {
		this.empty = empty;
	}

	public OrderTable(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTable(int numberOfGuests, boolean empty, TableGroup tableGroup) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
		this.tableGroup = tableGroup;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}


	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}


	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(final int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(final boolean empty) {
		this.empty = empty;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}


	public void setOrder(Orders orders) {
		this.orders.add(orders);
	}

	public void changeEmpty(boolean empty) {
		this.empty = empty;
	}

	private void validateOrderTable() {
		if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
			throw new IllegalArgumentException("손님의 수는 0보다 작을 수 없습니다.");
		}

		if (this.isEmpty()) {
			throw new IllegalArgumentException("비어있는 테이블에는 손님의 수를 변경할 수 없습니다.");
		}

	}

	public void changeNumberOfGuests(int numberOfGuests) {
		validateOrderTable();
		this.numberOfGuests = numberOfGuests;
	}


	public void unGroup(boolean allOrderCompleted) {
		validateCompleteAllOrders(allOrderCompleted);
		this.tableGroup = null;
	}

	private void validateCompleteAllOrders(boolean allOrderCompleted) {
		if (!allOrderCompleted) {
			throw new IllegalArgumentException("계산완료 테이블 상태일때만 상태를 변경할 수 있습니다.");
		}
	}

	public Long getTableGroupId() {
		return this.tableGroup == null ? null : this.tableGroup.getId();
	}

	public void group(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}


}
