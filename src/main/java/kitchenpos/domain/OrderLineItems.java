package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	public void add(Order order, List<Menu> menus, List<Long> quantities) {
		for (int i = 0; i < menus.size(); i++) {
			orderLineItems.add(OrderLineItem.create(order, menus.get(i), quantities.get(i)));
		}
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

}
