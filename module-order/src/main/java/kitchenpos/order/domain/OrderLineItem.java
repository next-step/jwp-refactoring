package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	private Long menuId;

	private Integer quantity;

	protected OrderLineItem() {
	}

	public OrderLineItem(Long menuId, Integer quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrderLineItem that = (OrderLineItem)o;
		return seq.equals(that.seq);
	}

	@Override
	public int hashCode() {
		return Objects.hash(seq);
	}
}
