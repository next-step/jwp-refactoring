package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;
	@ManyToOne
	private Order order;
	@OneToOne
	private Menu menu;
	private long quantity;

	protected OrderLineItem() {

	}

	private OrderLineItem(Order order, Menu menu, long quantity) {
		validate(menu);
		this.order = order;
		this.menu = menu;
		this.quantity = quantity;
	}

	private void validate(Menu menu) {
		if (Objects.isNull(menu)) {
			throw new IllegalArgumentException("메뉴가 없습니다.");
		}
	}

	public static OrderLineItemBuilder builder() {
		return new OrderLineItemBuilder();
	}

	public Long getSeq() {
		return seq;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Menu getMenu() {
		return menu;
	}

	public long getQuantity() {
		return quantity;
	}

	public static final class OrderLineItemBuilder {
		private Order order;
		private Menu menu;
		private long quantity;

		private OrderLineItemBuilder() {
		}

		public OrderLineItemBuilder order(Order order) {
			this.order = order;
			return this;
		}

		public OrderLineItemBuilder menu(Menu menu) {
			this.menu = menu;
			return this;
		}

		public OrderLineItemBuilder quantity(long quantity) {
			this.quantity = quantity;
			return this;
		}

		public OrderLineItem build() {
			return new OrderLineItem(order, menu, quantity);
		}
	}
}
