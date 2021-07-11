package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreatedDate
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@OneToMany(mappedBy = "tableGroup")
	private List<OrderTable> orderTables = new ArrayList<>();

	protected TableGroup() {
	}

	public TableGroup(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException("주문 테이블이이 둘 이상이어야 단체지정을 할 수 있습니다.");
		}

		for (OrderTable orderTable : orderTables) {
			addOrderTable(orderTable);
		}
		this.createdDate = createdDate;
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

	public void addOrderTable(OrderTable orderTable) {
		if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
			throw new IllegalArgumentException("주문테이블이 단체지정이 되어있거나, 비어있지 않은 테이블입니다.");
		}

		this.orderTables.add(orderTable);
		orderTable.changeTableGroup(this);
	}

	public void unGroup() {
		for (OrderTable orderTable : this.orderTables) {
			orderTable.unGroup();
		}
	}
}
