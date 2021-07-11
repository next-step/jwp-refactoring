package kitchenpos.order.domain;

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

	@Column(name = "menu_id")
	private Long menuId;

	@Column(name = "quantity")
	private long quantity;

	protected OrderLineItem() {
	}

	public OrderLineItem(Order order, Long menuId, long quantity) {
		this.order = order;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public OrderLineItem(Long menuId, long quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public Long getOrderId() {
		if (order == null) {
			return null;
		}
		return order.getId();
	}

	public long getQuantity() {
		return quantity;
	}

	public void changeOrder(Order order) {
		this.order = order;
	}
}
