package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {
	@OneToMany(mappedBy = "id", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

	public void addAllOrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems.addAll(orderLineItems);
	}
}
