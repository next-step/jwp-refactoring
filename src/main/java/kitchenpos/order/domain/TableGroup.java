package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
	private static final int MINIMUM_TABLES_SIZE = 2;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreatedDate
	private LocalDateTime createdDate;

	@Embedded
	private OrderTables orderTables;

	protected TableGroup() {
	}

	private TableGroup(OrderTables orderTables) {
		validate(orderTables);
		orderTables.updateGroup(this);
		this.orderTables = orderTables;
	}

	private void validate(OrderTables orderTables) {
		Assert.notNull(orderTables, "주문 테이블들은 필수입니다.");
		Assert.isTrue(orderTables.size() >= MINIMUM_TABLES_SIZE,
			String.format("주문 테이블은 %d개 이상이어야 합니다.", MINIMUM_TABLES_SIZE));
		Assert.isTrue(orderTables.emptyAndNoGroup(), "주문 테이블들은 비어있고, 그룹이 지정되지 않아야 합니다.");
	}

	public static TableGroup from(OrderTables orderTables) {
		return new TableGroup(orderTables);
	}

	public static TableGroup from(List<OrderTable> orderTables) {
		return new TableGroup(OrderTables.from(orderTables));
	}

	public Long id() {
		return id;
	}

	public LocalDateTime createdDate() {
		return createdDate;
	}

	public OrderTables orderTables() {
		return orderTables;
	}

	public void ungroup() {
		orderTables.ungroup();
	}

	public List<Long> orderTableIds() {
		return orderTables.list()
			.stream()
			.map(OrderTable::id)
			.collect(Collectors.toList());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TableGroup that = (TableGroup)o;
		return Objects.equals(orderTables, that.orderTables);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(orderTables);
	}
}
