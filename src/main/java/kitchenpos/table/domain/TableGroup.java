package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime createdDate;
	@OneToMany
	@JoinColumn(name = "tableGroupId")
	private Set<OrderTable> orderTables = new HashSet<>();

	public TableGroup() {
	}

	public TableGroup(Long id, LocalDateTime createdDate, Set<OrderTable> orderTables) {
		validate(orderTables);
		changeNotEmpty(orderTables);

		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	private void validate(Set<OrderTable> orderTables) {
		if (orderTables == null || orderTables.size() < 2) {
			throw new IllegalArgumentException("주문 테이블이 2개 이상이어야 단체로 지정될 수 있습니다.");
		}
		if (!orderTables.stream().allMatch(OrderTable::isEmpty)) {
			throw new IllegalArgumentException("주문 테이블이 모두 비어있지 않습니다.");
		}
		if (orderTables.stream().anyMatch(OrderTable::isInGroup)) {
			throw new IllegalArgumentException("이미 단체 지정이 되어있는 주문 테이블이 포함되어 있습니다.");
		}
	}

	private void changeNotEmpty(Set<OrderTable> orderTables) {
		for (OrderTable orderTable : orderTables) {
			orderTable.changeEmpty(false);
		}
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public Set<OrderTable> getOrderTables() {
		return orderTables;
	}

	public static final class Builder {
		private Long id;
		private LocalDateTime createdDate;
		private Set<OrderTable> orderTables = new HashSet<>();

		public Builder() {
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder createdDate(LocalDateTime createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public Builder orderTables(Set<OrderTable> orderTables) {
			this.orderTables = orderTables;
			return this;
		}

		public Builder orderTables(OrderTable... orderTables) {
			Collections.addAll(this.orderTables, orderTables);
			return this;
		}

		public TableGroup build() {
			return new TableGroup(id, createdDate, orderTables);
		}
	}
}
