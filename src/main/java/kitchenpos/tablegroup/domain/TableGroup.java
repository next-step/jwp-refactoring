package kitchenpos.tablegroup.domain;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "table_group")
public class TableGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime createdDate;

	@Embedded
	private OrderTables orderTables = new OrderTables();

	public TableGroup(OrderTables orderTables) {
		this.orderTables = orderTables;
	}

	public TableGroup() {

	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(final LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public OrderTables getOrderTables() {
		return orderTables;
	}

	public void setOrderTables(OrderTables orderTables) {
		this.orderTables = orderTables;
	}

	@PrePersist
	public void prePersist() {
		this.createdDate = LocalDateTime.now();
	}

	public void unTableGroup() {
		for (OrderTable orderTable : this.orderTables.getOrderTables()) {
			List<Orders> orders = orderTable.getOrders();
			checkOrderStatus(orders);
		}
		this.orderTables.getOrderTables().stream().map(orderTable -> {
			orderTable.unTableGroup();
			return null;
		});
	}

	private void checkOrderStatus(List<Orders> orders) {
		for (Orders order : orders) {
			validateStatus(order);
		}
	}

	private void validateStatus(Orders order) {
		if (order.getOrderStatus().equalsIgnoreCase(OrderStatus.COMPLETION.name())) {
			throw new IllegalArgumentException("계산완료 상태의 주문에 대해서는 테이블 그룹을 해제할 수 없습니다.");
		}
	}
}
