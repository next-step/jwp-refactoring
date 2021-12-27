package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.common.domain.Quantity;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long seq;

	private Long menuId;

	@Embedded
	private Quantity quantity;

	protected OrderLineItem() {
	}

	public static OrderLineItem of(Long seq, Long menuId, Quantity quantity) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.seq = seq;
		orderLineItem.menuId = menuId;
		orderLineItem.quantity = quantity;
		return orderLineItem;
	}

	public static OrderLineItem of(Long menuId, Quantity quantity) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.menuId = menuId;
		orderLineItem.quantity = quantity;
		return orderLineItem;
	}

	public Long getSeq() {
		return seq;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Quantity getQuantity() {
		return quantity;
	}
}
