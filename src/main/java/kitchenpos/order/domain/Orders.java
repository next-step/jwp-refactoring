package kitchenpos.order.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import kitchenpos.ordertable.domain.OrderTable;

@Entity
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_table_id")
	private OrderTable orderTable;

	private String orderStatus;

	private LocalDateTime orderedTime;

	protected Orders() {
	}

	public Orders(OrderTable orderTable, String orderStatus) {
		validate(orderTable);
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
	}

	public Orders(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void changeStatus(String orderStatus) {
		if (OrderStatus.COMPLETION.name().equals(this.orderStatus)) {
			throw new IllegalArgumentException("결제 완료된 주문은 상태를 변경할 수 없습니다.");
		}

		this.orderStatus = orderStatus;
	}

	private void validate(OrderTable orderTable) {
		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException("테이블이 비어있습니다.");
		}
	}

	public Long getId() {
		return id;
	}

	public OrderTable getOrderTable() {
		return orderTable;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	@PrePersist
	public void prePersist() {
		this.orderedTime = LocalDateTime.now();
	}
}
