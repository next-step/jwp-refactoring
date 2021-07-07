package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@Column(name = "quantity")
	private long quantity;

	protected OrderLineItem() {
	}

	public OrderLineItem(Order order, Menu menu, long quantity) {
		this.order = order;
		this.menu = menu;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public Order getOrder() {
		return order;
	}

	public Long getOrderId() {
		if (order == null) {
			return null;
		}
		return order.getId();
	}

	public Long getMenuId() {
		if (menu == null) {
			return null;
		}
		return menu.getId();
	}

	public Menu getMenu() {
		return menu;
	}

	public long getQuantity() {
		return quantity;
	}

	public void changeOrder(Order order) {
		this.order = order;
	}
}
