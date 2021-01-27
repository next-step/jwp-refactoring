package kitchenpos.domain.orders.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

/**
 * @author : byungkyu
 * @date : 2021/01/25
 * @description :
 **/
@Embeddable
public class OrderLineItems {
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	public OrderLineItems() {
	}

	public OrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

	public void add(Orders order, OrderLineItems orderLineItems) {
		for (OrderLineItem orderLineItem : orderLineItems.getOrderLineItems()) {
			this.orderLineItems.add(new OrderLineItem(order, orderLineItem.getMenu(), orderLineItem.getQuantity()));
		}
	}
}
