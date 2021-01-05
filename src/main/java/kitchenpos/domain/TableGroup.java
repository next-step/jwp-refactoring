package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import kitchenpos.exception.AlreadyTableGroupException;

@Entity
public class TableGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime createdDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderTable> orderTables = new ArrayList<>();

	protected TableGroup() {
	}

	private TableGroup(List<OrderTable> orderTables) {
		for (OrderTable orderTable : orderTables) {
			validateOrderTable(orderTable);
			orderTable.saveGroupInfo(this);
		}

		this.orderTables = orderTables;
		this.createdDate = LocalDateTime.now();
	}

	private void validateOrderTable(OrderTable orderTable) {
		if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
			throw new AlreadyTableGroupException("비어 있지 않은 테이블이거나 이미 단체테이블이 지정된 테이블입니다.");
		}
	}

	public static TableGroup create(List<OrderTable> orderTables) {
		return new TableGroup(orderTables);
	}

	public void ungroup() {
		for (final OrderTable orderTable : this.orderTables) {
			orderTable.ungroup();
		}
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}
}
