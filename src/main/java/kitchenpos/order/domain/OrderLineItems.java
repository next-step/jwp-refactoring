package kitchenpos.order.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {
	@OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<OrderLineItem> orderLineItems;

	protected OrderLineItems() {
	}

	public OrderLineItems(Order order, List<OrderLineItem> orderLineItems) {
		validate(orderLineItems);
		orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(order));
		this.orderLineItems = orderLineItems;
	}

	private void validate(List<OrderLineItem> orderLineItems) {
		if (CollectionUtils.isEmpty(orderLineItems)) {
			throw new IllegalArgumentException("주문 항목이 비어있습니다.");
		}
	}

	public List<OrderLineItem> getList() {
		return orderLineItems;
	}
}
