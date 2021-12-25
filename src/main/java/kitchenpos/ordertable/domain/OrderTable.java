package kitchenpos.ordertable.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.orders.domain.Order;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "bigint(20)")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_group_id", columnDefinition = "bigint(20)")
	private TableGroup tableGroup;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orderTable")
	private List<Order> orders = new ArrayList<>();

	@Column(columnDefinition = "int(11)", nullable = false)
	private int numberOfGuests;

	@Column(nullable = false)
	private Boolean empty;

	public OrderTable() {
	}

	public OrderTable(Long id) {
		this.id = id;
	}

	public OrderTable(int numberOfGuests, boolean empty) {
		this(null, null, numberOfGuests, empty);
	}

	public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroup = tableGroup;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public boolean isGrouped() {
		return Objects.nonNull(tableGroup);
	}

	public boolean isNotUse() {
		return this.empty == true;
	}

	public void changeNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public boolean isUseOrIsGrouped() {
		return isUse() || isGrouped();
	}

	public boolean isUse() {
		return this.empty == false;
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

	public Boolean getEmpty() {
		return empty;
	}

	public Long getTableGroupId() {
		if (isNotGrouped()) {
			return -1L;
		}
		return this.tableGroup.getId();
	}

	private boolean isNotGrouped() {
		return Objects.isNull(tableGroup);
	}

	public void use() {
		this.empty = false;
	}

	public void toGroup(TableGroup tableGroup) {
		use();
		this.tableGroup = tableGroup;
	}

	public void notUse() {
		this.empty = true;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public boolean isOrderCompletion() {
		return this.orders.stream()
			.allMatch(Order::isCompletion);
	}

	public void ungroup() {
		this.tableGroup = null;
	}


}
