package kitchenpos.order.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "order_id")
	private Order order;

	private Long menuId;

	private Integer quantity;

	protected OrderLineItem() {
	}

	public OrderLineItem(Order order, Long menuId, Integer quantity) {
		this.order = order;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static List<OrderLineItem> of(Order order, Map<Long, Integer> menus) {
		return menus.entrySet()
					.stream()
					.map(entry -> new OrderLineItem(order, entry.getKey(), entry.getValue()))
					.collect(Collectors.toList());
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
