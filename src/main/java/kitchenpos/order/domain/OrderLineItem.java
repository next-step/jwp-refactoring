package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;
	private Long orderId;
	@OneToOne
	private Menu menu;
	private long quantity;

	protected OrderLineItem() {

	}

	private OrderLineItem(Long orderId, Menu menu, long quantity) {
		validate(menu);
		this.orderId = orderId;
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long order) {
		this.orderId = order;
	}

	public Menu getMenu() {
		return menu;
	}

	public long getQuantity() {
		return quantity;
	}

	public static final class OrderLineItemBuilder {
		private Long orderId;
		private Menu menu;
		private long quantity;

		private OrderLineItemBuilder() {
		}

		public OrderLineItemBuilder order(Long order) {
			this.orderId = order;
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
			return new OrderLineItem(orderId, menu, quantity);
		}
	}
}
