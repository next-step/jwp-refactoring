package kitchenpos.order.domain;

import kitchenpos.common.QuantityEntity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem extends QuantityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Orders orders;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private Menu menu;

	private Long quantity;

	public OrderLineItem(Orders orders, Menu menu, Long quantity) {
		this.orders = orders;
		this.menu = menu;
		this.quantity = this.validate(quantity);
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

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(final Long quantity) {
		this.quantity = quantity;
	}

	public void setOrder(Orders orders) {
		this.orders = orders;
	}
}
