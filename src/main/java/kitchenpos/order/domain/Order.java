package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kitchenpos.menu.domain.Menu;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long orderTableId;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	private LocalDateTime orderedTime;
	@OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	public Order() {
	}

	public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
		List<OrderLineItem> orderLineItems) {
		validate(orderTableId, orderLineItems);

		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;

		belongToOrder(orderLineItems);
	}

	private void validate(Long orderTableId, List<OrderLineItem> orderLineItems) {
		if (orderTableId == null || orderTableId == 0) {
			throw new IllegalArgumentException("주문은 주문테이블을 반드시 포함해야 합니다.");
		}
		if (orderLineItems == null) {
			throw new IllegalArgumentException("주문 항목은 비어있을 수 없습니다.");
		}
		Set<Menu> menuSet = orderLineItems.stream()
			.map(OrderLineItem::getMenu)
			.filter(Objects::nonNull)
			.collect(Collectors.toSet());
		if (menuSet.isEmpty() || menuSet.size() != orderLineItems.size()) {
			throw new IllegalArgumentException("메뉴는 누락되거나 중복될 수 없습니다.");
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

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

	private void belongToOrder(List<OrderLineItem> orderLineItems) {
		if (orderLineItems == null) {
			return;
		}
		for (OrderLineItem orderLineItem : orderLineItems) {
			orderLineItem.belongToOrder(this);
		}
	}

	public void changeOrderStatus(OrderStatus orderStatus) {
		if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
			throw new IllegalArgumentException("이미 계산 완료 상태인 주문은 변경할 수 없습니다.");
		}
		this.orderStatus = orderStatus;
	}

	public static final class Builder {
		private Long id;
		private Long orderTableId;
		private OrderStatus orderStatus;
		private LocalDateTime orderedTime;
		private List<OrderLineItem> orderLineItems = new ArrayList<>();

		public Builder() {
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder orderTableId(Long orderTableId) {
			this.orderTableId = orderTableId;
			return this;
		}

		public Builder orderStatus(OrderStatus orderStatus) {
			this.orderStatus = orderStatus;
			return this;
		}

		public Builder orderedTime(LocalDateTime orderedTime) {
			this.orderedTime = orderedTime;
			return this;
		}

		public Builder orderLineItems(List<OrderLineItem> orderLineItems) {
			this.orderLineItems = orderLineItems;
			return this;
		}

		public Builder orderLineItems(OrderLineItem... orderLineItems) {
			this.orderLineItems = Arrays.asList(orderLineItems);
			return this;
		}

		public Order build() {
			return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
		}
	}
}
