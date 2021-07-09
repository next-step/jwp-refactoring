package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "table_group_id")
	private TableGroup tableGroup;

	@OneToMany(mappedBy = "orderTable")
	private List<Order> orders = new ArrayList<>();

	@Embedded
	private NumberOfGuests numberOfGuests;

	@Column(name = "empty")
	private boolean empty;

	protected OrderTable() {
	}

	public OrderTable(TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
		this.tableGroup = tableGroup;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public Long getId() {
		return id;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrderTable that = (OrderTable)o;
		return empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroup,
			that.tableGroup) && Objects.equals(numberOfGuests, that.numberOfGuests);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tableGroup, numberOfGuests, empty);
	}

	public void changeTableGroup(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}

	public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
		if (empty) {
			throw new IllegalArgumentException("비어있는 테이블입니다.");
		}
		this.numberOfGuests = numberOfGuests;
	}

	public void changeEmpty(boolean empty) {
		if (this.tableGroup != null) {
			throw new IllegalArgumentException("단체 지정되어있는 테이블은 변경할 수 없습니다.");
		}
		validateChangeableStatus();
		this.empty = empty;
	}

	public void unGroup() {
		validateChangeableStatus();
		this.tableGroup = null;
	}

	private void validateChangeableStatus() {
		boolean isUnChangeable = orders.stream().anyMatch(Order::isUnChangeable);
		if (isUnChangeable) {
			throw new IllegalArgumentException("주문 상태가 완료되어야 단체지정이 해제가능합니다.");
		}
	}

	public void addOrder(Order order) {
		this.orders.add(order);
	}
}
