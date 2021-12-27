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

import kitchenpos.common.domain.PositiveNumber;
import kitchenpos.menu.domain.Menu;

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
	@ManyToOne(fetch = FetchType.LAZY)
	private Menu menu;

	@Embedded
	@AttributeOverride(name = "number", column = @Column(name = "quantity", nullable = false))
	private PositiveNumber quantity;

	protected OrderLineItem() {
	}

	private OrderLineItem(Long seq, Order order, Menu menu, PositiveNumber quantity) {
		this.seq = seq;
		this.order = order;
		this.menu = menu;
		this.quantity = quantity;
	}

	public static OrderLineItem create(Order order, Menu menu, PositiveNumber quantity) {
		return new OrderLineItem(null, order, menu, quantity);
	}

	public Long getSeq() {
		return seq;
	}

	public Order getOrder() {
		return order;
	}

	public Menu getMenu() {
		return menu;
	}

	public PositiveNumber getQuantity() {
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
