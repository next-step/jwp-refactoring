package kitchenpos.order.domain;

import kitchenpos.common.BaseSeqEntity;
import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem extends BaseSeqEntity {

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	@Embedded
	private Quantity quantity;

	OrderLineItem() {
	}

	OrderLineItem(Order order, Menu menu, Quantity quantity) {
		this.order = order;
		this.menu = menu;
		this.quantity = quantity;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof OrderLineItem)) return false;
		if (!super.equals(o)) return false;
		OrderLineItem that = (OrderLineItem) o;
		return Objects.equals(order, that.order) &&
				Objects.equals(menu, that.menu) &&
				Objects.equals(quantity, that.quantity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), order, menu, quantity);
	}
}
