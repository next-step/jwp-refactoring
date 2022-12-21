package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
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
		this.orderTables = orderTables;
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
