package kitchenpos.order.domain;

import common.entity.BaseSeqEntity;
import common.entity.Quantity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem extends BaseSeqEntity {

	@Column(name = "order_id", nullable = false, insertable = false, updatable = false)
	private Long orderId;

	@ManyToOne
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	@Embedded
	private Quantity quantity;

	OrderLineItem() {
	}

	OrderLineItem(Menu menu, Quantity quantity) {
		this.menu = menu;
		this.quantity = quantity;
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
		return Objects.equals(orderId, that.orderId) &&
				Objects.equals(menu, that.menu) &&
				Objects.equals(quantity, that.quantity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), orderId, menu, quantity);
	}
}
