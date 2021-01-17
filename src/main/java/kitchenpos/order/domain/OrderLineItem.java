package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Orders orders;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private Menu menu;

	private long quantity;

	public OrderLineItem(Menu menu, long quantity) {
		this.menu = menu;
		this.quantity = quantity;
	}

	protected OrderLineItem() {
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(final Long seq) {
		this.seq = seq;
	}

	public Orders getOrders() {
		return orders;
	}

	public Menu getMenu() {
		return menu;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(final long quantity) {
		this.quantity = quantity;
	}

	public void setOrder(Orders orders) {
		this.orders = orders;
	}
}
