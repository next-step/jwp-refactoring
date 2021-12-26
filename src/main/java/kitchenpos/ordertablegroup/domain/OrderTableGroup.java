package kitchenpos.ordertablegroup.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.ordertable.domain.OrderTable;

@Table(name = "table_group")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OrderTableGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime createdDate;

	@OneToMany(mappedBy = "orderTableGroup", cascade = CascadeType.MERGE)
	private List<OrderTable> orderTables = new ArrayList<>();

	protected OrderTableGroup() {
	}

	public static OrderTableGroup from(List<OrderTable> orderTables) {
		throwOnLessThanTwo(orderTables);
		orderTables.forEach(OrderTableGroup::throwOnAlreadyHavingOrderTableGroup);
		orderTables.forEach(OrderTableGroup::throwOnNotEmpty);

		OrderTableGroup orderTableGroup = new OrderTableGroup();
		orderTableGroup.orderTables = orderTables;
		orderTableGroup.orderTables.forEach(orderTable -> orderTable.groupedBy(orderTableGroup));
		return orderTableGroup;
	}

	private static void throwOnLessThanTwo(List<OrderTable> orderTables) {
		if (orderTables == null || orderTables.size() < 2) {
			throw new IllegalArgumentException("주문 테이블이 2개 이상이어야 주문 테이블 그룹을 등록할 수 있습니다.");
		}
	}

	private static void throwOnAlreadyHavingOrderTableGroup(OrderTable orderTable) {
		if (orderTable.hasOrderTableGroup()) {
			throw new IllegalArgumentException("이미 주문 테이블 그룹이 있는 경우 등록할 수 없습니다.");
		}
	}

	private static void throwOnNotEmpty(OrderTable orderTable) {
		if (!orderTable.isEmpty()) {
			throw new IllegalArgumentException("주문 테이블이 비어있지 않은 경우 주문 테이블 그룹을 등록할 수 없습니다.");
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

	public void ungroup() {
		// TODO : fix this
		// if (orderTables.stream().anyMatch(OrderTable::hasNotCompletedOrder)) {
		// 	throw new IllegalStateException("주문 테이블에 완료되지 않은 주문이 있는 경우 주문 테이블 그룹을 해제할 수 없습니다.");
		// }

		orderTables.forEach(OrderTable::ungrouped);
		orderTables = new ArrayList<>();
	}
}
