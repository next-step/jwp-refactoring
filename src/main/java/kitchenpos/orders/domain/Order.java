package kitchenpos.orders.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
@Entity
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "bigint(20)")
	private Long id;

	@Column(name = "order_table_id", nullable = false, columnDefinition = "bigint(20)")
	private Long orderTableId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime orderedTime;

	public Order() {
	}

	public Order(Long orderTableId, OrderStatus orderStatus) {
		this(null, orderTableId, orderStatus);
	}

	public Order(Long id, Long orderTableId, OrderStatus orderStatus) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
	}

	public boolean isCompletion() {
		return Objects.equals(OrderStatus.COMPLETION, this.orderStatus);
	}

	public void changeOrderStatus(String orderStatus) {
		validateOrderIsCompletion();
		this.orderStatus = OrderStatus.valueOf(orderStatus);
	}

	private void validateOrderIsCompletion() {
		if (isCompletion()) {
			throw new IllegalArgumentException("계산완료 된 주문은 상태를 변경 할 수 없습니다");
		}
	}

	public Long getId() {
		return id;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

}
