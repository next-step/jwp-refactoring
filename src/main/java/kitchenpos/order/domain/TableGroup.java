package kitchenpos.order.domain;

import kitchenpos.common.BaseIdEntity;
import kitchenpos.order.application.TableGroupValidationException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "table_group")
@EntityListeners(AuditingEntityListener.class)
public class TableGroup extends BaseIdEntity {

	private static final int TABLE_GROUP_MIN = 2;
	static final String MSG_TABLE_COUNT_LEAST = String.format("TableIds'size must be at least %d", TABLE_GROUP_MIN);

	@CreatedDate
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@OneToMany(mappedBy = "tableGroup")
	private List<OrderTable> orderTables;

	public static TableGroup fromGroupingTables(List<OrderTable> orderTables) {
		return new TableGroup(orderTables);
	}

	TableGroup() {
	}

	TableGroup(List<OrderTable> orderTables) {
		groupTables(orderTables);
	}

	private void groupTables(List<OrderTable> orderTables) {
		if (orderTables.size() < TABLE_GROUP_MIN) {
			throw new TableGroupValidationException(MSG_TABLE_COUNT_LEAST);
		}

		orderTables.forEach(orderTable -> orderTable.putIntoGroup(this));
		this.orderTables = orderTables;
	}

	public void ungroupTables() {
		orderTables.forEach(OrderTable::ungroup);
		this.orderTables = Collections.emptyList();
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}
}
