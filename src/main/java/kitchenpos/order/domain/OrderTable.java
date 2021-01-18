package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OrderTable {

	private static int MIN_NUMBER_OF_GUESTS = 0;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "table_group_id")
	private TableGroup tableGroup;

	@OneToMany(mappedBy = "orderTable")
	private List<Orders> orders = new ArrayList<>();

	protected OrderTable() {
	}

	public OrderTable(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTable(TableGroup tableGroup, List<Orders> orders, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroup = tableGroup;
		this.orders = orders;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}

	private int numberOfGuests;

	private boolean empty;

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

	public void setTableGroup(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
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

	public void unTableGroup() {
		this.tableGroup = null;
	}
}
