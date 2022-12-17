package kitchenpos.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	private Integer quantity;

	protected OrderLineItem() {
	}

	public OrderLineItem(Order order, Menu menu, Integer quantity) {
		setOrder(order);
		this.menu = menu;
		this.quantity = quantity;
	}

	public static List<OrderLineItem> of(Order order, Map<Menu, Integer> menus) {
		return menus.entrySet()
			.stream()
			.map(entry -> new OrderLineItem(order, entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
	}

	public void setOrder(Order newOrder) {
		if (Objects.nonNull(order)) {
			order.getOrderLineItems().remove(this);
		}

		order = newOrder;

		if (!order.getOrderLineItems().contains(this)) {
			order.getOrderLineItems().add(this);
		}
	}

	public String getMenuName() {
		return menu.getName();
	}

	public Integer getQuantity() {
		return quantity;
	}
}
