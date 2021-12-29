package kitchenpos.orders.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.domain.Quantity;

@Entity
public class OrderLineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "bigint(20)")
	private Long seq;

	@Column(name = "order_id", nullable = false, columnDefinition = "bigint(20)")
	private Long orderId;

	@Column(name = "menu_id", nullable = false, columnDefinition = "bigint(20)")
	private Long menuId;

	@Embedded
	@Column(columnDefinition = "bigint(20)", nullable = false)
	private Quantity quantity;

	public OrderLineItem() {
	}

	public OrderLineItem(Long menuId, Quantity quantity) {
		this(null, null, menuId, quantity);
	}

	public OrderLineItem(Long orderId, Long menuId, Quantity quantity) {
		this(null, orderId, menuId, quantity);
	}

	public OrderLineItem(Long seq, Long orderId, Long menuId, Quantity quantity) {
		this.seq = seq;
		this.orderId = orderId;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getMenuId() {
		return this.menuId;
	}

	public long getQuantityValue() {
		return quantity.value();
	}
}
