package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embedded;
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

import kitchenpos.common.domain.Quantity;
import kitchenpos.order.dto.OrderLineItemDto;

@Table(name = "orders")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long orderTableId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime orderedTime;

	@Embedded
	private OrderLineItems orderLineItems;

	protected Order() {
	}

	private Order(Long id, Long orderTableId, List<OrderLineItemDto> orderLineItemDtos, OrderValidator validator) {
		validator.validateOrderTableExistAndNotEmpty(orderTableId);
		validator.validateMenusExist(orderLineItemDtos);

		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = OrderStatus.COOKING;
		this.orderLineItems = OrderLineItems.from(orderLineItemDtos.stream()
			.map(dto -> OrderLineItem.of(dto.getMenuId(), Quantity.from(dto.getQuantity())))
			.collect(Collectors.toList()));
	}

	public static Order of(
		Long id,
		Long orderTableId,
		List<OrderLineItemDto> orderLineItemDtos,
		OrderValidator validator
	) {
		return new Order(id, orderTableId, orderLineItemDtos, validator);
	}

	public static Order of(
		Long orderTableId,
		List<OrderLineItemDto> orderLineItemDtos,
		OrderValidator validator
	) {
		return new Order(null, orderTableId, orderLineItemDtos, validator);
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

	public OrderLineItems getOrderLineItems() {
		return orderLineItems;
	}

	public void changeOrderStatus(OrderStatus orderStatus) {
		if (this.orderStatus.isCompletion()) {
			throw new IllegalArgumentException("완료된 주문의 상태는 변경할 수 없습니다.");
		}

		this.orderStatus = orderStatus;
	}

	public boolean isCompleted() {
		return orderStatus.isCompletion();
	}
}
