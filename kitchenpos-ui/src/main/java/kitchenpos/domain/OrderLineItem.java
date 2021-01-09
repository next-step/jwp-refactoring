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

	@Column(name = "order_id")
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	private long quantity;

	protected OrderLineItem() {
	}

	private OrderLineItem(Long orderId, Menu menu, long quantity) {
		this.orderId = orderId;
		this.menu = menu;
		this.quantity = quantity;
	}

	public static OrderLineItem create(Long orderId, Menu menu, long quantity) {
		return new OrderLineItem(orderId, menu, quantity);
	}

	public Long getSeq() {
		return seq;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Menu getMenu() {
		return menu;
	}

	public long getQuantity() {
		return quantity;
	}
}
