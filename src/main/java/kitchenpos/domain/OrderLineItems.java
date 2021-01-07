package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	public void add(OrderLineItem orderLineItem) {
		this.orderLineItems.add(orderLineItem);
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

}
