package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.domain.Quantity;

@Entity
public class OrderLineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne
	@JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_to_order"))
	private Order order;
	@Column(nullable = false, updatable = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_to_product"))
	private long menuId;

	@Embedded
	private Quantity quantity;

	protected OrderLineItem() {
	}

	private OrderLineItem(long menuId, Quantity quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItem of(long menuId, Quantity quantity) {
		return new OrderLineItem(menuId, quantity);
	}

	public static OrderLineItem of(long menuId, long quantity) {
		return new OrderLineItem(menuId, Quantity.from(quantity));
	}

	public Long getSeq() {
		return seq;
	}

	public Long getOrderId() {
		return order.getId();
	}

	public Long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity.value();
	}

}
