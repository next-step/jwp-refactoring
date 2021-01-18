package kitchenpos.domain;

import kitchenpos.common.BaseSeqEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem extends BaseSeqEntity {

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	private long quantity;

	protected OrderLineItem() {
	}

	public OrderLineItem(Order order, Menu menu, long quantity) {
		this.order = order;
		this.menu = menu;
		this.quantity = quantity;
		this.order.addOrderLineItem(this);
	}

	public Order getOrder() {
		return order;
	}

	public Menu getMenu() {
		return menu;
	}

	public long getQuantity() {
		return quantity;
	}
}
