package kitchenpos.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.common.domain.Quantity;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Order order;

	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
	private Long menuId;

	@Embedded
	@AttributeOverride(name = "quantity", column = @Column(name = "quantity", nullable = false))
	private Quantity quantity;

	protected OrderLineItem() {
	}

	private OrderLineItem(Long seq, Order order, Long menuId, Quantity quantity) {
		this.seq = seq;
		this.order = order;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItem create(Long menuId, Long quantity) {
		return new OrderLineItem(null, null, menuId, Quantity.of(quantity));
	}

	public static OrderLineItem of(Long id, Long menuId, Long quantity) {
		return new OrderLineItem(id, null, menuId, Quantity.of(quantity));
	}

	public void setOrder(Order order) {
		this.order = order;
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

	public Quantity getQuantity() {
		return quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		OrderLineItem that = (OrderLineItem)o;

		return seq.equals(that.seq);
	}

	@Override
	public int hashCode() {
		return seq.hashCode();
	}

}
