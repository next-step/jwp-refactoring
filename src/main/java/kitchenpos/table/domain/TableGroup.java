package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kitchenpos.table.exception.TableGroupException;

@Entity
@Table(name = "table_group")
public class TableGroup {

	private static final int MIN_TABLE_GROUP_SIZE = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime createdDate;

	@OneToMany(mappedBy = "tableGroup")
	private List<OrderTable> orderTables;

	protected TableGroup() {

	}

	public TableGroup(List<OrderTable> orderTables) {
		validate(orderTables);
		this.orderTables = orderTables;
		this.createdDate = LocalDateTime.now();
		groupOrderTables();
	}

	public TableGroup(Long id, List<OrderTable> orderTables) {
		this(orderTables);
		this.id = id;
	}

	private void groupOrderTables() {
		this.orderTables.forEach(orderTable -> orderTable.group(this));
	}

	private void validate(List<OrderTable> orderTables) {
		if (orderTables.size() < MIN_TABLE_GROUP_SIZE) {
			throw new TableGroupException("단체 지정 시 2개 이상 주문테이블을 등록해야 합니다.");
		}

		if (orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty())) {
			throw new TableGroupException("단체 지정 시 주문 테이블이 비워져 있어야 합니다.");
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

	public void unGroup() {
		orderTables.stream()
			.forEach(OrderTable::unGroup);
	}

}
