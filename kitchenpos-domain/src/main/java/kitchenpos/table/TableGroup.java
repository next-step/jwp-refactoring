package kitchenpos.table;

import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.common.BaseEntity;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Embedded
	private OrderTables orderTables;

	public TableGroup() {
	}

	public Long getId() {
		return id;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables.getList();
	}

	public List<Long> getOrderTableIds() {
		return orderTables.getIds();
	}

	public void group(List<OrderTable> orderTables) {
		this.orderTables = new OrderTables(orderTables);
		validateGroup(this.orderTables);
		this.orderTables.group(this);
	}

	public void ungroup() {
		this.orderTables.ungroup();
	}

	private void validateGroup(OrderTables orderTables) {
		if (orderTables.isEmpty() || orderTables.isLessThan(2)) {
			throw new IllegalArgumentException("단체 지정은 2개 이상의 테이블 부터 가능합니다.");
		}

		for (final OrderTable orderTable : orderTables.getList()) {
			if (!orderTable.isEmpty()) {
				throw new IllegalArgumentException("단체 지정은 비어있는 테이블에서 가능합니다.");
			}

			if (Objects.nonNull(orderTable.getTableGroupId())) {
				throw new IllegalArgumentException("이미 단체 지정이 된 테이블 입니다.");
			}
		}
	}
}
