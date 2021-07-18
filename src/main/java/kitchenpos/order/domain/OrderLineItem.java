package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
	private Order order;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
	private Menu menu;

	private Long quantity;

	public OrderLineItem() {
	}

	public OrderLineItem(Order order, Menu menu, Long quantity) {
		this.order = order;
		this.menu = menu;
		this.quantity = quantity;
	}
}
