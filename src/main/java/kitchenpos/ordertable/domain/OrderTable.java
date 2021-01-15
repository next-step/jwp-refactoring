package kitchenpos.ordertable.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity(name = "order_table")
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int numberOfGuests;
	private boolean empty;

	@ManyToOne
	@JoinColumn(name = "table_group_id")
	private TableGroup tableGroup;

	@OneToMany(mappedBy = "orderTable")
	private List<Orders> orders = new ArrayList<>();

	public OrderTable() {
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

	public void changeNumberOfGuests(int numberOfGuests) {
		validateEmpty(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}

	public void changeEmpty(boolean empty) {
		validateCompleteAllOrders();
		validateNumberOfGuest();
		this.empty = empty;
	}

	public void unTableGroup() {
		validateCompleteAllOrders();
		this.tableGroup = null;
	}

	private void validateCompleteAllOrders() {
		Optional<Orders> notCompleteOrder = this.orders.stream()
			  .filter(order -> !OrderStatus.COMPLETION.name().equals(order.getOrderStatus()))
			  .findFirst();

		if (notCompleteOrder.isPresent()) {
			throw new IllegalArgumentException("조리, 식사 상태의 테이블은 상태를 변경할 수 없습니다.");
		}
	}

	private void validateEmpty(int numberOfGuests) {
		if (numberOfGuests < 0) {
			throw new IllegalArgumentException("게스트 수는 0명 이상이어야 합니다.");
		}

		if (this.empty) {
			throw new IllegalArgumentException("테이블이 비어있습니다.");
		}
	}

	private void validateNumberOfGuest() {
		if (this.tableGroup != null) {
			throw new IllegalArgumentException("단체 지정된 테이블은 상태를 변경할 수 없습니다.");
		}
	}

	public boolean isJoinedTableGroup() {
		return this.tableGroup != null;
	}

	public void setOrder(Orders orders) {
		this.orders.add(orders);
	}

	public void setTableGroup(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}

	public Long getId() {
		return id;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public Long getTableGroupId() {
		return this.tableGroup == null ? null : this.tableGroup.getId();
	}

	public boolean isEmpty() {
		return empty;
	}
}
