package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_to_order"))
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_to_menu"))
	private Menu menu;
	@Embedded
	private Quantity quantity;

	protected OrderLineItem() {}

	private OrderLineItem(Menu menu, int quantity) {
		this.menu = menu;
		this.quantity = Quantity.of(quantity);
	}

	public static OrderLineItem of(Menu menu, int quantity) {
		return new OrderLineItem(menu, quantity);
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

	public Quantity getQuantity() {
		return quantity;
	}

	public void updateOrder(Order order) {
		this.order = order;
	}
}
