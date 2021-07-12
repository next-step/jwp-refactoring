package kitchenpos.order.domain;

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

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Quantity;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20, nullable = false)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
	private Order order;

	@Embedded
	private Quantity quantity;

	protected OrderLineItem() {

	}

	public OrderLineItem(Menu menu, Quantity quantity) {
		this.menu = menu;
		this.quantity = quantity;
	}

	public OrderLineItem(Menu menu, Order order, Quantity quantity) {
		this(menu, quantity);
		this.order = order;
	}

	public OrderLineItem(Long id, Menu menu, Quantity quantity) {
		this(menu, quantity);
		this.seq = id;
	}

	public Menu getMenu() {
		return menu;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public Order getOrder() {
		return order;
	}
}
