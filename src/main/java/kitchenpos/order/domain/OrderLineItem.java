package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long seq;

	@ManyToOne(optional = false)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@Embedded
	private Quantity quantity;

	protected OrderLineItem() {
	}

	public static OrderLineItem of(Menu menu, Quantity quantity) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.menu = menu;
		orderLineItem.quantity = quantity;
		return orderLineItem;
	}

	public Long getSeq() {
		return seq;
	}

	public Menu getMenu() {
		return menu;
	}

	public Quantity getQuantity() {
		return quantity;
	}
}
