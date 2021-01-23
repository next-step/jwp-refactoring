package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.BaseEntity;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "orders_id")
	private Orders order;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@Embedded
	private Quantity quantity;

	protected OrderLineItem() {
	}

	private OrderLineItem(final Long id, final Orders order, final Menu menu, final int quantity) {
		this.id = id;
		this.order = order;
		this.menu = menu;
		this.quantity = Quantity.of(quantity);
	}

	public static OrderLineItem of(final Long id, final Orders order, final Menu menu, final int quantity) {
		return new OrderLineItem(id, order, menu, quantity);
	}

	public static OrderLineItem of(final Orders order, final Menu menu, final int quantity) {
		return of(null, order, menu, quantity);
	}

	public Long getId() {
		return id;
	}

	public Orders getOrder() {
		return order;
	}

	public Menu getMenu() {
		return menu;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public long quantity() {
		return this.quantity.getQuantity();
	}
}
