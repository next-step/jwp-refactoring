package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.order.dto.OrderLineItemResponse;

@Entity
public class OrderLineItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
	private Order order;

	@Column(nullable = false)
	private Long menuId;

	@Column(nullable = false)
	private Long quantity;

	protected OrderLineItem() {
	}

	private OrderLineItem(Order order, Long menuId, Long quantity) {
		this.order = order;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItem of(Order order, Long menuId, Long quantity) {
		return new OrderLineItem(order, menuId, quantity);
	}

	public Long getSeq() {
		return seq;
	}

	public Order getOrder() {
		return order;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public OrderLineItemResponse toResDto() {
		return OrderLineItemResponse.of(seq, menuId, quantity);
	}
}
