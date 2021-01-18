package kitchenpos.domain;

import kitchenpos.common.BaseSeqEntity;
import kitchenpos.common.Quantity;

import javax.persistence.*;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem extends BaseSeqEntity {

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	@Embedded
	private Quantity quantity;

	protected OrderLineItem() {
	}

	public OrderLineItem(Order order, Menu menu, long quantity) {
		this.order = order;
		this.menu = menu;
		this.quantity = new Quantity(quantity);
		this.order.addOrderLineItem(this);
	}

	public Order getOrder() {
		return order;
	}

	public Menu getMenu() {
		return menu;
	}

	public Quantity getQuantity() {
		return quantity;
	}
}
